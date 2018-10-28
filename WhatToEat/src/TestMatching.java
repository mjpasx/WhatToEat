
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
    	LinkedList<LinkedList<String>> allReviews = new LinkedList<LinkedList<String>>();
    	LinkedList<String> reviews = new LinkedList<String>();
    	// Get all of the reviews for each business
		for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
    		businessId = backend.FindBusinessId(TEST_RESTAURANTS[i], businessScanner);
    		reviews = backend.GetReviews(businessId, reviewScanner);
    		allReviews.add(backend.EliminateQuotes(reviews));
    	}
		
		//int numLevenstein = GetLevensteinMatches(allReviews);
    	
    }
    
    public static boolean LevensteinEditDistance(String str1, String str2, int distance)
    {
    	str1 = str1.toLowerCase();
    	str2 = str2.toLowerCase();
    	
    	int len1 = str1.length();
    	int len2 = str2.length();
    	int[][] matrix = new int[len1 + 1][len2 + 1];
    	int cost;
    	
    	// Initialize the first column and row to be i and j respectively
    	for (int i = 0; i < len1; i ++)
    	{
    		matrix[i][0] = i;
    	}
    	for (int i = 0; i < len2; i ++)
    	{
    		matrix[0][i] = i;
    	}
    	
    	for (int j = 1; j < len2 + 1; j ++)
    	{
    		for (int i = 1; i < len1 + 1; i ++)
    		{
    			// If the characters are the same, a substitution has cost 0
    			cost = 1;
    			if (str1.charAt(i - 1) == str2.charAt(j - 1))
    			{
    				cost = 0;
    			}
    			// Find min of 3 operations
    			matrix[i][j] = Math.min(Math.min(matrix[i-1][j] + 1, // deletion
    					matrix[i][j-1] + 1), // addition
    					matrix[i-1][j-1] + cost); //substitution
    		}
    	}
    	        
        if (matrix[len1][len2] > distance)
        {
        	return false;
        }
        return true;
    }
}






