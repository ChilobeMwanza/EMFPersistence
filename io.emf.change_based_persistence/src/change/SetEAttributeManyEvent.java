package change;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class SetEAttributeManyEvent extends Event 
{
	private EObject focus_obj;
	private EAttribute attr;
	private List<Object> attr_values_list;
	
	public SetEAttributeManyEvent(EObject focus_obj, EAttribute attr, List<Object> attr_values_list)
	{
		super(Event.SET_EATTRIBUTE_MANY);
		
		this.focus_obj = focus_obj;
		this.attr = attr;
		this.attr_values_list = attr_values_list;
	}
	
	@SuppressWarnings("unchecked")
	public SetEAttributeManyEvent(Notification n)
	{
		this((EObject) n.getNotifier(),(EAttribute) n.getFeature(),(List<Object>) n.getNewValue());
	}
	
	public EObject getFocusObj()
	{
		return this.focus_obj;
	}
	
	public EAttribute getEAttribute()
	{
		return this.attr;
	}
	
	public List<Object> getAttributeValuesList()
	{
		return this.attr_values_list;
	}
}