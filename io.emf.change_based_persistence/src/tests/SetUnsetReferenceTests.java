/*
 * Contains tests that test setting and unsetting ref values
 */
package tests;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import university.Book;
import university.Computer;
import university.Department;
import university.Library;
import university.Module;
import university.StaffMember;
import university.Student;
import university.University;
import university.UniversityFactory;
import university.Vehicle;

public class SetUnsetReferenceTests extends TestBase 
{
	/*
	 * Tests setting 1 single valued containment reference
	 */
	@Test
	public void testSetOneSingleValuedContainmentReference()
	{
		Library lib = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		Computer comp = UniversityFactory.eINSTANCE.createComputer();
		
		lib.setMainComputer(comp);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests unsetting one single valued containment reference
	 */
	@Test
	public void testUnsetOneSingleValuedContainmentReference()
	{
		Library lib = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		Computer comp = UniversityFactory.eINSTANCE.createComputer();
		
		lib.setMainComputer(comp);
		lib.setMainComputer(null);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests setting multiple distinct single valued containment refs
	 */
	@Test
	public void testSetMultipleSingleValuedContainmentRefs()
	{
		Library lib = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		Computer comp = UniversityFactory.eINSTANCE.createComputer();
		
		lib.setMainComputer(comp);
		
		Library lib2 = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib2);
		
		Computer comp2 = UniversityFactory.eINSTANCE.createComputer();
		
		lib2.setMainComputer(comp2);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests unsetting multiple distinct single valued containment refs
	 */
	@Test
	public void testUnsetMultipleSingleValuedContainmentRefs()
	{
		Library lib = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		Computer comp = UniversityFactory.eINSTANCE.createComputer();
		
		lib.setMainComputer(comp);
		
		Library lib2 = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib2);
		
		Computer comp2 = UniversityFactory.eINSTANCE.createComputer();
		
		lib2.setMainComputer(comp2);
		
		Library lib3 = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib3);
		
		Computer comp3 = UniversityFactory.eINSTANCE.createComputer();
		resource.getContents().add(comp3);
		
		lib.setMainComputer(null);
		lib3.setMainComputer(null);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests adding objects directly to a multi valued containment ref (i.e via .add)
	 */
	@Test
	public void testMultipleAddToManyValuedContainmentRef()
	{
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		Department dep1 = UniversityFactory.eINSTANCE.createDepartment();
		Department dep2 = UniversityFactory.eINSTANCE.createDepartment();
		Department dep3 = UniversityFactory.eINSTANCE.createDepartment();
		
		uni.getDepartments().add(dep1);
		uni.getDepartments().add(dep2);
		uni.getDepartments().add(dep3);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests removing objects directly from a multi valued containment remove (i.e via .remove)
	 */
	@Test
	public void testRemoveOneFromManyValuedContainmentRef()
	{
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		Department dep1 = UniversityFactory.eINSTANCE.createDepartment();
		Department dep2 = UniversityFactory.eINSTANCE.createDepartment();
		Department dep3 = UniversityFactory.eINSTANCE.createDepartment();
		
		uni.getDepartments().add(dep1);
		uni.getDepartments().add(dep2);
		uni.getDepartments().add(dep3);
		
		uni.getDepartments().remove(dep1);
		
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	

	
	
	/*
	 * Tests adding objects to a multi valued containment ref via a list (i.e addAll)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddAllIToManyValuedContainmentRef()
	{
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		Department dep1 = UniversityFactory.eINSTANCE.createDepartment();
		Department dep2 = UniversityFactory.eINSTANCE.createDepartment();
		Department dep3 = UniversityFactory.eINSTANCE.createDepartment();
		
		List <EObject> list = new ArrayList<EObject>();
		list.add(dep1);
		list.add(dep2);
		list.add(dep3);
		
		uni.getDepartments().addAll((Collection<? extends Department>) list);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
		
	}
	/*
	 * Tests removing objects from a multi valued containment ref via a list (i.e removeAll)
	 */
	@Test
	public void testRemoveAllFromManyValuedContainmentRef()
	{
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		Department dep1 = UniversityFactory.eINSTANCE.createDepartment();
		Department dep2 = UniversityFactory.eINSTANCE.createDepartment();
		Department dep3 = UniversityFactory.eINSTANCE.createDepartment();
		
		uni.getDepartments().add(dep1);
		uni.getDepartments().add(dep2);
		uni.getDepartments().add(dep3);
		
		
		List <EObject> list = new ArrayList<EObject>();
		list.add(dep1);
		list.add(dep3);
		
		uni.getDepartments().removeAll(list);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests setting 1 single valued non-containment ref
	 */
	@Test
	public void testSetOneSingleValuedNonContainmentRef()
	{
		Student s = UniversityFactory.eINSTANCE.createStudent();
		resource.getContents().add(s);
		
		Vehicle v = UniversityFactory.eINSTANCE.createVehicle();
		s.setRegisteredVehicle(v);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests un-setting 1 single valued non-containment ref
	 */
	@Test
	public void testUnsetOneSingleValuedNonContainmentRef()
	{
		Student s = UniversityFactory.eINSTANCE.createStudent();
		resource.getContents().add(s);
		
		Vehicle v = UniversityFactory.eINSTANCE.createVehicle();
		s.setRegisteredVehicle(v);
		s.setRegisteredVehicle(null);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests setting multiple distinct single valued containment refs
	 */
	@Test
	public void testSetMultipleSingleValuedNonContainmentRefs()
	{
		Student s = UniversityFactory.eINSTANCE.createStudent();
		resource.getContents().add(s);
		
		Vehicle v = UniversityFactory.eINSTANCE.createVehicle();
		s.setRegisteredVehicle(v);
		
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		StaffMember sm = UniversityFactory.eINSTANCE.createStaffMember();
		
		uni.setChancelor(sm);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	@Test
	public void testUnsetMultipleSingleValuedNonContainmentRefs()
	{
		Student s = UniversityFactory.eINSTANCE.createStudent();
		resource.getContents().add(s);
		
		Student s1 = UniversityFactory.eINSTANCE.createStudent();
		resource.getContents().add(s1);
		
		Vehicle v = UniversityFactory.eINSTANCE.createVehicle();
		s.setRegisteredVehicle(v);
		
		Vehicle v2 = UniversityFactory.eINSTANCE.createVehicle();
		s1.setRegisteredVehicle(v2);
		
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		StaffMember sm = UniversityFactory.eINSTANCE.createStaffMember();
		
		uni.setChancelor(sm);
		
		s1.setRegisteredVehicle(null);
		uni.setChancelor(null);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	@Test
	public void testAddMultipleToManyValuedNonContainmentRef()
	{
		Library lib = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		Book b1 = UniversityFactory.eINSTANCE.createBook();
		Book b2 = UniversityFactory.eINSTANCE.createBook();
		Book b3 = UniversityFactory.eINSTANCE.createBook();
		
		lib.getBooks().add(b1);
		lib.getBooks().add(b2);
		lib.getBooks().add(b3);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	@Test
	public void testRemoveOneFromManyValuedNonContainmentRef()
	{
		Library lib = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		Book b1 = UniversityFactory.eINSTANCE.createBook();
		Book b2 = UniversityFactory.eINSTANCE.createBook();
		Book b3 = UniversityFactory.eINSTANCE.createBook();
		
		lib.getBooks().add(b1);
		lib.getBooks().add(b2);
		lib.getBooks().add(b3);
		
		lib.getBooks().remove(b2);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	@Test
	public void testAddAllIToManyValuedNonContainmentRef()
	{
		Library lib = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		Book b1 = UniversityFactory.eINSTANCE.createBook();
		Book b2 = UniversityFactory.eINSTANCE.createBook();
		Book b3 = UniversityFactory.eINSTANCE.createBook();
		
		List <Book> list = new ArrayList<Book>();
		list.add(b1);
		list.add(b2);
		list.add(b3);
		
		lib.getBooks().addAll(list);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	@Test
	public void testRemoveAllFromManyValuedNonContainmentRef()
	{
		Library lib = UniversityFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		Book b1 = UniversityFactory.eINSTANCE.createBook();
		Book b2 = UniversityFactory.eINSTANCE.createBook();
		Book b3 = UniversityFactory.eINSTANCE.createBook();
		Book b4 = UniversityFactory.eINSTANCE.createBook();
		Book b5 = UniversityFactory.eINSTANCE.createBook();
		Book b6 = UniversityFactory.eINSTANCE.createBook();
		Book b7 = UniversityFactory.eINSTANCE.createBook();
		
		List <Book> list = new ArrayList<Book>();
		list.add(b1);
		list.add(b2);
		list.add(b3);
		list.add(b4);
		list.add(b5);
		list.add(b6);
		list.add(b7);
		
		lib.getBooks().addAll(list);
		
		List <Book> remlist = new ArrayList<Book>();
		remlist.add(b4);
		remlist.add(b1);
		remlist.add(b6);
		
		lib.getBooks().removeAll(remlist);
		
		saveAndLoadResource();
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests that setting an opposite reference works accordingly
	 */
	@Test
	public void testSetOppositeReference()
	{
		Student s1 = UniversityFactory.eINSTANCE.createStudent();
		resource.getContents().add(s1);
		
		Module m1 = UniversityFactory.eINSTANCE.createModule();
		
		s1.getEnrolledModules().add(m1);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
		//assertTrue(m1.getEnrolledStudents().contains(s1));
	}
	
	@Test
	public void testUnsetOppositeReference()
	{
		Student s1 = UniversityFactory.eINSTANCE.createStudent();
		resource.getContents().add(s1);
		
		Module m1 = UniversityFactory.eINSTANCE.createModule();
		
		s1.getEnrolledModules().add(m1);
		
		m1.getEnrolledStudents().remove(s1);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
}