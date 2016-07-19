
package impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import change.ChangeLog;
import change.EventAdapter;

import drivers.PersistenceManager;


public class DeltaResourceImpl extends ResourceImpl
{
	private  ChangeLog changeLog = new ChangeLog();
    private PersistenceManager persistenceManager;

	public DeltaResourceImpl(URI uri, EPackage epackage)
	{
		super(uri);
		EventAdapter adapter = new EventAdapter(changeLog);
		this.eAdapters().add(adapter); 
		
		persistenceManager = new PersistenceManager(changeLog,this); //is changelog variable pointing to same changelog as adapter?
	}

	@Override
	public void save(Map<?, ?> options)
	{
		//persistenceManager.save(options);
		
		changeLog.showLogEntries();
		//System.out.println();
        //System.out.println();
       // System.out.println("Sorted changelog:");
		//changeLog.showLogEntries(changeLog.sortChangeLog());
	}
	
	@Override
	public void load(Map<?, ?> options)throws IOException
	{
		System.out.println("DeltaResourceImpl.java: Load called!");
		persistenceManager.load(options);
	}
	
	/*@Override
	public void attached(EObject eObject) // tbr
	{
		super.attached(eObject);
	}*/
		
	public ChangeLog getChangeLog()
	{
		return this.changeLog;
	}
	
}
