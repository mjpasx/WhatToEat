
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
    	"Case Insensitive Matching",
    	"Levenstein Edit Distance",
    	"Jaro Similarity Metric"
    };
    
    // Restaurants that we'll use to test
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
    
    // Per review, LinkedList of items (entities) we should (hope to) match
    // Per restaurant, LinkedList of reviews
    // Overall, LinkedList of all the restaurants
    static LinkedList<LinkedList<LinkedList<EntityClass>>> GOAL_MATCHES = new LinkedList<LinkedList<LinkedList<EntityClass>>>();
    
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
    	
    	// Open up scanners to read from the files
    	LinkedList<Scanner> scanners = new LinkedList<Scanner>();
    	File businessFile = new File(BUSINESS_PATH);
        Scanner businessScanner = new Scanner(businessFile);
        scanners.add(businessScanner);
        File reviewFile = new File(REVIEWS_PATH);
        Scanner reviewScanner = new Scanner(reviewFile);
        scanners.add(reviewScanner);
        
        String businessId = "";
    	LinkedList<String> reviews = new LinkedList<String>();
    	LinkedList<String> googleReplies = new LinkedList<String>();

    	LinkedList<EntityClass> perReview = new LinkedList<EntityClass>();
    	LinkedList<LinkedList<EntityClass>> perRestaurant = new LinkedList<LinkedList<EntityClass>>();
    	// Get all of the reviews for each business
		for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
			// Get the business ID, then the reviews, and send them to Google
    		businessId = backend.FindBusinessId(TEST_RESTAURANTS[i], businessScanner);
    		reviews = backend.GetReviews(businessId, reviewScanner);
    		reviews = backend.EliminateQuotes(reviews);
    		googleReplies = backend.QueryGoogleApi(reviews);
    		
    		perRestaurant.clear();
    		
    		// Get the entities from each review and add to that restaurant's entity list
    		for (int j = 0; j < googleReplies.size(); j ++)
    		{
    			perReview = backend.GetEntities(googleReplies.get(j), reviews.get(j));
    			perRestaurant.add(perReview);
    		}
    		// Add the restaurant's entities to the overall entity list
    		GOAL_MATCHES.add(perRestaurant);
    	}
		
		// Get the menu and menu items for each restaurant
		
		// Count the matches we get for each matching algorithm
		
		// Calculate the precision/recall/F1 score for each
		
		// Output some sort of table
		
		
    	backend.CleanUp(scanners);
    }
    
    public static boolean CaseInsensitiveMatch(String str1, String str2)
    {
    	// Check if the strings are exactly eqaul
    	str1 = str1.toLowerCase();
    	str2 = str2.toLowerCase();
    	
    	return str1.equals(str2);
    }
    
    public static boolean LevensteinEditDistance(String str1, String str2, int maxDistance)
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
    	
    	// Go through the rest of the strings
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
    	        
    	// Return true if the edit distance is not greater than the max distance
        if (matrix[len1][len2] <= maxDistance)
        {
        	return true;
        }
        // False otherwise
        return false;
    }
    
    public static boolean JaroSimilarity(String str1, String str2, double maxAmt)
    {
    	str1 = str1.toLowerCase();
    	str2 = str2.toLowerCase();
    	
    	int len1 = str1.length();
    	int len2 = str2.length();
    	
    	int matchDist = (int) Math.floor(Math.max(len1, len2) / 2) - 1;
    	double num1Matching = 0;
    	double num2Matching = 0;
    	double numTranspositions = 0;
    	
    	String str1Matches = "";
    	String str2Matches = "";
    	
    	// Go through each letter of the first word
    	for (int i = 0; i < len1; i ++)
    	{
    		// Find if it matches with any letter within matchDist in the other word
    		for (int j = 0; j < matchDist + 1; j ++)
    		{
    			// Make sure we are in bounds for the other word
    			if (i + j < len2)
    			{
    				// If we have a matching character moving forward
    				if (str1.charAt(i) == str2.charAt(i + j))
        			{
        				str1Matches += str1.charAt(i);
        				num1Matching ++;
        				break;
        			}
    			}
    			if ((i - j > 0) && (i - j < len2))
    			{
    				// If we have a matching character moving backwards
    				if (str1.charAt(i) == str2.charAt(i - j))
        			{
        				str1Matches += str1.charAt(i);
        				num1Matching ++;
        				break;
        			}
    			}
    		}
    	}
    	
    	// Go through each letter of the second word
    	for (int i = 0; i < len2; i ++)
    	{
    		// Find if it matches with any letter within matchDist in the other word
    		for (int j = 0; j < matchDist + 1; j ++)
    		{
    			// Make sure we are in bounds for the other word
    			if (i + j < len1)
    			{
    				// If we have a matching character moving forward
    				if (str2.charAt(i) == str1.charAt(i + j))
        			{
        				str2Matches += str2.charAt(i);
        				num2Matching ++;
        				break;
        			}
    			}
    			if ((i - j > 0) && (i - j < len1))
    			{
    				// If we have a matching character moving backwards
    				if (str2.charAt(i) == str1.charAt(i - j))
        			{
        				str2Matches += str2.charAt(i);
        				num2Matching ++;
        				break;
        			}
    			}
    		}
    	}
    	
    	double temp = num1Matching;
    	int index;
    	// Get the number of transpositions between the matching letters
    	while (temp > 0)
    	{
    		if (str1Matches.charAt(0) == str2Matches.charAt(0))
    		{
    			str2Matches = str2Matches.substring(1);
    		}
    		else
    		{
    			index = str2Matches.indexOf(str1Matches.charAt(0));
    			str2Matches = str2Matches.substring(0, index) + str2Matches.substring(index + 1);
    			numTranspositions ++;
    		}
    		str1Matches = str1Matches.substring(1);
    		temp --;
    	}
    	
    	// Calculate the total Jaro score
    	double metric = (num1Matching / len1 + num2Matching / len2 + (num1Matching - numTranspositions) / num1Matching) / 3.0;
    	System.out.println(metric);
    	
    	// If the metric is at least as high as given amount, return True
    	if (metric >= maxAmt)
    	{
    		System.out.println("True");
    		return true;
    	}
    	// False otherwise 
    	System.out.println("False");
    	return false;
    }
}






