/**
 * BUGS/ISSUES
 */

package main;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;



import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
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
import university.StaffMember;
import university.StaffMemberType;
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
		//app.createResource();
		//app.print();
		
	
	
		
	
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
		
		StaffMember s1 = UniversityFactory.eINSTANCE.createStaffMember();
	
		res.getContents().add(s1);
		
		s1.setStaffMemberType(StaffMemberType.RESEARCH);
		
				
		//perform first save
		res.save(null);
		
	
		
		
		
		
		/*SAVE STARTS HERE*/
		res.save(null); 
	}
	
	Map <String,Integer> classifier_ordinality_map = new HashMap<String, Integer>();
	
	public void print()
	{
		EPackage ePackage = UniversityPackage.eINSTANCE;
		
		/*List<String> classNames = new ArrayList<String>();
		
		for(Iterator it = ePackage.getEClassifiers().iterator();it.hasNext();)
		{
			EClassifier classifier = (EClassifier)it.next();
			
			System.out.println(classifier.getName());
			classNames.add(classifier.getName());
			System.out.print(" ");
					
		}*/
		
		List<EClassifier> classifiers_list = ePackage.getEClassifiers();
		
		for(int i = 0; i < classifiers_list.size(); i++ )
		{
			EClassifier classifier = classifiers_list.get(i);
			
			System.out.println(classifiers_list.get(i).getName());
			//System.out.print(" ");
			classifier_ordinality_map.put(classifiers_list.get(i).getName(), i);
			
						//classifier.
			if(classifier instanceof EClass)
			{
				EClass eClass = (EClass) classifier;
			
				for(EStructuralFeature sf: eClass.getEAllStructuralFeatures())
				{
					System.out.println(" "+sf.getName()+" ");
				}
			}
			
		}
		
		//getOridinalNumber("University");
		
	}
	
	public void getOridinalNumber(String className)
	{
		System.out.println(classifier_ordinality_map.get(className));
	}
}
