
//todo: specify encoding ?
//http://www.javapractices.com/topic/TopicAction.do?Id=31
package drivers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import change.Changelog;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import impl.CBPBinaryResourceImpl;
import impl.CBPResource;
import impl.CBPTextResourceImpl;

public class PersistenceManager 
{
	
	public static final int CREATE_AND_ADD_TO_RESOURCE = 0;
	public static final int CREATE_AND_SET_EREFERENCE_VALUE = 1;
    public static final int ADD_TO_RESOURCE = 2;
    public static final int SET_PRIMITIVE_EATTRIBUTE_VALUE = 3;
    public static final int SET_COMPLEX_EATTRIBUTE_VALUE = 4;
    public static final int SET_EREFERENCE_VALUE = 5;
    public static final int DELETE_FROM_RESOURCE = 6;
    public static final int UNSET_PRIMITIVE_EATTRIBUTE_VALUE = 7;
    public static final int UNSET_COMPLEX_EATTRIBUTE_VALUE = 8;
    public static final int UNSET_EREFERENCE_VALUE = 9;
    
    public static final int SIMPLE_TYPE_INT = 0;
    public static final int SIMPLE_TYPE_BOOLEAN = 1;
    public static final int SIMPLE_TYPE_FLOAT = 2;
    public static final int SIMPLE_TYPE_DOUBLE = 3;
    public static final int SIMPLE_TYPE_BYTE = 4;
    public static final int SIMPLE_TYPE_SHORT = 5;
    public static final int SIMPLE_TYPE_LONG = 6;
    public static final int SIMPLE_TYPE_CHAR = 7;
    public static final int COMPLEX_TYPE = 8;
    
    public final int INTEGER_SIZE = 4;
    public final int BYTE_SIZE = 1;
    public final int CHAR_SIZE = 2;
    public final int DOUBLE_SIZE = 8;
    public final int FLOAT_SIZE = 4;
    public final int LONG_SIZE = 8;
    public final int SHORT_SIZE = 2;
    
	public static final String DELIMITER = ","; 
	public static final String ESCAPE_CHAR ="+"; 
	public final Charset STRING_ENCODING = StandardCharsets.UTF_8;
	public final String NULL_STRING = "pFgrW";
	
	private final Changelog changelog; 

	private final CBPResource resource;
	
    private final EPackageElementsNamesMap ePackageElementsNamesMap;
	
	private boolean resume = false;
	
	private final TObjectIntMap<String> simpleTypesMap = 
									new TObjectIntHashMap<String>(15);;
    
	public PersistenceManager(Changelog changelog, CBPResource resource, 
			EPackageElementsNamesMap ePackageElementsNamesMap)
	{
		this.changelog = changelog;
		this.resource = resource;
		this.ePackageElementsNamesMap = ePackageElementsNamesMap;
		
		populateSimpleTypesMap();
	}
	
	public void setResume(boolean b)
	{
		resume = b;
	}
	
	private void populateSimpleTypesMap()
	{
		simpleTypesMap.put("EInt", SIMPLE_TYPE_INT);
    	simpleTypesMap.put("EIntegerObject", SIMPLE_TYPE_INT);
    	simpleTypesMap.put("EBoolean", SIMPLE_TYPE_BOOLEAN);
    	simpleTypesMap.put("EBooleanObject", SIMPLE_TYPE_BOOLEAN);
    	simpleTypesMap.put("EFloat", SIMPLE_TYPE_FLOAT);
    	simpleTypesMap.put("EFloatObject", SIMPLE_TYPE_FLOAT);
    	simpleTypesMap.put("EDouble", SIMPLE_TYPE_DOUBLE);
    	simpleTypesMap.put("EDoubleObject", SIMPLE_TYPE_DOUBLE);
    	simpleTypesMap.put("EByte", SIMPLE_TYPE_BYTE);
    	simpleTypesMap.put("EByteObject", SIMPLE_TYPE_BYTE);
    	simpleTypesMap.put("EShort", SIMPLE_TYPE_SHORT);
    	simpleTypesMap.put("EShortObject", SIMPLE_TYPE_SHORT);
    	simpleTypesMap.put("ELong", SIMPLE_TYPE_LONG);
    	simpleTypesMap.put("ELongObject", SIMPLE_TYPE_LONG);
    	simpleTypesMap.put("EChar", SIMPLE_TYPE_CHAR);
	}
	
	public boolean isResume()
	{
		return resume;
	}
	
	public boolean addEObjectsToContents(List<EObject> objects)
	{
		return resource.getContents().addAll(objects);
	}
	
	public boolean removeEObjectsFromContents(List<EObject> objects)
	{
		return resource.getContents().removeAll(objects);
	}
	
	public boolean addEObjectToContents(EObject object)
	{
		return resource.getContents().add(object);
	}
	
	public boolean removeEObjectFromContents(EObject obj)
	{
		return resource.getContents().remove(obj);
	}
	
	public Resource getResource()
	{
		return this.resource;
	}
	
	public URI getURI()
	{
		return resource.getURI();
	}

	public void save(Map<?,?> options)
	{
		if(resource instanceof CBPBinaryResourceImpl)
		{
			CBPBinarySerializer serializer = new CBPBinarySerializer(this,changelog, ePackageElementsNamesMap,simpleTypesMap);
			try {
				serializer.save(options);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(resource instanceof CBPTextResourceImpl)
		{
			CBPTextSerializer serializer = new CBPTextSerializer(this, changelog,ePackageElementsNamesMap);
			serializer.save(options);
		}
		//
		
	}

	
	public void load(Map<?,?> options) throws Exception
	{	
		//CBPTextDeserializer textDeserializer = new CBPTextDeserializer(this,changelog,ePackageElementsNamesMap);
		CBPBinaryDeserializer deserializer = new CBPBinaryDeserializer(this,changelog,ePackageElementsNamesMap);
		deserializer.load(options);
	}
	
	public Changelog getChangelog()
	{
		return this.changelog;
	}
}
