/**
 * BUGS/ISSUES
 * 1) In order for this implementation to work, newly created model elements
 * must be added directly to the resource, or added to an object that who's hierachy
 * eventually leads to the resource. 
 */

package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
//import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;


import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import change.EventAdapter;
import impl.DeltaResourceImpl;
import library.Book;
import library.Library;
import library.LibraryFactory;
import library.LibraryPackage;

public class App 
{
	private static String fileSaveLocation ="library.txt";
	
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		App app = new App();
	//	app.loadResource();
		app.createResource();
	}
	
	public void loadResource() throws IOException
	{
		ResourceSetImpl rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put
		("txt", new Resource.Factory()
		{
			@Override
			public Resource createResource(URI uri)
			{
				return new DeltaResourceImpl(uri,LibraryPackage.eINSTANCE);
			}
		});
		
		rs.getPackageRegistry().put(LibraryPackage.eINSTANCE.getNsURI(), 
				LibraryPackage.eINSTANCE);
		
		Resource resource = rs.createResource(URI.createFileURI(fileSaveLocation));
		resource.load(null);
		
		Library library = (Library) resource.getContents().get(0);
		
		//Map<String, String> loadOptions = new HashMap<String, String>();
		//loadOptions.put("FILE_LOCATION", fileSaveLocation);
		
	}
	
	
	public void createResource() throws Exception
	{
		//Resource resource = new XMIResourceImpl();

		Resource resource = new DeltaResourceImpl(URI.createURI("library.txt"),LibraryPackage.eINSTANCE);
		
		//Create root object, add it to resource.
		Library lib1 = LibraryFactory.eINSTANCE.createLibrary();
		
		Library lib2 = LibraryFactory.eINSTANCE.createLibrary();
		lib2.setName("dfdf");
		lib2.getNumbersList().add(1);
		
		lib1.getEmployeeNames().add("Employee 1");
		lib1.getEmployeeNames().add("Employee 2");
		lib1.getEmployeeNames().add("Employee 3");
		
		lib1.setName("Awesome Library");
		lib1.setNumEmployees(27);
		lib1.getNumbersList().add(1);
		lib1.getNumbersList().add(2);
		lib1.getNumbersList().add(3);
		lib1.setADouble(3.1415);
		
		
		
		resource.getContents().add(lib1);
		resource.getContents().add(lib2);
		
         
		
		//Map<String, String> saveOptions = new HashMap<String, String>();
		//saveOptions.put("FILE_SAVE_LOCATION", fileSaveLocation);
		
		resource.save(null);
	    
	}

}
