package evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.profiling.Stopwatch;

import drivers.EPackageElementsNamesMap;
import drivers.PersistenceManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class CBPTextCompare 
{
	
	private final List<String> differences = new ArrayList<String>();
	
	public void compare(String model1Location, String model2Location) throws IOException
	{
		
		
		 getChangeEvents(countLines(model1Location)+1,model2Location);
	}
	
	public void getChangeEvents(int startLine,String fileLocation) throws IOException
	{
		BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileLocation), PersistenceManager.STRING_ENCODING));
		
		String line;
		
		startLine --;
		for(int i = 0; i < startLine; i++)
		{
			br.readLine();
		}
		
		int eventType = -1;
		
		while((line = br.readLine())!=null)
		{
			//System.out.println(line);
			StringTokenizer st = new StringTokenizer(line);

            if (st.hasMoreElements())
                eventType = Integer.valueOf(st.nextToken());
            
			switch(eventType)
			{
			case PersistenceManager.CREATE_AND_ADD_EOBJECTS_TO_RESOURCE:
				System.out.println(eventType);
                //createAndAddEObjectsToResource(line);
                break;
            case PersistenceManager.CREATE_EOBJECTS_AND_SET_EREFERENCE_VALUES:
            	System.out.println(eventType);
               // createEObjectsAndSetEReferenceValues(line);
                break;
            case PersistenceManager.SET_EOBJECT_COMPLEX_EATTRIBUTE_VALUES:
                handleEAttributeEvent(line);
                break;
            case PersistenceManager.SET_EOBJECT_EREFERENCE_VALUES:
            	System.out.println(eventType);
               // handleEReferenceEvent(line,true);
                break;
            case PersistenceManager.UNSET_EOBJECT_COMPLEX_EATTRIBUTE_VALUES:
            	System.out.println(eventType);
               // handleEAttributeEvent(line,false);
                break;
            case PersistenceManager.UNSET_EOBJECT_EREFERENCE_VALUES:
            	System.out.println(eventType);
               // handleEReferenceEvent(line,false);
                break;
            case PersistenceManager.ADD_EOBJECTS_TO_RESOURCE:
            	System.out.println(eventType);
                //createAndAddEObjectsToResource(line);
                break;
            case PersistenceManager.REMOVE_EOBJECTS_FROM_RESOURCE:
            	System.out.println(eventType);
                //removeEObjectsFromResource(line);
                break;
            default:
			}
			
		}
		br.close();
	}
	
	public void handleEAttributeEvent(String line)
	{
		String[] stringArray = line.split(" ");

		int focusObjectID = Integer.valueOf(stringArray[1]);
        
		int eAttributeID = Integer.valueOf(stringArray[2]);
		
		String[] featureValuesArray = tokeniseString(getValueInSquareBrackets(line));

		StringBuilder sb = new StringBuilder("EAttributeChangeSpec{focusObjectID = "+focusObjectID+" attributeID = "+eAttributeID+" value = ");
	
		String delimeter = "";
		
		for(String str: featureValuesArray)
		{
			sb.append(str+delimeter);
			delimeter = ",";
		}
		sb.append("}");
		
		differences.add(sb.toString());
		
		//System.out.println(sb.toString());
	}
	
	public List<String> getDifferences()
	{
		return this.differences;
	}
	
	
	/*
	 * http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	 */
	public int countLines(String fileLocation) throws IOException
	{
		InputStream is = new FileInputStream(fileLocation);
		
		try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean endsWithoutNewLine = false;
	        while ((readChars = is.read(c)) != -1) {
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n')
	                    ++count;
	            }
	            endsWithoutNewLine = (c[readChars - 1] != '\n');
	        }
	        if(endsWithoutNewLine) {
	            ++count;
	        } 
	        
	       // System.out.println(count);
	        return count;
	    } finally 
		{
	        is.close();
	    }
	}
	
	// returns everything inbetween []
		private String getValueInSquareBrackets(String str) {
			Pattern p = Pattern.compile("\\[(.*?)\\]");
			Matcher m = p.matcher(str);

			String result = "";

			if (m.find())
				result = m.group(1);
			return result;
		}
		
		private String[] tokeniseString(String input) {
			String regex = "(?<!" + Pattern.quote(PersistenceManager.ESCAPE_CHAR) + ")"
					+ Pattern.quote(PersistenceManager.DELIMITER);

			String[] output = input.split(regex);

			for (int i = 0; i < output.length; i++) {
				output[i] = output[i].replace(PersistenceManager.ESCAPE_CHAR + PersistenceManager.DELIMITER,
						PersistenceManager.DELIMITER);
			}

			return output;
		}

	
}
