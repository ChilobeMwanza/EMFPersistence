package change;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import impl.CBPResource;

public class SetEReferenceEvent extends Event 
{
	private NotifierType type;
	
	private List<EObject> obj_list = new ArrayList<EObject>();
	
	private EObject focus_obj;
	
	private EReference eref;
	
	@SuppressWarnings("unchecked")
	public SetEReferenceEvent(EObject newValue, NotifierType type)
    {
        super(Event.SET_EREFERENCE_EVENT);
        
        if(newValue instanceof Collection)
        	this.obj_list = (List<EObject>) newValue;
        
        else
        	this.obj_list.add((EObject) newValue);
        
        this.type = type;
    }
    
    @SuppressWarnings("unchecked")
	public SetEReferenceEvent(EObject focus_obj,EObject newValue,EReference eref, NotifierType type)
    {
        super(Event.SET_EREFERENCE_EVENT);
        
        this.focus_obj = focus_obj;
        
        if(newValue instanceof Collection)
        	this.obj_list = (List<EObject>) newValue;
        
        else
        	this.obj_list.add((EObject) newValue);
        
        this.type = type;
        
        this.eref = eref;
    }
    
    
    @SuppressWarnings("unchecked")
    public SetEReferenceEvent(Notification n)
    {
    	
        super(Event.SET_EREFERENCE_EVENT);
        
        Object newValue = n.getNewValue();
        
        if(newValue instanceof Collection)
        	this.obj_list = (List<EObject>) newValue;
        
        else
        	this.obj_list.add((EObject) newValue);
        
         if(n.getNotifier() instanceof CBPResource)
         {
             this.type = NotifierType.RESOURCE;
         }
         else if(n.getNotifier() instanceof EObject)
         {
             this.type = NotifierType.EOBJECT;
             this.focus_obj = (EObject) n.getNotifier();
             this.eref = (EReference) n.getFeature();
         } 
    }
    
    public NotifierType getNotifierType()
    {
        return this.type;
    }
    
    public EObject getFocusObj()
    {
        return focus_obj;
    }
    
    public List<EObject> getEObjectList()
    {
        return this.obj_list;
    }
    
    public EReference getEReference()
    {
        return this.eref;
    }
}

