
package impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import change.ChangeLog;
import change.EventAdapter;

import drivers.PersistenceManager;


public class DeltaResourceImpl extends ResourceImpl
{
	private  final String classname = this.getClass().getSimpleName();
	private final  ChangeLog changeLog;
	private final BiMap<EObject,Double> map ;
    private PersistenceManager persistenceManager;
    EventAdapter eventAdapter;
    
   
    
	public DeltaResourceImpl(URI uri)
	{
		super(uri);
		
		map = HashBiMap.create(); //bi map , 
		changeLog = new ChangeLog();
	
		eventAdapter = new EventAdapter(changeLog);
		this.eAdapters().add(eventAdapter); 
		
		persistenceManager = new PersistenceManager(changeLog,this); //is changelog variable pointing to same changelog as adapter?
		
	}

	@Override
	public void save(Map<?, ?> options)
	{
		
		persistenceManager.save(options);
	    System.out.println("DeltaResourceImpl: Print save file contents : ");
		try
		{
			showSaveFileContents();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//System.out.println();
        //System.out.println();
       // System.out.println("Sorted changelog:");
		//changeLog.showLogEntries(changeLog.sortChangeLog());
	}

	@Override
	public void load(Map<?, ?> options)throws IOException
	{
		eventAdapter.setEnabled(false);
		
		System.out.println(classname+": Load called!");
		
		persistenceManager.load(options);
		
		eventAdapter.setEnabled(true);
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
	
	public BiMap<EObject,Double> getMap()
	{
		return this.map;
	}
	
	public void showSaveFileContents() throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(this.uri.path()),"Ascii"));
		String line;
		
		while((line = in.readLine())!= null)
		{
			System.out.println(line);
		}
		in.close();
	}
	
}