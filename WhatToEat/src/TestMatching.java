
import java.io.*;
import java.net.*;
import java.util.Scanner;

import org.json.simple.JSONObject; 
import org.json.simple.parser.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

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
    static int[][] Matches = {
    		{ 0, 0, 0 },
    		{ 0, 0, 0 },
    		{ 0, 0, 0 },
    		{ 0, 0, 0 },
    		{ 0, 0, 0 },
    		{ 0, 0, 0 }
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
    
    // First item is the food item from the review, the second is what we get from google
    static final String[][] PASTA_POMODORO_MATCHES = {
    	{
    		"pasta", "fettuccine alfredo with chicken"
    	}, 
    	{
    		"tortellini soup", "Cesar salad"
    	}, 
    	{
    		
    	},
    	{
    		
    	},
    	{
    		"Linguine with Fresh Dungenous crab & fresh veggies"
    	},
    	{
    		"Tortellini"
    	},
    	{
    		"Risotto w/ clams", "Ravioli di Zucca"
    	},
    	{
    		
    	},
    	{
    		"Ravioli di magro with pomodoro sauce", "fettuccine alfredo", "bellinis"
    	},
    	{
    		"Ravioli di Zucca", "Healthy Fish", "Penne Salsiccia"
    	},
    	{
    		"Spinach salad", "raviolis"
    	},
    	{
    		"Spag' and meatballs"
    	},
    	{
    		"shrimp with garlic and chili", "ravioli with brown butter and sage", "vongole with linguine"
    	},
    	{
    		"Polenta Farcita", "Gamberi", "Tortellini alla Panna"
    	},
    	{
    		"gemeli", "chicken f alfredo"
    	},
    	{
    		
    	}
    };
    
    /*
    static final String[][] PASTA_POMODORO_MATCHES = {
    		{
    			"PAD THAI", "SEAFOOD KAREE"
    		},
    		{
    			"DRUNKEN NOODLE"
    		}
    };
    */
    
    static String[][][] GROUND_TRUTH = { PASTA_POMODORO_MATCHES };
    
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
        File reviewFile = new File(REVIEWS_PATH);
        Scanner reviewScanner = new Scanner(reviewFile);
        scanners.add(reviewScanner);
        
        RestaurantClass business = new RestaurantClass();
    	ArrayList<String> reviews = new ArrayList<String>();
    	ArrayList<String> googleReplies = new ArrayList<String>();

    	// Get all of the reviews for each business
		for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
			
			/*
			
			// Get the business ID, then the reviews, and send them to Google
    		business = backend.FindBusinessId(TEST_RESTAURANTS[i], businessScanner);
    		businessId = business.GetId();
    		reviews = backend.GetReviews(businessId, reviewScanner);
    		reviews = backend.EliminateQuotes(reviews);
    		
    		googleReplies = backend.QueryGoogleApi(reviews);
    		
    		
    		*/
			
			
			
			
			
			List<String> test = Arrays.asList("{\"entities\":[{\"name\":\"experience\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.28340155,\"mentions\":[{\"text\":{\"content\":\"experience\",\"beginOffset\":44},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"times\",\"type\":\"EVENT\",\"metadata\":{},\"salience\":0.28340155,\"mentions\":[{\"text\":{\"content\":\"times\",\"beginOffset\":60},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"food\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.079445906,\"mentions\":[{\"text\":{\"content\":\"food\",\"beginOffset\":122},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"reviewer\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.054920267,\"mentions\":[{\"text\":{\"content\":\"reviewer\",\"beginOffset\":75},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"service\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.04467418,\"mentions\":[{\"text\":{\"content\":\"service\",\"beginOffset\":133},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"food\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.042315524,\"mentions\":[{\"text\":{\"content\":\"food\",\"beginOffset\":159},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.3,\"score\":0.3}}],\"sentiment\":{\"magnitude\":0.3,\"score\":0.3}},{\"name\":\"pasta\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.040823992,\"mentions\":[{\"text\":{\"content\":\"pasta\",\"beginOffset\":199},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":-0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":-0.9}},{\"name\":\"fettuccine alfredo\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.03429935,\"mentions\":[{\"text\":{\"content\":\"fettuccine alfredo\",\"beginOffset\":270},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"chicken\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.03429935,\"mentions\":[{\"text\":{\"content\":\"chicken\",\"beginOffset\":294},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Italian\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.03264804,\"mentions\":[{\"text\":{\"content\":\"Italian\",\"beginOffset\":114},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"water\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.0258281,\"mentions\":[{\"text\":{\"content\":\"water\",\"beginOffset\":365},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"star\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.0258281,\"mentions\":[{\"text\":{\"content\":\"star\",\"beginOffset\":384},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"visit\",\"type\":\"EVENT\",\"metadata\":{},\"salience\":0.018114071,\"mentions\":[{\"text\":{\"content\":\"visit\",\"beginOffset\":325},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"language\":\"en\"}", "{\"entities\":[{\"name\":\"vibe\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.16891496,\"mentions\":[{\"text\":{\"content\":\"vibe\",\"beginOffset\":94},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}}],\"sentiment\":{\"magnitude\":0.1,\"score\":0}},{\"name\":\"average\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.13899317,\"mentions\":[{\"text\":{\"content\":\"average\",\"beginOffset\":33},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"restaurants\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.13899317,\"mentions\":[{\"text\":{\"content\":\"restaurants\",\"beginOffset\":72},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Italian\",\"type\":\"LOCATION\",\"metadata\":{\"mid\":\"/m/09y2k2\",\"wikipedia_url\":\"https://en.wikipedia.org/wiki/Italian_cuisine\"},\"salience\":0.10448828,\"mentions\":[{\"text\":{\"content\":\"Italian\",\"beginOffset\":64},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"text\":{\"content\":\"Italian\",\"beginOffset\":133},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"text\":{\"content\":\"Italian\",\"beginOffset\":645},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Pasta Pomodoro\",\"type\":\"CONSUMER_GOOD\",\"metadata\":{\"mid\":\"/m/04qsz7\",\"wikipedia_url\":\"https://en.wikipedia.org/wiki/Pasta_al_pomodoro\"},\"salience\":0.09982995,\"mentions\":[{\"text\":{\"content\":\"Pasta Pomodoro\",\"beginOffset\":0},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"prices\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.05160996,\"mentions\":[{\"text\":{\"content\":\"prices\",\"beginOffset\":394},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":1.6,\"score\":0.4}},{\"name\":\"bistro\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.025960812,\"mentions\":[{\"text\":{\"content\":\"bistro\",\"beginOffset\":141},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}}],\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}},{\"name\":\"soup\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.024012858,\"mentions\":[{\"text\":{\"content\":\"soup\",\"beginOffset\":297},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"salad\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.022587096,\"mentions\":[{\"text\":{\"content\":\"salad\",\"beginOffset\":334},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"Jazz musicians\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.020259658,\"mentions\":[{\"text\":{\"content\":\"Jazz musicians\",\"beginOffset\":577},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"tortellini soup\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.019863768,\"mentions\":[{\"text\":{\"content\":\"tortellini soup\",\"beginOffset\":238},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"salad\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.019863768,\"mentions\":[{\"text\":{\"content\":\"salad\",\"beginOffset\":264},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Cesar\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.017823191,\"mentions\":[{\"text\":{\"content\":\"Cesar\",\"beginOffset\":258},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"text\":{\"content\":\"Cesar\",\"beginOffset\":328},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0.2,\"score\":0.2}}],\"sentiment\":{\"magnitude\":0.2,\"score\":0.1}},{\"name\":\"shavings\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.017560944,\"mentions\":[{\"text\":{\"content\":\"shavings\",\"beginOffset\":361},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}}],\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}},{\"name\":\"times\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.017455934,\"mentions\":[{\"text\":{\"content\":\"times\",\"beginOffset\":196},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Pecorino cheese\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.01611636,\"mentions\":[{\"text\":{\"content\":\"Pecorino cheese\",\"beginOffset\":373},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"amount\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.015884323,\"mentions\":[{\"text\":{\"content\":\"amount\",\"beginOffset\":447},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"New York City\",\"type\":\"LOCATION\",\"metadata\":{\"mid\":\"/m/02_286\",\"wikipedia_url\":\"https://en.wikipedia.org/wiki/New_York_City\"},\"salience\":0.015378489,\"mentions\":[{\"text\":{\"content\":\"New York City\",\"beginOffset\":173},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"book\",\"type\":\"WORK_OF_ART\",\"metadata\":{},\"salience\":0.012083021,\"mentions\":[{\"text\":{\"content\":\"book\",\"beginOffset\":611},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"way\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.010818679,\"mentions\":[{\"text\":{\"content\":\"way\",\"beginOffset\":669},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"jazz\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.009631952,\"mentions\":[{\"text\":{\"content\":\"jazz\",\"beginOffset\":626},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}}],\"sentiment\":{\"magnitude\":0.1,\"score\":0.1}},{\"name\":\"food\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.008461598,\"mentions\":[{\"text\":{\"content\":\"food\",\"beginOffset\":653},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"Both\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.007830428,\"mentions\":[{\"text\":{\"content\":\"Both\",\"beginOffset\":271},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}},{\"name\":\"choices\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.005321883,\"mentions\":[{\"text\":{\"content\":\"choices\",\"beginOffset\":457},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}}],\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}},{\"name\":\"lunch menu\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.005321883,\"mentions\":[{\"text\":{\"content\":\"lunch menu\",\"beginOffset\":474},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"flier\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.0049338653,\"mentions\":[{\"text\":{\"content\":\"flier\",\"beginOffset\":517},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"language\":\"en\"}", "{\"entities\":[{\"name\":\"everything\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.21558267,\"mentions\":[{\"text\":{\"content\":\"everything\",\"beginOffset\":18},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"fact\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.21558267,\"mentions\":[{\"text\":{\"content\":\"fact\",\"beginOffset\":74},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Calamari F.\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.11323813,\"mentions\":[{\"text\":{\"content\":\"Calamari F.\",\"beginOffset\":29},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"text\":{\"content\":\"Calamari F.\",\"beginOffset\":460},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"kids\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.084122136,\"mentions\":[{\"text\":{\"content\":\"kids\",\"beginOffset\":92},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"service\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.06495431,\"mentions\":[{\"text\":{\"content\":\"service\",\"beginOffset\":374},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.7,\"score\":0.7}}],\"sentiment\":{\"magnitude\":0.7,\"score\":0.3}},{\"name\":\"OMG\",\"type\":\"ORGANIZATION\",\"metadata\":{},\"salience\":0.050097227,\"mentions\":[{\"text\":{\"content\":\"OMG\",\"beginOffset\":84},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"LAGUNA BEACH\",\"type\":\"LOCATION\",\"metadata\":{\"mid\":\"/m/0r2gj\",\"wikipedia_url\":\"https://en.wikipedia.org/wiki/Laguna_Beach,_California\"},\"salience\":0.050097227,\"mentions\":[{\"text\":{\"content\":\"LAGUNA BEACH\",\"beginOffset\":100},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"Pasta Pomodoro\",\"type\":\"ORGANIZATION\",\"metadata\":{},\"salience\":0.050097227,\"mentions\":[{\"text\":{\"content\":\"Pasta Pomodoro\",\"beginOffset\":120},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"star\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.037015732,\"mentions\":[{\"text\":{\"content\":\"star\",\"beginOffset\":349},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"PV mall\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.027896617,\"mentions\":[{\"text\":{\"content\":\"PV mall\",\"beginOffset\":230},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"choices\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.025605854,\"mentions\":[{\"text\":{\"content\":\"choices\",\"beginOffset\":304},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"area\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.025605854,\"mentions\":[{\"text\":{\"content\":\"area\",\"beginOffset\":323},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"service\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.021795997,\"mentions\":[{\"text\":{\"content\":\"service\",\"beginOffset\":491},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"stars\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.011624987,\"mentions\":[{\"text\":{\"content\":\"stars\",\"beginOffset\":598},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"difference\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.0066833715,\"mentions\":[{\"text\":{\"content\":\"difference\",\"beginOffset\":441},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"language\":\"en\"}", "{\"entities\":[{\"name\":\"food\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.63773847,\"mentions\":[{\"text\":{\"content\":\"food\",\"beginOffset\":32},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":0.9}}],\"sentiment\":{\"magnitude\":1.9,\"score\":0.9}},{\"name\":\"Italian\",\"type\":\"LOCATION\",\"metadata\":{},\"salience\":0.115508616,\"mentions\":[{\"text\":{\"content\":\"Italian\",\"beginOffset\":24},\"type\":\"PROPER\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"wine\",\"type\":\"CONSUMER_GOOD\",\"metadata\":{},\"salience\":0.10651932,\"mentions\":[{\"text\":{\"content\":\"wine\",\"beginOffset\":42},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}}],\"sentiment\":{\"magnitude\":0.8,\"score\":0.8}},{\"name\":\"service\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.041607823,\"mentions\":[{\"text\":{\"content\":\"service\",\"beginOffset\":63},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.9,\"score\":-0.9}}],\"sentiment\":{\"magnitude\":0.9,\"score\":-0.9}},{\"name\":\"mom\",\"type\":\"PERSON\",\"metadata\":{},\"salience\":0.025314653,\"mentions\":[{\"text\":{\"content\":\"mom\",\"beginOffset\":126},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"PV\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.023247197,\"mentions\":[{\"text\":{\"content\":\"PV\",\"beginOffset\":152},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"pesto oil\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.020910718,\"mentions\":[{\"text\":{\"content\":\"pesto oil\",\"beginOffset\":230},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.2,\"score\":0.2}}],\"sentiment\":{\"magnitude\":0.2,\"score\":0.2}},{\"name\":\"bread\",\"type\":\"CONSUMER_GOOD\",\"metadata\":{},\"salience\":0.012213877,\"mentions\":[{\"text\":{\"content\":\"bread\",\"beginOffset\":259},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0.4,\"score\":0.4}}],\"sentiment\":{\"magnitude\":0.4,\"score\":0.4}},{\"name\":\"lot\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.009246689,\"mentions\":[{\"text\":{\"content\":\"lot\",\"beginOffset\":184},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}},{\"name\":\"place\",\"type\":\"OTHER\",\"metadata\":{},\"salience\":0.007692615,\"mentions\":[{\"text\":{\"content\":\"place\",\"beginOffset\":211},\"type\":\"COMMON\",\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"sentiment\":{\"magnitude\":0,\"score\":0}}],\"language\":\"en\"}");
    		googleReplies = new ArrayList<String>(test);
    		
    		List<String> test2 = Arrays.asList("butt", "sex", "is", "fun");
    		reviews = new ArrayList<String>(test2);
    		
    		
    		
    		
    		
    		
    		ArrayList<ArrayList<EntityClass>> perRestaurant = new ArrayList<ArrayList<EntityClass>>();
    		perRestaurant.clear();
    		
    		// Get the entities from each review and add to that restaurant's entity list
    		for (int j = 0; j < googleReplies.size(); j ++)
    		{
    			ArrayList<EntityClass> perReview = new ArrayList<EntityClass>();
    			perReview = backend.GetEntities(googleReplies.get(j), reviews.get(j), business);
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
    	String menuResult = "{\"response\":{\"api\":{\"status\":200,\"api_version\":\"2.1\",\"format\":\"json\",\"api_key\":\"51a5b6d4-dcb3-11e8-8d62-525400552a35\"},\"result\":{\"restaurant_info\":{\"restaurant_name\":\"5ThaiBistro\",\"brief_description\":\"At 5 Thai Bistro, we offer all your favorite Thai dishes under one roof. Our cuisine is reasonably priced and made from only the freshest ingredients to ensure the best flavor with every bite\",\"full_description\":\"5 Thai Bistro at a Glance:\\r\\n  \\u2022 Open for Lunch and Dinner\\r\\n  \\u2022 Casual and Business-Casual Attire Welcome\\r\\n  \\u2022 Each Table Comfortably Seats up to Four People\\r\\n  \\u2022 We Serve Beer, Wine, Appetizers, and Entrees\\r\\n  \\u2022 We Offer Wheat-Free Dishes\",\"location_id\":\"40 Pleasant Street Portsm\",\"mobile\":\"0\",\"address_1\":\"40 Pleasant st \",\"address_2\":\"\",\"city_town\":\"Portsmouth\",\"state_province\":\"NH\",\"postal_code\":\"\",\"country\":\"US\",\"phone\":\"(603) 373-8871\",\"fax\":\"\",\"longitude\":\"-70.757203\",\"latitude\":\"43.076079\",\"business_type\":\"independent\",\"utc_offset\":null,\"website_url\":\"http:\\/\\/www.5thaibistro.com\"},\"environment_info\":{\"cuisine_type_primary\":\"Thai\",\"cuisine_type_secondary\":\"Asian\",\"smoking_allowed\":\"0\",\"takeout_available\":\"1\",\"seating_qty\":\"50\",\"max_group_size\":\"20\",\"pets_allowed\":\"1\",\"wheelchair_accessible\":\"1\",\"age_level_preference\":\"Ever\",\"dress_code\":\"none\",\"delivery_available\":\"1\",\"delivery_radius\":\"50.00\",\"delivery_fee\":\"4.00\",\"catering_available\":\"1\",\"reservations\":\"suggested\",\"alcohol_type\":\"beer and wine\",\"music_type\":\"pre-recorded\"},\"operating_days\":[{\"day_of_week\":\"1\",\"open_time\":\"11:00:00\",\"close_time\":\"09:30:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:30AM\",\"day\":\"Monday\",\"day_short\":\"Mon\"},{\"day_of_week\":\"2\",\"open_time\":\"11:00:00\",\"close_time\":\"09:30:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:30AM\",\"day\":\"Tuesday\",\"day_short\":\"Tue\"},{\"day_of_week\":\"3\",\"open_time\":\"11:00:00\",\"close_time\":\"09:30:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:30AM\",\"day\":\"Wednesday\",\"day_short\":\"Wed\"},{\"day_of_week\":\"4\",\"open_time\":\"11:00:00\",\"close_time\":\"09:30:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:30AM\",\"day\":\"Thursday\",\"day_short\":\"Thu\"},{\"day_of_week\":\"5\",\"open_time\":\"11:00:00\",\"close_time\":\"10:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"10:00AM\",\"day\":\"Friday\",\"day_short\":\"Fri\"},{\"day_of_week\":\"6\",\"open_time\":\"11:00:00\",\"close_time\":\"10:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"10:00AM\",\"day\":\"Saturday\",\"day_short\":\"Sat\"},{\"day_of_week\":\"7\",\"open_time\":\"11:00:00\",\"close_time\":\"09:30:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:30AM\",\"day\":\"Sunday\",\"day_short\":\"Sun\"}],\"operating_days_printable\":{\"1\":{\"day_name\":\"Monday\",\"display\":\"11:00AM-9:30AM\"},\"2\":{\"day_name\":\"Tuesday\",\"display\":\"11:00AM-9:30AM\"},\"3\":{\"day_name\":\"Wednesday\",\"display\":\"11:00AM-9:30AM\"},\"4\":{\"day_name\":\"Thursday\",\"display\":\"11:00AM-9:30AM\"},\"5\":{\"day_name\":\"Friday\",\"display\":\"11:00AM-10:00AM\"},\"6\":{\"day_name\":\"Saturday\",\"display\":\"11:00AM-10:00AM\"},\"7\":{\"day_name\":\"Sunday\",\"display\":\"11:00AM-9:30AM\"}},\"logo_urls\":[],\"seating_locations\":[{\"seating_location\":\"indoor\"}],\"accepted_currencies\":[{\"accepted_currency\":\"USD\"}],\"parking\":{\"street_free\":\"1\",\"street_metered\":\"1\",\"private_lot\":null,\"garage\":null,\"valet\":null},\"settings\":{\"social\":false},\"menus\":[{\"menu_name\":\" Menu\",\"menu_description\":\"\",\"menu_note\":\"\",\"currency_symbol\":\"USD\",\"language\":\"\",\"menu_duration_name\":\"lunch-dinner\",\"menu_duration_time_start\":\"\",\"menu_duration_time_end\":\"\",\"menu_groups\":[{\"group_name\":\"Appetizers\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\".FRESH ROLL ( WHEAT FREE)\",\"menu_item_description\":\"Fresh lettuce, carrots, cucumber, mint leaves, rolled in rice paper. Served with Thai sweet sauce topped with ground peanuts.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Your choice\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Vegetables only\",\"menu_item_option_additional_cost\":\"6.95\",\"selected\":null},{\"menu_item_option_name\":\"Shrimp \",\"menu_item_option_additional_cost\":\"7.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Crispy SpringRolls\",\"menu_item_description\":\"Thai rolls which wrapped with bean thread, carrots, cabbage, and celery. Served with Thai sweet and sour sauce. 5.95\",\"menu_item_price\":\"5.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Tofu \",\"menu_item_description\":\"Fresh tofu and flour mixed together and deep fried until crispy outside. Served with Thai sweet chili sauce and tamarind sauce\",\"menu_item_price\":\"6.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN DUMPLING \",\"menu_item_description\":\"Steamed dumpling with special filling (ground chicken breastssauteed withherbs, ground roasted peanuts and turnips).Served with soy sauce.\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN SATAY  (WHEAT FREE)\",\"menu_item_description\":\"Chicken tender marinated in coconut milk and Thai herbs. Served with creamy peanut sauce and cumber sauce.\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN WINGS  (YOUR CHOICE: ORIGINAL OR SPICY)\",\"menu_item_description\":\"Golden crispy wings served with a sweet and sour sauce\",\"menu_item_price\":\"6.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"KRA-TONG THONG  CRISPY CUPS\",\"menu_item_description\":\"Crispy pastry cups filled with sauteed chicken, carrot, onion, green peas and corns with a slight touch of yellow curry powder and garlic, served with sweet and sour cucumber sauce\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"THAI CRAB CAKE\",\"menu_item_description\":\"Crisp-on-the-outside and moist-on-the-inside crab cakes served with spicy mayo\",\"menu_item_price\":\"8.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"KOONG KRA BEUNG \",\"menu_item_description\":\"Seasoned Shrimp with Thai herbs, deep fried in egg roll wrap, served with sweet and sour sauce.\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Soup\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"TOM YUM (THAI HOT & SOUR SOUP) Wheat Free\",\"menu_item_description\":\"Aside from being one of the most popular soup in Thailand, Tom Yum Soup has many health benefits, due its potent combination of herbs and spices. (Flavored with Lemon grass, Galanga, Lime leaves, , mushroom, tomato and topped with cilantro) Wheat Free\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Your Choice\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Fresh Tofu, vegetables, Or Chicken \",\"menu_item_option_additional_cost\":\"4.95\",\"selected\":null},{\"menu_item_option_name\":\"Shrimp \",\"menu_item_option_additional_cost\":\"5.95\",\"selected\":null},{\"menu_item_option_name\":\"Seafood (Shrimp, Squid, Sacllop& Mussel w. Basil) \",\"menu_item_option_additional_cost\":\"6.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"TOM KHA (THAI GALANGA COCONUT SOUP) Wheat Free\",\"menu_item_description\":\"This classic Thai soup gets its rich flavor from quintessential Thai ingredients: coconut milk, lemongrass, Galanga, and Mushroom. Wheat Free\",\"menu_item_price\":\"4.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Your choice\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Fresh Tofu, Vegetables, Or Chicken \",\"menu_item_option_additional_cost\":\"4.95\",\"selected\":null},{\"menu_item_option_name\":\"Shrimp \",\"menu_item_option_additional_cost\":\"5.95\",\"selected\":null},{\"menu_item_option_name\":\"Seafood (Shrimp, Squid, Sacllop& Mussel )\",\"menu_item_option_additional_cost\":\"6.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"TOFU VEGETABLE SOUP\",\"menu_item_description\":\"Mixed Veg with fresh tofu in clear chicken broth\",\"menu_item_price\":\"4.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"AUTHENTIC THAI SALAD\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"HOUSE SALAD WHEAT FREE\",\"menu_item_description\":\"Lettuce ,spinach, romain heart , tomatoes, cucumbers , carrots, red onion, shredded chicken and shrimp. Served with ginger salad dressing or peanut sauce.\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"LARB CHICKEN* WHEAT FREE\",\"menu_item_description\":\"Ground chicken tossed with red onion, scallion, cilantro, mint leaves, red pepper , chili powder, and roasted rice powder in lime dressing served on a lettuce bed.\",\"menu_item_price\":\"12.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"YUM NUER* (SPICY BEEF SALAD) WHEAT FREE\",\"menu_item_description\":\"Grilled steak sliced thinly tossed with red onion, scallion, mint leaves, red pepper, cilantro, lemon glass, ground roasted rice powder served on a lettuce bed.\",\"menu_item_price\":\"12.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"GREEN PAPAYA SALAD * WHEAT FREE \\u201cWHEN AVAILABLE\\u201d\",\"menu_item_description\":\"Known as\\u201d SOM TUM\\u201d in Thailand. A mixture of fresh shredded green papaya, green bean, carrot, tomatoes, peanut, fresh garlic and Thai chili in fresh lime dressing topped with grill shrimps served with sticky rice.\",\"menu_item_price\":\"12.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"TOFU & VEGETABLE LOVER\",\"group_note\":\"\",\"group_description\":\"Everything is already used vegetarian sauce. (NO OYSTER SAUCE OR FISH SAUCE!)\\r\\n\",\"menu_group_options\":[{\"group_options_name\":\"Tofu (Fried or Steamed) or Vegetables \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"8.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"12.95\",\"selected\":null}]},{\"group_options_name\":\"Soy Chicken Nugget (Vegetarian)\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"All day\",\"menu_group_option_additional_cost\":\"13.95\",\"selected\":null}]}],\"menu_items\":[{\"menu_item_name\":\"TOFU NOODLE\",\"menu_item_description\":\"Steamed rice noodles with assorted vegetables and fried tofu, topped with peanut sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"JAY DELIGHT\",\"menu_item_description\":\"Assorted vegetables sauteed in garlic sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"VEGETABLE FRIED RICE\",\"menu_item_description\":\"Thai style fried rice with assorted vegetables. Served with cucumber and tomatoes slices.Topped with cilantro.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"JAY RAMA\",\"menu_item_description\":\"Assorted mixed vegetables with peanut sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"VOLCANO TOFU\",\"menu_item_description\":\"Lightly fried tofu saut\\u00e9ed in chili sauce with onion, carrots, red and green peppers, green bean and scallions\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"JAY CURRY\",\"menu_item_description\":\"Assorted vegetables in red curry sauce. Sprinkled with basil leaves\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"JAY PADTHAI\",\"menu_item_description\":\"One of the most popular Thai noodles dish with ground peanuts, scallions and bean sprouts.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"JAY DRUNKEN NOODLE\",\"menu_item_description\":\"Soft wide rice noodle sauteed in chili garlic sauce with red and green pepper, onion, bamboo, carrot.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"CURRIES WITH COCONUT MILK\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[{\"group_options_name\":\"Tofu (Fried or Steamed) or Vegetables\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"8.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"12.95\",\"selected\":null}]},{\"group_options_name\":\"Chicken, Pork, or Beef \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"8.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"13.95\",\"selected\":null}]},{\"group_options_name\":\"Chicken & Shrimp \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"10.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"15.95\",\"selected\":null}]},{\"group_options_name\":\"Shrimp, Squid or Scallop \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"10.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"16.95\",\"selected\":null}]},{\"group_options_name\":\"Duck (Crispy or Not?) \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"All day\",\"menu_group_option_additional_cost\":\"19.95\",\"selected\":null}]},{\"group_options_name\":\"Seafood (Shrimp, Squid and Scallop) \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"12.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"19.95\",\"selected\":null}]},{\"group_options_name\":\"Soy Chicken Nugget (Vegetarian)\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"All day\",\"menu_group_option_additional_cost\":\"13.95\",\"selected\":null}]}],\"menu_items\":[{\"menu_item_name\":\"RED CURRY\",\"menu_item_description\":\"Bamboo shoot, red pepper, green bean,pumpkin and basil leaves in red curry sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"GREEN CURRY\",\"menu_item_description\":\"Green bean, asparagus, red and green pepper, bamboo shoot, kaffir lime leaves with basil leaves and kra-chai (strong, distinctive aroma) in green curry sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"MASSAMAN CURRY\",\"menu_item_description\":\"Flavored with red chili paste with potato cubes, carrots, onion slices and roasted peanuts. This curry has a slight tangy sour tamarind taste.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"ALA CARTE\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[{\"group_options_name\":\"Tofu (Fried or Steamed) or Vegetables\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"8.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"12.95\",\"selected\":null}]},{\"group_options_name\":\"Chicken, Pork, or Beef\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"8.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"13.95\",\"selected\":null}]},{\"group_options_name\":\"Chicken & Shrimp \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"10.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"15.95\",\"selected\":null}]},{\"group_options_name\":\"Shrimp, Squid or Scallop \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"10.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"16.95\",\"selected\":null}]},{\"group_options_name\":\"Duck (Crispy or Not?)\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"All day\",\"menu_group_option_additional_cost\":\"19.95\",\"selected\":null}]},{\"group_options_name\":\"Seafood (Shrimp, Squid and Scallop) \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"12.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"19.95\",\"selected\":null}]},{\"group_options_name\":\"Soy Chicken Nugget (Vegetarian) \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"All day\",\"menu_group_option_additional_cost\":\"13.95\",\"selected\":null}]}],\"menu_items\":[{\"menu_item_name\":\"BROCCOLI\",\"menu_item_description\":\"Sauteed broccoli, carrots, asparagus and mushrooms in garlic sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"FRESH GINGER\",\"menu_item_description\":\"Sauteed ginger, mushroom, red peppers, onions, celery and scallion in black bean sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"BASIL LEAVES\",\"menu_item_description\":\"Traditional Thai brown sauce with chili-garlic, onions, mushrooms, red & green peppers and basil leaves.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"VEGETABLES DELIGHT\",\"menu_item_description\":\"Sauteed fresh assorted vegetables in brown sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"NOODLE AND FRIED RICE DISHES\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[{\"group_options_name\":\"Tofu (Fried or Steamed) or Vegetables\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"8.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"12.95\",\"selected\":null}]},{\"group_options_name\":\"Chicken, Pork, or Beef \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"8.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"13.95\",\"selected\":null}]},{\"group_options_name\":\"Chicken & Shrimp \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"9.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"14.95\",\"selected\":null}]},{\"group_options_name\":\"Shrimp, Squid or Scallop \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"9.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"15.95\",\"selected\":null}]},{\"group_options_name\":\"Duck (Crispy or Not?) \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"All day\",\"menu_group_option_additional_cost\":\"15.95\",\"selected\":null}]},{\"group_options_name\":\"Seafood (Shrimp, Squid and Scallop) \",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Lunch\",\"menu_group_option_additional_cost\":\"10.95\",\"selected\":null},{\"menu_group_option_name\":\"Dinner\",\"menu_group_option_additional_cost\":\"17.95\",\"selected\":null}]},{\"group_options_name\":\"Soy Chicken Nugget (Vegetarian)\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"All day\",\"menu_group_option_additional_cost\":\"13.95\",\"selected\":null}]}],\"menu_items\":[{\"menu_item_name\":\"PAD THAI\",\"menu_item_description\":\"One of the most popular Thai noodles dish, stir-fried with your choice of meat or tofu, eggs, sprinkled with ground peanuts, scallions and bean sprouts.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"DELUXE PAD THAI\",\"menu_item_description\":\"Special chef idea for people whom loves spiciness; using rice noodles stir-fried with chicken, shrimps, tofu and egg, bean sprout, ground peanut and scallions in Pad Thai sauce with a touch of shrimp pastepaprika, and chili flakes 9.95\\/14.95\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"PAD SEE-EW( PAN FRIED RICE NOODLE)\",\"menu_item_description\":\"Soft wide rice noodles sauteed with egg, carrots and broccoli in brown sauce with a touch of dark sweet sauce, vinegar and minced garlic.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"DRUNKEN NOODLE (PAD KHEE-MAO)\",\"menu_item_description\":\"Soft wide rice noodles sauteed in chili-garlic sauce with red & green peppers, onions, bamboo, carrots, and a touch of dark sweet sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"HOUSE FRIED RICE (KHAO PAD)\",\"menu_item_description\":\"Thai style fried rice with egg, tomatoes, onion, and scallions, served with cucumber slices. Topped with cilantro.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"SPICY FRIED RICE\",\"menu_item_description\":\"Basil Fried Rice Fried rice with red and green peppers, onions, broccoli, carrots, and basil leaves in chili-garlic sauce, flavored with fried rice sauce. Served with cucumber slices.Topped with cilantro.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"PINEAPPLE FRIED RICE\",\"menu_item_description\":\"Fried rice with shrimp, chicken, egg, pineapple chunks, cashew nuts, scallions and raisin. Served with fresh cucumber and tomatoes slices. Topped with cilantro 9.95\\/14.95\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"OUR CHEF\\u2019S SPECIAL\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"THREE FLAVORED SEAFOOD\",\"menu_item_description\":\"Lightly battered scallop, shrimp, squid and fillet of Haddock, topped with red and green pepper, onion, and pineapple in spicy tamarind sauce. Served with stream Broccoli\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch \",\"menu_item_option_additional_cost\":\"12.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"19.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"SEAFOOD KAREE\",\"menu_item_description\":\"Combination of scallops, shrimp, squids, and mussels, sauteed with carrots, onions, celery, red peppers, scallions, eggs, and a touch of curry powder in chili sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"12.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"19.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"SHRIMP LOVE SCALLOP\",\"menu_item_description\":\"Sauteed shrimp and scallop with fresh ginger and assorted vegetables in garlic sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"10.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"17.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"SEAFOOD PAD SHA\",\"menu_item_description\":\"Lightly battered combination of scallops, shrimp, squids and mussels, sauteed with red peppers, minced garlic, fresh peppercorn, Krachai and basil leaves in chef special sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"12.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"19.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"FISH GARLIC\",\"menu_item_description\":\"Lightly battered fillet of Haddock, topped with chopped shrimp in special garlic sauce mix with fried herb. Served with steam spinach and asparagus well-blended garlic sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"12.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"19.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CURRY DUCK ( WHEAT FREE)\",\"menu_item_description\":\"This is one of the most popular on Thai restaurant menus especially in the West, we use the aromatic red curry dish comes with distinctive flavors from roasted duck cooked in coconut-based red curry with fresh pineapple chunk, red peppers, cherry tomato and basil leaves.\",\"menu_item_price\":\"19.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"SPICY CRISPY DUCK\",\"menu_item_description\":\"Duck is used in a variety of dishes around the world, most of which involve roasting for at least part of the cooking process to aid in crisping the skin. Half crispy roasted duck topped with mushrooms, onions, red and green peppers, and basil leaves in spicy chili-garlic sauce.\",\"menu_item_price\":\"19.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"SPICY CRISPY CHICKEN CASHEW NUT\",\"menu_item_description\":\"Lightly battered white meat chicken sauteed with cashew nut, onion, dry red pepper, and scallion in PrikPao sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"9.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"14.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"BBQ PORK MOO YANG\",\"menu_item_description\":\"Thai style grilled pork loin (Marinated with cilantro, garlic, and brown sauce). Served with sticky rice, spicy tamarind dipping sauce and Thai sweet chili sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"9.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"14.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"DRUNKEN CHICKEN\",\"menu_item_description\":\"Stir-fried ground chicken breasts, green beans,and basil leaves in spicy sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"9.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"14.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Tortellini soup\",\"menu_item_description\":\"Stir-fried ground chicken breasts, green beans,and basil leaves in spicy sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"9.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"14.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN PINEAPPLE\",\"menu_item_description\":\"A Thai inspired stir fry dish with a wonderful savory, colorful bell peppers, fresh pineapple, mushroom, snow peas, onions, scallion, cashew nuts and chicken tender in brown sauce.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Available\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Lunch\",\"menu_item_option_additional_cost\":\"9.95\",\"selected\":null},{\"menu_item_option_name\":\"Dinner\",\"menu_item_option_additional_cost\":\"14.95\",\"selected\":null}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]}]}]}],\"id\":\"eGUxRHBzbVU0dzJ0RGRtMHNxcE9nMjZkRlRDUXNBckpnOUJHYjR4UUo0V21JQ0VzTEhGTXZPS0JUSmlWRURlRg==\"}}}";
		
    	// Get the menu items for each restaurant
    	ArrayList<ArrayList<String>> menuItems = new ArrayList<ArrayList<String>>();
    	for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
    		menuItems.add(backend.GetMenuItems(menuResult));
    	}
		
		// Count the matches we get for each matching algorithm
    	// Also categorize as true pos/false pos/false neg in Matches
    	// True pos - match with a menu item and menu item is in GROUND_TRUTH
    	// False pos - match with a menu item and menu item is not in GROUND_TRUTH
    	// False neg - didn't match with a menu item but menu item is in GROUND_TRUTH
    	
    	ArrayList<ArrayList<EntityClass>> restaurant;
    	ArrayList<EntityClass> review;
    	ArrayList<String> restaurantItems;
    	String menuItem;
    	String[][] restaurantGroundTruth;
    	String[] reviewGroundTruth;
    	String restaurantItem;
    	String[] tokens1;
    	String[] tokens2;
    	int containIndex;
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
    				restaurantItem = restaurantItems.get(l);
    				System.out.println(restaurantItem);
    				for (int k = 0; k < review.size(); k ++)
    				{
    					// For each entity, check if you get a match with any menu item
        				menuItem = review.get(k).GetWord();
    					if (menuItem.equals("tortellini soup") && restaurantItem.equals("Tortellini soup"))
    					{
    						System.out.println("Menu item:" + menuItem);
    						System.out.println("Restaurant item: " + restaurantItem);
    						System.out.println((CaseInsensitiveMatch(menuItem, restaurantItem)));
    						System.out.println((LevensteinEditDistance(menuItem, restaurantItem, 4)));
    						System.out.println((JaroSimilarity(menuItem, restaurantItem, 0.8)));
    						tokens1 = Tokenize(menuItem, 3, false);
        					tokens2 = Tokenize(restaurantItem, 3, false);
    						System.out.println((JaccardSimilarity(tokens1, tokens2, 0.8)));
    						tokens1 = Tokenize(menuItem, 2, false);
        					tokens2 = Tokenize(restaurantItem, 2, false);
    						System.out.println((JaccardSimilarity(tokens1, tokens2, 0.8)));
    						tokens1 = Tokenize(menuItem, 1, true);
        					tokens2 = Tokenize(restaurantItem, 1, true);
    						System.out.println((JaccardSimilarity(tokens1, tokens2, 0.8)));
    						System.out.println("\n\n");
    						System.out.println(Contains2(reviewGroundTruth, menuItem));
    					}
    					
    					containIndex = Contains(reviewGroundTruth, menuItem);
    					
    					
    					// Test each matching algorithm
    					if (CaseInsensitiveMatch(menuItem, restaurantItem))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (containIndex != -1)
    						{
    							Matches[0][0] ++;
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[0][1] ++;
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (containIndex != -1)
    						{
    							Matches[0][2] ++;
    						}
    					}
    					if (LevensteinEditDistance(menuItem, restaurantItem, 4))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (containIndex != -1)
    						{
    							Matches[1][0] ++;
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[1][1] ++;
    						}
    					}	
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (containIndex != -1)
    						{
    							Matches[1][2] ++;
    						}
    					}
    					if (JaroSimilarity(menuItem, restaurantItem, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (containIndex != -1)
    						{
    							Matches[2][0] ++;
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[2][1] ++;
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (containIndex != -1)
    						{
    							Matches[2][2] ++;
    						}
    					}
    					tokens1 = Tokenize(menuItem, 2, false);
    					tokens2 = Tokenize(restaurantItem, 2, false);
    					if (JaccardSimilarity(tokens1, tokens2, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (containIndex != -1)
    						{
    							Matches[3][0] ++;
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[3][1] ++;
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (containIndex != -1)
    						{
    							Matches[3][2] ++;
    						}
    					}
    					tokens1 = Tokenize(menuItem, 3, false);
    					tokens2 = Tokenize(restaurantItem, 3, false);
    					if (JaccardSimilarity(tokens1, tokens2, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (containIndex != -1)
    						{
    							Matches[4][0] ++;
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[4][1] ++;
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (containIndex != -1)
    						{
    							Matches[4][2] ++;
    						}
    					}
    					tokens1 = Tokenize(menuItem, 1, true);
    					tokens2 = Tokenize(restaurantItem, 1, true);
    					if (JaccardSimilarity(tokens1, tokens2, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (containIndex != -1)
    						{
    							Matches[5][0] ++;
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[5][1] ++;
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (containIndex != -1)
    						{
    							Matches[5][2] ++;
    						}
    					}
//    					if (containIndex != -1)
//    					{
//    						reviewGroundTruth[i] = "";
//    					}
    				}
    			}
    		}
    	}
    	
    	System.out.println(Matches[1][0]);
    	System.out.println(Matches[1][1]);
    	System.out.println(Matches[1][2]);
    	
    	
		
		// Calculate the precision/recall/F1 score for each and output as table
    	// precision = true pos / (true pos + false pos)
    	// recall = true pos / (true pos + false neg)
    	// F1 = 2 * precision * recall / (precision + recall)
    	float precision;
    	float recall;
    	float f1;
    	for (int i = 0; i < MATCHING_ALGS.length; i ++)
    	{
    		if (Matches[i][0] == 0)
    		{
    			precision = 0;
    			recall = 0;
    			f1 = 0;
    		}
    		else
    		{
    			precision = Matches[i][0] / (Matches[i][0] + Matches[i][1]);
    			recall = Matches[i][0] / (Matches[i][0] + Matches[i][2]);
    			f1 = 2 * precision * recall / (precision + recall);
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
    
    public static boolean JaroSimilarity(String str1, String str2, double minAmt)
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
    	
    	double temp1 = num1Matching;
    	double temp2 = num2Matching;
    	int index;
    	// Get the number of transpositions between the matching letters
    	while ((temp1 > 0) && (temp2 > 0))
    	{
    		if (str1Matches.charAt(0) == str2Matches.charAt(0))
    		{
    			str2Matches = str2Matches.substring(1);
    		}
    		else
    		{
    			index = str2Matches.indexOf(str1Matches.charAt(0));
    			if (index != -1)
    			{
    				str2Matches = str2Matches.substring(0, index) + str2Matches.substring(index + 1);
        			numTranspositions ++;
    			}
    		}
    		str1Matches = str1Matches.substring(1);
    		temp1 --;
    		temp2 --;
    	}
    	
    	// Calculate the total Jaro score
    	double metric = (num1Matching / len1 + num2Matching / len2 + (num1Matching - numTranspositions) / num1Matching) / 3.0;
    	
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
    
    public static int Contains(String[] arr, String str)
    {
    	for (int i = 0; i < arr.length; i ++)
    	{
    		if (arr[i].equals(str))
    		{
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static int Contains2(String[] arr, String str)
    {
    	System.out.println("String:" + str);
    	for (int i = 0; i < arr.length; i ++)
    	{
    		System.out.println("Array: " + arr[i]);
    		if (arr[i].equals(str))
    		{
    			return i;
    		}
    	}
    	return -1;
    }
}






