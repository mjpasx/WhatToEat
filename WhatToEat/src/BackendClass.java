import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Class file that holds the functions common between TestMatching.java and YelpBackend.java
public class BackendClass
{
	// Constants
    private final String GOOGLE_URL = "https://language.googleapis.com/v1/documents:analyzeEntitySentiment?key=";
    private final String REQUEST_BEG = "{\"document\":{\"type\":\"PLAIN_TEXT\",\"content\":\"";
    private final String REQUEST_END = "\"},\"encodingType\":\"UTF8\"}";
    private final String FOURSQUARE_MENU_URL = "https://api.foursquare.com/v2/venues/VENUE_ID/menu";
  	private final String FOURSQUARE_VENUEID_URL = "https://api.foursquare.com/v2/venues/search?venues/search?near=";

  	public BackendClass()
  	{
  		
  	}
  	
  	public void CleanUp(LinkedList<Scanner> scanners)
    {
    	// Clean up once we are done
    	for (int i = 0; i < scanners.size(); i ++)
    	{
    		scanners.get(i).close();
    	}
    }

    public String FindBusinessId(String restName, Scanner businessScanner) throws FileNotFoundException, ParseException
    {
        while (businessScanner.hasNextLine())
        {
            String line = businessScanner.nextLine();
            Object obj = new JSONParser().parse(line);
            JSONObject businessObj = (JSONObject) obj;
            String name = (String) businessObj.get("name");
            // If you get a matching business name, then return the business ID
            if (name.equals(restName))
            {
            	return (String) businessObj.get("business_id");
            }
        }
        // Return null if we don't have a matching business
        return null;
    }

    public LinkedList<String> GetReviews(String businessId, Scanner reviewScanner) throws FileNotFoundException, ParseException
    {
    	LinkedList<String> reviews = new LinkedList<String>();
        while (reviewScanner.hasNextLine())
        {
            String line = reviewScanner.nextLine();
            Object obj = new JSONParser().parse(line);
            JSONObject reviewObj = (JSONObject) obj;
            String id = (String) reviewObj.get("business_id");
            // If you get a matching business ID, add it to the list
            if (businessId.equals(id))
            {
            	reviews.add((String) reviewObj.get("text"));
            }
        }
        // Return all the reviews at the end
    	return reviews;
    }

    public LinkedList<String> EliminateQuotes(LinkedList<String> reviews)
    {
    	String text = "";
    	// Within each review, escape all quotes
    	// Need to do this for the Google API calls to work
    	for (int i = 0; i < reviews.size(); i ++)
    	{
    		text = reviews.get(i);
    		reviews.remove(i);
    		reviews.add(i, text.replace("\"", "\\\""));
    	}
    	return reviews;
    }

    public LinkedList<String> QueryGoogleApi(LinkedList<String> reviews)
    {
    	// Get the API key from the environment
    	String apiKey = System.getenv("API_KEY");
    	LinkedList<String> sentimentAnalysis = new LinkedList<String>();
    	System.out.println(reviews.size());
    	for (int i = 0; i < reviews.size(); i ++)
    	{
    		try
        	{
    			// Set up a connection with Google API
        		URL url = new URL(GOOGLE_URL + apiKey);
            	HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            	// Construct the POST message
            	connection.setRequestMethod("POST");
            	connection.setRequestProperty("Content-Type", "application/json");
            	connection.setDoOutput(true);
            	// Write the request to Google
            	OutputStream os = connection.getOutputStream();
            	OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            	osw.write(REQUEST_BEG + reviews.get(i) + REQUEST_END);
            	osw.flush();
            	osw.close();
            	os.close();
            	connection.connect();

            	// Read in the response
            	String reply;
            	BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            	ByteArrayOutputStream buf = new ByteArrayOutputStream();
            	int result = inputStream.read();
            	while(result != -1) {
            	    buf.write((byte) result);
            	    result = inputStream.read();
            	}
            	// Turn the message into a string
            	reply = buf.toString();
            	System.out.println(reply);
            	System.out.println("\n\n\n\n\n");
            	sentimentAnalysis.add(reply);
        	}
    		// Catch if there is an error
        	catch(Exception e)
        	{
        		System.out.println("Error.");
        		System.out.println(e);

        		//return null;
        	}
    	}
    	// Return all of the sentiment analysis results
    	return sentimentAnalysis;
    }
    
    public LinkedList<EntityClass> GetEntities(String review, String revText) throws ParseException
    {
    	LinkedList<EntityClass> entities = new LinkedList<EntityClass>();
    	JSONObject entityObj;
    	String name;
    	Object sentiment;
    	JSONObject sentimentObj;
    	Object mag;
    	double magnitude;
    	
    	// Turn the object into a JSON object
    	Object obj = new JSONParser().parse(review);
        JSONObject reviewObj = (JSONObject) obj;
        // Get the entities array, iterate through it
        JSONArray ents = (JSONArray) reviewObj.get("entities");
        for (int i = 0; i < ents.size(); i ++)
        {
        	EntityClass object = new EntityClass();
        	entityObj = (JSONObject) ents.get(i);
        	// Get the name field from each object
        	name = (String) entityObj.get("name");
        	// From the sentiment object within the entity, get the sentiment score
        	sentiment = entityObj.get("sentiment");
        	sentimentObj = (JSONObject) sentiment;
        	mag = sentimentObj.get("score");
        	// Need to convert to to a double (sometimes comes as a long, sometimes a double)
        	if (mag instanceof Long)
        	{
        		magnitude = ((Long) mag).doubleValue();
        	}
        	else
        	{
        		magnitude = (double) mag;
        	}
        	// Add the new entity object
        	object.SetWord(name);
        	object.SetSentiment(magnitude);
        	object.SetReview(revText);
        	entities.add(object);
        }

    	return entities;
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
	public String FindVenueID(String restName) {
		String client_id = System.getenv("FOURSQUARE_CLIENT_KEY");
		String client_secret = System.getenv("FOURSQUARE_CLIENT_SECRET");
		String reply;
			try {
				URL url = new URL(FOURSQUARE_VENUEID_URL);
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

				return null;
			} finally {
				System.out.println("Finally");
			}
			return reply;
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
	public String QueryMenu(String venueID) {
		String client_id = System.getenv("FOURSQUARE_CLIENT_KEY");
		String client_secret = System.getenv("FOURSQUARE_CLIENT_SECRET");
		String reply;
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

				return null;
			} finally {
				System.out.println("Finally");
			}
			return reply;
		}
}
