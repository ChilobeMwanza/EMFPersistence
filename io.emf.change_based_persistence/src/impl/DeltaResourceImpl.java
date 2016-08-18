
package impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import change.Changelog;
import change.EventAdapter;
import drivers.EPackageOrdinalList;
import drivers.PersistenceManager;
import university.UniversityPackage;

public class DeltaResourceImpl extends ResourceImpl implements DeltaResource
{
	private  final String classname = this.getClass().getSimpleName();
	
	private final Changelog changelog = new Changelog();
 
	private final PersistenceManager persistenceManager;
	
    private final EventAdapter eventAdapter;
    
    private final EPackage ePackage;
    
    private final EPackageOrdinalList ordinalList = new EPackageOrdinalList();
    
    public DeltaResourceImpl(URI uri, EPackage ePackage)
	{
		super(uri);
		
		this.ePackage = ePackage;
		
		eventAdapter = new EventAdapter(changelog);
		
		this.eAdapters().add(eventAdapter); 
		
		populateOrdinalList();
		
		persistenceManager = new PersistenceManager(changelog,this); //is changelog variable pointing to same changelog as adapter?
	}
    
    public DeltaResourceImpl(EPackage ePackage)
    {
    	this.ePackage = ePackage;
    	
		eventAdapter = new EventAdapter(changelog);
		
		this.eAdapters().add(eventAdapter); 
		
		populateOrdinalList();
		
		persistenceManager = new PersistenceManager(changelog,this); //is changelog variable pointing to same changelog as adapter?
    }
    
    private void populateOrdinalList()
	{
		List<EClassifier> classifiers_list = ePackage.getEClassifiers();
		
		for(int i = 0; i < classifiers_list.size(); i++)
		{
			EClassifier classifier = classifiers_list.get(i);
			
			if(classifier instanceof EClass)
			{
				EClass eClass = (EClass) classifier;
				
				ordinalList.addEClass(eClass.getName(), i);
				
				List<EStructuralFeature> featuresList = eClass.getEAllStructuralFeatures();
				
				for(int j = 0; j < featuresList.size(); j++)
				{
					EStructuralFeature feature = featuresList.get(j);
					ordinalList.addEStructuralFeature(eClass.getName(),feature.getName(),j);
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
