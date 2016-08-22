/*
 * todo:
 * make txt format less verbose
 * when unsetting single valued feature, value of feature is not needed
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
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.profiling.Stopwatch;

import change.Changelog;
import change.Event;
import change.SetEAttributeEvent;
import change.SetEReferenceEvent;
import change.UnsetEAttributeEvent;
import change.UnsetEReferenceEvent;

public class CBPTextSerializer 
{
	private  final String classname = this.getClass().getSimpleName();
	
	private final String FORMAT_ID = "CBP_TEXT"; 
	
	private final double VERSION = 1.0;
	
    private final List<Event> eventList;
    
	private final PersistenceManager manager;
	
	private final Changelog changelog; 
	
	private final EPackageElementsNamesMap ePackageElementsNamesMap;
	
	private PrintWriter printWriter;
	
	public CBPTextSerializer(PersistenceManager manager, Changelog aChangelog, EPackageElementsNamesMap 
			ePackageElementsNamesMap)
	{
		this.manager =  manager;
		this.changelog = aChangelog;
		this.ePackageElementsNamesMap = ePackageElementsNamesMap;
		
		this.eventList = manager.getChangelog().getEventsList();
	}
	
	public void save(Map<?,?> options)
	{
		if(eventList.isEmpty())
			return;
		
		//setup printwriter
	    try
        {
	    	BufferedWriter bw = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(manager.getURI().path(),manager.isResume()),
                            manager.STRING_ENCODING));
            printWriter = new PrintWriter(bw);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
		
		//if we're not in resume mode, serialise initial entry
		if(!manager.isResume())
			serialiseInitialEntry();
		
		for(Event e : eventList)
		{
			switch(e.getEventType())
			{
			case Event.SET_EATTRIBUTE_EVENT:
				handleSetEAttributeEvent((SetEAttributeEvent)e);
				break;
			case Event.SET_EREFERENCE_EVENT:
				handleSetEReferenceEvent((SetEReferenceEvent)e);
				break;
			case Event.UNSET_EATTRIBUTE_EVENT:
				handleUnsetEAttributeEvent((UnsetEAttributeEvent)e);
				break;
			case Event.UNSET_EREFERENCE_EVENT:
				handleUnsetEReferenceEvent((UnsetEReferenceEvent)e);
				break;
			}
		}
		
		changelog.clearEvents();
		
		printWriter.close();
		manager.setResume(true);
	}
	
	private void handleSetEAttributeEvent(SetEAttributeEvent e)
	{
		EObject focus_obj = e.getFocusObj();
		EAttribute attr = e.getEAttribute();
		
		List<Object> attr_values_list = e.getAttributeValuesList();
		
		StringBuilder obj_list_blr = new StringBuilder("[");
		
		EDataType type = attr.getEAttributeType();
		for(Object object: attr_values_list)
		{
			String newValue = (EcoreUtil.convertToString(type, object));
			
			if(newValue == null)
				newValue = manager.NULL_STRING;
			
			newValue = newValue.replace(PersistenceManager.DELIMITER, 
					PersistenceManager.ESCAPE_CHAR+PersistenceManager.DELIMITER); //escape delimiter
			
			obj_list_blr.append(newValue).append(PersistenceManager.DELIMITER);
		}
		
		obj_list_blr.substring(0,obj_list_blr.length()-1);
		obj_list_blr.append("]");
		
	
		printWriter.println(PersistenceManager.SET_COMPLEX_EATTRIBUTE_VALUE
				+" "+ePackageElementsNamesMap.getID(attr.getName())+" "
				+changelog.getObjectId(focus_obj)+" "+obj_list_blr.toString());
	}
	
	private void handleUnsetEAttributeEvent(UnsetEAttributeEvent e)
	{
		EObject focus_obj = e.getFocusObj();
		EAttribute attr = e.getEAttribute();
		
		List<Object> attr_values_list = e .getAttributeValuesList();
		
		StringBuilder sb = new StringBuilder("[");
		
		
		for(Object object: attr_values_list)
		{
			String newValue = (EcoreUtil.convertToString(attr.getEAttributeType(), object));
			sb.append(newValue).append(PersistenceManager.DELIMITER);
		}
		
		sb.substring(0,sb.length());
		sb.append("]");
		
		printWriter.println(PersistenceManager.UNSET_COMPLEX_EATTRIBUTE_VALUE
				+" "+ePackageElementsNamesMap.getID(attr.getName())+" "
				+changelog.getObjectId(focus_obj)+" "+sb.toString());
	}
	
	private void handleSetEReferenceEvent(SetEReferenceEvent e)
	{
		//Stopwatch s = new Stopwatch();
		//s.resume();
		
		StringBuilder added_obj_list_blr = new StringBuilder("[");
		StringBuilder obj_create_list_blr = new StringBuilder("[");
	
		for(EObject obj : e.getEObjectList())
		{
			if(changelog.addObjectToMap(obj)) //if obj does not already exist
			{
				obj_create_list_blr.append(ePackageElementsNamesMap.getID(obj.eClass().getName())).append(" ")
					.append(changelog.getObjectId(obj)).append(PersistenceManager.DELIMITER); //may swithc to read scan and write
			}
			else //obj exists, i.e we're updating some reference
			{
				added_obj_list_blr.append(changelog.getObjectId(obj)+PersistenceManager.DELIMITER); 
			}
		}
		
		if(e.getNotifierType() == Event.NotifierType.RESOURCE) //updating a resource ref
	    {
			if(obj_create_list_blr.length()>1) //if we have items to create,
			{
				 obj_create_list_blr.substring(0,obj_create_list_blr.length()-1);
				 obj_create_list_blr.append("]");
				 
				 StringBuilder sb = new StringBuilder().append(PersistenceManager.CREATE_AND_ADD_TO_RESOURCE)
						 .append(" ").append(obj_create_list_blr);
				
				 printWriter.println(sb.toString());
			}
			if(added_obj_list_blr.length() > 1) //if we have items to update
			{
				added_obj_list_blr.substring(0,added_obj_list_blr.length()-1);
				added_obj_list_blr.append("]");
				
				StringBuilder sb = new StringBuilder().append(PersistenceManager.ADD_TO_RESOURCE)
						.append(" ").append(added_obj_list_blr);
				
				printWriter.println(sb.toString());
			}
	    }
		else if(e.getNotifierType() == Event.NotifierType.EOBJECT) 
		{
			EObject focus_obj = e.getFocusObj();
			
			if(obj_create_list_blr.length()>1) //if we have items to create
			{
				 obj_create_list_blr.substring(0,obj_create_list_blr.length()-1);
				 obj_create_list_blr.append("]");
				 
				 StringBuilder sb = new StringBuilder().append(PersistenceManager.CREATE_AND_SET_EREFERENCE_VALUE)
						 .append(" ").append(ePackageElementsNamesMap.getID(e.getEReference().getName()))
						 .append(" ").append(changelog.getObjectId(focus_obj)).append(" ").append(obj_create_list_blr);
				 
				 printWriter.println(sb.toString());
			}
			if(added_obj_list_blr.length() > 1)
			{
				added_obj_list_blr.substring(0,added_obj_list_blr.length()-1);
				added_obj_list_blr.append("]");
				
				StringBuilder sb = new StringBuilder().append(PersistenceManager.SET_EREFERENCE_VALUE)
						.append(" ").append(ePackageElementsNamesMap.getID(e.getEReference().getName()))
						.append(" ").append(changelog.getObjectId(focus_obj)).append(" ")
						.append(added_obj_list_blr);
				
				printWriter.println(sb.toString());
			}   	
		}
		
	 //  s.pause();
	    
	   // System.out.println("Time taken : "+ s.getElapsed());
	   // System.exit(0);
	    
	}
	
	private void handleUnsetEReferenceEvent(UnsetEReferenceEvent e)
	{
		//STRING BUILDER IS NOT USED! FIX
		List<EObject> removed_obj_list = e.getObjectList();
		
		String obj_delete_list_str = "["; 
		//StringBuilder sb = new StringBuilder("]");//list of obj to delete
		
		for (EObject obj : removed_obj_list)
		{
			long removed_obj_id = changelog.getObjectId(obj);
			
			obj_delete_list_str = obj_delete_list_str+removed_obj_id +PersistenceManager.DELIMITER; 
			//sb.append(removed_obj_id).append(PersistenceManager.DELIMITER);
			
			changelog.deleteEObjectFromMap(obj);	
		}
		
		 obj_delete_list_str = obj_delete_list_str.substring(0,obj_delete_list_str.length()-1)+"]";
		
		if(e.getNotiferType() == Event.NotifierType.RESOURCE) //DELETE OBJs FROM RESOURCE
		{
			printWriter.println(PersistenceManager.DELETE_FROM_RESOURCE+" "+obj_delete_list_str);
		}
		else if(e.getNotiferType() == Event.NotifierType.EOBJECT)
		{
			EObject focus_obj = e.getFocusObj();
			
			printWriter.println(PersistenceManager.UNSET_EREFERENCE_VALUE+" "
					+(ePackageElementsNamesMap.getID(e.getEReference().getName())+" "
					+changelog.getObjectId(focus_obj)+" "+obj_delete_list_str));
		}	
	
	}
	
	private void serialiseInitialEntry() 
	{
		EObject obj = null;
		Event e = eventList.get(0);
		
		if(e.getEventType()!= Event.SET_EREFERENCE_EVENT) //tbr
		{
			try 
			{
				System.out.println(classname+" "+e.getEventType());
				throw new Exception("Error! first item in events list was not an Add event.");
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
				System.exit(0);
			}
		}
		obj = ((SetEReferenceEvent)e).getEObjectList().get(0);
		
		if(obj == null) //TBR
		{
			System.out.println(classname+" "+e.getEventType());
			System.exit(0);
		}
		
	
		printWriter.println(FORMAT_ID+" "+VERSION);
		printWriter.println("NAMESPACE_URI "+obj.eClass().getEPackage().getNsURI());
	}
}
