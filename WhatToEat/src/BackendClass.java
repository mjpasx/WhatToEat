import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.cloud.firestore.DocumentReference;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.auth.oauth2.GoogleCredentials;


// Class file that holds the functions common between TestMatching.java and YelpBackend.java
public class BackendClass {
	// Constants
	private final String GOOGLE_URL = "https://language.googleapis.com/v1/documents:analyzeEntitySentiment?key=";
	private final String REQUEST_BEG = "{\"document\":{\"type\":\"PLAIN_TEXT\",\"content\":\"";
	private final String REQUEST_END = "\"},\"encodingType\":\"UTF8\"}";
	private final String OPENMENU_SEARCH_URL = "https://openmenu.com/api/v2/search.php?key=";
	private final String OPENMENU_MENU_URL = "https://openmenu.com/api/v2/restaurant.php?key=";
	private final String REVIEWS_PATH = "yelp_dataset/yelp_academic_dataset_review.json";
	private final String BUSINESSES_PATH = "yelp_dataset/yelp_academic_dataset_business.json";
	//private final String REVIEWS_PATH = "yelp_dataset/smallReviews.json";
	private final String USERS_PATH = "yelp_dataset/yelp_academic_dataset_user.json";

	public BackendClass() {

	}

	public void CleanUp(ArrayList<Scanner> scanners) {
		// Clean up once we are done
		for (int i = 0; i < scanners.size(); i++) {
			scanners.get(i).close();
		}
	}

	public ArrayList<RestaurantClass> FindBusinessId(String restName)
			throws FileNotFoundException, ParseException {
		File businessFile = new File(BUSINESSES_PATH);
		Scanner businessScanner = new Scanner(businessFile);
		ArrayList<RestaurantClass> restaurants = new ArrayList<RestaurantClass>();
		
		while (businessScanner.hasNextLine()) {
			String line = businessScanner.nextLine();
			Object obj = new JSONParser().parse(line);
			JSONObject businessObj = (JSONObject) obj;
			String name = (String) businessObj.get("name");
			// If you get a matching business name, then return the business ID
			if (name.equals(restName)) {
				RestaurantClass restaurant = new RestaurantClass();
				restaurant.SetId((String) businessObj.get("business_id"));
				restaurant.SetRestName(restName);
				restaurant.SetZipCode((String) businessObj.get("postal_code"));
				restaurants.add(restaurant);
				System.out.println(restaurant.GetRestName());
			}
		}
		// Return empty ArrayList if we don't have a matching business
		businessScanner.close();
		return restaurants;
	}

	public ArrayList<ReviewClass> GetReviews(RestaurantClass business) throws FileNotFoundException, ParseException {
		File reviewFile = new File(REVIEWS_PATH);
		Scanner reviewScanner = new Scanner(reviewFile);
		ArrayList<ReviewClass> reviews = new ArrayList<ReviewClass>();

		String review;
		String restName;
		String zipCode;
		String userId;
		String user;
		String time;
		String businessId = business.GetId();
		int count = 0;

		while (reviewScanner.hasNextLine()) {
			String line = reviewScanner.nextLine();
			Object obj = new JSONParser().parse(line);
			JSONObject reviewObj = (JSONObject) obj;
			String id = (String) reviewObj.get("business_id");
			// If you get a matching business ID, add it to the list
			if (businessId.equals(id)) {
				review = (String) reviewObj.get("text");
				restName = business.GetRestName();
				zipCode = business.GetZipCode();
				userId = (String) reviewObj.get("user_id");
				user = GetUser(userId);
				time = (String) reviewObj.get("date");
				ReviewClass newReview = new ReviewClass(review, restName, zipCode, user, time);
				reviews.add(newReview);
				count ++;
				System.out.println(count);
			}
		}
		reviewScanner.close();
		// Return all the reviews at the end
		return reviews;
	}

	public String GetUser(String userId) throws FileNotFoundException, ParseException {
		File userFile = new File(USERS_PATH);
		Scanner userScanner = new Scanner(userFile);
		while (userScanner.hasNextLine()) {
			String line = userScanner.nextLine();
			Object obj = new JSONParser().parse(line);
			JSONObject userObj = (JSONObject) obj;
			String id = (String) userObj.get("user_id");
			if (userId.equals(id)) {
				userScanner.close();
				return (String) userObj.get("name");
			}
		}

		userScanner.close();
		return "";
	}

	public ArrayList<ReviewClass> EliminateQuotes(ArrayList<ReviewClass> reviews) {
		String text = "";
		// Within each review, escape all quotes
		// Need to do this for the Google API calls to work
		for (int i = 0; i < reviews.size(); i++) {
			ReviewClass review = reviews.get(i);
			reviews.remove(i);
			text = review.GetReview();
			text = text.replace("\"", "\\\"");
			review.SetReview(text);
			reviews.add(i, review);
		}
		return reviews;
	}

