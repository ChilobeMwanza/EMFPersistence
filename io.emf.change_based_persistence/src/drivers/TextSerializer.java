/*
 * todo:
 * make txt format less verbose
 * when unsetting single valued feature, name is not needed
 * binary
 * change log optimisation
 * more tests
 * 
 */
package drivers;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import change.ChangeLog;
import change.Event;
import change.SetEAttributeManyEvent;
import change.SetEAttributeSingleEvent;
import change.SetEReferenceManyEvent;
import change.SetEReferenceSingleEvent;
import change.UnsetEAttributeManyEvent;
import change.UnsetEAttributeSingleEvent;
import change.UnsetEReferenceManyEvent;
import change.UnsetEReferenceSingleEvent;



public class TextSerializer 
{
	private  final String classname = this.getClass().getSimpleName();
	private final PersistenceManager manager;
	private List<String> output_list;
    private List<Event> event_list;
	private final ChangeLog changelog; 
	
	
	public TextSerializer(PersistenceManager manager, ChangeLog aChangeLog)
	{
		this.manager =  manager;
		this.changelog = aChangeLog;
		this.output_list = new ArrayList<String>();
		
		this.event_list = manager.getChangeLog().getEventsList();
	}
	
	public void save(Map<?,?> options)
	{
		if(event_list.isEmpty())
			return;
		
		//if we're not in resume mode, add namespace entry to output list
		if(!manager.isResume())
			addInitialEntryToOutputList();
		
		for(Event e : event_list)
		{
			switch(e.getEventType())
			{
			case Event.SET_EATTRIBUTE_SINGLE:
				handleSetEAttributeSingleEvent((SetEAttributeSingleEvent)e);
				break;
			case Event.SET_EATTRIBUTE_MANY:
				handleSetEAttributeManyEvent((SetEAttributeManyEvent)e);
				break;
			case Event.SET_EREFERENCE_SINGLE:
				handleSetEReferenceSingleEvent((SetEReferenceSingleEvent)e);
				break;
			case Event.SET_EREFERENCE_MANY:
				handleSetEReferenceManyEvent((SetEReferenceManyEvent)e);
				break;
			case Event.UNSET_EATTRIBUTE_SINGLE:
				handleUnsetEAttributeSingleEvent((UnsetEAttributeSingleEvent)e);
				break;
			case Event.UNSET_EATTRIBUTE_MANY:
				handleUnsetEAttributeManyEvent((UnsetEAttributeManyEvent)e);
				break;
			case Event.UNSET_EREFERENCE_SINGLE:
				handleUnsetEReferenceSingleEvent((UnsetEReferenceSingleEvent)e);
				break;
			case Event.UNSET_EREFERENCE_MANY:
				handleUnsetEReferenceManyEvent((UnsetEReferenceManyEvent)e);
				break;
			}
		}
		
		changelog.clearEvents();
		
		/*finally append strings to file, if no previous successful load,don't 
		 * serialise in append mode(i.e create new file, e.t.c)
		 */
		//System.out.println(classname+" appendmode: "+manager.isResume());
		
		/*Write contents of output list to file*/
		writeOutputListToFile(manager.isResume()); 
	}
	
	private void handleSetEAttributeSingleEvent(SetEAttributeSingleEvent e)
	{
		EObject focus_obj = e.getFocusObj();
		
		EAttribute attr = e.getEAttribte();
		
		String newValue = EcoreUtil.convertToString(attr.getEAttributeType(), e.getNewValue());
		
		if(newValue == null)
			newValue = "null";
		
		newValue = newValue.replace(PersistenceManager.DELIMITER, PersistenceManager.ESCAPE_CHAR+PersistenceManager.DELIMITER); //escape delimiter (if any)
		
		output_list.add(PersistenceManager.SET_EATTRIBUTE_VALUE+" "+attr.getName()+" "+focus_obj.eClass().getName()+" "
				+changelog.getObjectId(focus_obj)+" ["+newValue+"]");
	}
	
	/*private boolean isInitialEntrySerialised() 
	{
		
		// if output file doesn't exits, initial entry doesn't exist
		File f = new File(manager.getURI().path());
		if(!f.exists() || f.isDirectory())
			return false;
		
		// output file exists, check it for 'namespace' entry
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(manager.getURI().path()), manager.TEXT_ENCODING));)
		{
			String line;
			
			if((line = br.readLine()) != null)
			{
				return line.contains("NAMESPACE_URI");
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}*/
	
