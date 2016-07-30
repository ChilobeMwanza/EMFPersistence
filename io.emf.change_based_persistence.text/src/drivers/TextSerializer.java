package drivers;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import change.AbstractEntry;
import change.AddLinkEntry;
import change.AddToResourceEntry;
import change.ChangeLog;
import change.NewObjectEntry;
import change.SetAttributeEntry;

public class TextSerializer 
{
	private final PersistenceManager manager;
	private boolean appendMode = false;
	private List<String> outputList;
    private List<AbstractEntry> eventsList;
	private final String className = this.getClass().getSimpleName();
	private final ChangeLog changelog; 
	
	public TextSerializer(PersistenceManager manager, ChangeLog aChangeLog)
	{
		this.manager =  manager;
		this.changelog = aChangeLog;
		this.outputList = new ArrayList<String>();
		this.eventsList = new ArrayList<AbstractEntry>();
		
		eventsList = manager.getChangeLog().getEventsList();
	}
	
	public void save(Map<?,?> options)
	{
		if(eventsList.isEmpty())
			return;
		
		if(!appendMode)
			createInitialEntry();
		
		//String fileSaveLocation = (String) options.get("FILE_SAVE_LOCATION");
		
		for(AbstractEntry e : eventsList )
		{
			if(e instanceof NewObjectEntry)
				createNewObjectEntry((NewObjectEntry) e);
			else if(e instanceof SetAttributeEntry)
				createSetAttributeEntry((SetAttributeEntry) e);
			else if(e instanceof AddToResourceEntry)
				serialiseAddToResourceEntry((AddToResourceEntry)e);
			else if(e instanceof AddLinkEntry)
				serialiseAddLinkEntry((AddLinkEntry) e);
			
		}
		
		//finally append strings to file
		appendStringsToFile(appendMode);
		
	}
	
	private void serialiseAddLinkEntry(AddLinkEntry e)
	{
		EReference ref = e.getReference();
		EObject newObj = e.getNewObj();
		EObject destObj = e.getEObject();
		
		if(ref.getEOpposite() != null)
			return;
		
		outputList.add("ADD "+newObj.eClass().getName()+" "+changelog.getObjectId(newObj)
		+" "+ref.getName()+" "+destObj.eClass().getName()+" "+
				changelog.getObjectId(destObj));
		
		//System.out.println(className+" "+ref.);
		
	}
	
	private void serialiseAddToResourceEntry(AddToResourceEntry e)
	{
		EObject obj = e.getEObject();
		outputList.add("ADD_R "+obj.eClass().getName()+" "+changelog.getObjectId(obj)); 
	}
	
	private void createInitialEntry()
	{
		EObject obj = eventsList.get(0).getEObject(); 
		outputList.add("NAMESPACE_URI "+obj.eClass().getEPackage().getNsURI());
	}
	
	private void createNewObjectEntry(NewObjectEntry e)
	{
		EObject obj = e.getEObject();
		outputList.add("CREATE "+obj.eClass().getName()+ " "+manager.getChangeLog().getObjectId(obj));
	}
	
	private boolean isSimpleType(String str)
	{
		String[] types = new String[]{"EString","EDouble"};
		if(Arrays.asList(types).contains(str))
			return true;
		return false;
		
	}
	/*
	 * Many, many datatypes not supported.
	 */
	private void createSetAttributeEntry(SetAttributeEntry e)
	{
		
		/*
		 * null values
		 * many 
		 */
		//check that null is possible
		EObject obj = e.getEObject();
		Object newValue = e.getNewValue();
		EAttribute atrr = e.geteAttribute();
		
		if(newValue != null && !atrr.isMany())
		{
			//System.out.println(className + " "+atrr.getEAttributeType().getInstanceClassName());
			//System.out.println(className+" "+atrr.getName());
			//System.out.println(className+" "+atrr.getEAttributeType().getName());
			
			
			if(atrr instanceof EEnum)
				return;
			else if(isSimpleType(atrr.getEAttributeType().getName()) && !atrr.isMany())
				outputList.add("SET "+atrr.getName()+" "+obj.eClass().getName()+" "+changelog.getObjectId(obj) +" \""+ newValue.toString()+"\"");
		}
		
	}

	private void appendStringsToFile(boolean appendMode)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter
				    (new OutputStreamWriter(new FileOutputStream(manager.getURI().path(),appendMode),
				    		Charset.forName(manager.TEXT_ENCODING).newEncoder()));
			PrintWriter out = new PrintWriter(bw);
			for(String string: outputList)
			{
				//System.out.println("PersistenceManager.java "+string);
				out.println(string);
			}
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}	
}