	public ArrayList<ReviewClass> QueryGoogleApi(ArrayList<ReviewClass> reviews) {
		// Get the API key from the environment
		String apiKey = System.getenv("API_KEY");
		for (int i = 0; i < reviews.size(); i++) {
			try {
				// Set up a connection with Google API
				URL url = new URL(GOOGLE_URL + apiKey);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				// Construct the POST message
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);
				// Write the request to Google
				OutputStream os = connection.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
				osw.write(REQUEST_BEG + reviews.get(i).GetReview() + REQUEST_END);
				osw.flush();
				osw.close();
				os.close();
				connection.connect();

				// Read in the response
				String reply;
				BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				int result = inputStream.read();
				while (result != -1) {
					buf.write((byte) result);
					result = inputStream.read();
				}
				// Turn the message into a string
				reply = buf.toString();
				ReviewClass review = reviews.get(i);
				reviews.remove(i);
				review.SetAnalyzedReview(reply);
				reviews.add(i, review);
			}
			// Catch if there is an error
			catch (Exception e) {
				System.out.println("Error.");
				System.out.println(e);

				// return null;
			}
		}
		// Return all of the sentiment analysis results
		return reviews;
	}

	public String GrabMenu(String restaurantID) {

		String apiKey = System.getenv("OPENMENU_API");
		String menu = "";
		try {
			// Set up a connection with Google API
			URL url = new URL(OPENMENU_MENU_URL + apiKey + "&id=" + restaurantID);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Construct the POST message
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.connect();

			// Read in the response
			String reply;
			BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = inputStream.read();
			while (result != -1) {
				buf.write((byte) result);
				result = inputStream.read();
			}

			// Turn the message into a string
			reply = buf.toString();
			System.out.println(reply);
			System.out.println("\n\n\n\n\n");
			menu = reply;
		}

		// Catch if there is an error
		catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e);
		}
		return menu;
	}
	
	private String GetOpenMenuId(String restaurantInfo) throws ParseException
	{
		String id = "";
		Object obj = new JSONParser().parse(restaurantInfo);
		JSONObject reviewObj = (JSONObject) obj;
		JSONObject response = (JSONObject) reviewObj.get("response");
		// Get the entities array, iterate through it
		JSONObject result = (JSONObject) response.get("result");
		JSONArray restaurants = (JSONArray) result.get("restaurants");
		JSONObject restaurant = (JSONObject) restaurants.get(0);
		id = (String) restaurant.get("id");
		
		// Automatically a "==" at the end of the id that we need to remove
		int length = id.length();
		id = id.substring(0, length - 2);
		return id;
	}

	public String QueryOpenMenuSearch(String restaurantName, String zipCode) throws ParseException {
		// Get the API key from the environment
		String apiKey = System.getenv("OPENMENU_API");
		String restaurantInfo = "";
		String country = "US";
		restaurantName = restaurantName.replace(" ", "%20");

		try {
			// Set up a connection with Google API
			URL url = new URL(OPENMENU_SEARCH_URL + apiKey + "&s=" + restaurantName + "&postal_code=" + zipCode + "&country=" + country);
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Construct the POST message
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.connect();

			// Read in the response
			String reply;
			BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = inputStream.read();
			while (result != -1) {
				buf.write((byte) result);
				result = inputStream.read();
			}
			// Turn the message into a string
			reply = buf.toString();
			System.out.println(reply);
			restaurantInfo = reply;
			
		}
		// Catch if there is an error
		catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e);
		}
		String restId = GetOpenMenuId(restaurantInfo);
		return restId;
	}

	public ArrayList<EntityClass> GetEntities(ReviewClass review) throws ParseException {
		ArrayList<EntityClass> entities = new ArrayList<EntityClass>();
		JSONObject entityObj;
		String name;
		Object sentiment;
		JSONObject sentimentObj;
		Object mag;
		double magnitude;

		String text = review.GetAnalyzedReview();

		// Turn the object into a JSON object
		Object obj = new JSONParser().parse(text);
		JSONObject reviewObj = (JSONObject) obj;
		// Get the entities array, iterate through it
		JSONArray ents = (JSONArray) reviewObj.get("entities");
		for (int i = 0; i < ents.size(); i++) {
			entityObj = (JSONObject) ents.get(i);
			// Get the name field from each object
			name = (String) entityObj.get("name");
			// From the sentiment object within the entity, get the sentiment score
			sentiment = entityObj.get("sentiment");
			sentimentObj = (JSONObject) sentiment;
			mag = sentimentObj.get("score");
			// Need to convert to to a double (sometimes comes as a long, sometimes a
			// double)
			if (mag instanceof Long) {
				magnitude = ((Long) mag).doubleValue();
			} else {
				magnitude = (double) mag;
			}
			// Add the new entity object
			EntityClass object = new EntityClass();
			object.SetWord(name);
			object.SetSentiment(magnitude);
			object.SetReview(review.GetReview());
			object.SetRestName(review.GetRestName());
			object.SetZipCode(review.GetZipCode());
			object.SetName(review.GetUser());
			object.SetTime(review.GetTime());
			object.SetDescription(review.GetDescription());
			entities.add(object);
		}

		return entities;
	}

	public ArrayList<String[]> GetMenuItems(String restInfo) throws ParseException {
		ArrayList<String[]> items = new ArrayList<String[]>();
		// Get the "response"
		Object obj = new JSONParser().parse(restInfo);
		JSONObject reviewObj = (JSONObject) obj;
		// From that get "result"
		JSONObject response = (JSONObject) reviewObj.get("response");
		JSONObject result = (JSONObject) response.get("result");
		// From that get the "menus" JSON object array
		JSONArray menus = (JSONArray) result.get("menus");

		JSONObject menu;
		JSONArray menuGroups;
		JSONObject menuGroup;
		JSONArray menuItems;
		JSONObject item;
		String itemName;
		String itemDesc;

		// For each menu, go to "menu_groups"
		for (int i = 0; i < menus.size(); i++) {
			menu = (JSONObject) menus.get(i);
			menuGroups = (JSONArray) menu.get("menu_groups");
			// Within each menu group, get the "menu_items" JSON array
			for (int j = 0; j < menuGroups.size(); j++) {
				menuGroup = (JSONObject) menuGroups.get(j);
				menuItems = (JSONArray) menuGroup.get("menu_items");
				// For each menu item, get the "menu_item_name"
				for (int k = 0; k < menuItems.size(); k++) {
					item = (JSONObject) menuItems.get(k);
					itemName = (String) item.get("menu_item_name");
					itemDesc = (String) item.get("menu_item_description");
					String[] itemTuple = { itemName, itemDesc };
					items.add(itemTuple);
				}
			}
		}

		return items;

	}

	public ArrayList<EntityClass> MapSentimentScores(ArrayList<EntityClass> entities) {
		double sentiment;
		for (int i = 0; i < entities.size(); i++) {
			EntityClass entity = new EntityClass();
			entity = entities.get(i);
			entities.remove(i);
			sentiment = entity.GetSentiment();
			sentiment = (sentiment - -1) / (1 - -1) * (5 - 0);
			entity.SetSentiment(sentiment);
			entities.add(i, entity);
		}
		return entities;
	}

	public ArrayList<EntityClass> MatchMenuItems(ArrayList<EntityClass> entities, ArrayList<String[]> menuItems) {
		ArrayList<EntityClass> databaseEnts = new ArrayList<EntityClass>();
		for (int i = 0; i < entities.size(); i++) {
			for (int j = 0; j < menuItems.size(); j++) {
				EntityClass entity = entities.get(i);
				String meal = entity.GetWord();
				String menuItem = menuItems.get(j)[0];
				if (Match(meal, menuItem)) {
					entity.SetWord(menuItem);
					entity.SetDescription(menuItems.get(j)[1]);
					databaseEnts.add(entity);
				}
			}
		}
		return databaseEnts;
	}

	private boolean Match(String menu1, String menu2) {
		
		String[] tokens1 = Tokenize(menu1, 1, true);
		String[] tokens2 = Tokenize(menu2, 2, true);
		
		// Use our best matching metric and threshold from testing program
		return JaccardSimilarity(tokens1, tokens2, 0.6);
	}

	public int SendToDatabase(ArrayList<EntityClass> ents) throws IOException, InterruptedException, ExecutionException {
		int count = 0;
		
		FileInputStream serviceAccount = new FileInputStream("/Users/Bleecher/Desktop/My Stuff/Senior Year/Senior Seminar/WhatToEat/WhatToEat/src/firestore/What to Eat-8f9201211161.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://what-to-eat-219113.firebaseio.com/")
				.build();

		FirebaseApp.initializeApp(options);
		
		Firestore db = FirestoreClient.getFirestore();
		
		for (int i = 0; i < ents.size(); i ++)
		{
			EntityClass entity = ents.get(i);
			ApiFuture<DocumentReference> addedDocRef = db.collection("meal-items").add(entity);
			System.out.println(addedDocRef.get().getId());
			count ++;
		}
		
		return count;
	}
	
    public boolean JaccardSimilarity(String[] str1, String[] str2, double minAmt)
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
    
    public String[] Tokenize(String str, int ngramLen, boolean asWords)
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
    
    public String RemovePunct(String str)
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
}









