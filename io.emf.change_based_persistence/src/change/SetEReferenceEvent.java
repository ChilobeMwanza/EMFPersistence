package change;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class SetEReferenceEvent extends EReferenceEvent
{
	/*EObject added to another EObject via some EReference*/
	public SetEReferenceEvent(EObject focusObject,Object newValue,EReference eReference)
    {
        super(Event.SET_EREFERENCE_EVENT, focusObject, newValue, eReference);
    }
	
	/*EObject added directly to resource contents*/
	public SetEReferenceEvent(Object newValue) //i.e objects added to resource
	{
		super(Event.SET_EREFERENCE_EVENT,newValue);
	}
    
    public SetEReferenceEvent(Notification n)
    {
       super(Event.SET_EREFERENCE_EVENT,n);
    } 
}


