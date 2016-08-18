package change;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import impl.DeltaResourceImpl;

public class UnsetEReferenceManyEvent extends Event 
{
	private NotifierType type;
	
	private List<EObject> obj_list;
	private EObject focus_obj;
	private EReference eref;
	
	public UnsetEReferenceManyEvent(List<EObject> obj_list, NotifierType type)
	{
		super(Event.UNSET_EREFERENCE_MANY);
		
		this.obj_list = obj_list;
		this.type = type;
	}
	
	public UnsetEReferenceManyEvent(List<EObject> obj_list, EObject focus_obj, EReference eref, NotifierType type)
	{
		super(Event.UNSET_EREFERENCE_MANY);
		
		this.obj_list = obj_list;
		this.focus_obj = focus_obj;
		this.eref = eref;
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	public UnsetEReferenceManyEvent(Notification n)
	{
		super(Event.UNSET_EREFERENCE_MANY);
		
		this.obj_list = (List<EObject>) n.getOldValue();
		
		if(n.getNotifier() instanceof DeltaResourceImpl)
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
	
	public List<EObject> getObjectList()
	{
		return this.obj_list;
	}
	
	public EObject getFocusObj()
	{
		return this.focus_obj;
	}
	
	public NotifierType getNotiferType()
	{
		return this.type;
	}
	
	public EReference getEReference()
	{
		return this.eref;
	}
}