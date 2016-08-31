package evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicMonitor;
//import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.epsilon.profiling.Stopwatch;

import drivers.EPackageElementsNamesMap;
import impl.CBPTextResourceImpl;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;

import university.Book;
import university.Library;
import university.UniversityFactory;
import university.UniversityPackage;

public class ChangeDetectionEvaluations 
{
    private static final String XMI_MODEL1_LOCATION =  new File("").getAbsolutePath()+"/evaluation models/model1.xmi";
    private static final String XMI_MODEL2_LOCATION =  new File("").getAbsolutePath()+"/evaluation models/model2.xmi";
    
    private static final String CBP_TEXT_MODEL1_LOCATION = "model1.cbpt";
    private static final String CBP_TEXT_MODEL2_LOCATION = "model2.cbpt";
    
    
    public static void main(String[] args) throws Exception
    {
    	
    	/*Configure jlog*/
        BasicConfigurator.configure(); 
        
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for ( Logger logger : loggers ) {
            logger.setLevel(Level.OFF);
        }
        
        
        
        ChangeDetectionEvaluations eval = new ChangeDetectionEvaluations();
        
        /*eval.generateXMIModel1();
        eval.generateXMIModel2();
        eval.compare(URI.createFileURI(XMI_MODEL1_LOCATION), URI.createFileURI(XMI_MODEL2_LOCATION));*/
        
        eval.populateCBPTModels();
        eval.populateXMIModels();
        
        
       /* long cbTime = 0;
        CBPTextCompare ctc = new CBPTextCompare();
        
        Stopwatch sw = new Stopwatch();
        
        sw.resume();
        for(int i =0; i < 100; i++)
        {
        	
            ctc.compare(CBP_TEXT_MODEL1_LOCATION , CBP_TEXT_MODEL2_LOCATION );
            
        }
        
        sw.pause();
        
        System.out.println("ctc time: "+sw.getElapsed());
        
        
        sw.resume();
        for(int i =0; i < 100; i++)
        {
        	
        	 eval.XMIcompare(URI.createFileURI(XMI_MODEL1_LOCATION),URI.createFileURI(XMI_MODEL2_LOCATION));
        }
        sw.pause();
        
        System.out.println("xmi time : "+ sw.getElapsed());*/
        eval.XMIcompare(URI.createFileURI(XMI_MODEL1_LOCATION),URI.createFileURI(XMI_MODEL2_LOCATION));
          
    }
    
    private void populateXMIModels() throws FileNotFoundException, IOException
    {
        Resource res1 = new XMLResourceImpl();
        Resource res2 = new XMLResourceImpl();
        
        
        
        addModel1Contents(res1);
        addModel2Contents(res2);
        
        res1.save(new FileOutputStream(new File(XMI_MODEL1_LOCATION)),null);
        res2.save(new FileOutputStream(new File(XMI_MODEL2_LOCATION)),null);
    }
    
    
    
    
    private void XMIcompare(URI uri1, URI uri2)
    {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put
            ("xmi", new XMIResourceFactoryImpl());
        
        ResourceSet resourceSet1 = new ResourceSetImpl();
        ResourceSet resourceSet2 = new ResourceSetImpl();

        resourceSet1.getResource(uri1, true);
        resourceSet2.getResource(uri2, true);
        
        IComparisonScope scope = new DefaultComparisonScope(resourceSet1, resourceSet2,null);
        Comparison comparison = EMFCompare.builder().build().compare(scope);
        
        
        List<Diff> differences = comparison.getDifferences();
        
        for(Diff d : differences)
        {
            System.out.println(d);
        }
    }
    
    private void populateCBPTModels() throws IOException
    {
        Resource res = new 
                CBPTextResourceImpl(URI.createFileURI(CBP_TEXT_MODEL1_LOCATION),UniversityPackage.eINSTANCE);
        
        Resource res2 = new 
                CBPTextResourceImpl(URI.createFileURI(CBP_TEXT_MODEL2_LOCATION),UniversityPackage.eINSTANCE);
        
        addModel1Contents(res);
        addModel2Contents(res2);
        
        
        res.save(null);
        res2.save(null);
    }
    
    
    private void addModel1Contents(Resource res)
    {
        Library library = UniversityFactory.eINSTANCE.createLibrary();
        res.getContents().add(library);
        
        library.setName("York Library");
    }
    
    private void addModel2Contents(Resource res)
    {
        Library library = UniversityFactory.eINSTANCE.createLibrary();
        res.getContents().add(library);
        
        library.setName("York Library");
        library.setName("Leeds University Library");
    }
    
   
    
    
    
    
}