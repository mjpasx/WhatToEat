
// Backend code to get reviews from a business name using Yelp dataset

import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.util.LinkedList;

public class YelpBackend
{
	// Constants
    static final String BUSINESS_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
    static final String REVIEWS_PATH = "yelp_dataset/yelp_academic_dataset_review.json";
    static final String GOOGLE_URL = "https://language.googleapis.com/v1/documents:analyzeEntitySentiment?key=";
    static final String REQUEST_BEG = "{\"document\":{\"type\":\"PLAIN_TEXT\",\"content\":\"";
    static final String REQUEST_END = "\"},\"encodingType\":\"UTF8\"}";
    static final String FOURSQUARE_MENU_URL = "https://api.foursquare.com/v2/venues/VENUE_ID/menu";
  	static final String FOURSQUARE_VENUEID_URL = "https://api.foursquare.com/v2/venues/search?venues/search?near=";
  	static final String FOURSQUARE_CLIENT_KEY = "";
  	static final String FOURSQUARE_CLIENT_SECRET = "";


    public static void main(String[] args) throws FileNotFoundException, ParseException
    {
    	LinkedList<Scanner> scanners = new LinkedList<Scanner>();
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
        String venueId = FindVenueID(restName);
        String businessId = FindBusinessId(restName, businessScanner);
        if (businessId == null)
        {
            System.out.println("Sorry, but we do not have any data on " + restName);
            System.out.println("Please try a different restaurant, or add your own review.");
            CleanUp(scanners);
            return;
        }
        LinkedList<String> reviews = GetReviews(businessId, reviewScanner);
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
        	System.out.println("\n\n\n\n\n\n");
        }

        reviews = EliminateQuotes(reviews);

        System.out.println("There are " + reviews.size() + " reviews for this place");

        LinkedList<String> sentimentAnalysis = new LinkedList<String>();
        sentimentAnalysis = QueryGoogleApi(reviews);

        CleanUp(scanners);
    }

    public static void CleanUp(LinkedList<Scanner> scanners)
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

    public static LinkedList<String> GetReviews(String businessId, Scanner reviewScanner) throws FileNotFoundException, ParseException
    {
    	LinkedList<String> reviews = new LinkedList<String>();
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

    public static LinkedList<String> EliminateQuotes(LinkedList<String> reviews)
    {
    	String text = "";
    	for (int i = 0; i < reviews.size(); i ++)
    	{
    		text = reviews.get(i);
    		reviews.remove(i);
    		reviews.add(i, text.replace("\"", "\\\""));
    	}
    	return reviews;
    }

    public static LinkedList<String> QueryGoogleApi(LinkedList<String> reviews)
    {
    	String apiKey = System.getenv("API_KEY");
    	LinkedList<String> sentimentAnalysis = new LinkedList<String>();
    	System.out.println(reviews.size());
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
            	//System.out.println(reply);
            	sentimentAnalysis.add(reply);
        	}
        	catch(Exception e)
        	{
        		System.out.println("Error.");
        		System.out.println(e);

        		return null;
        	}
        	finally
        	{
        		System.out.println("Finally");
        	}
    	}
    	return sentimentAnalysis;
    }

	/**
	 * curl call
	 * curl -X GET -G \
		  'https://api.foursquare.com/v2/venues/explore' \
		    -d client_id="CLIENT_ID" \
		    -d client_secret="CLIENT_SECRET" \
		    -d near = ""\
		    -d query = "" \
		    -d limit = 1
	 */
	public static String FindVenueID(String restName) {
		String client_id = System.getenv("FOURSQUARE_CLIENT_KEY");
		String client_secret = System.getenv("FOURSQUARE_CLIENT_SECRET");
			try {
				URL url = new URL(FOU);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setRequestMethod("GET");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);
				OutputStream os = connection.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
				osw.write(url + restName + "CLIENT_ID" + client_id + "CLIENT_SECRET" + client_secret);
				osw.flush();
				osw.close();
				os.close();
				connection.connect();
				String reply;
				BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				int result = inputStream.read();
				while (result != -1) {
					buf.write((byte) result);
					result = inputStream.read();
				}
				reply = buf.toString();
				System.out.println(reply);
			} catch (Exception e) {
				System.out.println("Error.");
				System.out.println(e);

				return;
			} finally {
				System.out.println("Finally");
			}
		}

		/**
		 * curl call
		 * curl -X GET -G \
			  'https://api.foursquare.com/v2/VENUE_ID/menu' \
			    -d client_id="CLIENT_ID" \
			    -d client_secret="CLIENT_SECRET" \
			    -d venue_id=""\
			    -d limit = 1
		 */
	public static String QueryMenu(String venueID) {
		String client_id = System.getenv("FOURSQUARE_CLIENT_KEY");
		String client_secret = System.getenv("FOURSQUARE_CLIENT_SECRET");
			try {
				URL url = new URL(FOURSQUARE_MENU_URL);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setRequestMethod("GET");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);
				OutputStream os = connection.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
				osw.write(url + venueID + "/menu" + "CLIENT_ID" + client_id + "CLIENT_SECRET" + client_secret);
				osw.flush();
				osw.close();
				os.close();
				connection.connect();

				String reply;
				BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				int result = inputStream.read();
				while (result != -1) {
					buf.write((byte) result);
					result = inputStream.read();
				}
				reply = buf.toString();
				System.out.println(reply);
			} catch (Exception e) {
				System.out.println("Error.");
				System.out.println(e);

				return;
			} finally {
				System.out.println("Finally");
			}
		}
}
