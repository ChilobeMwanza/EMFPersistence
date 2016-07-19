package change;

import org.eclipse.emf.ecore.EObject;

public abstract class AbstractEntry implements Entry
{
	private EObject eObject;
	private Double id;
	
	public AbstractEntry(EObject eObject, Double id)
	{
		this.eObject = eObject;
		this.id = id;
	}
	
	public EObject getEObject()
	{
		return eObject;
	}
	
	public Double getID()
	{
		return id;
	}
}
