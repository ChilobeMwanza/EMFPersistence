
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

public class CBPTextResourceImpl extends ResourceImpl implements DeltaResource
{
	private  final String classname = this.getClass().getSimpleName();
	
	private final Changelog changelog = new Changelog();
 
	private final PersistenceManager persistenceManager;
	
    private final EventAdapter eventAdapter;
    
    private final EPackage ePackage;
    
    private  final EPackageElementsNamesMap ePackageElementsNamesMap = 
    		new EPackageElementsNamesMap();
    
    public CBPTextResourceImpl(URI uri, EPackage ePackage)
	{
		super(uri);
		
		this.ePackage = ePackage;
		
		eventAdapter = new EventAdapter(changelog);
		
		this.eAdapters().add(eventAdapter); 
		
		populateEPackageElementNamesMap();
		
		persistenceManager = new PersistenceManager(changelog,this, 
				ePackageElementsNamesMap);
	}
    
    public CBPTextResourceImpl(EPackage ePackage)
    {
    	this.ePackage = ePackage;
    	
		eventAdapter = new EventAdapter(changelog);
		
		this.eAdapters().add(eventAdapter); 
		
		populateEPackageElementNamesMap();
		
		persistenceManager = new PersistenceManager(changelog,this,
				ePackageElementsNamesMap); 
    }
    
    private void populateEPackageElementNamesMap()
	{
		for(EClassifier eClassifier : ePackage.getEClassifiers())
		{
			if(eClassifier instanceof EClass)
			{
				EClass eClass = (EClass) eClassifier;
				
				ePackageElementsNamesMap.addName(eClass.getName());
				
				for(EStructuralFeature feature : eClass.getEAllStructuralFeatures())
				{
					ePackageElementsNamesMap.addName(feature.getName());
				}
			}
		}
	}

	@Override
	public void save(Map<?, ?> options)
	{
		persistenceManager.save(options);
		
		/*If save file exists, print contents to console*/
		File f = new File(this.uri.path());
		
		/*if(f.exists() && !f.isDirectory())
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
		}*/
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
