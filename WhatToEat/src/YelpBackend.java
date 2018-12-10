
// Backend code to get reviews from a business name using Yelp dataset

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.simple.parser.*;
import java.util.ArrayList;

public class YelpBackend
{
    public static void main(String[] args) throws ParseException, IOException, InterruptedException, ExecutionException
    {
    	BackendClass backend = new BackendClass();
    	
    	// Scanners to open the businesses and reviews files and to scan input
    	ArrayList<Scanner> scanners = new ArrayList<Scanner>();
        Scanner inputScanner = new Scanner(System.in);
        scanners.add(inputScanner);

        // Find the business ID corresponding to the name
        System.out.println("Enter the restaurant name: ");
        String restName = inputScanner.nextLine();
        
        System.out.println(restName);
        
        ArrayList<RestaurantClass> businesses = backend.FindBusinessId(restName);
        if (businesses.size() == 0)
        {
            System.out.println("Sorry, but we do not have any data on " + restName);
            System.out.println("Please try a different restaurant, or add your own review.");
            backend.CleanUp(scanners);
            return;
        }

        ArrayList<ReviewClass> reviews = new ArrayList<ReviewClass>();
        for (int i = 0; i < businesses.size(); i ++)
        {
        	RestaurantClass business = businesses.get(i);
            // Get all the reviews about the specific restaurant
        	reviews.addAll(backend.GetReviews(business));
        }
        if (reviews.size() == 0)
        {
        	System.out.println("Sorry, we do not have any reviews for " + restName);
        	System.out.println("Please try a different restaurant or add your own review.");
        	backend.CleanUp(scanners);
        	return;
        }
        
        //Testing the Open Menu Search
        String restaurantInfo = backend.QueryOpenMenuSearch(restName, "06855");
        if (restaurantInfo.equals(""))
        {
        	System.out.println("Sorry but we do not have information about " + restName);
        	backend.CleanUp(scanners);
        	return;
        }
        
        // Getting the Open Menu menu
        String menuInfo = backend.GrabMenu(restaurantInfo);

        // Escape quotes in reviews which were causing errors with Google API
        reviews = backend.EliminateQuotes(reviews);

        // Send the reviews to Google API and receive the response
        reviews = backend.QueryGoogleApi(reviews);
        
        // Parse the Google response to get all of the entities
        ArrayList<EntityClass> entities = new ArrayList<EntityClass>();
        for (int i = 0; i < reviews.size(); i ++)
        {
        	entities.addAll(backend.GetEntities(reviews.get(i)));
        }
        
        // Map the sentiment scores to 0-5
        entities = backend.MapSentimentScores(entities);
        
        // Match these entities with the menu items from OpenMenu
        // using the best matching algorithm from our testing class
        ArrayList<String[]> menuItems = new ArrayList<String[]>();
        menuItems = backend.GetMenuItems(menuInfo);
        
        ArrayList<EntityClass> databaseEntities = new ArrayList<EntityClass>();
        databaseEntities = backend.MatchMenuItems(entities, menuItems);
        		
        // Send the matched entities to the database
        backend.SendToDatabase(databaseEntities);

        backend.CleanUp(scanners);
    }
}
