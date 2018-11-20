import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetRestaurantNames {
	
	static final String BUSINESS_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
    static final String REVIEWS_PATH = "yelp_dataset/yelp_academic_dataset_review.json";
    //static final String REVIEWS_PATH = "yelp_dataset/smallReviews.json";

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		File businessFile = new File(BUSINESS_PATH);
        Scanner businessScanner = new Scanner(businessFile);
        
        
        HashSet<String> restaurants = new HashSet<String>();
        String line;
        String restName;
        int count = 0;
        while (businessScanner.hasNextLine())
        {
        	count ++;
        	line = businessScanner.nextLine();
        	restName = ParseRestaurantName(line);
        	if (restName == null)
        	{
        		continue;
        	}
        	restaurants.add(restName);
        }
        
        System.out.println(restaurants.size() + "/" + count);
        System.out.println((double) restaurants.size() / (double) count);
                
        businessScanner.close();
	}
	
	public static String ParseRestaurantName(String line) throws ParseException
	{
		Object obj = new JSONParser().parse(line);
        JSONObject reviewObj = (JSONObject) obj;
        String categories = (String) reviewObj.get("categories");
        
        if (categories == null)
        {
        	return null;
        }

		if (categories.contains("Restaurant"))
		{
			return (String) reviewObj.get("name");
		}
        return null;
	}

}
