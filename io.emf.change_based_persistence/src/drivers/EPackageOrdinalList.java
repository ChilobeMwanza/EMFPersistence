package drivers;
/**
 * The EPackageOridinalList class stores the order in which EPackage EClasses and their structural 
 * features appear. The first EClass in the EPackage will have ordinality 0, likewise the first
 * EStructural feature of the first EClass will have ordinality 0. For compaction, 
 * EStructural features have ID's of the form XY. Where X = ordinality of parent EClass within 
 * EPackage and Y = ordinality of feature within EClass.
 */
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class EPackageOrdinalList 
{
	private Map <String, EClass> name_eclass_map  = new HashMap<String, EClass>(); 
	private Map <Integer, String> id_eclass_name_map  = new HashMap<Integer,String>();
	
	public void addEClass(String name, int id)
	{
		EClass eClass = new EClass(id);
		name_eclass_map.put(name, eClass);
		id_eclass_name_map.put(id, name);
	}
	
	public int getEClassID(String name)
	{
		return name_eclass_map.get(name).getID();
	}
	
	public String getEClassName(int id)
	{
		return id_eclass_name_map.get(id);
	}
	
	public void addEStructuralFeature(String eclass_name, String feature_name, int feature_ordinality)
	{
		EClass eClass = name_eclass_map.get(eclass_name);
		eClass.addFeature(feature_name, feature_ordinality);
	}
	
	public String getEStructuralFeatureName(int feature_id)
	{
		int eClassID = feature_id / 10; //get value before the .
		
	
		String eclass_name = id_eclass_name_map.get(eClassID);
		EClass eClass = name_eclass_map.get(eclass_name);
		return eClass.getFeatureName(feature_id);
	}
	
	public double getEStructuralFeatureID(String eclass_name, String feature_name)
	{
		EClass eClass = name_eclass_map.get(eclass_name);
		return eClass.getFeatureID(feature_name);
	}
	
	class EClass
	{
		int class_id;
		private BiMap <String, Double> features_map  = HashBiMap.create(); 
		
		public EClass(int id)
		{
			this.class_id = id;
		}
		
		private void addFeature(String feature_name, int feature_ordinality)
		{
			String feature_id_str = ""+class_id+"."+feature_ordinality;
			features_map.put(feature_name, Double.valueOf(feature_id_str));
		}
		
		private double getFeatureID(String name)
		{
			return features_map.get(name);
		}
		
		private String getFeatureName(double id)
		{
			return features_map.inverse().get(id);
		}
		
		private int getID()
		{
			return this.class_id;
		}
	}
}
