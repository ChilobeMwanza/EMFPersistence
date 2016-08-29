package change;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.emf.ecore.EObject;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;


public class Changelog 
{
	private final List<Event> event_list;
	
	private final TObjectIntMap<EObject> eObjectToIDMap = new TObjectIntHashMap<EObject>();

	private final String classname = this.getClass().getSimpleName();
	
	private static int current_id = 0; 
	
	public Changelog()
	{
		event_list = new ArrayList<Event>();
	}
	
	public boolean addObjectToMap(EObject obj)
	{
		if(!eObjectToIDMap.containsKey(obj))
		{
			eObjectToIDMap.put(obj, current_id);
			
			current_id = current_id +1;
			return true;
		}
		return false;
	}
	
	public boolean addObjectToMap(EObject obj, int id)
	{
		if(!eObjectToIDMap.containsKey(obj))
		{
			eObjectToIDMap.put(obj, id);
			
			if(id >= current_id)
			{
				current_id = id + 1;
			}
			return true;
		}
		return false;
	}
	
	public void deleteEObjectFromMap(EObject obj)
	{
		eObjectToIDMap.remove(obj);
		
		if(eObjectToIDMap.containsKey(obj)) //tbr
		{
			System.out.println(classname+ " nope!!");
			System.exit(0);
		}
	}
	
	public int getObjectId(EObject obj)
	{
	
		if(!eObjectToIDMap.containsKey(obj)) //tbr
		{
			System.out.println(classname+" search returned false");
			System.exit(0);
		}
		return eObjectToIDMap.get(obj);
	
	}

	public void addEvent(Event e)
	{
		event_list.add(e);
	}
	
	public void removeEvent(Event e)
	{
		event_list.remove(e);
	}
	
	public void clearEvents()
	{
		event_list.clear();
	}
	
	public List<Event> getEventsList()
	{
		return event_list;
	}
	
	public void removeRedundantEvents()
	{
	//	TObjectArrayList foo;
	
		//map of eattribute names
		TIntObjectMap<List<Object>> attributeIDToValueMap = new TIntObjectHashMap<List<Object>>();
		
		TIntObjectMap<TIntObjectMap<List<Object>>> eObjectToEAttributeMap = new TIntObjectHashMap<TIntObjectMap<List<Object>>>();
		
		//TIntegerObjectArrayList;
		

	}
}
