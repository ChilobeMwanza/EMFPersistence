package cbp_text_tests;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.*;

import impl.DeltaResourceImpl;
import university.UniversityPackage;

public abstract class BasicTest 
{
	protected static String fileSaveLocation ="university.txt";
	protected Resource resource = null;
	protected List<EObject> savedContentsList = null;
	protected List<EObject> loadedContentsList = null;
	
	@Before
	public void runOnceBeforeTest()
	{
		/*Delete save file if it exists*/
		File file = new File(fileSaveLocation);
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resource = new DeltaResourceImpl(URI.createURI(fileSaveLocation));
	}
	
	
	public void loadResource()
	{
		/*Load persisted model into resource contents*/
		ResourceSetImpl rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put
		("txt", new Resource.Factory()
		{
			@Override
			public Resource createResource(URI uri)
			{
				return new DeltaResourceImpl(uri);
			}
		});
		
		rs.getPackageRegistry().put(UniversityPackage.eINSTANCE.getNsURI(), 
				UniversityPackage.eINSTANCE);
		
		resource = rs.createResource(URI.createFileURI(fileSaveLocation));
		try {
			resource.load(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<EObject> getResourceContentsList()
	{
		List<EObject> outputList = new ArrayList<EObject>();
		for(TreeIterator<EObject> it = resource.getAllContents(); it.hasNext();)
		{
			outputList.add(it.next());
		}
		
		return outputList;
	}
	
	protected void saveAndLoadResource()
	{
		/*Save them*/
		try {
			resource.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Save a list of the current eobjects in contents*/
		savedContentsList = getResourceContentsList();
		
		/*Clear contents*/
		resource.getContents().clear();
				
		/*load persited model*/
		loadResource();
		loadedContentsList = getResourceContentsList();
	}
	
}