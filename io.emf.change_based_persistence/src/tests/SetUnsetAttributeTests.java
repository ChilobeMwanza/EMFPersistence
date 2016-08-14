
//add tests for adding null values to list or direct e.t.c
package tests;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import impl.DeltaResourceImpl;
import university.Book;
import university.Computer;
import university.Department;
import university.Library;
import university.Module;
import university.StaffMember;
import university.Student;
import university.University;
import university.UniversityFactory;
import university.UniversityPackage;
import university.Vehicle;

public class SetUnsetAttributeTests extends TestBase
{
	/*
	 * Tests setting value of 1 single valued attribute
	 */
	@Test
	public void testSetOneSingleValuedAttribute() throws IOException
	{
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		uni.setName("York Uni");
		
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
		
		
		
		University loadedUni = (University) res.getContents().get(0);
		
		assertTrue(loadedUni.getName().equals("York Uni"));
	}
	
	@Test
	public void testUnsetOneSingleValuedAttribute() throws IOException
	{
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		uni.setName("York Uni");
		uni.setName(null);
		
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
		
		
		
		University loadedUni = (University) res.getContents().get(0);
		
	
		assertNull(loadedUni.getName());
		
	}
}