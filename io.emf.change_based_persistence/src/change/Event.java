package change;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

public abstract class Event 
{
	private int eventType;
	
	public static final int ADD_EOBJECTS_TO_RESOURCE_EVENT = 0;
	public static final int ADD_EOBJECTS_TO_EREFERENCE_EVENT = 1;
	public static final int ADD_OBJECTS_TO_EATTRIBUTE_EVENT = 2;
	public static final int REMOVE_EOBJECTS_FROM_RESOURCE_EVENT = 3;
	public static final int REMOVE_EOBJECTS_FROM_EREFERENCE_EVENT = 4;
	public static final int REMOVE_OBJECTS_FROM_EATTRIBUTE_EVENT = 5;
	
	protected List<EObject> eObjectList = new ArrayList<EObject>();
	protected List<Object> eAttributeValuesList = new ArrayList<Object>();

	public Event(int eventType)
	{
		this.eventType = eventType;
	}
	
	public int getEventType()
	{
		return eventType;
	}
	
	public List<Object> getEAttributeValuesList()
	{
		return eAttributeValuesList;	
	}
	
	public List<EObject> getEObjectList()
	{
		return eObjectList;	
	}
	
}
