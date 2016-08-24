package change;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import impl.CBPResource;

public abstract class EReferenceEvent extends Event
{
	public static enum NotifierType
	{
		EOBJECT,
		RESOURCE;
	}
	
	private NotifierType notifierType;
	
	private List<EObject> eObjectList = new ArrayList<EObject>();
	
	private EObject focusObject;
	
	private EReference eReference;
	
    @SuppressWarnings("unchecked")
	public EReferenceEvent(int eventType, EObject focusObject,Object value,EReference eReference)
    {
        super(eventType);
        
        this.focusObject = focusObject;
        
        if(value instanceof Collection)
        	this.eObjectList = (List<EObject>) value;
        
        else
        	this.eObjectList.add((EObject) value);
        
        this.notifierType = NotifierType.EOBJECT;
        
        this.eReference = eReference; 
    }
    
    @SuppressWarnings("unchecked")
	public EReferenceEvent(int eventType, Object value)
    {
        super(eventType);
        
        if(value instanceof Collection)
        	this.eObjectList = (List<EObject>) value;
        
        else
        	this.eObjectList.add((EObject) value);
        
        this.notifierType = NotifierType.RESOURCE;
    }
    
    @SuppressWarnings("unchecked")
	public EReferenceEvent(int eventType, Notification n)
    {
    	super(eventType);
    	
    	if(eventType == Event.SET_EREFERENCE_EVENT)
    	{
    		if(n.getNewValue() instanceof Collection)
    			this.eObjectList = (List<EObject>) n.getNewValue();
    		
    		else //n.getNewValue() ! instanceof Collection
    			this.eObjectList.add((EObject) n.getNewValue());
    	}
    	else //eventType == Event.UNSET_EREFERENCE_EVENT
    	{
    		if(n.getOldValue() instanceof Collection)
    			this.eObjectList = (List<EObject>) n.getOldValue();
    		
    		else //n.getNewValue() ! instanceof Collection
    			this.eObjectList.add((EObject) n.getOldValue());
    	}
    	
    	if(n.getNotifier() instanceof CBPResource)
        {
            notifierType = NotifierType.RESOURCE;
        }
        else if(n.getNotifier() instanceof EObject)
        {
            notifierType = NotifierType.EOBJECT;
            focusObject = (EObject) n.getNotifier();
            eReference = (EReference) n.getFeature();
        } 
    }
    
    public NotifierType getNotifierType()
    {
        return this.notifierType;
    }
    
    public EObject getFocusObject()
    {
        return focusObject;
    }
    
    public List<EObject> getEObjectList()
    {
        return eObjectList;
    }
    
    public EReference getEReference()
    {
        return eReference;
    }
}
