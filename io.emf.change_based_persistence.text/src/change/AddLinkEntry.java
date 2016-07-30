/*
 * Creates entries for containment / non containment references.
 * e.g Add book1 to mainBook of Library
 * where mainBook is a containment ref within 'LIB'
 */
package change;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class AddLinkEntry extends AbstractEntry 
{

	private EObject newObj; 
	private EReference eReference;
	
	public AddLinkEntry(EObject newObj,EObject destObj,EReference ref) 
	{
		super(destObj);
		this.newObj = newObj;
		this.eReference = ref;
		// TODO Auto-generated constructor stub
	}
	
	public EReference getReference()
	{
		return this.eReference;
	}
	
	public 	EObject getNewObj()
	{
		return newObj;
	}
	
}
