
// Backend code to get reviews from a business name using Yelp dataset

import java.io.*;
import java.util.Scanner;
import org.json.simple.parser.*;
import java.util.LinkedList;

public class YelpBackend
{
	// Constants
    static final String BUSINESS_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
    //static final String REVIEWS_PATH = "yelp_dataset/yelp_academic_dataset_review.json";
    static final String REVIEWS_PATH = "yelp_dataset/smallReviews.json";
    
    public static void main(String[] args) throws FileNotFoundException, ParseException
    {
    	BackendClass backend = new BackendClass();
    	
    	// Scanners to open the businesses and reviews files and to scan input
    	LinkedList<Scanner> scanners = new LinkedList<Scanner>();
        Scanner inputScanner = new Scanner(System.in);
        scanners.add(inputScanner);
        File businessFile = new File(BUSINESS_PATH);
        Scanner businessScanner = new Scanner(businessFile);
        scanners.add(businessScanner);
        File reviewFile = new File(REVIEWS_PATH);
        Scanner reviewScanner = new Scanner(reviewFile);
        scanners.add(reviewScanner);

        // Find the business ID corresponding to the name
        System.out.println("Enter the restaurant name: ");
        String restName = inputScanner.nextLine();
        //String venueId = FindVenueID(restName);
        String businessId = backend.FindBusinessId(restName, businessScanner);
        if (businessId == null)
        {
            System.out.println("Sorry, but we do not have any data on " + restName);
            System.out.println("Please try a different restaurant, or add your own review.");
            backend.CleanUp(scanners);
            return;
        }
        // Get all the reviews about the specific restaurant
        LinkedList<String> reviews = backend.GetReviews(businessId, reviewScanner);
        if (reviews.size() == 0)
        {
        	System.out.println("Sorry, we do not have any reviews for " + restName);
        	System.out.println("Please try a different restaurant or add your own review.");
        	backend.CleanUp(scanners);
        	return;
        }
        for (int i = 0; i < reviews.size(); i ++)
        {
        	System.out.println(reviews.get(i));
        	System.out.println("\n\n\n\n\n\n");
        }

        // Escape quotes in reviews which were causing errors with Google API
        reviews = backend.EliminateQuotes(reviews);

        System.out.println("There are " + reviews.size() + " reviews for this place");

        // Send the reviews to Google API and receive the response
        LinkedList<String> sentimentAnalysis = backend.QueryGoogleApi(reviews);
        
        // Parse the Google response to get all of the entities
        LinkedList<EntityClass> entities = new LinkedList<EntityClass>();
        for (int i = 0; i < sentimentAnalysis.size(); i ++)
        {
        	entities.addAll(backend.GetEntities(sentimentAnalysis.get(i), reviews.get(i)));
        }
        
        //Testing the Open Menu Search
        LinkedList<String> restaurantInfo = backend.QueryOpenMenuSearch("5ThaiBistro", "Portsmouth");
        System.out.println(restaurantInfo);
        
                
        //System.out.println(entities.get(0).GetSentiment());
        //System.out.println(entities.get(0).GetWord());
        //System.out.println(entities.get(0).GetReview());
        for (int i = 0; i < entities.size(); i ++)
        {
        	System.out.println(entities.get(i).GetSentiment());
        	System.out.println(entities.get(i).GetWord());
            //System.out.println(entities.get(i).GetReview());
        	System.out.println("\n");
        }

        backend.CleanUp(scanners);
    }
}
