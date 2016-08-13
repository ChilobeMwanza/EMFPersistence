package cbp_text_tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import impl.DeltaResourceImpl;
import university.University;
import university.UniversityFactory;
import university.UniversityPackage;

public class GeneralSaveTest extends BasicTest
{
	/*Calling save() serialises all monitored changes (notifications) and clears these
	 * Notifications from the event list. Calling save repeatedly without having made
	 * any changes to the model should not result in subsequent modifications of the save
	 * file.
	 */
	@Test
	public void testRepeatedSaveNoModification() throws IOException
	{
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni1);
		
		//perform first save
		resource.save(null);
		
		//save last modified time
		File file = new File(fileSaveLocation);
		Long timeStamp = file.lastModified();
		
		//perform further saves without modifiying the resource
		resource.save(null);
		resource.save(null);
		resource.save(null);
		resource.save(null);
		
		//check that the file has not been modified
		assertTrue(timeStamp == file.lastModified());
	}
	
	/*
	 * Test that making initial modifications, calling save, making more modifications,
	 *  results in the save file being modifed accordingly.
	 */
	@Test
	public void testMultipleSaveWithModifications() throws IOException
	{
		//initial modifications
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni1);
		
		//perform first save
		resource.save(null);
		
		//save last modified time
		File file = new File(fileSaveLocation);
		Long timeStamp = file.lastModified();
		
		//make further modifications
		uni1.setName("York University");
		
		resource.save(null);
		
		//check that the file has been modified
		assertFalse(timeStamp == file.lastModified());
		
	}
	
	/*
	 * Test that saved object and loaded object are equal
	 */
	@Test
	public void testBasicSaveAndLoad() throws IOException
	{
		University savedUni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(savedUni);
		
		resource.save(null);
		
		//Load in the resource
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
		
		Resource res = rs.createResource(URI.createFileURI(fileSaveLocation));
		res.load(null);
		
		//check objects are equal
		
		University loadedUni = (University) res.getContents().get(0);
		
		assertTrue(EcoreUtil.equals(savedUni, loadedUni));
	}
	
	
	/*
	 * Test empty save does not result in output file creation
	 */
	@Test
	public void testEmptySave() throws IOException
	{
		resource.save(null);
		
		File f = new File(fileSaveLocation);
		
		assertFalse(f.exists());
	}
	
	/*
	 * Tests that redundant modifications do not result in changes to the output file
	 */
	@Test
	public void testRedundantModification() throws IOException
	{
		University savedUni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(savedUni);
		savedUni.setName("York Uni");
		
		resource.save(null);
		
		File file = new File(fileSaveLocation);
		Long timeStamp = file.lastModified();
		
		
		//Load in the resource
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
		
		Resource res = rs.createResource(URI.createFileURI(fileSaveLocation));
		res.load(null);
		
		
		
		University loadedUni = (University) res.getContents().get(0);
		loadedUni.setName("York Uni");
		
		//check that the file has not been modified
		assertTrue(timeStamp == file.lastModified());
	}
}
