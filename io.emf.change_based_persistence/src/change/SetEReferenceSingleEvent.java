package change;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import impl.CBPTextResourceImpl;

public class SetEReferenceSingleEvent extends Event
{
	private EObject added_obj;
	private EObject focus_obj;
	private EReference eref;
	private NotifierType notifier_type;
	
	public SetEReferenceSingleEvent(EObject added_obj,NotifierType notifier_type)
	{
		super(Event.SET_EREFERENCE_SINGLE);
		
		this.added_obj = added_obj;
		this.notifier_type = notifier_type;
	}
	
	public SetEReferenceSingleEvent(EObject added_obj, EObject focus_obj,EReference eref, NotifierType notifier_type)
	{
		super(Event.SET_EREFERENCE_SINGLE);
		
		this.added_obj = added_obj;
		this.focus_obj = focus_obj;
		this.notifier_type = notifier_type;
		this.eref = eref;
	}
	
	public SetEReferenceSingleEvent(Notification n)
	{
		super(Event.SET_EREFERENCE_SINGLE);
		
		this.added_obj = (EObject)n.getNewValue();
		
		if(n.getNotifier() instanceof CBPTextResourceImpl)
		{
			this.notifier_type = NotifierType.RESOURCE;
		}
		else if(n.getNotifier() instanceof EObject)
		{
			this.notifier_type = NotifierType.EOBJECT;
			this.focus_obj = (EObject) n.getNotifier();
			this.eref = (EReference) n.getFeature();
		}	
	}
	
	public EObject getAddedObject()
	{
		return this.added_obj;
	}
	
	public EObject getFocusObject()
	{
		return this.focus_obj;
	}
	
	public NotifierType getNotifierType()
	{
		return this.notifier_type;
	}
	
	public EReference getEReference()
	{
		return this.eref;
	}
	
}
