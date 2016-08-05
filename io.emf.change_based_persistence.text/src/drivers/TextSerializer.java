/*
 * todo:
 * deletion
 * opposite ref
 * enums
 * resume after load (as in pick up where you left off)
 * thoroughness (i.e of supported types e.t.c, start with enum)
 * optimisation algorithm
 * check everything works with non generated emf (Reflective)
 * make txt format less verbose
 * binary
 */
package drivers;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import change.ChangeLog;

import util.Printer;

public class TextSerializer 
{
	private final String RESOURCE_NAME = "DeltaResourceImpl";
	private  final String classname = this.getClass().getSimpleName();
	private final PersistenceManager manager;
	private boolean appendMode = false;
	private List<String> outputList;
    private List<Notification> notificationsList;
    Printer out = new Printer (this.getClass());
	private final ChangeLog changelog; 
	
	public TextSerializer(PersistenceManager manager, ChangeLog aChangeLog)
	{
		this.manager =  manager;
		this.changelog = aChangeLog;
		this.outputList = new ArrayList<String>();
		this.notificationsList = new ArrayList<Notification>();
		
		notificationsList = manager.getChangeLog().getEventsList();
	}
	
	public void save(Map<?,?> options)
	{
		if(notificationsList.isEmpty())
			return;
		
		if(!appendMode)
			serialiseInitialEntry();
		
		//String fileSaveLocation = (String) options.get("FILE_SAVE_LOCATION");
		
		for(Notification n : notificationsList)
		{
			switch(n.getEventType())
			{
				case Notification.ADD:
				{
					if(n.getNewValue() instanceof EObject) //EObject added to EObject or to resource
						handleAddEObjectEvent(n);
					else if(n.getFeature() instanceof EAttribute)//Java object, e.g. String added to eattribute of eobject
						handleAddToEAttributeEvent(n);
					break;
				}
				case Notification.ADD_MANY:
				{
					List<Object> list =  (List<Object>) n.getNewValue();
					if(list.get(0) instanceof EObject)
					{
						handleAddManyEObjectsEvent(n);
					}
					else if(n.getFeature() instanceof EAttribute)
					{
						handleAddManyToEAttributeEvent(n);
					}
					break;
				}
				case Notification.SET:
				{
					if(n.getFeature() instanceof EAttribute)
					{
						handleAddToEAttributeEvent(n);
					}
					else 
					{
						handleAddEObjectEvent(n);
					}
				}
			}
		}
		
		//finally append strings to file
		appendStringsToFile(appendMode);
	}
	private void handleAddToEAttributeEvent(Notification n)
	{
		EObject obj = (EObject) n.getNotifier();
		EAttribute attr = (EAttribute) n.getFeature();
		String newValue = EcoreUtil.convertToString(attr.getEAttributeType(),  n.getNewValue());
		
		outputList.add("SET "+attr.getName()+" "+obj.eClass().getName()+" "+changelog.getObjectId(obj)+" ["+newValue+"]");
	}
	
	private void handleAddManyToEAttributeEvent(Notification n)
	{
		EObject obj = (EObject) n.getNotifier();
		EAttribute attr = (EAttribute) n.getFeature();
		List<Object> attr_values_list = (List<Object>) n.getNewValue();
		
		
		String obj_list_str = "[";
		
		for(Object object: attr_values_list)
		{
			String newValue = EcoreUtil.convertToString(attr.getEAttributeType(), object);
			obj_list_str = obj_list_str + newValue+",";
		}
		obj_list_str = obj_list_str.substring(0,obj_list_str.length()-1)+"]"; // remove final "," , add "]"
		outputList.add("SET "+attr.getName()+" "+obj.eClass().getName()+" "+changelog.getObjectId(obj)+" "+obj_list_str);
	}
	
	private void serialiseInitialEntry()
	{
		EObject obj = null;
		for(Notification n : notificationsList)
		{
			if(n.getEventType() == Notification.ADD)
			{
				obj = (EObject) n.getNewValue();
				break;
			}
			else if(n.getEventType() == Notification.ADD_MANY)
			{
				List<EObject> objectsList = (List<EObject>) n.getNewValue();
				obj = objectsList.get(0);
				break;
			}
		}
		outputList.add("NAMESPACE_URI "+obj.eClass().getEPackage().getNsURI());
	}
	
	private void handleAddEObjectEvent(Notification n)
	{
		EObject obj = (EObject)n.getNewValue();
		
		changelog.addObjectToMap(obj);
		
		outputList.add("CREATE ["+obj.eClass().getName()+ " "+
				changelog.getObjectId(obj)+"]");
		
		if(n.getNotifier().getClass().getSimpleName().equals(RESOURCE_NAME))
		{
			outputList.add("ADD_R ["+obj.eClass().getName()+" "+
					changelog.getObjectId(obj)+"]"); 
		}
		else
		{
			
			EObject dest_obj = (EObject) n.getNotifier();
			outputList.add("ADD "+((EReference)n.getFeature()).getName()+" "+dest_obj.eClass().getName()+" "
					+changelog.getObjectId(dest_obj)+" ["+obj.eClass().getName()+" "
					+changelog.getObjectId(obj)+"]");
		}
	}
	
	private void handleAddManyEObjectsEvent(Notification n)
	{
		List<EObject> obj_list = (List<EObject>) n.getNewValue();
		
		String obj_list_str = "[";
		
		for(EObject obj : obj_list)
		{
			changelog.addObjectToMap(obj);
			obj_list_str = obj_list_str + obj.eClass().getName()+" "+changelog.getObjectId(obj)+","; 
		}
		
	    obj_list_str = obj_list_str.substring(0,obj_list_str.length()-1)+"]"; // remove final "," , add "]"
	    outputList.add("CREATE "+obj_list_str);
	    
	    if(n.getNotifier().getClass().getSimpleName().equals(RESOURCE_NAME))
	    {
	    	outputList.add("ADD_R "+obj_list_str);
	    }	
	    else
	    {
	    	EObject dest_obj = (EObject) n.getNotifier();
	    	
	    	outputList.add("ADD "+((EReference)n.getFeature()).getName()+" "+dest_obj.eClass().getName()+" "
					+changelog.getObjectId(dest_obj)+" "+obj_list_str);
	    }
	}
	
	private void appendStringsToFile(boolean appendMode)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter
				    (new OutputStreamWriter(new FileOutputStream(manager.getURI().path(),appendMode),
				    		Charset.forName(manager.TEXT_ENCODING).newEncoder()));
			PrintWriter out = new PrintWriter(bw);
			for(String string: outputList)
			{
				//System.out.println("PersistenceManager.java "+string);
				out.println(string);
			}
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}	
}