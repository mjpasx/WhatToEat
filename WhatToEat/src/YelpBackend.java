// Backend code to get reviews from a business name using Yelp dataset

import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import java.util.ArrayList;

public class YelpBackend
{
	// Constants
    static final String BUSINESS_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
    static final String REVIEWS_PATH = "yelp_dataset/smallReviews.json";
    static final String GOOGLE_URL = "https://language.googleapis.com/v1/documents:analyzeEntitySentiment?key=";
    static final String REQUEST_BEG = "{\"document\":{\"type\":\"PLAIN_TEXT\",\"content\":\"";
    static final String REQUEST_END = "\"},\"encodingType\":\"UTF8\"}";
    
    
    public static void main(String[] args) throws FileNotFoundException, ParseException
    {
    	ArrayList<Scanner> scanners = new ArrayList<Scanner>();
        Scanner inputScanner = new Scanner(System.in);
        scanners.add(inputScanner);
        File businessFile = new File(BUSINESS_PATH);
        Scanner businessScanner = new Scanner(businessFile);
        scanners.add(businessScanner);
        File reviewFile = new File(REVIEWS_PATH);
        Scanner reviewScanner = new Scanner(reviewFile);
        scanners.add(reviewScanner);
        
        System.out.println("Enter the restaurant name: ");
        String restName = inputScanner.nextLine();
        String businessId = FindBusinessId(restName, businessScanner);
        if (businessId == null)
        {
            System.out.println("Sorry, but we do not have any data on " + restName);
            System.out.println("Please try a different restaurant, or add your own review.");
            CleanUp(scanners);
            return;
        }
        ArrayList<String> reviews = GetReviews(businessId, reviewScanner);
        if (reviews.size() == 0)
        {
        	System.out.println("Sorry, we do not have any reviews for " + restName);
        	System.out.println("Please try a different restaurant or add your own review.");
        	CleanUp(scanners);
        	return;
        }
        for (int i = 0; i < reviews.size(); i ++)
        {
        	System.out.println(reviews.get(i));
        }
        
        QueryGoogleApi(reviews);
        
        CleanUp(scanners);
    }
    
    public static void CleanUp(ArrayList<Scanner> scanners)
    {
    	// Clean up once we are done
    	for (int i = 0; i < scanners.size(); i ++)
    	{
    		scanners.get(i).close();
    	}
    }

    public static String FindBusinessId(String restName, Scanner businessScanner) throws FileNotFoundException, ParseException
    {
        while (businessScanner.hasNextLine())
        {
            String line = businessScanner.nextLine();
            Object obj = new JSONParser().parse(line);
            JSONObject businessObj = (JSONObject) obj;
            String name = (String) businessObj.get("name");
            if (name.equals(restName))
            {
            	return (String) businessObj.get("business_id");
            }
        }
        return null;
    }
    
    public static ArrayList<String> GetReviews(String businessId, Scanner reviewScanner) throws FileNotFoundException, ParseException
    {
    	ArrayList<String> reviews = new ArrayList<String>();
        while (reviewScanner.hasNextLine())
        {
            String line = reviewScanner.nextLine();
            Object obj = new JSONParser().parse(line);
            JSONObject reviewObj = (JSONObject) obj;
            String id = (String) reviewObj.get("business_id");
            if (businessId.equals(id))
            {
            	reviews.add((String) reviewObj.get("text"));
            }
        }
    	return reviews;
    }
    
    public static void QueryGoogleApi(ArrayList<String> reviews)
    {
    	String apiKey = System.getenv("API_KEY");
    	for (int i = 0; i < reviews.size(); i ++)
    	{
    		try
        	{
        		URL url = new URL(GOOGLE_URL + apiKey);
            	HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            	connection.setRequestMethod("POST");
            	connection.setRequestProperty("Content-Type", "application/json");
            	connection.setDoOutput(true);
            	OutputStream os = connection.getOutputStream();
            	OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
            	osw.write(REQUEST_BEG + reviews.get(i) + REQUEST_END);
            	osw.flush();
            	osw.close();
            	os.close();
            	connection.connect();
            	
            	String reply;
            	BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            	ByteArrayOutputStream buf = new ByteArrayOutputStream();
            	int result = inputStream.read();
            	while(result != -1) {
            	    buf.write((byte) result);
            	    result = inputStream.read();
            	}
            	reply = buf.toString();
            	System.out.println(reply);
        	}
        	catch(Exception e)
        	{
        		System.out.println("Error.");
        		System.out.println(e);
        		
        		return;
        	}
        	finally
        	{
        		System.out.println("Finally");
        	}
    	}
    }
}

