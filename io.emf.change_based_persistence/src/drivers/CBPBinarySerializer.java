package drivers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import change.Changelog;
import change.Event;
import change.SetEAttributeEvent;
import change.SetEReferenceEvent;
import change.UnsetEAttributeEvent;
import change.UnsetEReferenceEvent;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class CBPBinarySerializer 
{
	private  final String classname = this.getClass().getSimpleName();
	private final String FORMAT_ID = "CBP_BIN"; 
	private final int FORMAT_VERSION = 1;
	
	//private final int BUFFER_SIZE = 8 * 1024;
	
	
	private final List<Event> eventList;
    
    private final PersistenceManager manager;
    
    private final Changelog changelog; 
    
    private final EPackageElementsNamesMap ePackageElementsNamesMap;
    
    private OutputStream outputStream;
    private final TObjectIntMap<String> simpleTypeNameMap;
    
    public CBPBinarySerializer(PersistenceManager manager, Changelog changelog,EPackageElementsNamesMap 
    		ePackageElementsNamesMap,TObjectIntMap<String>   simpleTypeNameMap)
    {
    	this.manager =  manager;
        this.changelog = changelog;
        this.ePackageElementsNamesMap = ePackageElementsNamesMap;
        
        this.eventList = manager.getChangelog().getEventsList();
        
        this.simpleTypeNameMap = simpleTypeNameMap;
    }
    
    public void save(Map<?,?> options) throws IOException
    {
    	if(eventList.isEmpty())
    		return;
    	
        outputStream = new BufferedOutputStream
        		(new FileOutputStream(manager.getURI().path(), manager.isResume()));
    
        //if we're not in resume mode, serialise initial entry
        if(!manager.isResume())
            writeInitialRecord(outputStream);
        
        for(Event e : eventList)
        {
        	switch(e.getEventType())
        	{
        	case Event.SET_EREFERENCE_EVENT:
        		handleSetEReferenceEvent((SetEReferenceEvent)e,outputStream);
        		break;
        	case Event.UNSET_EREFERENCE_EVENT:
        		handleUnsetEReferenceEvent((UnsetEReferenceEvent)e, outputStream);
        		break;
        	case Event.SET_EATTRIBUTE_EVENT:
        		handleSetEAttributeEvent((SetEAttributeEvent)e,outputStream);
        		break;
        	case Event.UNSET_EATTRIBUTE_EVENT:
        		handleUnsetEAttributeEvent((UnsetEAttributeEvent)e, outputStream);
        		break;
        	}
        }
        
        changelog.clearEvents();
        
      	outputStream.close();
      	
      	manager.setResume(true);
	
    }
    
    private void handleUnsetEAttributeEvent(UnsetEAttributeEvent e, OutputStream out) throws IOException
    {
    	EDataType type = e.getEAttribute().getEAttributeType();
        
        if(type instanceof EEnum)
        {
            handleUnsetComplexEAttributes(e,out);
            return;
        }
        
        switch(getTypeID(type))
        {
        case PersistenceManager.SIMPLE_TYPE_INT:
            handleUnsetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_INT);
            return;
        case PersistenceManager.SIMPLE_TYPE_SHORT:
            handleUnsetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_SHORT);
            return;
        case PersistenceManager.SIMPLE_TYPE_LONG:
            handleUnsetPrimitiveEAttributes(e,out,PersistenceManager.SIMPLE_TYPE_LONG);
            return;
        case PersistenceManager.SIMPLE_TYPE_FLOAT:
            handleUnsetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_FLOAT);
            return;
        case PersistenceManager.SIMPLE_TYPE_DOUBLE:
            handleUnsetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_DOUBLE);
            return;
        case PersistenceManager.SIMPLE_TYPE_BOOLEAN:
            handleUnsetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_BOOLEAN);
            return;
        case PersistenceManager.SIMPLE_TYPE_CHAR:
            handleUnsetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_CHAR);
            return;
        case PersistenceManager.COMPLEX_TYPE:
            handleUnsetComplexEAttributes(e,out);
            return;
        }
    }
    
    private void handleUnsetPrimitiveEAttributes(UnsetEAttributeEvent e,OutputStream out, int primitiveType ) throws IOException
    {
    	EObject focus_obj = e.getFocusObj();
    	EAttribute attr = e.getEAttribute();
    	List<Object> attr_values_list = e.getAttributeValuesList();
    	
    	 writePrimitive(out,PersistenceManager.UNSET_PRIMITIVE_EATTRIBUTE_VALUE);
         writePrimitive(out,changelog.getObjectId(focus_obj));
         writePrimitive(out,ePackageElementsNamesMap.getID(attr.getName()));
         writePrimitive(out,attr_values_list.size());
         
         for(Object obj : attr_values_list)
         {
             writePrimitive(out,primitiveType,obj);
         }  
    }
    
    private void handleUnsetComplexEAttributes(UnsetEAttributeEvent e,OutputStream out) throws IOException
    {
    	EObject focus_obj = e.getFocusObj();
    	
    	EAttribute attr = e.getEAttribute();
    	
    	EDataType dataType = attr.getEAttributeType();
    	
    	List<Object> attr_values = e.getAttributeValuesList();
    	
    	writePrimitive(out,PersistenceManager.UNSET_COMPLEX_EATTRIBUTE_VALUE);
    	writePrimitive(out,changelog.getObjectId(focus_obj));
    	writePrimitive(out,ePackageElementsNamesMap.getID(attr.getName()));
    	writePrimitive(out,attr_values.size());
    	
    	if(dataType.getName().equals("EString"))
    	{
    		for(Object obj : attr_values)
    		{
    			if(obj == null)
    				obj = manager.NULL_STRING;
    			
    			writeString(out,(String)obj);
    		}
    	}
    	else
    	{
    		for(Object obj : attr_values)
    		{
    			String valueString = EcoreUtil.convertToString(dataType, obj);
    			
    			if(valueString == null)
    				valueString = manager.NULL_STRING;
    			
    			writeString(out,valueString);
    		}
    	}
    }
    
    
    
    private void handleSetEAttributeEvent(SetEAttributeEvent e, OutputStream out) throws IOException
    {
    	EDataType type = e.getEAttribute().getEAttributeType();
    	
    	if(type instanceof EEnum)
    	{
    		handleSetComplexEAttributes(e,out);
    		return;
    	}
    	
    	switch(getTypeID(type))
    	{
    	case PersistenceManager.SIMPLE_TYPE_INT:
    		handleSetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_INT);
    		return;
    	case PersistenceManager.SIMPLE_TYPE_SHORT:
    		handleSetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_SHORT);
    		return;
    	case PersistenceManager.SIMPLE_TYPE_LONG:
    		handleSetPrimitiveEAttributes(e,out,PersistenceManager.SIMPLE_TYPE_LONG);
    		return;
    	case PersistenceManager.SIMPLE_TYPE_FLOAT:
    		handleSetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_FLOAT);
    		return;
    	case PersistenceManager.SIMPLE_TYPE_DOUBLE:
    		handleSetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_DOUBLE);
    		return;
    	case PersistenceManager.SIMPLE_TYPE_BOOLEAN:
    		handleSetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_BOOLEAN);
    		return;
    	case PersistenceManager.SIMPLE_TYPE_CHAR:
    		handleSetPrimitiveEAttributes(e,out, PersistenceManager.SIMPLE_TYPE_CHAR);
    		return;
    	case PersistenceManager.COMPLEX_TYPE:
    		handleSetComplexEAttributes(e,out);
    		return;
    	}
    }
    
    private void handleSetPrimitiveEAttributes(SetEAttributeEvent e,OutputStream out, int primitiveType ) throws IOException
    {
    	EObject focus_obj = e.getFocusObj();
    	EAttribute attr = e.getEAttribute();
    	List<Object> attr_values_list = e.getAttributeValuesList();
    	
        writePrimitive(out,PersistenceManager.SET_PRIMITIVE_EATTRIBUTE_VALUE);
        writePrimitive(out,changelog.getObjectId(focus_obj));
        writePrimitive(out,ePackageElementsNamesMap.getID(attr.getName()));
        writePrimitive(out,attr_values_list.size());
        
        int nullCounter = 0;
        
        for(Object obj : attr_values_list)
        {
        	if(obj == null)
        	{
        		nullCounter++;
        		continue;
        	}
        	
        	writePrimitive(out,primitiveType,obj);
        }
        
        if(nullCounter > 0) // for obj with null values, serialise as complex types
        {
        	String[] nullsArray = new String[nullCounter];
        	
        	Arrays.fill(nullsArray,manager.NULL_STRING);
        	
        	List<Object> nullList = new ArrayList<Object>(Arrays.asList(nullsArray));
        	
        	handleSetComplexEAttributes(out,focus_obj,attr,nullList);
        }
    }
    
    private void handleSetComplexEAttributes(OutputStream out,EObject focus_obj,EAttribute attr, List<Object> attr_values) throws IOException
    {
    	EDataType dataType = attr.getEAttributeType();
    	
    	writePrimitive(out,PersistenceManager.SET_COMPLEX_EATTRIBUTE_VALUE);
    	writePrimitive(out,changelog.getObjectId(focus_obj));
    	writePrimitive(out,ePackageElementsNamesMap.getID(attr.getName()));
    	writePrimitive(out,attr_values.size());
    	
    	if(dataType.getName().equals("EString"))
    	{
    		for(Object obj : attr_values)
    		{
    			if(obj == null)
    				obj = manager.NULL_STRING;
    			
    			writeString(out,(String)obj);
    		}
    	}
    	else
    	{
    		for(Object obj : attr_values)
    		{
    			String valueString = EcoreUtil.convertToString(dataType, obj);
    			
    			if(valueString == null)
    				valueString = manager.NULL_STRING;
    			
    			writeString(out,valueString);
    		}
    	}
    }
    
    private void handleSetComplexEAttributes(SetEAttributeEvent e,OutputStream out) throws IOException
    {
    	handleSetComplexEAttributes(out,e.getFocusObj(),e.getEAttribute(),e.getAttributeValuesList());
    }
    
    private int getTypeID(EDataType type)
    {
    	if(simpleTypeNameMap.containsKey(type.getName()))
    	{
    		return simpleTypeNameMap.get(type.getName());
    	}
    	
    	return PersistenceManager.COMPLEX_TYPE;
    }
    
    private void handleSetEReferenceEvent(SetEReferenceEvent e, OutputStream out) throws IOException
    {
    	TIntArrayList added_obj_list = new TIntArrayList();
    	TIntArrayList obj_create_list = new TIntArrayList();
    	
    	for(EObject obj : e.getEObjectList())
    	{
    		if(changelog.addObjectToMap(obj))
    		{
    			obj_create_list.add(ePackageElementsNamesMap.getID(obj.eClass().getName()));
    			obj_create_list.add(changelog.getObjectId(obj));
    		}
    		else
    		{
    			added_obj_list.add(changelog.getObjectId(obj));
    		}
    	}
    		
    	if(e.getNotifierType() == Event.NotifierType.RESOURCE) 
    	{
    		if(!obj_create_list.isEmpty()) //CREATE_AND_ADD_TO_RESOURCE 
    		{
    			writePrimitive(out,PersistenceManager.CREATE_AND_ADD_TO_RESOURCE);
    			writePrimitive(out,obj_create_list.size());
    			
    			for(TIntIterator it = obj_create_list.iterator(); it.hasNext();)
    			{
    				writePrimitive(out,it.next());
    			}
    			obj_create_list.clear();
    		}
    		if(!added_obj_list.isEmpty()) //ADD TO RESOURCE
    		{
    			writePrimitive(out, PersistenceManager.ADD_TO_RESOURCE);
    			writePrimitive(out,added_obj_list.size());
    			
    			for(TIntIterator it = added_obj_list.iterator(); it.hasNext();)
    			{
    				writePrimitive(out,it.next());
    			}
    			added_obj_list.clear();
    		}
    	}
    	else if(e.getNotifierType() == Event.NotifierType.EOBJECT)
    	{
    		EObject focus_obj = e.getFocusObj(); 
    		
    		if(!obj_create_list.isEmpty())//CREATE_AND_SET_REF_VALUE
    		{
    			writePrimitive(out,PersistenceManager.CREATE_AND_SET_EREFERENCE_VALUE);
    			writePrimitive(out,changelog.getObjectId(focus_obj));
    			writePrimitive(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    			writePrimitive(out,obj_create_list.size());
    			
    			for(TIntIterator it = obj_create_list.iterator(); it.hasNext();)
    			{
    				writePrimitive(out,it.next());
    			}
    		}
    		if(!added_obj_list.isEmpty()) //SET_REFERENCE_VALUE
    		{
    			writePrimitive(out,PersistenceManager.SET_EREFERENCE_VALUE);
    			writePrimitive(out,changelog.getObjectId(focus_obj));
    			writePrimitive(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    			writePrimitive(out,added_obj_list.size());
    			
    			for(TIntIterator it = added_obj_list.iterator(); it.hasNext();)
    			{
    				writePrimitive(out,it.next());
    			}
    		}
    	}
    }
    
    private void handleUnsetEReferenceEvent(UnsetEReferenceEvent e, OutputStream out) throws IOException
    {
    	List<EObject> removed_obj_list = e.getObjectList();
    	
    	
    
    	if(e.getNotiferType() == Event.NotifierType.RESOURCE)
    	{
    		writePrimitive(out, PersistenceManager.DELETE_FROM_RESOURCE);
    		writePrimitive(out,removed_obj_list.size());
    		
    		for(EObject obj : removed_obj_list)
    		{
    			writePrimitive(out,changelog.getObjectId(obj));
    		}
    	}
    	else if(e.getNotiferType() == Event.NotifierType.EOBJECT)
    	{
    		EObject focus_obj = e.getFocusObj();
    		
    		writePrimitive(out,PersistenceManager.UNSET_EREFERENCE_VALUE);
    		writePrimitive(out,changelog.getObjectId(focus_obj));
    		writePrimitive(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    		writePrimitive(out,removed_obj_list.size());
    		
    		for(EObject obj: removed_obj_list)
    		{
    			writePrimitive(out,changelog.getObjectId(obj));
    		}
    	}
    }
    
	private void writeInitialRecord(OutputStream out) throws IOException 
	{
		EObject obj = null;
		Event e = eventList.get(0);

		
		if(e.getEventType()!= Event.SET_EREFERENCE_EVENT) //tbr
		{
			try 
			{
				System.out.println(classname+" "+e.getEventType());
				throw new Exception("Error! first item in events list was not an Add event.");
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
				System.exit(0);
			}
		}
		obj = ((SetEReferenceEvent)e).getEObjectList().get(0);
		
		if (obj == null) //tbr
		{
			System.out.println(classname+" Null obj!");
			System.out.println(classname+" "+e.getEventType()); 
			System.exit(0);
		}
		
		
		out.write(FORMAT_ID.getBytes(manager.STRING_ENCODING)); //FORMAT ID
		writePrimitive(out, FORMAT_VERSION); //FORMAT VERSION
		writeString(out,obj.eClass().getEPackage().getNsURI()); //NS URI
	}
	
	
	private void writeString(OutputStream out, String str) throws IOException
	{
		byte[] bytes = str.getBytes(manager.STRING_ENCODING);
	
		writePrimitive(out,bytes.length);
		
		out.write(bytes);
	}
	
	private void writePrimitive(OutputStream out, int primitiveType, Object obj ) throws IOException
	{
		switch(primitiveType)
		{
		case PersistenceManager.SIMPLE_TYPE_INT:
			writePrimitive(out,(int)obj);
			return;
		case PersistenceManager.SIMPLE_TYPE_BOOLEAN:
			writePrimitive(out,(boolean)obj);
			return;
		case PersistenceManager.SIMPLE_TYPE_BYTE:
			writePrimitive(out,(byte)obj);
			return;
		case PersistenceManager.SIMPLE_TYPE_CHAR:
			writePrimitive(out,(char)obj);
			return;
		case PersistenceManager.SIMPLE_TYPE_DOUBLE:
			writePrimitive(out,(double)obj);
			return;
		case PersistenceManager.SIMPLE_TYPE_FLOAT:
			writePrimitive(out,(short)obj);
			return;
		case PersistenceManager.SIMPLE_TYPE_LONG:
			writePrimitive(out,(long)obj);
			return;
		case PersistenceManager.SIMPLE_TYPE_SHORT:
			writePrimitive(out,(short)obj);
			return;
		}
		
	}
	
	private void writePrimitive(OutputStream out, int i) throws IOException
	{
		byte[] bytes = ByteBuffer.allocate(manager.INTEGER_SIZE).putInt(i).array();
		out.write(bytes);
	}
	
	private void writePrimitive(OutputStream out, short s) throws IOException
	{
		byte[] bytes = ByteBuffer.allocate(manager.SHORT_SIZE).putShort(s).array();
		out.write(bytes);
	}
	
	private void writePrimitive(OutputStream out, byte b) throws IOException
	{
		byte[] bytes = ByteBuffer.allocate(manager.BYTE_SIZE).put(b).array();
		out.write(bytes);
	}
	
	private void writePrimitive(OutputStream out, char c) throws IOException
	{
		byte[] bytes = ByteBuffer.allocate(manager.CHAR_SIZE).putChar(c).array();
		out.write(bytes);
	}
	
	private void writePrimitive(OutputStream out, double d) throws IOException
	{
		byte[] bytes = ByteBuffer.allocate(manager.DOUBLE_SIZE).putDouble(d).array();
		out.write(bytes);
	}
	
	private void writePrimitive(OutputStream out, long l) throws IOException
	{
		byte[] bytes = ByteBuffer.allocate(manager.LONG_SIZE).putLong(l).array();
		out.write(bytes);
	}
	
	private void writePrimitive(OutputStream out, boolean b) throws IOException
	{
		if(b)
			writePrimitive(out,1);
		else
			writePrimitive(out,0);
	}
	
}