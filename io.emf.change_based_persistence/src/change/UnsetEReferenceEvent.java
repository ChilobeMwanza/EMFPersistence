package change;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class UnsetEReferenceEvent extends EReferenceEvent
{
	/*EObject added to another EObject via some EReference*/
	public UnsetEReferenceEvent(EObject focusObject,Object oldValue,EReference eReference)
    {
        super(Event.UNSET_EREFERENCE_EVENT, focusObject, oldValue, eReference);
    }
	
	/*EObject added directly to resource contents*/
	public UnsetEReferenceEvent(Object oldValue) //i.e objects added to resource
	{
		super(Event.UNSET_EREFERENCE_EVENT, oldValue);
	}
    
    public UnsetEReferenceEvent(Notification n)
    {
       super(Event.UNSET_EREFERENCE_EVENT,n);
    } 
}


