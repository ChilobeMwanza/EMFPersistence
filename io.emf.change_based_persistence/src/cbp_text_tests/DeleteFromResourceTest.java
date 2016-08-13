package cbp_text_tests;

import static org.junit.Assert.assertTrue;

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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.*;
import impl.DeltaResourceImpl;
import university.Department;
import university.Library;
import university.Student;
import university.University;
import university.UniversityFactory;
import university.UniversityPackage;
import university.Vehicle;

public class DeleteFromResourceTest 
{
	private static String fileSaveLocation ="university.txt";
	private Resource resource = null;
	private List<EObject> savedContentsList = null;
	List<EObject> loadedContentsList = null;
	
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
	
	private List<EObject> getResourceContentsList()
	{
		List<EObject> outputList = new ArrayList<EObject>();
		for(TreeIterator<EObject> it = resource.getAllContents(); it.hasNext();)
		{
			outputList.add(it.next());
		}
		
		return outputList;
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
	
	private void saveAndLoadResource()
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

	
	/*
	 * Tests (adding and) deleting a single item
	 */
	@Test
	public void testDeleteSingleFromResource()
	{
		/*Create some objects */
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		resource.getContents().add(uni);
		
		resource.getContents().remove(uni);
        
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	@Test
	public void testDeleteRepeatedFromResource()
	{
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		University uni2 = UniversityFactory.eINSTANCE.createUniversity();
		Library lib1 = UniversityFactory.eINSTANCE.createLibrary();
		Department dep1 = UniversityFactory.eINSTANCE.createDepartment();
		
		resource.getContents().add(uni1);
		resource.getContents().add(uni2);
		resource.getContents().add(lib1);
		resource.getContents().add(dep1);
		
		
	}
	
	/*
	 * Tests (adding and) deleting a collection of objects 
	 */
	@Test
	public void testDeleteCollectionFromResource()
	{
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		University uni2 = UniversityFactory.eINSTANCE.createUniversity();
		Library lib1 = UniversityFactory.eINSTANCE.createLibrary();
		Department dep1 = UniversityFactory.eINSTANCE.createDepartment();
		
		List <EObject> list = new ArrayList<EObject>();
		list.add(uni1);
		list.add(uni2);
		list.add(lib1);
		list.add(dep1);
		
		resource.getContents().addAll(list);
		
		List<EObject> removelist = new ArrayList<EObject>();
		removelist.add(dep1);
		removelist.add(uni1);
		
		
		resource.getContents().removeAll(removelist);
		
        saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Combines the various methods of adding and deleting items from the resource
	 */
	@Test
	public void testAllDeleteFromResource()
	{
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		University uni2 = UniversityFactory.eINSTANCE.createUniversity();
		Vehicle v1 = UniversityFactory.eINSTANCE.createVehicle();
		
		resource.getContents().add(uni1);
		resource.getContents().add(uni2);
		resource.getContents().add(v1);
		
		List<EObject> list = new ArrayList<EObject>();
		Library lib1 = UniversityFactory.eINSTANCE.createLibrary();
		Library lib2 = UniversityFactory.eINSTANCE.createLibrary();
		Department d1 = UniversityFactory.eINSTANCE.eINSTANCE.createDepartment();
		Student s1 = UniversityFactory.eINSTANCE.createStudent();
		
		list.add(lib1);
		list.add(lib2);
		list.add(d1);
		list.add(s1);
		
		resource.getContents().addAll(list);
		
		List<EObject> removelist = new ArrayList<EObject>();
		removelist.add(lib1);
		removelist.add(v1);
		removelist.add(d1);
		


		resource.getContents().remove(0);
		resource.getContents().remove(lib2);
		resource.getContents().removeAll(removelist);
		
		saveAndLoadResource();
			
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
}