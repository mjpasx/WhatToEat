
// Backend code to get reviews from a business name using Yelp dataset

import java.io.*;
import java.util.Scanner;
import org.json.simple.parser.*;
import java.util.LinkedList;

public class YelpBackend
{
	// Constants
    static final String BUSINESS_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
    static final String REVIEWS_PATH = "yelp_dataset/yelp_academic_dataset_review.json";

    public static void main(String[] args) throws FileNotFoundException, ParseException
    {
    	BackendClass backend = new BackendClass();
    	
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
        //String venueId = FindVenueID(restName);
        String businessId = backend.FindBusinessId(restName, businessScanner);
        if (businessId == null)
        {
            System.out.println("Sorry, but we do not have any data on " + restName);
            System.out.println("Please try a different restaurant, or add your own review.");
            backend.CleanUp(scanners);
            return;
        }
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

        reviews = backend.EliminateQuotes(reviews);

        System.out.println("There are " + reviews.size() + " reviews for this place");

        LinkedList<String> sentimentAnalysis = new LinkedList<String>();
        sentimentAnalysis = backend.QueryGoogleApi(reviews);

        backend.CleanUp(scanners);
    }
}
