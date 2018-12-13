/*
 * author : pritesh ranjan
 * date 5th Nov 2018
 */
package CountUniqueData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CountUniqueData {

	public static Map<String, Integer> userMap=new HashMap<>();
	public static int count=0;
	public static int noOfLinesRead=0;
	public static int sum=0;
	
	public static void main(String[] args) {

		try {
			readFromFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		displayMap();
		System.out.println("noOfLinesRead : "+ noOfLinesRead);
		System.out.println("total sum of count per user : " + sum);
	}
	
	public static void readFromFile() throws FileNotFoundException,IOException
	{
		String str =System.getProperty("user.dir");
		// System.out.println(str);
		File file = new File(str + "/trackerUserList.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		while ((st = br.readLine()) != null)
		{
			addToMap(st);
			noOfLinesRead++;
		}
		System.out.println("\n\ncame outside");
		
		br.close();
		return;
	}

	private static void addToMap(String st) {
		if (userMap.containsKey(st)) {
			userMap.put(st,userMap.get(st)+1);
		}
		else
		{
			userMap.put(st,1);
		}
		
	}
	
	private static void displayMap() {
		for(Map.Entry<String, Integer> m : userMap.entrySet())
		{
			System.out.println(++count + " >> " + m.getKey() + ":" + m.getValue());
			sum+=m.getValue();
		}
		
	}
	
	
}
