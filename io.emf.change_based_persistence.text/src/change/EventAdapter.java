/**
 * notification.getNotifier().getClass(); should use eclass instead, but how?
 */
package change;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class EventAdapter extends EContentAdapter
{
	private boolean initEntryAdded = false;
	private ChangeLog changeLog;
	private final String RESOURCE_NAME = "DeltaResourceImpl";
	
	BiMap<EObject,UUID> map = HashBiMap.create(); //bi map , 
		
	public EventAdapter(ChangeLog aChangelog)
	{
		this.changeLog = aChangelog;
	}
	
	public EventAdapter()
	{
		
	}
	
	
	@Override
	public void notifyChanged(Notification msg)
	{
		super.notifyChanged(msg);
		
		if(msg.isTouch())
			return; 
		//System.out.println("EventAdapter: "+msg.getEventType());
		
		//Get the class of the object affected by the change.
	    Class<? extends Object> affectedClass = msg.getNotifier().getClass(); //if we just want a name, get notifer works.
		 
		
	   
		 int eventType = msg.getEventType();
	   // Object object = notification.getNotifier();
	    
	   // Object object = notification.getNewValue();
	    
	//	System.out.println("Change made to: "+affectedClass
	//	.getSimpleName());
		
	//	System.out.println("Event type :"+eventType);
		
		
	  // System.out.println("debugging object: "+object.getClass().);
		
	
		switch(msg.getEventType())
		{
		case Notification.ADD:
			if(msg.getNotifier().getClass().getSimpleName().equals(RESOURCE_NAME))
				addObjectToResource(msg);
			//System.out.println("EventAdapter: Add just happened!");
			//EObject eObject = (EObject)msg.getNewValue();
		//	AddToResourceEntry entry = new AddToResourceEntry(eObject,msg.getNotifier().getClass().getSimpleName());
			//changeLog.addEvent(entry);
			break;
		case Notification.REMOVE:
			
		default:
			//System.out.println("EventAdapater.java default");
			break;
		}
	}
	
	private void addObjectToResource(Notification msg)
	{
		EObject obj = (EObject) msg.getNewValue();
		
		/*Add object to our map*/
		if(map.get(obj) == null)
		{
			//System.out.println("Event Adapter: map is null for this obj, adding entry");
			map.put(obj, UUID.randomUUID());
		}
		else //tbr after handle remove
		{
			System.out.println("Event Adapter: Impossible!");
		}
		
		/*Create 'NewObjectEntry' for this object*/
		NewObjectEntry newObjectEntry = new NewObjectEntry((EObject) msg.getNewValue(), map.get(obj));
		changeLog.addEvent(newObjectEntry);
				
				
		//AddToResourceEntry entry = new AddToResourceEntry(msg,map.get(obj)); 
		//changeLog.addEvent(entry);
		
		/*Check if object has any features set, if so, add relevant 'setAttribute' entries to changelog*/
		for(EAttribute attr : obj.eClass().getEAllAttributes())
		{
			if(obj.eIsSet(attr))
			{
				SetAttributeEntry setAttrEntry = new SetAttributeEntry(obj,attr,obj.eGet(attr),map.get(obj));
				changeLog.addEvent(setAttrEntry); //add to entry
			}
			//System.out.println(attr.getName());
			//System.out.println("is set: "+obj.eIsSet(attr));
		}


		//deal with contained objects
		Iterator<EObject> it = obj.eAllContents();
		while(it.hasNext())
		{
			//System.out.println(it.next().eClass().getName());
		}
		
		//add obj to resource
	}
	
	private void removeObjectFromResource(Notification msg)
	{
		
	}
	
	private void handleAddEvent()
	{
		
	}
}
