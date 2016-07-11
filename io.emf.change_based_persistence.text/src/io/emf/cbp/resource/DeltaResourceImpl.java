
package io.emf.cbp.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import io.emf.cbp.text.change.ChangeLog;
import io.emf.cbp.text.change.CreateObjectEntry;
import io.emf.cbp.text.change.InitialEntry;
import main.PersistenceManager;


public class DeltaResourceImpl extends ResourceImpl
{
	private final ChangeLog changeLog = ChangeLog.INSTANCE;
    private PersistenceManager persistenceManager;
	
	private boolean initEntryAdded = false;
	
	public DeltaResourceImpl()
	{
		persistenceManager = new PersistenceManager(changeLog,this);
	}
	
	
	@Override
	public void save(Map<?, ?> options)
	{
		persistenceManager.save(options);
	}
	
	@Override
	public void load(Map<?, ?> options)throws IOException
	{
		System.out.println("DeltaResourceImpl.java: Load called!");
		persistenceManager.load(options);
	}
	


	//called when object is added to resource, directly or indirectly.
	@Override
	public void attached(EObject eObject) // this should be done via event manager. will avoid if(!loaded) e.t.c
	{
		System.out.println("DeltaResourceImpl.java: Attached called");
		super.attached(eObject);
		
		if(!initEntryAdded)
			addChangeLogInitialEntry(eObject);
		
		//System.out.println(eObject.getClass().getSimpleName()+" Object attached to le resource");
	}
	
	private void addChangeLogInitialEntry(EObject eObject)
	{
		changeLog.addEvent(new InitialEntry(eObject));
		initEntryAdded = true;
	}
	
	private void addChangeLogCreateObjectEntry(EObject eObject)
	{
		//changeLog.addEvent(new CreateObjectEntry(eObject));
	}
}
