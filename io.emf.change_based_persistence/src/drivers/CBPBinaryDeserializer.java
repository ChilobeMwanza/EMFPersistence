package drivers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import change.Changelog;
import exceptions.UnknownPackageException;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class CBPBinaryDeserializer 
{
	private final String classname = this.getClass().getSimpleName();
	private EPackage ePackage = null;
	private final Changelog changelog;
	
	private final TIntObjectMap<EObject> IDToEObjectMap = 
			new TIntObjectHashMap<EObject>();
	
	private final PersistenceManager manager;
    private final EPackageElementsNamesMap ePackageElementsNamesMap;
    private final Charset STRING_ENCODING = StandardCharsets.UTF_8;
    
    
    
    public CBPBinaryDeserializer(PersistenceManager manager, Changelog aChangelog,
            EPackageElementsNamesMap ePackageElementsNamesMap)
    {
    	 this.manager = manager;
         this.changelog = aChangelog;
         this.ePackageElementsNamesMap = ePackageElementsNamesMap;
    }
    
    public void load(Map<?,?> options) throws Exception
    { 
    	InputStream inputStream = new BufferedInputStream
				(new FileInputStream(manager.getURI().path()));
		
    	/*Read File Header*/
		 inputStream.skip(11); // skip file header
		
		/*Load metamodel*/
		int nsUriLength = readInt(inputStream);
		loadMetamodel(readString(inputStream,nsUriLength));
		
		/*Read binary records*/
		while(inputStream.available()> 0)
		{
			switch (readInt(inputStream))
			{
			case PersistenceManager.CREATE_AND_ADD_TO_RESOURCE:
				handleCreateAndAddToResource(inputStream);
				break;
			case PersistenceManager.CREATE_AND_SET_EREFERENCE_VALUE:
				handeCreateAndSetEReferenceValue(inputStream);
				break;
			case PersistenceManager.ADD_TO_RESOURCE:
				handleAddToResource(inputStream);
				break;
			case PersistenceManager.DELETE_FROM_RESOURCE:
				handleRemoveFromResource(inputStream);
				break;
			case PersistenceManager.SET_EREFERENCE_VALUE:
				handleSetEReference(inputStream);
				break;
			case PersistenceManager.UNSET_EREFERENCE_VALUE:
				handleUnsetEReferenceValue(inputStream);
				break;
			}
		}
		
		inputStream.close();	
    }
    
    private void handleUnsetEReferenceValue(InputStream in) throws IOException
    {
    	EObject focus_obj = IDToEObjectMap.get(readInt(in));
    	
    	EReference ref = (EReference) focus_obj.eClass().getEStructuralFeature
				(ePackageElementsNamesMap.getName(readInt(in)));
    	
    	int numInts = readInt(in);
    	
    	byte[] buffer = new byte[numInts * 4];
    	
    	int[] intArray = new int[numInts]; //stores 'n' numbers
    	
    	int index = 0;
    	
    	for(int i = 0; i < numInts; i++)
    	{
    		intArray[i] = byteArrayToInt(Arrays.copyOfRange(buffer, index,index+4));
    		
    		index = index + 4;
    	}
    	
    	if(ref.isMany())
    	{
    		@SuppressWarnings("unchecked")
			EList<EObject> feature_value_list = (EList<EObject>) focus_obj.eGet(ref);
    		
    		for(int i : intArray)
    		{
    			feature_value_list.remove(IDToEObjectMap.remove(i));
    		}
    	}
    	
    }
    private void handleRemoveFromResource(InputStream in) throws IOException
    {
    	int numInts = readInt(in);
    	
    	byte[] buffer = new byte[numInts * 4];
    	
    	in.read(buffer);
    	
    	int index = 0;
    	
    	for(int i = 0; i < numInts; i++)
    	{
    		int id = byteArrayToInt(Arrays.copyOfRange(buffer, index,index+4));
    		
    		manager.removeEObjectFromContents(IDToEObjectMap.remove(id));
    		
    		index = index + 4;
    	}
    }
    
    private void handleSetEReference(InputStream in) throws IOException
    {
    	EObject focus_obj = IDToEObjectMap.get(readInt(in));
    	
    	EReference ref = (EReference) focus_obj.eClass().getEStructuralFeature
				(ePackageElementsNamesMap.getName(readInt(in)));
    	
    	int numInts = readInt(in);
    	
    	byte[] buffer = new byte[numInts * 4]; //stores bytes for all 'n' numbers
    	
    	in.read(buffer); //read in bytes for all 'n' numbers
    	
    	int[] intArray = new int[numInts]; //stores 'n' numbers
    	
    	int index = 0;
    	
    	for(int i = 0; i < numInts; i++)
    	{
    		intArray[i] = byteArrayToInt(Arrays.copyOfRange(buffer, index,index+4));
    		
    		index = index + 4;
    	}
    	
    	if(ref.isMany())
    	{
    		@SuppressWarnings("unchecked")
			EList<EObject> feature_value_list = (EList<EObject>) focus_obj.eGet(ref);
    		
    		for(int i : intArray)
    		{
    			feature_value_list.add(IDToEObjectMap.get(i));
    		}
    	}
    	else
    	{
    		focus_obj.eSet(ref, IDToEObjectMap.get(intArray[0]));
    	}
    }
    
    
    private void handleCreateAndAddToResource(InputStream in) throws IOException
    {
    	int numInts = readInt(in);
    	
    	byte[] buffer = new byte[numInts * 4]; //stores bytes for all 'n' numbers
    	
    	in.read(buffer);
    	
    	int[] intArray = new int[numInts]; //stores 'n' numbers
    	
    	int index = 0;
    	
    	for(int i = 0; i < numInts; i++)
    	{
    		intArray[i] = byteArrayToInt(Arrays.copyOfRange(buffer, index,index+4));
    		
    		index = index + 4;
    	}
    	
    	index = 0;
    	
    	for(int i = 0; i < (numInts / 2); i++)
    	{
    		EObject obj = createEObject(ePackageElementsNamesMap.getName(intArray[index]));
    		
    		int id = intArray[index+1];
    		
    		index = index + 2;
    		
    		changelog.addObjectToMap(obj, id);
    		IDToEObjectMap.put(id,obj);
    		
    		manager.addEObjectToContents(obj);
    	}
    }
    
    private void handeCreateAndSetEReferenceValue(InputStream in) throws IOException
    {
    	EObject focus_obj = IDToEObjectMap.get(readInt(in));
    	
    	EReference ref = (EReference) focus_obj.eClass().getEStructuralFeature
                (ePackageElementsNamesMap.getName(readInt(in)));
    	
    	int numInts = readInt(in);
    	
    	byte[] buffer = new byte[numInts * 4]; //stores 'n' numbers
    	
    	in.read(buffer); //read in bytes for 'n' numbers
    	
    	int[] intArray = new int[numInts]; //stores 'n' numbers
    	
    	int index = 0;
    	
    	for(int i = 0; i < numInts; i++)
    	{
    		intArray[i] = byteArrayToInt(Arrays.copyOfRange(buffer, index,index+4));
    		
    		index = index + 4;
    	}
    	
    	index = 0;
    	
    	for(int i = 0; i < (numInts / 2); i++)
    	{
    		EObject obj = createEObject(ePackageElementsNamesMap.getName(intArray[index]));
    		
    		int id = intArray[index + 1];
    		
    		index = index + 2;
    		
    		changelog.addObjectToMap(obj, id);
   
    		IDToEObjectMap.put(id,obj);
    	}
    	
    	if(ref.isMany())
    	{
    		@SuppressWarnings("unchecked")
			EList<EObject> feature_value_list = (EList<EObject>) focus_obj.eGet(ref);
    		
    		for(int i = 1; i < numInts; i = i + 2)
    		{
    			feature_value_list.add(IDToEObjectMap.get(intArray[i]));
    		}
    	}
    	else
    	{
    		focus_obj.eSet(ref, IDToEObjectMap.get(intArray[1]));
    	}
    	
    }
    
    private void handleAddToResource(InputStream in) throws IOException
    {
    	int numInts = readInt(in);
    	
    	byte[] buffer = new byte[numInts * 4]; //stores 'n' numbers
    	
    	in.read(buffer); //read in bytes for 'n' numbers
    	
    	int startIndex = 0;
    	
    	for(int i = 0; i < numInts; i++)
    	{
    		int id = byteArrayToInt(Arrays.copyOfRange(buffer, startIndex,startIndex+4));
    		
    		startIndex = startIndex + 4;
    		
    		manager.addEObjectToContents(IDToEObjectMap.get(id));
    	}
    }
    
    /*private void readInitialRecord(InputStream in) throws IOException
    {
    	byte[] buffer = new byte[4];
    	in.read(buffer);
    }*/
    
    private int readInt(InputStream in) throws IOException
    {
    	byte[] bytes = new byte[4];
    	in.read(bytes);
    	
    	return ByteBuffer.wrap(bytes).getInt();
    }
    
    
    private String readString(InputStream in, int length) throws IOException
    {
    	byte[] bytes = new byte[length];
    	in.read(bytes);
    	
    	return new String(bytes, STRING_ENCODING);
    }
    
    private void loadMetamodel(String metamodelURI) throws UnknownPackageException
    {
    	
        if(EPackage.Registry.INSTANCE.containsKey(metamodelURI))
            ePackage = EPackage.Registry.INSTANCE.getEPackage(metamodelURI);
        
        else
            throw new UnknownPackageException(metamodelURI);
        
    }
    
    private int byteArrayToInt(byte[] bytes)
    {
    	return ByteBuffer.wrap(bytes).getInt();
    }
    
    private String byteArrayToString(byte[] bytes)
    {
    	return new String(bytes, STRING_ENCODING);
    }
    
    private EObject createEObject(String eClassName) //does this need to be a method?
	{
		return ePackage.getEFactoryInstance().create((EClass)
				ePackage.getEClassifier(eClassName));
	}
    
    		
    	
    		
	 
}
