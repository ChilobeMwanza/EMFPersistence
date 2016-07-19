//A new object has been created.

package change;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

public class NewObjectEntry extends AbstractEntry
{
	public NewObjectEntry(EObject value, Double id)
	{
		super(value, id);
	}
	
	public NewObjectEntry(Notification msg, Double id)
	{
		this((EObject)msg.getNotifier(),id) ;
	}

}
