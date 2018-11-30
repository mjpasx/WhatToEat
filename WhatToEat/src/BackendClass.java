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
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.cloud.firestore.DocumentReference;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
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
	private final String OPENMENU_URL = "https://openmenu.com/api/v2/search.php?key=";
	private final String REVIEWS_PATH = "yelp_dataset/yelp_academic_dataset_review.json";
	// private final String REVIEWS_PATH = "yelp_dataset/smallReviews.json";
	private final String USERS_PATH = "yelp_dataset/yelp_academic_dataset_user.json";

	public BackendClass() {

	}

	public void CleanUp(ArrayList<Scanner> scanners) {
		// Clean up once we are done
		for (int i = 0; i < scanners.size(); i++) {
			scanners.get(i).close();
		}
	}

	public ArrayList<RestaurantClass> FindBusinessId(String restName, Scanner businessScanner)
			throws FileNotFoundException, ParseException {
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
			}
		}
		// Return empty ArrayList if we don't have a matching business
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
			URL url = new URL(OPENMENU_URL + apiKey + "&id=" + restaurantID);
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

	public String QueryOpenMenuSearch(String restaurantName, String zipCode) {
		// Get the API key from the environment
		String apiKey = System.getenv("OPENMENU_API");
		String restaurantInfo = "";
		String country = "US";
		restaurantName = restaurantName.replace(" ", "%20");

		try {
			// Set up a connection with Google API
			URL url = new URL(OPENMENU_URL + apiKey + "&s=" + restaurantName + "&postal_code=" + zipCode + "&country" + country);
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
			restaurantInfo = reply;
		}
		// Catch if there is an error
		catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e);
		}
		return restaurantInfo;
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
		return false;
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
}









