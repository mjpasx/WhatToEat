
import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import java.util.ArrayList;

public class TestMatching
{
	// Constants
    static final String BUSINESS_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
    static final String REVIEWS_PATH = "yelp_dataset/smallReviews.json";
    static final String GOOGLE_URL = "https://language.googleapis.com/v1/documents:analyzeEntitySentiment?key=";
    static final String REQUEST_BEG = "{\"document\":{\"type\":\"PLAIN_TEXT\",\"content\":\"";
    static final String REQUEST_END = "\"},\"encodingType\":\"UTF8\"}";
    
    static final String[] TEST_RESTAURANTS = {"Tokyo Kitchen",
    		"Wing's Express",
    		"Cardenas",
    		"Pasta Pomodoro",
    		"Yuki Japanese & Korean Restaurant",
    		"Crabby Don's Bar and Grill",
    		"Fat City Franks",
    		"Doner King",
    		"Westside Bistro",
    		"Lumiere French Kitchen"};
    
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
    	int matches = 0;
    	
    }
}
