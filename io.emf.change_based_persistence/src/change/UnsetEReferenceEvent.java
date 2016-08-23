package change;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import impl.CBPResource;

public class UnsetEReferenceEvent extends Event {
	private NotifierType type;

	private List<EObject> obj_list = new ArrayList<EObject>();
	private EObject focus_obj;
	private EReference eref;

	@SuppressWarnings("unchecked")
	public UnsetEReferenceEvent(Object oldValue, EObject focus_obj, EReference eref, NotifierType type) {
		super(Event.UNSET_EREFERENCE_EVENT);

		if (oldValue instanceof Collection)
			this.obj_list = (List<EObject>) oldValue;

		else
			this.obj_list.add((EObject) oldValue);

		this.focus_obj = focus_obj;
		this.eref = eref;
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public UnsetEReferenceEvent(Notification n) {
		super(Event.UNSET_EREFERENCE_EVENT);

		Object oldValue = n.getOldValue();

		if (oldValue instanceof Collection)
			this.obj_list = (List<EObject>) oldValue;

		else
			this.obj_list.add((EObject) oldValue);

		if (n.getNotifier() instanceof CBPResource) {
			this.type = NotifierType.RESOURCE;
		} else if (n.getNotifier() instanceof EObject) {
			this.type = NotifierType.EOBJECT;
			this.focus_obj = (EObject) n.getNotifier();
			this.eref = (EReference) n.getFeature();
		}
	}

	public List<EObject> getObjectList() {
		return this.obj_list;
	}

	public EObject getFocusObj() {
		return this.focus_obj;
	}

	public NotifierType getNotiferType() {
		return this.type;
	}

	public EReference getEReference() {
		return this.eref;
	}
}