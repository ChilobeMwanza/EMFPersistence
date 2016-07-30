/**
 * notification.getNotifier().getClass(); should use eclass instead, but how?
 */
package change;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;

import org.eclipse.epsilon.profiling.Stopwatch;

public class EventAdapter extends EContentAdapter
{
	private ChangeLog changeLog;
	private final String RESOURCE_NAME = "DeltaResourceImpl";
	
	public EventAdapter(ChangeLog aChangelog)
	{
		this.changeLog = aChangelog;
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
				addObjectToResource((EObject)msg.getNewValue());
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
	
/*	void doSomething(Eobject a)
	{
	create object a
	set attributes
	add a to to destination

	for each child of a,c
	doSomething(c,dest)
	}*/


	private void addObjectToResource(EObject obj)
	{
		createNewObjectEntry(obj);
		changeLog.addObjectToMap(obj);
		createSetAttributeEntries(obj);
		
		AddToResourceEntry entry = new AddToResourceEntry(obj);
		changeLog.addEvent(entry);
		
		//Stopwatch stopwatch = new Stopwatch();
		
	
		//handleContainments(obj);
	
		//stopwatch.resume();
		handleContainments(obj);
		//stopwatch.pause();
		//System.out.println("non recursive speed: " + stopwatch.getElapsed());
	}
	
	private void handleContainmentsRecursive(EObject obj)
	{
		for(EObject o: obj.eContents())//for all the objects containment refs
		{
			//System.out.println(o.eContainingFeature().getName());
			createNewObjectEntry(o);
			
			changeLog.addObjectToMap(o);
			
			createSetAttributeEntries(o);
			
			if(changeLog.getObjectId(obj)== -1) //tbr
				System.out.println("EventAdapter: error !!! adding object to an object which"
						+ " has not yet been created!");
			
			createAddLinkEntry (o,obj,o.eContainmentFeature());
			
			handleContainmentsRecursive(o);
			
	    } 
	}
	
	private void handleContainments(EObject root) 
	{
		//List <EObject> objects = new ArrayList<EObject>();
		
		for(TreeIterator<EObject> it = root.eAllContents(); it.hasNext();)
		{
			EObject obj = it.next();
			createNewObjectEntry(obj);
			changeLog.addObjectToMap(obj);
			createSetAttributeEntries(obj);
			
			if(changeLog.getObjectId(obj.eContainer())== -1) //tbr
				System.out.println("EventAdapter: error !!! adding object to an object which"
						+ " has not yet been created!");
			createAddLinkEntry(obj,obj.eContainer(),obj.eContainmentFeature());
		}
	}
	
	private void createAddLinkEntry(EObject obj, EObject dest, EReference eRef)
	{
		AddLinkEntry entry = new AddLinkEntry(obj,dest,eRef);
		changeLog.addEvent(entry);
	}

	private void createSetAttributeEntries(EObject obj)
	{
		for(EAttribute attr : obj.eClass().getEAllAttributes()) //e vs eall
		{
			if(obj.eIsSet(attr))
			{
				SetAttributeEntry setAttrEntry = new SetAttributeEntry(obj,attr,obj.eGet(attr));
				changeLog.addEvent(setAttrEntry); //add to entry
			}
		}
	}
	
	private void createNewObjectEntry(EObject obj)
	{
		/*Create 'NewObjectEntry' for this object*/
		NewObjectEntry entry = new NewObjectEntry(obj);
		changeLog.addEvent(entry);
	}
}