	private void handleUnsetEAttributeSingleEvent(UnsetEAttributeSingleEvent e)
	{
		EObject focus_obj = e.getFocusObject();
		
		EAttribute attr = e.getEAttribute();
		
		String oldValue = EcoreUtil.convertToString(attr.getEAttributeType(), e.getOldValue());
		
		oldValue = oldValue.replace(PersistenceManager.DELIMITER, PersistenceManager.ESCAPE_CHAR+PersistenceManager.DELIMITER); //escape delimiter (if any)
		
		output_list.add(PersistenceManager.UNSET_EATTRIBUTE_VALUE+" "+attr.getName()+" "+focus_obj.eClass().getName()+" "
					+changelog.getObjectId(focus_obj)+" ["+oldValue+"]");
	}
	
	private void handleSetEAttributeManyEvent(SetEAttributeManyEvent e)
	{
		EObject focus_obj = e.getFocusObj();
		EAttribute attr = e.getEAttribute();
		
		List<Object> attr_values_list = e.getAttributeValuesList();
		
		String obj_list_str = "[";
		
		for(Object object: attr_values_list)
		{
			String newValue = (EcoreUtil.convertToString(attr.getEAttributeType(), object));
			
			if(newValue == null)
				newValue = "null";
			
			newValue = newValue.replace(PersistenceManager.DELIMITER, PersistenceManager.ESCAPE_CHAR+PersistenceManager.DELIMITER); //escape delimiter
			
			obj_list_str = obj_list_str +newValue+PersistenceManager.DELIMITER;
		}
		obj_list_str = obj_list_str.substring(0,obj_list_str.length()-1)+"]"; // remove final delimiter  add "]"
		output_list.add(PersistenceManager.SET_EATTRIBUTE_VALUE+" "+attr.getName()+" "+focus_obj.eClass().getName()+" "
				+changelog.getObjectId(focus_obj)+" "+obj_list_str);
	}
	
	private void handleUnsetEAttributeManyEvent(UnsetEAttributeManyEvent e)
	{
		EObject focus_obj = e.getFocusObj();
		EAttribute attr = e.getEAttribute();
		
		
		List<Object> attr_values_list = e .getAttributeValuesList();
		
		String obj_list_str = "[";
		
		for(Object object: attr_values_list)
		{
			String newValue = (EcoreUtil.convertToString(attr.getEAttributeType(), object));
			obj_list_str = obj_list_str + newValue+PersistenceManager.DELIMITER;
		}
		obj_list_str = obj_list_str.substring(0,obj_list_str.length()-1)+"]"; // remove final delimiter  add "]"
		output_list.add(PersistenceManager.UNSET_EATTRIBUTE_VALUE+" "+attr.getName()+" "+focus_obj.eClass().getName()+" "
				+changelog.getObjectId(focus_obj)+" "+obj_list_str);
		
	}
	
	private void handleSetEReferenceSingleEvent(SetEReferenceSingleEvent e)
	{
		EObject added_obj = e.getAddedObject();
		
		if(changelog.addObjectToMap(added_obj))//make 'create' entries for obj which don't already exist
			output_list.add(PersistenceManager.CREATE+" ["+added_obj.eClass().getName()+ " "+changelog.getObjectId(added_obj)+"]");
			
		if(e.getNotifierType() == Event.NotifierType.RESOURCE) //add eobject to resource
		{
			output_list.add(PersistenceManager.ADD_TO_RESOURCE+" ["+added_obj.eClass().getName()+" "+changelog.getObjectId(added_obj)+"]"); 
		}
		else if(e.getNotifierType() == Event.NotifierType.EOBJECT) //add eobject to eobject
		{
			EObject focus_obj = e.getFocusObject();
			
			output_list.add(PersistenceManager.SET_EREFERENCE_VALUE+" "+e.getEReference().getName()+" "+focus_obj.eClass().getName()+" "
					+changelog.getObjectId(focus_obj)+" ["+added_obj.eClass().getName()+" "
					+changelog.getObjectId(added_obj)+"]");
		}
	}
	
	private void handleUnsetEReferenceSingleEvent(UnsetEReferenceSingleEvent e)
	{
		EObject removed_obj = e.getRemovedObject();
		long removed_obj_id = changelog.getObjectId(removed_obj);
		if(e.getNotifierType() == Event.NotifierType.RESOURCE) //delete eobject from resource
		{
			output_list.add(PersistenceManager.DELETE_FROM_RESOURCE+" ["+removed_obj.eClass().getName()+" "+
					removed_obj_id+"]"); 
			
			changelog.deleteEObjectFromMap(removed_obj_id);
		}
		else if(e.getNotifierType() == Event.NotifierType.EOBJECT)
		{
			EObject focus_obj = e.getFocusObject();
			
			output_list.add(PersistenceManager.UNSET_EREFERENCE_VALUE+" "+e.getEReference().getName()+" "+focus_obj.eClass().getName()+" "
					+changelog.getObjectId(focus_obj)+" ["+removed_obj.eClass().getName()+" "
					+removed_obj_id+"]");
			
			changelog.deleteEObjectFromMap(removed_obj_id);
		}
		
	}
	
