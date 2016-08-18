/**
 * BUGS/ISSUES
 */

package main;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
//import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import com.google.common.hash.HashCode;
import com.google.common.io.Files;

import impl.DeltaResourceImpl;
import university.Book;
import university.University;
import university.UniversityFactory;
import university.UniversityPackage;


public class App 
{
	private static String fileSaveLocation ="university.txt";
	private  final String classname = this.getClass().getSimpleName();
	
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		App app = new App();
	//	app.loadResource() ;
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
				return new DeltaResourceImpl(uri);
			}
		});
		
		rs.getPackageRegistry().put(UniversityPackage.eINSTANCE.getNsURI(), 
				UniversityPackage.eINSTANCE);
		
		Resource resource = rs.createResource(URI.createFileURI(fileSaveLocation));
		resource.load(null);
		
		University uni1 = (University) resource.getContents().get(0);
		uni1.setName("Leeds Uni");
		resource.save(null);
	}
	

	
	public void createResource() throws Exception
	{
		Resource res = new DeltaResourceImpl(URI.createURI(fileSaveLocation));
	
		//initial modifications
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		res.getContents().add(uni1);
		
				
		//perform first save
		res.save(null);
		
		uni1.setName("York University");
		
		
		
		
		/*SAVE STARTS HERE*/
		res.save(null); 
	}
}
