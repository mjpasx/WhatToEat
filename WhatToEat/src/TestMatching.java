
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

import org.json.simple.parser.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;

public class TestMatching
{
	// Constants
    static final String BUSINESS_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
    static final String REVIEWS_PATH = "yelp_dataset/yelp_academic_dataset_review.json";

    
    static final String[] MATCHING_ALGS = {
    	"Case Insensitive Matching",
    	"Levenstein Edit Distance",
    	"Jaro Similarity Metric",
    	"Jaccard Similarity (2-gram)",
    	"Jaccard Similarity (3-gram)",
    	"Jaccard Similarity (word-gram)"
    };
    
    // Each int[] is true positives, false positives, false negatives
    // for each of the MATCHING_ALGS
    static BigDecimal[][] Matches = {
    		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
    		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
    		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
    		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
    		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
    		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO }
    };
    
    // Restaurants that we'll use to test
    /*
    static final String[] TEST_RESTAURANTS = {
		"Pasta Pomodoro",
		"Lumiere French Kitchen"
    };
    */
    static final String[] TEST_RESTAURANTS = {
    		"Pasta Pomodoro"
        };
    
    // First item is the food item from the review, the second is what we get from OpenMenu menu
    // If the second item of the 2-string array is empty, that means that there is no corresponding
    // meal item. So any match should count as a false positive
    static final String[][][] PASTA_POMODORO_MATCHES = {
    	{
    		{"pasta", ""}, {"fettuccine alfredo", ""}, {"chicken", ""}
    	}, 
    	{
    		{"tortellini soup", "Tortellini Soup"}, {"Cesar", "Caesar Salad"}
    	}, 
    	{
    		{"Calamari F.", ""}
    	},
    	{
    		{}
    	},
    	{
    		{"Linguine", ""}, {"crab", ""}
    	},
    	{
    		{"Tortellini...Rest", "Tortellini Soup"}
    	},
    	{
    		{"risotto w/ clams", ""},  {"Ravioli di Zucca", "Ravioli"}, {"ravioli", "Ravioli"}
    	},
    	{
    		{}
    	},
    	{
    		{"fettuccine alfredo", ""},
    	},
    	{
    		{"Ravioli di Zucca Appetizer", "Ravioli"}, {"Healthy Fish", "HEALTHY FISH"}, {"Penne Salsiccia", ""}, {"butternut squash ravioli", "Butternut Squash Ravioli"}
    	},
    	{
    		{"spinach salad", "Spinach"}, {"raviolis", "Ravioli"}, {"salads", ""}
    	},
    	{
    		{"meatballs", ""}
    	},
    	{
    		{"shrimp", "Sautu00e9ed Shrimp"}, {"ravioli", "Ravioli"}, {"linguine", ""}
    	},
    	{
    		{"Polenta Farcita", "Polenta"}, {"Gamberi", ""}, {"Tortellini alla Panna", ""}
    	},
    	{
    		{"gemeli", "Gemelli"}, {"chicken f alfredo", ""}
    	},
    	{
    		{}
    	}
    };
    
    static String[][][][] GROUND_TRUTH = { PASTA_POMODORO_MATCHES };
    
    // Per review, ArrayList of items (entities) we should (hope to) match
    // Per restaurant, ArrayList of reviews
    // Overall, ArrayList of all the restaurants
    static ArrayList<ArrayList<ArrayList<EntityClass>>> GOAL_MATCHES = new ArrayList<ArrayList<ArrayList<EntityClass>>>();
    
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
    	ArrayList<Scanner> scanners = new ArrayList<Scanner>();
    	File businessFile = new File(BUSINESS_PATH);
        Scanner businessScanner = new Scanner(businessFile);
        scanners.add(businessScanner);
        
        ArrayList<RestaurantClass> businessIds = new ArrayList<RestaurantClass>();
    	ArrayList<ReviewClass> reviews = new ArrayList<ReviewClass>();

    	// Get all of the reviews for each business
		for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
			

			// Get the business IDs, then the reviews, and send them to Google
    		businessIds = backend.FindBusinessId(TEST_RESTAURANTS[i], businessScanner);
    		for (int j = 0; j < businessIds.size(); j ++)
    		{
    			ArrayList<ReviewClass> newReviews = backend.GetReviews(businessIds.get(j));
    			newReviews = backend.EliminateQuotes(newReviews);
    			reviews.addAll(newReviews);
    		}
    		    		
    		reviews = backend.QueryGoogleApi(reviews);
    		
    		ArrayList<ArrayList<EntityClass>> perRestaurant = new ArrayList<ArrayList<EntityClass>>();
    		perRestaurant.clear();
    		
    		// Get the entities from each review and add to that restaurant's entity list
    		for (int j = 0; j < reviews.size(); j ++)
    		{
    			ArrayList<EntityClass> perReview = new ArrayList<EntityClass>();
    			perReview = backend.GetEntities(reviews.get(j));
    			perRestaurant.add(perReview);
    		}
    		
    		// Add the restaurant's entities to the overall entity list
    		GOAL_MATCHES.add(perRestaurant);
    	}
		
		
		//"{\"entities\":[{\"name\":\"experience\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.28340155,\"mentions\":[{\"text\":{\"content\":\"experience\",\"beginOffset\":44},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"times\",\"type\":\"EVENT\",\"metadata\":{},\"salience\":0.28340155,\"mentions\":[{\"text\":{\"content\":\"times\",\"beginOffset\":60},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"food\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.079445906,\"mentions\":[{\"text\":{\"content\":\"food\",\"beginOffset\":122},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"reviewer\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.054920267,\"mentions\":[{\"text\":{\"content\":\"reviewer\",\"beginOffset\":75},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"service\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.04467418,\"mentions\":[{\"text\":{\"content\":\"service\",\"beginOffset\":133},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"food\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.042315524,\"mentions\":[{\"text\":{\"content\":\"food\",\"beginOffset\":159},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.3,\"score\":0.3}}],\"sentiment\":{\"magnitude\":0.3,\"score\":0.3}},{\"name\":\"pasta\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.040823992,\"mentions\":[{\"text\":{\"content\":\"pasta\",\"beginOffset\":199},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":-0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":-0.9}},{\"name\":\"fettuccine alfredo\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.03429935,\"mentions\":[{\"text\":{\"content\":\"fettuccine alfredo\",\"beginOffset\":270},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"chicken\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.03429935,\"mentions\":[{\"text\":{\"content\":\"chicken\",\"beginOffset\":294},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Italian\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.03264804,\"mentions\":[{\"text\":{\"content\":\"Italian\",\"beginOffset\":114},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"water\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.0258281,\"mentions\":[{\"text\":{\"content\":\"water\",\"beginOffset\":365},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"star\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.0258281,\"mentions\":[{\"text\":{\"content\":\"star\",\"beginOffset\":384},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"visit\",\"type\":\"EVENT\",\"metadata\":{},\"salience\":0.018114071,\"mentions\":[{\"text\":{\"content\":\"visit\",\"beginOffset\":325},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"language\":}"
		//"{\"entities\":[{\"name\":\"vibe\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.16891496,\"mentions\":[{\"text\":{\"content\":\"vibe\",\"beginOffset\":94},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}}],\"sentiment\":{\"magnitude\":0.1,\"score\":0}},{\"name\":\"average\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.13899317,\"mentions\":[{\"text\":{\"content\":\"average\",\"beginOffset\":33},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"restaurants\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.13899317,\"mentions\":[{\"text\":{\"content\":\"restaurants\",\"beginOffset\":72},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Italian\",\"type\":\"LOCATION\",\"metadata\":{\"mid\":\"/m/09y2k2\",\"wikipedia_url\":\"https://en.wikipedia.org/wiki/Italian_cuisine\"},\"salience\":0.10448828,\"mentions\":[{\"text\":{\"content\":\"Italian\",\"beginOffset\":64},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"text\":{\"content\":\"Italian\",\"beginOffset\":133},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"text\":{\"content\":\"Italian\",\"beginOffset\":645},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Pasta Pomodoro\",\"type\":\"CONSUMER_GOOD\",\"metadata\":{\"mid\":\"/m/04qsz7\",\"wikipedia_url\":\"https://en.wikipedia.org/wiki/Pasta_al_pomodoro\"},\"salience\":0.09982995,\"mentions\":[{\"text\":{\"content\":\"Pasta Pomodoro\",\"beginOffset\":0},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"prices\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.05160996,\"mentions\":[{\"text\":{\"content\":\"prices\",\"beginOffset\":394},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":1.6,\"score\":0.4}},{\"name\":\"bistro\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.025960812,\"mentions\":[{\"text\":{\"content\":\"bistro\",\"beginOffset\":141},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}}],\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}},{\"name\":\"soup\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.024012858,\"mentions\":[{\"text\":{\"content\":\"soup\",\"beginOffset\":297},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"salad\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.022587096,\"mentions\":[{\"text\":{\"content\":\"salad\",\"beginOffset\":334},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"Jazz musicians\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.020259658,\"mentions\":[{\"text\":{\"content\":\"Jazz musicians\",\"beginOffset\":577},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"tortellini soup\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.019863768,\"mentions\":[{\"text\":{\"content\":\"tortellini soup\",\"beginOffset\":238},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"salad\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.019863768,\"mentions\":[{\"text\":{\"content\":\"salad\",\"beginOffset\":264},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Cesar\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.017823191,\"mentions\":[{\"text\":{\"content\":\"Cesar\",\"beginOffset\":258},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"text\":{\"content\":\"Cesar\",\"beginOffset\":328},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0.2,\"score\":0.2}}],\"sentiment\":{\"magnitude\":0.2,\"score\":0.1}},{\"name\":\"shavings\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.017560944,\"mentions\":[{\"text\":{\"content\":\"shavings\",\"beginOffset\":361},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}}],\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}},{\"name\":\"times\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.017455934,\"mentions\":[{\"text\":{\"content\":\"times\",\"beginOffset\":196},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Pecorino cheese\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.01611636,\"mentions\":[{\"text\":{\"content\":\"Pecorino cheese\",\"beginOffset\":373},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"amount\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.015884323,\"mentions\":[{\"text\":{\"content\":\"amount\",\"beginOffset\":447},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"New York City\",\"type\":\"LOCATION\",\"metadata\":{\"mid\":\"/m/02_286\",\"wikipedia_url\":\"https://en.wikipedia.org/wiki/New_York_City\"},\"salience\":0.015378489,\"mentions\":[{\"text\":{\"content\":\"New York City\",\"beginOffset\":173},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"book\",\"type\":\"WORK_OF_ART\",\"metadata\":{},\"salience\":0.012083021,\"mentions\":[{\"text\":{\"content\":\"book\",\"beginOffset\":611},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"way\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.010818679,\"mentions\":[{\"text\":{\"content\":\"way\",\"beginOffset\":669},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"jazz\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.009631952,\"mentions\":[{\"text\":{\"content\":\"jazz\",\"beginOffset\":626},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}}],\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}},{\"name\":\"food\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.008461598,\"mentions\":[{\"text\":{\"content\":\"food\",\"beginOffset\":653},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"Both\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.007830428,\"mentions\":[{\"text\":{\"content\":\"Both\",\"beginOffset\":271},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"choices\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.005321883,\"mentions\":[{\"text\":{\"content\":\"choices\",\"beginOffset\":457},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}}],\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}},{\"name\":\"lunch menu\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.005321883,\"mentions\":[{\"text\":{\"content\":\"lunch menu\",\"beginOffset\":474},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"flier\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.0049338653,\"mentions\":[{\"text\":{\"content\":\"flier\",\"beginOffset\":517},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"language\":\"en\"}"
		//"{\"entities\":[{\"name\":\"everything\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.21558267,\"mentions\":[{\"text\":{\"content\":\"everything\",\"beginOffset\":18},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"fact\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.21558267,\"mentions\":[{\"text\":{\"content\":\"fact\",\"beginOffset\":74},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Calamari F.\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.11323813,\"mentions\":[{\"text\":{\"content\":\"Calamari F.\",\"beginOffset\":29},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"text\":{\"content\":\"Calamari F.\",\"beginOffset\":460},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"kids\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.084122136,\"mentions\":[{\"text\":{\"content\":\"kids\",\"beginOffset\":92},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"service\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.06495431,\"mentions\":[{\"text\":{\"content\":\"service\",\"beginOffset\":374},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.7,\"score\":0.7}}],\"sentiment\":{\"magnitude\":0.7,\"score\":0.3}},{\"name\":\"OMG\",\"type\":\"ORGANIZATION\",\"metadata\":{},\"salience\":0.050097227,\"mentions\":[{\"text\":{\"content\":\"OMG\",\"beginOffset\":84},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"LAGUNA BEACH\",\"type\":\"LOCATION\",\"metadata\":{\"mid\":\"\/m\/0r2gj\",\"wikipedia_url\":\"https:\/\/en.wikipedia.org\/wiki\/Laguna_Beach,_California\"},\"salience\":0.050097227,\"mentions\":[{\"text\":{\"content\":\"LAGUNA BEACH\",\"beginOffset\":100},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Pasta Pomodoro\",\"type\":\"ORGANIZATION\",\"metadata\":{},\"salience\":0.050097227,\"mentions\":[{\"text\":{\"content\":\"Pasta Pomodoro\",\"beginOffset\":120},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"star\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.037015732,\"mentions\":[{\"text\":{\"content\":\"star\",\"beginOffset\":349},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"PV mall\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.027896617,\"mentions\":[{\"text\":{\"content\":\"PV mall\",\"beginOffset\":230},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"choices\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.025605854,\"mentions\":[{\"text\":{\"content\":\"choices\",\"beginOffset\":304},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"area\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.025605854,\"mentions\":[{\"text\":{\"content\":\"area\",\"beginOffset\":323},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"service\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.021795997,\"mentions\":[{\"text\":{\"content\":\"service\",\"beginOffset\":491},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"stars\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.011624987,\"mentions\":[{\"text\":{\"content\":\"stars\",\"beginOffset\":598},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"difference\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.0066833715,\"mentions\":[{\"text\":{\"content\":\"difference\",\"beginOffset\":441},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"language\":\"en\"}"
		//"{\"entities\":[{\"name\":\"food\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.63773847,\"mentions\":[{\"text\":{\"content\":\"food\",\"beginOffset\":32},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":1.9,\"score\":0.9}},{\"name\":\"Italian\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.115508616,\"mentions\":[{\"text\":{\"content\":\"Italian\",\"beginOffset\":24},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"wine\",\"type\":\"CONSUMER_GOOD\",\"metadata\":{},\"salience\":0.10651932,\"mentions\":[{\"text\":{\"content\":\"wine\",\"beginOffset\":42},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}}],\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}},{\"name\":\"service\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.041607823,\"mentions\":[{\"text\":{\"content\":\"service\",\"beginOffset\":63},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":-0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":-0.9}},{\"name\":\"mom\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.025314653,\"mentions\":[{\"text\":{\"content\":\"mom\",\"beginOffset\":126},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"PV\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.023247197,\"mentions\":[{\"text\":{\"content\":\"PV\",\"beginOffset\":152},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"pesto oil\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.020910718,\"mentions\":[{\"text\":{\"content\":\"pesto oil\",\"beginOffset\":230},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.2,\"score\":0.2}}],\"sentiment\":{\"magnitude\":0.2,\"score\":0.2}},{\"name\":\"bread\",\"type\":\"CONSUMER_GOOD\",\"metadata\":{},\"salience\":0.012213877,\"mentions\":[{\"text\":{\"content\":\"bread\",\"beginOffset\":259},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.4,\"score\":0.4}}],\"sentiment\":{\"magnitude\":0.4,\"score\":0.4}},{\"name\":\"lot\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.009246689,\"mentions\":[{\"text\":{\"content\":\"lot\",\"beginOffset\":184},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"place\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.007692615,\"mentions\":[{\"text\":{\"content\":\"place\",\"beginOffset\":211},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"language\":\"en\"}"
						
			
		
		// Get the menu
		ArrayList<String> menuResults = new ArrayList<String>();
		for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
		{
			//String restaurantInfo = backend.QueryOpenMenuSearch(TEST_RESTAURANTS[i], businessIds.get(0).GetZipCode());
	       	//Testing the Open Menu Restaurant API call
	        //String menuInfo = backend.GrabMenu(restaurantInfo);
			String menuInfo = "{\"response\":{\"api\":{\"status\":200,\"api_version\":\"2.1\",\"format\":\"json\",\"api_key\":\"51a5b6d4-dcb3-11e8-8d62-525400552a35\"},\"result\":{\"restaurant_info\":{\"restaurant_name\":\"Pasta Pomodoro\",\"brief_description\":\"Fresh. Local. Authentic. A collection of modern, family friendly Italian restaurants.\",\"full_description\":null,\"location_id\":\"\",\"mobile\":null,\"address_1\":\"146 Sunset Drive #A-3\",\"address_2\":\"\",\"city_town\":\"San Ramon\",\"state_province\":\"CA\",\"postal_code\":\"94583\",\"country\":\"US\",\"phone\":\"925-867-1407\",\"fax\":\"\",\"longitude\":\"-121.908762\",\"latitude\":\"37.7952771\",\"business_type\":\"Corporate\",\"utc_offset\":null,\"website_url\":\"http://www.pastapomodoro.com\"},\"environment_info\":{\"cuisine_type_primary\":\"Italian\",\"cuisine_type_secondary\":null,\"smoking_allowed\":null,\"takeout_available\":null,\"seating_qty\":null,\"max_group_size\":null,\"pets_allowed\":null,\"wheelchair_accessible\":null,\"age_level_preference\":null,\"dress_code\":null,\"delivery_available\":null,\"delivery_radius\":null,\"delivery_fee\":null,\"catering_available\":null,\"reservations\":null,\"alcohol_type\":null,\"music_type\":null},\"operating_days\":[],\"operating_days_printable\":[],\"logo_urls\":[],\"seating_locations\":[{\"seating_location\":\"indoor\"}],\"accepted_currencies\":[{\"accepted_currency\":\"USD\"}],\"parking\":false,\"settings\":{\"social\":{\"ChainID\":\"250\",\"facebook\":\"https://www.facebook.com/pastapomodoro\",\"twitter\":\"https://twitter.com/PastaPomodoro\",\"instagram\":\"\",\"pinterest\":\"\",\"youtube\":\"\",\"linkedin\":\"\"}},\"menus\":[{\"menu_name\":\"Dinner Menu\",\"menu_description\":\"\",\"menu_note\":\"\",\"currency_symbol\":\"USD\",\"language\":\"en\",\"menu_duration_name\":\"dinner\",\"menu_duration_time_start\":\"\",\"menu_duration_time_end\":\"\",\"menu_groups\":[{\"group_name\":\"appetizers\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Sautu00e9ed Shrimp\",\"menu_item_description\":\"gulf shrimp sautu00e9ed in tomato sauce with garlic and chili, served with grilled bread\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Caprese\",\"menu_item_description\":\"local tomatoes layered with fresh Belgioso mozzarella, fresh basil and a balsamic reduction\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Steamed Mussels\",\"menu_item_description\":\"OR CLAMS P.E.I. mussels or manila clams sautu00e9ed in white wine, fresh herbs, garlic and butter\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Polenta\",\"menu_item_description\":\"polenta rolled and stuffed with organic spinach and provolone, topped with brown butter, crispy sage and tomatoes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Bruschetta\",\"menu_item_description\":\"grilled rustic bread with tomatoes, fresh basil, garlic, extra virgin olive oil and salsa verde\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Garlic Bread\",\"menu_item_description\":\"garlic bread with spicy tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Minestrone Soup\",\"menu_item_description\":\"genovese style vegetable soup with salsa verde\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Tortellini Soup\",\"menu_item_description\":\"savory chicken broth, beef and pork tortellini, braised beef and organic spinach\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Tomato Basil Soup\",\"menu_item_description\":\"creamy tomato and fresh basil soup with house-made croutons\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"salads\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Mixed Greens\",\"menu_item_description\":\"organic mixed greens, tomatoes, garbanzo beans, pine nuts and gorgonzola, with garlic parmesan dressing\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Caesar\",\"menu_item_description\":\"organic romaine hearts with shaved asiago and crunchy garlic croutons\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Spinach\",\"menu_item_description\":\"organic spinach, cranberries, marinated red onions, pine nuts, crispy bacon and shaved asiago, in a balsamic vinaigrette\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chopped Salad\",\"menu_item_description\":\"italian chopped salad with bacon, salami, hard boiled egg, asiago and fresh local vegetables tossed with a garlic parmesan dressing\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Grilled Vegetable Salad\",\"menu_item_description\":\"grilled seasonal vegetables and organic mixed greens topped with a balsamic reduction\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"pasta\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Seafood Pasta\",\"menu_item_description\":\"spaghetti with P.E.I. mussels, manila clams, gulf shrimp, calamari and garlic in a light tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Penne Portobello\",\"menu_item_description\":\"portobello mushrooms, grilled chicken and italian sausage in a roasted garlic cream sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Gemelli\",\"menu_item_description\":\"gemelli pasta with grilled and smoked chicken, sun-dried tomatoes, mushrooms and cream\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Butternut Squash Ravioli\",\"menu_item_description\":\"roasted butternut squash ravioli with parmesan, brown butter, crispy sage and crumbled amaretti\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Shrimp & Asparagus Pasta\",\"menu_item_description\":\"pasta shells with gulf shrimp, asparagus, tomato, cream and shrimp stock\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ravioli\",\"menu_item_description\":\"ravioli filled with ricotta with your choice of gorgonzola cream OR pomodoro sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Pesto Fettuccine\",\"menu_item_description\":\"fettuccine with smoked chicken, cherry tomatoes and chili flakes in a creamy pesto with toasted pine nuts\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Lasagna\",\"menu_item_description\":\"traditional layered lasagna with house-made bolognese, italian sausage, organic spinach, roasted mushrooms and provolone\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Tri Color Capellini\",\"menu_item_description\":\"capellini with baby kale, cherry tomatoes, asparagus and capers with olive oil, garlic, chili flakes and shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sicilian Pasta\",\"menu_item_description\":\"fettuccine, red & yellow bell peppers, roasted eggplant, fresh mozzarella, fresh mint and a spicy tomato sauce, topped with shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"pizzas\",\"group_note\":\"@ select locations\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Chicken Pesto\",\"menu_item_description\":\"grilled chicken, basil pesto, mozzarella, crispy bacon, asiago and sun-dried tomatoes grilled chicken, basil pesto, mozzarella, crispy bacon and sun-dried tomatoes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Supreme\",\"menu_item_description\":\"italian sausage, salami, bacon, mozzarella and roasted crimini mushrooms over tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Margherita\",\"menu_item_description\":\"mozzarella, sliced tomatoes, fresh basil and tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Artichoke & Mushroom\",\"menu_item_description\":\"artichokes, mushrooms, tomatoes, red onions and mozzarella over tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Pepperoni\",\"menu_item_description\":\"pepperoni, mozzarella and zesty tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"entru00e9es\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Chicken Parmesan\",\"menu_item_description\":\"breaded chicken breast topped with tomato sauce and asiago, served with capellini pomodoro\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Marsala\",\"menu_item_description\":\"chicken breast pan-seared with mushrooms and marsala wine sauce, served with sautu00e9ed kale\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Salmon Cannellini\",\"menu_item_description\":\"sustainably raised grilled salmon over tuscan cannellini beans with sautu00e9ed baby kale\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Robertou2019s Chicken\",\"menu_item_description\":\"chicken breast pan-seared and sautu00e9ed with bacon, artichokes, lemon, fresh tomatoes and cream over capellini\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Grilled Chicken\",\"menu_item_description\":\"grilled chicken breast marinated in fresh garlic, thyme, lemon and spices, served with sautu00e9ed farro\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Roasted Veggie Pasta\",\"menu_item_description\":\"seasonal vegetable medley sautu00e9ed with fusilli and cherry tomatoes with your choice of olive oil and garlic OR spicy pomodoro sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"sides\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Asparagus\",\"menu_item_description\":\"grilled asparagus with olive oil and cracked black pepper topped with shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Brussels Sprouts\",\"menu_item_description\":\"brussels sprouts with onions, garlic, brown butter and fresh sage\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Broccoli\",\"menu_item_description\":\"broccoli sautu00e9ed with olive oil, garlic and chili flakes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Farro\",\"menu_item_description\":\"farro sautu00e9ed with olive oil, fresh garlic, organic spinach and roma tomatoes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"dessert & coffee\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Tiramisu\",\"menu_item_description\":\"ladyfingers, espresso and Kahlua layered with chocolate shavings and mascarpone zabaione\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Panna Cotta\",\"menu_item_description\":\"eggless vanilla bean custard with marinated berries\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chocolate Lava Cake\",\"menu_item_description\":\"warm chocolate cake with a melted chocolate center, chocolate sauce and fresh whipped cream\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Cannoli\",\"menu_item_description\":\"crispy shells with ricotta cream and mini chocolate chips, topped with chocolate sauce and powdered sugar\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"New York Style Cheesecake\",\"menu_item_description\":\"cheesecake, graham cracker crust with marinated berries\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sorbet / Ice Cream\",\"menu_item_description\":\"artisan mixed berry sorbet or vanilla ice cream\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]}]},{\"menu_name\":\"Lunch Menu\",\"menu_description\":\"All Lunch Menu items include choice of: Zuppa di Minestrone, Caesar or Insalata Mista\",\"menu_note\":\"\",\"currency_symbol\":\"USD\",\"language\":\"en\",\"menu_duration_name\":\"lunch\",\"menu_duration_time_start\":\"\",\"menu_duration_time_end\":\"\",\"menu_groups\":[{\"group_name\":\"Lunch Menu\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"HEALTHY FISH\",\"menu_item_description\":\"whole grain fusilli sautu00e9ed with select pieces of sustainably raised salmon, fresh asparagus, tomato sauce and fresh basil\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"RAVIOLI\",\"menu_item_description\":\"ravioli filled with ricotta with your choice of gorgonzola cream OR pomodoro sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"RIGATONI\",\"menu_item_description\":\"rigatoni baked with a creamy bolognese sauce, provolone and parmesan cheese\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN WITH BROCCOLI\",\"menu_item_description\":\"grilled chicken breast served over orecchiette pasta and sautu00e9ed broccoli tossed in a lemon garlic cream sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CAPELLINI\",\"menu_item_description\":\"choice of: pomodoro sauce with garlic, fresh basil and olive oil OR basil pesto with shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"HEALTHY CHICKEN\",\"menu_item_description\":\"whole grain fusilli sautu00e9ed with sliced grilled chicken, fresh zucchini, tomato sauce and fresh basil\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"BOLOGNESE\",\"menu_item_description\":\"rigatoni tossed with rich beef, pork and porcini mushroom sauce, topped with shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"TRI COLOR CAPELLINI\",\"menu_item_description\":\"capellini with baby kale, cherry tomatoes, asparagus and capers with olive oil, garlic, chili flakes and shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"MOZZARELLA PANINI\",\"menu_item_description\":\"mozzarella, vine tomatoes, sun-dried tomatoes and basil pesto\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN PANINI\",\"menu_item_description\":\"grilled chicken, provolone, organic arugula drizzled with balsamic vinegar, sun-dried tomatoes and basil pesto\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"ARTICHOKE & MUSHROOM PIZZA\",\"menu_item_description\":\"artichokes, mushrooms, tomatoes, red onions and mozzarella over tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN PESTO PIZZA\",\"menu_item_description\":\"grilled chicken, basil pesto, mozzarella, crispy bacon, asiago and sun-dried tomatoes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"MARGHERITA PIZZA\",\"menu_item_description\":\"mozzarella, sliced tomatoes, fresh basil and tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"PEPPERONI PIZZA\",\"menu_item_description\":\"pepperoni, mozzarella and zesty tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]}]}],\"id\":\"Umsxc2dNS1BCdUkrdW5QMDdaRlBSSmpLNm5zckNLNlQ3cUxWalQrZWN2WlNFcFhRNEpOb0IwZDlRV2FTaEc0Zg==\"}}}";
	        menuResults.add(menuInfo);
		}
				
    	// Get the menu items for each restaurant
    	ArrayList<ArrayList<String[]>> menuItems = new ArrayList<ArrayList<String[]>>();
    	for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
    		menuItems.add(backend.GetMenuItems(menuResults.get(i)));
    	}
		
		// Count the matches we get for each matching algorithm
    	// Also categorize as true pos/false pos/false neg in Matches
    	// True pos - match with a menu item and menu item is in GROUND_TRUTH
    	// False pos - match with a menu item and menu item is not in GROUND_TRUTH
    	// False neg - didn't match with a menu item but menu item is in GROUND_TRUTH
    	
    	ArrayList<ArrayList<EntityClass>> restaurant;
    	ArrayList<EntityClass> review;
    	ArrayList<String[]> restaurantItems;
    	String menuItem;
    	String[][][] restaurantGroundTruth;
    	String[][] reviewGroundTruth;
    	String restaurantItem;
    	String[] tokens1;
    	String[] tokens2;
    	boolean shouldMatch = false;
    	// Go through each restaurant
    	for (int i = 0; i < GOAL_MATCHES.size(); i ++)
    	{
    		restaurant = GOAL_MATCHES.get(i);
    		restaurantItems = menuItems.get(i);
    		restaurantGroundTruth = GROUND_TRUTH[i];
    		// For each restaurant, go through each review
    		for (int j = 0; j < restaurant.size(); j ++)
    		{
    			// For each review, go through each entity
    			review = restaurant.get(j);
    			reviewGroundTruth = restaurantGroundTruth[j];
    			for (int l = 0; l < restaurantItems.size(); l ++)
    			{
    				// Go through each menu item
    				restaurantItem = restaurantItems.get(l)[0];
    				for (int k = 0; k < review.size(); k ++)
    				{
    					// For each entity, check if you get a match with any menu item
        				menuItem = review.get(k).GetWord();
    					
    					shouldMatch = CorrectMatch(reviewGroundTruth, menuItem, restaurantItem);
    					
    					// Test each matching algorithm
    					if (CaseInsensitiveMatch(menuItem, restaurantItem))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[0][0] = Matches[0][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							System.out.println("CaseInsensitive False positive: " + menuItem + "  " + restaurantItem);
    							Matches[0][1] = Matches[0][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[0][2] = Matches[0][2].add(BigDecimal.ONE);
    						}
    					}
    					if (LevensteinEditDistance(menuItem, restaurantItem, 4))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[1][0] = Matches[1][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							System.out.println("Levenstein False positive: " + menuItem + "  " + restaurantItem);
    							Matches[1][1] = Matches[1][1].add(BigDecimal.ONE);
    						}
    					}	
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[1][2] = Matches[1][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaroSimilarity(menuItem, restaurantItem, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[2][0] = Matches[2][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							System.out.println("Jaro False positive: " + menuItem + "  " + restaurantItem);
    							Matches[2][1] = Matches[2][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[2][2] = Matches[2][2].add(BigDecimal.ONE);
    						}
    					}
    					tokens1 = Tokenize(menuItem, 2, false);
    					tokens2 = Tokenize(restaurantItem, 2, false);
    					if (JaccardSimilarity(tokens1, tokens2, 0.80))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[3][0] = Matches[3][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							System.out.println("Jaccard2 False positive: " + menuItem + "  " + restaurantItem);
    							Matches[3][1] = Matches[3][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[3][2] = Matches[3][2].add(BigDecimal.ONE);
    						}
    					}
    					tokens1 = Tokenize(menuItem, 3, false);
    					tokens2 = Tokenize(restaurantItem, 3, false);
    					if (JaccardSimilarity(tokens1, tokens2, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[4][0] = Matches[4][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							System.out.println("Jaccard3 False positive: " + menuItem + "  " + restaurantItem);
    							Matches[4][1] = Matches[4][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[4][2] = Matches[4][2].add(BigDecimal.ONE);
    						}
    					}
    					tokens1 = Tokenize(menuItem, 1, true);
    					tokens2 = Tokenize(restaurantItem, 1, true);
    					if (JaccardSimilarity(tokens1, tokens2, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[5][0] = Matches[5][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							System.out.println("JaccardW False positive: " + menuItem + "  " + restaurantItem);
    							Matches[5][1] = Matches[5][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[5][2] = Matches[5][2].add(BigDecimal.ONE);
    						}
    					}
    				}
    			}
    		}
    	}
    	
//    	for (int i = 0; i < MATCHING_ALGS.length; i ++)
//    	{
//    		System.out.println(Matches[i][0]);
//        	System.out.println(Matches[i][1]);
//        	System.out.println(Matches[i][2]);
//        	System.out.println("\n\n\n");
//    	}
    	
    	
		
		// Calculate the precision/recall/F1 score for each and output as table
    	// precision = true pos / (true pos + false pos)
    	// recall = true pos / (true pos + false neg)
    	// F1 = 2 * precision * recall / (precision + recall)
    	BigDecimal precision;
    	BigDecimal recall;
    	BigDecimal f1;
    	BigDecimal two = new BigDecimal(2);
    	for (int i = 0; i < MATCHING_ALGS.length; i ++)
    	{
    		if (Matches[i][0] == BigDecimal.ZERO)
    		{
    			precision = BigDecimal.ZERO;
    			recall = BigDecimal.ZERO;
    			f1 = BigDecimal.ZERO;
    		}
    		else
    		{
    			precision = Matches[i][0].divide(Matches[i][0].add(Matches[i][1]), 5, RoundingMode.HALF_UP);
    			recall = Matches[i][0].divide(Matches[i][0].add(Matches[i][2]), 5, RoundingMode.HALF_UP);
    			f1 = precision.multiply(two).multiply(recall).divide(precision.add(recall), 5, RoundingMode.HALF_UP);
    		}
    		System.out.println(MATCHING_ALGS[i]);
    		System.out.println("Precision: " + precision);
    		System.out.println("Recall: " + recall);
    		System.out.println("F1 Score: " + f1);
    		System.out.println("\n\n\n");
    	}
		
		
    	backend.CleanUp(scanners);
    	
    }
    
    public static boolean CaseInsensitiveMatch(String str1, String str2)
    {
    	// Check if the strings are exactly equal
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
    	for (int i = 0; i <= len1; i ++)
    	{
    		matrix[i][0] = i;
    	}
    	for (int i = 0; i <= len2; i ++)
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
    
    public static boolean JaroSimilarity(String str1, String str2, double minAmt)
    {
    	str1 = str1.toLowerCase();
    	str2 = str2.toLowerCase();
    	
    	int len1 = str1.length();
    	int len2 = str2.length();
    	
    	int matchDist = (int) Math.floor(Math.max(len1, len2) / 2) - 1;
    	double numMatches = 0;
    	double numHalfTranspositions = 0;
    	
    	boolean[] str1Matches = new boolean[len1];
    	boolean[] str2Matches = new boolean[len2];
    	
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
    				if ((str1.charAt(i) == str2.charAt(i + j)) && (str2Matches[i + j] == false))
        			{
        				str1Matches[i] = true;
        				str2Matches[i + j] = true;
        				numMatches ++;
        				break;
        			}
    			}
    			if ((i - j > 0) && (i - j < len2))
    			{
    				// If we have a matching character moving backwards
    				if ((str1.charAt(i) == str2.charAt(i - j)) && (str2Matches[i - j] == false))
        			{
        				str1Matches[i] = true;
        				str2Matches[i - j] = true;
        				numMatches ++;
        				break;
        			}
    			}
    		}
    	}
    	
    	String str1Chars = "";
    	String str2Chars = "";
    	
    	for (int i = 0; i < len1; i ++)
    	{
    		if (str1Matches[i])
    		{
    			str1Chars += str1.charAt(i);
    		}
    	}
    	for (int i = 0; i < len2; i ++)
    	{
    		if (str2Matches[i])
    		{
    			str2Chars += str2.charAt(i);
    		}
    	}
    	
    	int index;
    	// Get the number of transpositions between the matching letters
    	for (int i = 0; i < str1Chars.length(); i ++)
    	{
    		if (str1Chars.charAt(0) == str2Chars.charAt(0))
    		{
    			str2Chars = str2Chars.substring(1);
    		}
    		else
    		{
    			index = str2Chars.indexOf(str1Chars.charAt(0));
    			if (index != -1)
    			{
    				str2Chars = str2Chars.substring(0, index) + str2Chars.substring(index + 1);
        			numHalfTranspositions ++;
    			}
    		}
    		str1Chars = str1Chars.substring(1);
    	}
    	
    	for (int i = 0; i < len1; i ++)
    	{
    		if (str1Matches[i])
    		{
    			str1Chars += str1.charAt(i);
    		}
    	}
    	for (int i = 0; i < len2; i ++)
    	{
    		if (str2Matches[i])
    		{
    			str2Chars += str2.charAt(i);
    		}
    	}
    	
    	if (str2Chars.equals("") || str2Chars.equals(""))
    	{
    		numHalfTranspositions = 0;
    	}
    	else
    	{
	    	// Get the number of transpositions between the matching letters
	    	for (int i = 0; i < str2Chars.length(); i ++)
	    	{
	    		if (str2Chars.charAt(0) == str1Chars.charAt(0))
	    		{
	    			str1Chars = str1Chars.substring(1);
	    		}
	    		else
	    		{
	    			index = str1Chars.indexOf(str2Chars.charAt(0));
	    			if (index != -1)
	    			{
	    				str1Chars = str1Chars.substring(0, index) + str1Chars.substring(index + 1);
	        			numHalfTranspositions ++;
	    			}
	    		}
	    		str2Chars = str2Chars.substring(1);
	    	}
    	}
    	
    	double numTranspositions = Math.floor(numHalfTranspositions / 2);
    	
    	// Calculate the total Jaro score
    	double metric = (numMatches / len1 + numMatches / len2 + (numMatches - numTranspositions) / numMatches) / 3.0;
    	     	
    	// If the metric is at least as high as given amount, return True
    	if (metric >= minAmt)
    	{
    		return true;
    	}
    	// False otherwise 
    	return false;
    }
    
    public static boolean JaccardSimilarity(String[] str1, String[] str2, double minAmt)
    {
    	// Turn the tokens into sets
    	HashSet<String> str1Tokens = new HashSet<String>(Arrays.asList(str1));
    	HashSet<String> str2Tokens = new HashSet<String>(Arrays.asList(str2));
    	
    	// Get the intersection of the tokens
    	HashSet<String> intersection = new HashSet<String>(str1Tokens);
    	intersection.retainAll(str2Tokens);
    	
    	// Get the union of the tokens
    	HashSet<String> union = new HashSet<String>(str1Tokens);
    	union.addAll(str2Tokens);
    	
    	// Calculate the similarity
    	double similarity = ((double) intersection.size()) / ((double) union.size());
    	
    	// Return true if similarity is greater than the min amount acceptable
    	if (similarity >= minAmt)
    	{
    		return true;
    	}
    	// False otherwise
    	return false;
    }
    
    public static String[] Tokenize(String str, int ngramLen, boolean asWords)
    {
    	// Remove punctuation
    	str = RemovePunct(str);
    	str = str.toLowerCase();
    	
    	// If tokenizing as words, then split on spaces
    	if (asWords)
    	{
    		return str.split(" ");
    	}
    	
    	ArrayList<String> tokens = new ArrayList<String>();
    	String token = "";
    	// Iterate through the string to split it up into ngrams
    	for (int i = 0; i < str.length() - ngramLen + 1; i ++)
    	{
    		token = str.substring(i, i + ngramLen);
    		tokens.add(token);
    	}
    	
    	// Convert to a String[]
    	String[] tokensArr = tokens.toArray(new String[tokens.size()]);
    	return tokensArr;
    }
    
    public static String RemovePunct(String str)
    {
    	// Remove all the punctuation from the strings
    	str.replaceAll(".", "");
    	str.replaceAll(",", "");
    	str.replaceAll("'", "");
    	str.replaceAll(":", "");
    	str.replaceAll("!", "");
    	str.replaceAll("\\?", "");
    	str.replaceAll("@", "");
    	str.replaceAll("#", "");
    	str.replaceAll("$", "");
    	str.replaceAll("&", "");
    	str.replaceAll("\\(", "");
    	str.replaceAll("\\)", "");
    	str.replaceAll(";", "");
    	str.replaceAll("\"", "");
    	
    	return str;
    }
    
    public static boolean CorrectMatch(String[][] reviewGroundTruth, String menuItem, String restaurantItem)
    {
    	String[] entry = new String[2];
    	for (int i = 0; i < reviewGroundTruth.length; i ++)
    	{
    		// If the first item in entry is the same as that parsed by Google and the second is
    		// on the OpenMenu menu, then we consider those two as supposed to be matches
    		entry = reviewGroundTruth[i];
    		if (entry.length < 1)
    		{
    			continue;
    		}
    		if ((entry[0].equalsIgnoreCase(menuItem)) && (entry[1].equalsIgnoreCase(restaurantItem)))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
}






