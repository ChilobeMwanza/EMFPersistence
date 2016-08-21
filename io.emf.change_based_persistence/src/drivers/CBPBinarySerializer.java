package drivers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import change.Changelog;
import change.Event;
import change.SetEAttributeSingleEvent;
import change.SetEReferenceManyEvent;
import change.SetEReferenceSingleEvent;
import change.UnsetEAttributeManyEvent;
import change.UnsetEReferenceManyEvent;
import change.UnsetEReferenceSingleEvent;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;

public class CBPBinarySerializer 
{
	private  final String classname = this.getClass().getSimpleName();
	private final String FORMAT_ID = "CBP_BIN"; 
	private final int FORMAT_VERSION = 1;
	//private final int BUFFER_SIZE = 8 * 1024;
	private final Charset STRING_ENCODING = StandardCharsets.UTF_8;
	
	private final List<Event> eventList;
    
    private final PersistenceManager manager;
    
    private final Changelog changelog; 
    
    private final EPackageElementsNamesMap ePackageElementsNamesMap;
    
    private OutputStream outputStream;
    
    public CBPBinarySerializer(PersistenceManager manager, Changelog changelog,EPackageElementsNamesMap 
    		ePackageElementsNamesMap)
    {
    	this.manager =  manager;
        this.changelog = changelog;
        this.ePackageElementsNamesMap = ePackageElementsNamesMap;
        
        this.eventList = manager.getChangelog().getEventsList();
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
        	case Event.SET_EREFERENCE_SINGLE:
        		handleSetEReferenceSingleEvent((SetEReferenceSingleEvent)e,outputStream);
        		break;
        	case Event.SET_EREFERENCE_MANY:
        		handleSetEReferenceManyEvent((SetEReferenceManyEvent)e,outputStream);
        		break;
        	case Event.UNSET_EREFERENCE_MANY:
        		handleUnsetEReferenceManyEvent((UnsetEReferenceManyEvent)e, outputStream);
        		break;
        	case Event.UNSET_EREFERENCE_SINGLE:
        		 handleUnsetEReferenceSingleEvent((UnsetEReferenceSingleEvent)e, outputStream);
                 break;
        	}
        }
        
        // switch 
        changelog.clearEvents();
      	outputStream.close();
      	manager.setResume(true);
	
    }
    
    
    private void handleSetEReferenceSingleEvent(SetEReferenceSingleEvent e,OutputStream out) throws IOException
    {
    	EObject added_obj = e.getAddedObject();
    	
    	if(e.getNotifierType() == Event.NotifierType.RESOURCE)
    	{
    		
    		if(changelog.addObjectToMap(added_obj))
    		{
    			writeInt(out,PersistenceManager.CREATE_AND_ADD_TO_RESOURCE);
    			writeInt(out,2);
    			writeInt(out,ePackageElementsNamesMap.getID(added_obj.eClass().getName()));
    			writeInt(out,changelog.getObjectId(added_obj));
    		}
    		else
    		{
    			writeInt(out,PersistenceManager.ADD_TO_RESOURCE);
    			writeInt(out,1);
    			writeInt(out,changelog.getObjectId(added_obj));
    		}
    	}
    	else if(e.getNotifierType() == Event.NotifierType.EOBJECT)
    	{
    		EObject focus_obj = e.getFocusObject();
    		
    		if(changelog.addObjectToMap(added_obj))
    		{
    			writeInt(out,PersistenceManager.CREATE_AND_SET_EREFERENCE_VALUE);
    			writeInt(out,changelog.getObjectId(focus_obj));
    			writeInt(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    			writeInt(out,2);
    			writeInt(out,ePackageElementsNamesMap.getID(added_obj.eClass().getName()));
    			writeInt(out,changelog.getObjectId(added_obj));
    		}
    		else
    		{
    			writeInt(out, PersistenceManager.SET_EREFERENCE_VALUE);
    			writeInt(out,changelog.getObjectId(focus_obj));
    			writeInt(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    			writeInt(out,1);
    			writeInt(out,changelog.getObjectId(added_obj));
    		}
    	}
    }
    
    private void handleUnsetEReferenceSingleEvent(UnsetEReferenceSingleEvent e, OutputStream out) throws IOException
    {
    	if(e.getNotifierType() == Event.NotifierType.RESOURCE)
    	{
    		writeInt(out, PersistenceManager.DELETE_FROM_RESOURCE);
    		writeInt(out,1);
    		writeInt(out,changelog.getObjectId(e.getRemovedObject()));
    	}
    	else if(e.getNotifierType() == Event.NotifierType.EOBJECT)
    	{
    		EObject focus_obj = e.getFocusObject();
    		
    		writeInt(out,PersistenceManager.UNSET_EREFERENCE_VALUE);
    		writeInt(out,changelog.getObjectId(focus_obj));
    		writeInt(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    		writeInt(out,1);
    		writeInt(out,changelog.getObjectId(e.getRemovedObject()));
    	}
    }
    
    
    private void handleSetEReferenceManyEvent(SetEReferenceManyEvent e, OutputStream out) throws IOException
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
    			writeInt(out,PersistenceManager.CREATE_AND_ADD_TO_RESOURCE);
    			writeInt(out,obj_create_list.size());
    			
    			for(TIntIterator it = obj_create_list.iterator(); it.hasNext();)
    			{
    				writeInt(out,it.next());
    			}
    			obj_create_list.clear();
    		}
    		if(!added_obj_list.isEmpty()) //ADD TO RESOURCE
    		{
    			writeInt(out, PersistenceManager.ADD_TO_RESOURCE);
    			writeInt(out,added_obj_list.size());
    			
    			for(TIntIterator it = added_obj_list.iterator(); it.hasNext();)
    			{
    				writeInt(out,it.next());
    			}
    			added_obj_list.clear();
    		}
    	}
    	else if(e.getNotifierType() == Event.NotifierType.EOBJECT)
    	{
    		EObject focus_obj = e.getFocusObj(); 
    		
    		if(!obj_create_list.isEmpty())//CREATE_AND_SET_REF_VALUE
    		{
    			writeInt(out,PersistenceManager.CREATE_AND_SET_EREFERENCE_VALUE);
    			writeInt(out,changelog.getObjectId(focus_obj));
    			writeInt(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    			writeInt(out,obj_create_list.size());
    			
    			for(TIntIterator it = obj_create_list.iterator(); it.hasNext();)
    			{
    				writeInt(out,it.next());
    			}
    		}
    		if(!added_obj_list.isEmpty()) //SET_REFERENCE_VALUE
    		{
    			writeInt(out,PersistenceManager.SET_EREFERENCE_VALUE);
    			writeInt(out,changelog.getObjectId(focus_obj));
    			writeInt(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    			writeInt(out,added_obj_list.size());
    			
    			for(TIntIterator it = added_obj_list.iterator(); it.hasNext();)
    			{
    				writeInt(out,it.next());
    			}
    		}
    	}
    }
    
    private void handleUnsetEReferenceManyEvent(UnsetEReferenceManyEvent e, OutputStream out) throws IOException
    {
    	List<EObject> removed_obj_list = e.getObjectList();
    
    	if(e.getNotiferType() == Event.NotifierType.RESOURCE)
    	{
    		writeInt(out, PersistenceManager.DELETE_FROM_RESOURCE);
    		writeInt(out,removed_obj_list.size());
    		
    		for(EObject obj : removed_obj_list)
    		{
    			writeInt(out,changelog.getObjectId(obj));
    		}
    	}
    	else if(e.getNotiferType() == Event.NotifierType.EOBJECT)
    	{
    		EObject focus_obj = e.getFocusObj();
    		
    		writeInt(out,PersistenceManager.UNSET_EREFERENCE_VALUE);
    		writeInt(out,changelog.getObjectId(focus_obj));
    		writeInt(out,ePackageElementsNamesMap.getID(e.getEReference().getName()));
    		writeInt(out,removed_obj_list.size());
    		
    		for(EObject obj: removed_obj_list)
    		{
    			writeInt(out,changelog.getObjectId(obj));
    		}
    	}
    }
    
	private void writeInitialRecord(OutputStream out) throws IOException 
	{
		EObject obj = null;
		Event e = eventList.get(0);

		switch (e.getEventType()) 
		{
		case Event.SET_EREFERENCE_SINGLE:
			obj = ((SetEReferenceSingleEvent) e).getAddedObject();
			break;
		case Event.SET_EREFERENCE_MANY:
			obj = ((SetEReferenceManyEvent) e).getEObjectList().get(0);
			break;
		default:
			try 
			{
				System.out.println(e.getEventType());
				throw new Exception("Error! first item in events list was not an Add event.");
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
				System.exit(0);
			}
		}

		if (obj == null) //tbr
		{
			System.out.println(e.getEventType()); 
			System.exit(0);
		}
		
		
		out.write(FORMAT_ID.getBytes(STRING_ENCODING)); //FORMAT ID
		writeInt(out, FORMAT_VERSION); //FORMAT VERSION
		writeString(out,obj.eClass().getEPackage().getNsURI()); //NS URI
	}
	
	
	private void writeString(OutputStream out, String str) throws IOException
	{
		byte[] bytes = str.getBytes(STRING_ENCODING);
		
		writeInt(out,bytes.length);
		out.write(bytes);
	}
	
	private void writeInt(OutputStream out, int i) throws IOException
	{
		byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
		out.write(bytes);
	}
	
}
