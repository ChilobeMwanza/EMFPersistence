package cbp_text_tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import university.University;
import university.UniversityFactory;

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
}