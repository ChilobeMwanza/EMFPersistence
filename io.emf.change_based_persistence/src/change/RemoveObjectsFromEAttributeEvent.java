package change;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class RemoveObjectsFromEAttributeEvent extends EAttributeEvent
{
	public RemoveObjectsFromEAttributeEvent(EObject focusObject, EAttribute eAttribute, Object oldValue)
    {
       super(Event.REMOVE_OBJECTS_FROM_EATTRIBUTE_EVENT, focusObject, eAttribute, oldValue);  
    }
    
    public RemoveObjectsFromEAttributeEvent(Notification n)
    {
        this((EObject) n.getNotifier(),(EAttribute) n.getFeature(),n.getOldValue());
    }
}