	private void handleSetEReferenceManyEvent(SetEReferenceManyEvent e)
	{
		List<EObject> obj_list = e.getEObjectList();
		
		String added_obj_list_str = "["; //list of obj to add
		String obj_create_list_str = "["; //list of obj to create

		
		for(EObject obj : obj_list)
		{
			if(changelog.addObjectToMap(obj)) //if obj does not already exist
			{
				obj_create_list_str = obj_create_list_str + obj.eClass().getName()+" "
						+ ""+changelog.getObjectId(obj)+PersistenceManager.DELIMITER; 
			}
			
			added_obj_list_str = added_obj_list_str + obj.eClass().getName()+" "+changelog.getObjectId(obj)+PersistenceManager.DELIMITER; 
		}
		
		if(obj_create_list_str.length()>1) //if we have items to create...
		{
			 obj_create_list_str = obj_create_list_str.substring(0,added_obj_list_str.length()-1)+"]"; // remove final delimiter  add "]"
			 output_list.add(PersistenceManager.CREATE+" "+ obj_create_list_str);
		}
		
	    added_obj_list_str = added_obj_list_str.substring(0,added_obj_list_str.length()-1)+"]"; // remove final delimiter add "]"
	    
	    if(e.getNotifierType() == Event.NotifierType.RESOURCE) //add eobject to resource
	    {
	    	output_list.add(PersistenceManager.ADD_TO_RESOURCE+" "+added_obj_list_str);
	    }	
	    else if(e.getNotifierType() == Event.NotifierType.EOBJECT) //add eobject to eobject
	    {
	    	EObject focus_obj = e.getFocusObj();
	    	
	    	output_list.add(PersistenceManager.SET_EREFERENCE_VALUE+" "+e.getEReference().getName()+" "+focus_obj.eClass().getName()+" "
					+changelog.getObjectId(focus_obj)+" "+added_obj_list_str);
	    }
	    
	}
	
	private void handleUnsetEReferenceManyEvent(UnsetEReferenceManyEvent e)
	{
		List<EObject> removed_obj_list = e.getObjectList();
		
		String obj_delete_list_str = "["; //list of obj to delete
		
		for (EObject obj : removed_obj_list)
		{
			long removed_obj_id = changelog.getObjectId(obj);
			
			obj_delete_list_str = obj_delete_list_str +  obj.eClass().getName()+" "+removed_obj_id +PersistenceManager.DELIMITER; 
			
			changelog.deleteEObjectFromMap(removed_obj_id);	
		}
		
		obj_delete_list_str = obj_delete_list_str.substring(0,obj_delete_list_str.length()-1)+"]";
		
		if(e.getNotiferType() == Event.NotifierType.RESOURCE) //DELETE OBJs FROM RESOURCE
		{
			output_list.add(PersistenceManager.DELETE_FROM_RESOURCE+" "+obj_delete_list_str);
		}
		else if(e.getNotiferType() == Event.NotifierType.EOBJECT)
		{
			EObject focus_obj = e.getFocusObj();
			
			output_list.add(PersistenceManager.UNSET_EREFERENCE_VALUE+" "+(e.getEReference().getName()+" "+focus_obj.eClass().getName()+" "
							+changelog.getObjectId(focus_obj)+" "+obj_delete_list_str));
		}	
	
	}
	
	private void addInitialEntryToOutputList() 
	{
		EObject obj = null;
		Event e = event_list.get(0);
		
		switch(e.getEventType())
		{
		case Event.SET_EREFERENCE_SINGLE:
			obj = ((SetEReferenceSingleEvent)e).getAddedObject();
			break;
		case Event.SET_EREFERENCE_MANY:
			obj = ((SetEReferenceManyEvent)e).getEObjectList().get(0);
			break;
		default:
			try 
			{
				System.out.println(e.getEventType());
				throw new Exception ("Error! first item in events list was not an Add event.");
			} catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
		
		if(obj == null)
		{
			System.out.println(e.getEventType());
			System.exit(0);
		}
		output_list.add("NAMESPACE_URI "+obj.eClass().getEPackage().getNsURI());
	}
	
	
	private void writeOutputListToFile(boolean appendMode)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter
				    (new OutputStreamWriter(new FileOutputStream(manager.getURI().path(),appendMode),
				    		Charset.forName(manager.TEXT_ENCODING).newEncoder()));
			PrintWriter out = new PrintWriter(bw);
			for(String string: output_list)
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
		
		manager.setResume(true);
	}	
}
