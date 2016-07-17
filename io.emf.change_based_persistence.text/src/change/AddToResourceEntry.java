package change;

import java.util.UUID;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

/*
 * Items can be added to other items via containment relationships e.g. Add library to book. Or add library to resource e.t.c
 */
public class AddToResourceEntry extends AbstractEntry  //tbr
{
	public AddToResourceEntry(EObject eObject, UUID id)
	{
		super(eObject, id);
	}
	
	public AddToResourceEntry(Notification msg, UUID id)
	{
		this((EObject)msg.getNewValue(),id);
	}
	
	public String[] getOutputStringsArray() //tbr
	{
		
		return null;
	}
}
