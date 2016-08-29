
package impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import change.Changelog;
import change.EventAdapter;
import drivers.EPackageElementsNamesMap;
import drivers.PersistenceManager;

public class CBPTextResourceImpl extends CBPResource
{
	private  final String classname = this.getClass().getSimpleName();
	
	private final Changelog changelog = new Changelog();
 
	private final PersistenceManager persistenceManager;
	
    private final EventAdapter eventAdapter;
 
    private  final EPackageElementsNamesMap ePackageElementsNamesMap;
    		
    
    public CBPTextResourceImpl(URI uri, EPackage ePackage)
	{
		super(uri);
		
		eventAdapter = new EventAdapter(changelog);
		
		this.eAdapters().add(eventAdapter); 
		
		ePackageElementsNamesMap = populateEPackageElementNamesMap(ePackage);
		
		persistenceManager = new PersistenceManager(changelog,this, 
				ePackageElementsNamesMap);
	}
    
    public CBPTextResourceImpl(EPackage ePackage)
    {
		eventAdapter = new EventAdapter(changelog);
		
		this.eAdapters().add(eventAdapter); 
		
		ePackageElementsNamesMap = populateEPackageElementNamesMap(ePackage);
		
		persistenceManager = new PersistenceManager(changelog,this,
				ePackageElementsNamesMap); 
    }
    
    
	@Override
	public void save(Map<?, ?> options)
	{
		if(options != null && options.containsKey(OPTION_OPTIMISE_MODEL))
		{
			if((boolean)options.get(OPTION_OPTIMISE_MODEL) == true)
			{
				System.out.println(classname+"yes");
			}
			
		}
		persistenceManager.save(options);
		
		/*If save file exists, print contents to console*/
		File f = new File(this.uri.path());
		
		if(f.exists() && !f.isDirectory())
		{
			System.out.println("DeltaResourceImpl: Print save file contents : ");
			
			try
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
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void load(Map<?, ?> options)throws IOException
	{
		eventAdapter.setEnabled(false);
		
		System.out.println(classname+": Load called!");
		
		try {
			persistenceManager.load(options);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		eventAdapter.setEnabled(true);
	}
		
	public Changelog getChangelog()
	{
		return this.changelog;
	}
	
}
