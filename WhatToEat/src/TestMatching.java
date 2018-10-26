
import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import java.util.LinkedList;

public class TestMatching
{
	// Constants
    static final String BUSINESS_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
    static final String REVIEWS_PATH = "yelp_dataset/smallReviews.json";
    static final String GOOGLE_URL = "https://language.googleapis.com/v1/documents:analyzeEntitySentiment?key=";
    static final String REQUEST_BEG = "{\"document\":{\"type\":\"PLAIN_TEXT\",\"content\":\"";
    static final String REQUEST_END = "\"},\"encodingType\":\"UTF8\"}";
    static final String FOURSQUARE_MENU_URL = "https://api.foursquare.com/v2/venues/VENUE_ID/menu";
  	static final String FOURSQUARE_VENUEID_URL = "https://api.foursquare.com/v2/venues/search?venues/search?near=";
  	
    
    static final String[] MATCHING_ALGS = {
    	"Levenstein Edit Distance"
    };
    
    static final String[] TEST_RESTAURANTS = {
		"Tokyo Kitchen",
		"Wing's Express",
		"Cardenas",
		"Pasta Pomodoro",
		"Yuki Japanese & Korean Restaurant",
		"Crabby Don's Bar and Grill",
		"Fat City Franks",
		"Doner King",
		"Westside Bistro",
		"Lumiere French Kitchen"
    };
    
    /*  
     *  TEST RESTAURANTS (How many meals we could match at most)
     *  Tokyo Kitchen (31)
     *  Wing's Express (2)
     *  Cardenas (2)
     *  Pasta Pomodoro (4)
     *  Yuki Japanese & Korean Restaurant (17)
     *  Crabby Don's Bar and Grill (1)
     *  Fat City Franks (9)
     *  Doner King (8)
     *  Westside Bistro (4)
     *  Lumiere French Kitchen (28)
     */
    
    
    public static void main(String[] args) throws FileNotFoundException, ParseException
    {    	
    	BackendClass backend = new BackendClass();
    	
    	LinkedList<Scanner> scanners = new LinkedList<Scanner>();
    	File businessFile = new File(BUSINESS_PATH);
        Scanner businessScanner = new Scanner(businessFile);
        scanners.add(businessScanner);
        File reviewFile = new File(REVIEWS_PATH);
        Scanner reviewScanner = new Scanner(reviewFile);
        scanners.add(reviewScanner);
        
        String businessId = "";
    	LinkedList<String> businessIds = new LinkedList<String>();
    	LinkedList<LinkedList<String>> allReviews = new LinkedList<LinkedList<String>>();
    	// Get all of the reviews for each business
		for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
    		businessId = backend.FindBusinessId(TEST_RESTAURANTS[i], businessScanner);
    		allReviews.add(backend.GetReviews(businessId, reviewScanner));
    	}
		
		int numLevenstein = GetLevensteinMatches(allReviews);
    	
    }
}






