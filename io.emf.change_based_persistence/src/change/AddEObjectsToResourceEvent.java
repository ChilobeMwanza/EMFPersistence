package change;

import org.eclipse.emf.common.notify.Notification;

public class AddEObjectsToResourceEvent extends ResourceEvent
{
	public AddEObjectsToResourceEvent(Notification n)
	{
		super(Event.ADD_EOBJECTS_TO_RESOURCE_EVENT,n);
	}
	
	public AddEObjectsToResourceEvent(Object addedEObjects)
	{
		super(Event.ADD_EOBJECTS_TO_RESOURCE_EVENT,addedEObjects);
	}
}
