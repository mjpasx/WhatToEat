
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.simple.parser.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;

public class TestMatching
{
    static ArrayList<EntityClass> MATCHING_ALGS = new ArrayList<EntityClass>();
    
    // Each int[] is true positives, false positives, false negatives
    // for each of the MATCHING_ALGS
    static BigDecimal[][] Matches = {
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO },
		{ BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO }
    };
    
    // Restaurants that we'll use to test
    static final String[] TEST_RESTAURANTS = {
		"Pasta Pomodoro",
		"Sunset Grille",
		"The Wing Company"
	};
    
    // First item is the food item from the review, the second is what we get from OpenMenu menu
    // If the second item of the 2-string array is empty, that means that there is no corresponding
    // meal item. So any match should count as a false positive
    static final String[][][] PASTA_POMODORO_MATCHES = {
    	{
    		{"fettuccine alfredo", ""}
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
    		{"spinach salad", "Spinach"}, {"raviolis", "Ravioli"}
    	},
    	{
    		{"meatballs", ""}
    	},
    	{ // OpenMenu API gives a weird version of Sauteed because it contains an accent
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
    
    static final String[][][] SUNSET_GRILLE_MATCHES = {
    	{
    		{"goat cheese", "Fried Goat Cheese Salad"}, {"croquetas", ""}, {"octopus", ""}, {"beef short ribs", ""}, {"potato omelette", ""}, {"patatas bravas", ""}, {"tres leches", ""}
    	}, 
    	{
    		{"ribs", "Award-Winning Baby Back Ribs"}, {"collard green", ""}, {"potato salad", ""}
    	},
    	{
    		{"risotto cake", ""}, {"short ribs", ""}, {"hanger steak", ""}, {"olive oil ice cream", ""}
    	},
    	{
    		{}
    	},
    	{
    		{}
    	},
    	{
    		{"quesadillas", "Chicken Quesadilla"}, {"mac", "Mac ‘n Cheese"}
    	},
    	{
    		{"BBQ pork", ""}, {"beef brisket sandwich", ""}
    	},
    	{
    		{"mussels", ""}, {"croquetas", ""}, {"Patatas Bravas", ""}, {"Alberginies Fritas", ""}, {"Kobe Skirt Steak", ""}, {"Champinones", ""}, {"Jamon Serrano", ""}, {"Brochetas de Gambas", ""}
    	},
    	{
    		{"fish tacos", "Fried Fish Tacos"}
    	},
    	{
    		{"Sonoma County Salad", ""}, {"chicken salad", ""}
    	},
    	{
    		{}
    	},
    	{
    		{}
    	},
    	{
    		{"chick pea salad", ""}, {"patatas bravas", ""}
    	},
    	{
    		{"broccoli cheddar soup", ""}, {"pimento cheese burger", ""}
    	},
    	{
    		{}
    	},
    	{
    		{"oxtail stew", ""}, {"olive tapenade", ""}, {"ribs", ""}
    	},
    	{
    		{}
    	},
    	{
    		{"potato salad", ""}, {"mac n cheese", "Mac ‘n Cheese"}, {"BBQ plate", ""}
    	},
    	{
    		{"nacho", ""}, {"cheese arugula pizza", ""}, {"BBQ sandwich", ""}
    	},
    	{
    		{"tres leches cake", ""}, {"Chorizo de Cider - Albondigas", ""}, {"Meatballs", ""}, {"Brochetas de Morunos", ""}
    	},
    	{
    		{"artichoke cakes", ""}, {"eggplant", ""}, {"tres leches cake", ""}
    	},
    	{
    		{"fritters", ""}
    	},
    	{
    		{"fish tacos", "Fried Fish Tacos"}, {"pizzas", "Seafood Pizza"}
    	},
    	{
    		{"Chicken salad", ""}, {"pizzas", "Seafood Pizza"}
    	},
    	{
    		{}
    	},
    	{
    		{"Eggs Benedict", ""}, {"canadian bacon", ""}, {"turkey sandwich", "Turkey Sandwich"}, {"blackbean burger", ""}
    	},
    	{
    		{"Pork Wet Burrito", ""}, {"beef brisket", ""}, {"pork sandwich", ""}, {"nachos", ""}
    	},
    	{
    		{"goat cheese app", "Fried Goat Cheese Salad"}, {"vegetable stack", ""}, {"burrito", ""}
    	},
    	{
    		{"benedict", ""}
    	},
    	{
    		{}
    	},
    	{
    		{"catfish tacos", ""}
    	},
    	{
    		{"Alberginies Fritas", ""}, {"Albondigas - Ground Pork", ""}
    	},
    	{
    		{"pico", ""}, {"guac", ""}, {"brisket sandwich", ""}, {"tuna tacos", "Ahi Tuna Tacos"}
    	},
    	{
    		{}
    	}
    };
    
    static final String[][][] WING_COMPANY_MATCHES = {
    	{
    		{"wings", "Wings"}, {"poutine", ""}, {"onion rings", "Beer Battered Onion Wings"}
    	},
    	{
    		{"fries", "Fresh Cut Fries"}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}, {"onion rings", "Beer Battered Onion Wings"}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}
    	},
    	{
    		{"poutine", ""}, {"fries", "Fresh Cut Fries"}, {"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}, {"onion rings", "Beer Battered Onion Wings"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"chicken burger", ""}, {"buffalo chicken crunch", ""}, {"burger", ""}, {"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}, {"onion rings", "Beer Battered Onion Wings"}, {"poutine", ""}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}, {"sweet potato fries", ""}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}, {"sweet potato fries", ""}
    	},
    	{
    		{"tuna sandwiches", ""}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"Wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}, {"sweet potato fries", ""}, {"onion rings", "Beer Battered Onion Wings"}, {"burger", ""}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}, {"poutine", ""}, {"onion rings", "Beer Battered Onion Wings"}, {"cheese sticks", "Mozzarella Sticks"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"fries", "Fresh Cut Fries"}, {"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"Fries", "Fresh Cut Fries"}, {"onion rings", "Beer Battered Onion Wings"}, {"burger", ""}, {"chicken wings", "Wings"}
    	},
    	{
    		{"Vegetarian Samosa", ""}, {"Wings", "Wings"}, {"Samosa", ""}
    	},
    	{
    		{"Wings", "Wings"}
    	},
    	{
    		{"Wing Man", ""}, {"Fries", "Fresh Cut Fries"}, {"wings", "Wings"}, {"Burgers", ""}, {"pizza", ""}
    	},
    	{
    		{"Wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}, {"Fries", "Fresh Cut Fries"}
    	},
    	{
    		{"wings", "Wings"}, {"pizza", ""}, {"fries", "Fresh Cut Fries"}, {"poutine", ""}
    	},
    	{
    		{"poutine", ""}, {"fries", "Fresh Cut Fries"}
    	},
    	{
    		{"wings", "Wings"}, {"fries", "Fresh Cut Fries"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"wings", "Wings"}
    	},
    	{
    		{"burger", ""}, {"wings", "Wings"}
    	}
    };
    
    static String[][] PASTA_POMODORO_ENTITIES = {
    	{
    		"fettuccine alfredo with chicken"
    	}, 
    	{
    		"tortellini soup", "Cesar salad"
    	}, 
    	{
    		
    	},
    	{
    		"pesto oil"
    	},
    	{
    		"Linguine with Fresh Dungenous crab & fresh veggies"
    	},
    	{
    		"Tortellini"
    	},
    	{
    		"risotto w/ clams, shrimp",  "Ravioli di Zucca", "ravioli"
    	},
    	{
    		
    	},
    	{
    		"ravioli di magro with pomodoro sauce", "fettuccine alfredo"
    	},
    	{
    		"Ravioli di Zucca Appetizer", "Healthy Fish", "Penne Salsiccia", "butternut squash ravioli"
    	},
    	{
    		"spinach salad", "raviolis"
    	},
    	{
    		"Spag' and meatballs"
    	},
    	{
    		"olive oil basil parsley dip", "shrimp with garlic and chili", "ravioli with brown butter and sage", "vongole with linguine"
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
    
    static String[][] SUNSET_GRILLE_ENTITIES = {
    	{
    		"fried goat cheese with honey", "chicken & Serrano ham croquetas", "grilled octopus", "beef short ribs", "potato omelette with spinach & manchego", "patatas bravas, tres leches"
    	}, 
    	{
    		"ribs", "collard green", "potato salad"
    	},
    	{
    		"risotto cake", "short ribs", "hanger steak", "olive oil ice cream"
    	},
    	{
    		
    	},
    	{
    		
    	},
    	{
    		"quesadillas", "mac & cheese"
    	},
    	{
    		"BBQ pork", "beef brisket sandwich", "burgers"
    	},
    	{
    		"mussels", "croquetas de pollo y jamon serrano", "Patatas Bravas (FRIED POTATOES)", "Alberginies Fritas (FRIED EGGPLANT)", "Kobe Skirt Steak", "Champinones (MUSHROOMS)", "Jamon Serrano and Manchego Cheese", "Brochetas de Gambas (SHRIMP AND SERRANO HAM ON A SKEWER)"
    	},
    	{
    		"fish tacos"
    	},
    	{
    		"Sonoma County Salad", "Asian chicken salad"
    	},
    	{
    		
    	},
    	{
    		
    	},
    	{
    		"chick pea salad", "patatas bravas"
    	},
    	{
    		"broccoli cheddar soup", "pimento cheese burger"
    	},
    	{
    		
    	},
    	{
    		"oxtail stew", "olive tapenade", "beef short ribs"
    	},
    	{
    		
    	},
    	{
    		"potato salad", "mac n cheese", "small BBQ plate"
    	},
    	{
    		"nacho's", "four cheese arugula pizza", "pulled pork BBQ sandwich"
    	},
    	{
    		"tres leches cake", "Grilled Asparagus", "Pure Patatas con Chorizo de Cider, Albondigas (veal & pork Meatballs)", "Brochetas de Morunos (pork Tenderloin with cider thyme reduction on a Skewer)"
    	},
    	{
    		"artichoke cakes", "eggplant", "tres leches cake"
    	},
    	{
    		"fritters"
    	},
    	{
    		"fish tacos", "pizzas"
    	},
    	{
    		"Asian Chicken salad"
    	},
    	{
    		
    	},
    	{
    		"Eggs Benedict's", "canadian bacon", "turkey sandwich", "blackbean burger"
    	},
    	{
    		"Chicken and Pork Wet Burrito", "beef brisket", "pork sandwich", "nachos"
    	},
    	{
    		"goat cheese app", "vegetable stack", "burrito"
    	},
    	{
    		"benedict"
    	},
    	{
    		
    	},
    	{
    		"burger", "fish dishes", "BBQ", "catfish tacos"
    	},
    	{
    		"Alberginies Fritas", "Albondigas"
    	},
    	{
    		"pico and guac", "brisket sandwich", "tuna tacos"
    	},
    	{
    		
    	}
    };
    
    static String[][] WING_COMPANY_ENTITIES = {
    	{
    		"wings", "poutine", "onion rings"
    	},
    	{
    		"poutine", "fries"
    	},
    	{
    		"wings", "fries", "onion rings"
    	},
    	{
    		"wings", "fries"
    	},
    	{
    		"poutine", "fries", "wings"
    	},
    	{
    		"wings", "fries", "onion rings"
    	},
    	{
    		"wings"
    	},
    	{
    		"chicken burger", "\"buffalo chicken crunch\" burger", "burger"
    	},
    	{
    		"wings", "fries", "onion rings", "poutine"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings", "sweet potato fries"
    	},
    	{
    		"bacon-wrapped wings", "fries", "sweet potato fries"
    	},
    	{
    		"tuna sandwiches"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings", "fries", "sweet potato fries", "onion rings", "vegetarian burger"
    	},
    	{
    		"wings", "fries"
    	},
    	{
    		"wings", "fries", "poutine", "onion rings", "cheese sticks"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings"
    	},
    	{
    		"fries", "wings"
    	},
    	{
    		"wings"
    	},
    	{
    		"fries", "onion rings", "burger", "chicken wings"
    	},
    	{
    		"Spicy vegetarian Samosa", "wings", "Samosa"
    	},
    	{
    		"wings"
    	},
    	{
    		"The Wing Man", "Fries", "Veggies & Dip", "Brownies", "pops", "wings", "Burgers", "pizza"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings", "Fries"
    	},
    	{
    		"wings", "pizza", "fries", "poutine"
    	},
    	{
    		"poutine", "fries"
    	},
    	{
    		"wings", "fries"
    	},
    	{
    		"wings"
    	},
    	{
    		"wings"
    	},
    	{
    		"burger", "wings"
    	}
    };
    
    static String[][][][] GROUND_TRUTH = { PASTA_POMODORO_MATCHES, SUNSET_GRILLE_MATCHES, WING_COMPANY_MATCHES };
    
    static String[][][] GOAL_ENTITIES = { PASTA_POMODORO_ENTITIES, SUNSET_GRILLE_ENTITIES, WING_COMPANY_ENTITIES };
    
    // By saving the menu results, we do not need to call OpenMenu each time we test
    static String[] MENU_RESULTS = {
    	"{\"response\":{\"api\":{\"status\":200,\"api_version\":\"2.1\",\"format\":\"json\",\"api_key\":\"51a5b6d4-dcb3-11e8-8d62-525400552a35\"},\"result\":{\"restaurant_info\":{\"restaurant_name\":\"Pasta Pomodoro\",\"brief_description\":\"Fresh. Local. Authentic. A collection of modern, family friendly Italian restaurants.\",\"full_description\":null,\"location_id\":\"\",\"mobile\":null,\"address_1\":\"146 Sunset Drive #A-3\",\"address_2\":\"\",\"city_town\":\"San Ramon\",\"state_province\":\"CA\",\"postal_code\":\"94583\",\"country\":\"US\",\"phone\":\"925-867-1407\",\"fax\":\"\",\"longitude\":\"-121.908762\",\"latitude\":\"37.7952771\",\"business_type\":\"Corporate\",\"utc_offset\":null,\"website_url\":\"http://www.pastapomodoro.com\"},\"environment_info\":{\"cuisine_type_primary\":\"Italian\",\"cuisine_type_secondary\":null,\"smoking_allowed\":null,\"takeout_available\":null,\"seating_qty\":null,\"max_group_size\":null,\"pets_allowed\":null,\"wheelchair_accessible\":null,\"age_level_preference\":null,\"dress_code\":null,\"delivery_available\":null,\"delivery_radius\":null,\"delivery_fee\":null,\"catering_available\":null,\"reservations\":null,\"alcohol_type\":null,\"music_type\":null},\"operating_days\":[],\"operating_days_printable\":[],\"logo_urls\":[],\"seating_locations\":[{\"seating_location\":\"indoor\"}],\"accepted_currencies\":[{\"accepted_currency\":\"USD\"}],\"parking\":false,\"settings\":{\"social\":{\"ChainID\":\"250\",\"facebook\":\"https://www.facebook.com/pastapomodoro\",\"twitter\":\"https://twitter.com/PastaPomodoro\",\"instagram\":\"\",\"pinterest\":\"\",\"youtube\":\"\",\"linkedin\":\"\"}},\"menus\":[{\"menu_name\":\"Dinner Menu\",\"menu_description\":\"\",\"menu_note\":\"\",\"currency_symbol\":\"USD\",\"language\":\"en\",\"menu_duration_name\":\"dinner\",\"menu_duration_time_start\":\"\",\"menu_duration_time_end\":\"\",\"menu_groups\":[{\"group_name\":\"appetizers\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Sautu00e9ed Shrimp\",\"menu_item_description\":\"gulf shrimp sautu00e9ed in tomato sauce with garlic and chili, served with grilled bread\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Caprese\",\"menu_item_description\":\"local tomatoes layered with fresh Belgioso mozzarella, fresh basil and a balsamic reduction\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Steamed Mussels\",\"menu_item_description\":\"OR CLAMS P.E.I. mussels or manila clams sautu00e9ed in white wine, fresh herbs, garlic and butter\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Polenta\",\"menu_item_description\":\"polenta rolled and stuffed with organic spinach and provolone, topped with brown butter, crispy sage and tomatoes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Bruschetta\",\"menu_item_description\":\"grilled rustic bread with tomatoes, fresh basil, garlic, extra virgin olive oil and salsa verde\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Garlic Bread\",\"menu_item_description\":\"garlic bread with spicy tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Minestrone Soup\",\"menu_item_description\":\"genovese style vegetable soup with salsa verde\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Tortellini Soup\",\"menu_item_description\":\"savory chicken broth, beef and pork tortellini, braised beef and organic spinach\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Tomato Basil Soup\",\"menu_item_description\":\"creamy tomato and fresh basil soup with house-made croutons\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"salads\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Mixed Greens\",\"menu_item_description\":\"organic mixed greens, tomatoes, garbanzo beans, pine nuts and gorgonzola, with garlic parmesan dressing\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Caesar\",\"menu_item_description\":\"organic romaine hearts with shaved asiago and crunchy garlic croutons\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Spinach\",\"menu_item_description\":\"organic spinach, cranberries, marinated red onions, pine nuts, crispy bacon and shaved asiago, in a balsamic vinaigrette\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chopped Salad\",\"menu_item_description\":\"italian chopped salad with bacon, salami, hard boiled egg, asiago and fresh local vegetables tossed with a garlic parmesan dressing\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Grilled Vegetable Salad\",\"menu_item_description\":\"grilled seasonal vegetables and organic mixed greens topped with a balsamic reduction\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"pasta\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Seafood Pasta\",\"menu_item_description\":\"spaghetti with P.E.I. mussels, manila clams, gulf shrimp, calamari and garlic in a light tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Penne Portobello\",\"menu_item_description\":\"portobello mushrooms, grilled chicken and italian sausage in a roasted garlic cream sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Gemelli\",\"menu_item_description\":\"gemelli pasta with grilled and smoked chicken, sun-dried tomatoes, mushrooms and cream\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Butternut Squash Ravioli\",\"menu_item_description\":\"roasted butternut squash ravioli with parmesan, brown butter, crispy sage and crumbled amaretti\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Shrimp & Asparagus Pasta\",\"menu_item_description\":\"pasta shells with gulf shrimp, asparagus, tomato, cream and shrimp stock\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ravioli\",\"menu_item_description\":\"ravioli filled with ricotta with your choice of gorgonzola cream OR pomodoro sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Pesto Fettuccine\",\"menu_item_description\":\"fettuccine with smoked chicken, cherry tomatoes and chili flakes in a creamy pesto with toasted pine nuts\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Lasagna\",\"menu_item_description\":\"traditional layered lasagna with house-made bolognese, italian sausage, organic spinach, roasted mushrooms and provolone\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Tri Color Capellini\",\"menu_item_description\":\"capellini with baby kale, cherry tomatoes, asparagus and capers with olive oil, garlic, chili flakes and shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sicilian Pasta\",\"menu_item_description\":\"fettuccine, red & yellow bell peppers, roasted eggplant, fresh mozzarella, fresh mint and a spicy tomato sauce, topped with shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"pizzas\",\"group_note\":\"@ select locations\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Chicken Pesto\",\"menu_item_description\":\"grilled chicken, basil pesto, mozzarella, crispy bacon, asiago and sun-dried tomatoes grilled chicken, basil pesto, mozzarella, crispy bacon and sun-dried tomatoes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Supreme\",\"menu_item_description\":\"italian sausage, salami, bacon, mozzarella and roasted crimini mushrooms over tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Margherita\",\"menu_item_description\":\"mozzarella, sliced tomatoes, fresh basil and tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Artichoke & Mushroom\",\"menu_item_description\":\"artichokes, mushrooms, tomatoes, red onions and mozzarella over tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Pepperoni\",\"menu_item_description\":\"pepperoni, mozzarella and zesty tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"entru00e9es\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Chicken Parmesan\",\"menu_item_description\":\"breaded chicken breast topped with tomato sauce and asiago, served with capellini pomodoro\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Marsala\",\"menu_item_description\":\"chicken breast pan-seared with mushrooms and marsala wine sauce, served with sautu00e9ed kale\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Salmon Cannellini\",\"menu_item_description\":\"sustainably raised grilled salmon over tuscan cannellini beans with sautu00e9ed baby kale\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Robertou2019s Chicken\",\"menu_item_description\":\"chicken breast pan-seared and sautu00e9ed with bacon, artichokes, lemon, fresh tomatoes and cream over capellini\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Grilled Chicken\",\"menu_item_description\":\"grilled chicken breast marinated in fresh garlic, thyme, lemon and spices, served with sautu00e9ed farro\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Roasted Veggie Pasta\",\"menu_item_description\":\"seasonal vegetable medley sautu00e9ed with fusilli and cherry tomatoes with your choice of olive oil and garlic OR spicy pomodoro sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"sides\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Asparagus\",\"menu_item_description\":\"grilled asparagus with olive oil and cracked black pepper topped with shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Brussels Sprouts\",\"menu_item_description\":\"brussels sprouts with onions, garlic, brown butter and fresh sage\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Broccoli\",\"menu_item_description\":\"broccoli sautu00e9ed with olive oil, garlic and chili flakes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Farro\",\"menu_item_description\":\"farro sautu00e9ed with olive oil, fresh garlic, organic spinach and roma tomatoes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"1\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"dessert & coffee\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Tiramisu\",\"menu_item_description\":\"ladyfingers, espresso and Kahlua layered with chocolate shavings and mascarpone zabaione\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Panna Cotta\",\"menu_item_description\":\"eggless vanilla bean custard with marinated berries\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chocolate Lava Cake\",\"menu_item_description\":\"warm chocolate cake with a melted chocolate center, chocolate sauce and fresh whipped cream\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Cannoli\",\"menu_item_description\":\"crispy shells with ricotta cream and mini chocolate chips, topped with chocolate sauce and powdered sugar\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"New York Style Cheesecake\",\"menu_item_description\":\"cheesecake, graham cracker crust with marinated berries\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sorbet / Ice Cream\",\"menu_item_description\":\"artisan mixed berry sorbet or vanilla ice cream\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]}]},{\"menu_name\":\"Lunch Menu\",\"menu_description\":\"All Lunch Menu items include choice of: Zuppa di Minestrone, Caesar or Insalata Mista\",\"menu_note\":\"\",\"currency_symbol\":\"USD\",\"language\":\"en\",\"menu_duration_name\":\"lunch\",\"menu_duration_time_start\":\"\",\"menu_duration_time_end\":\"\",\"menu_groups\":[{\"group_name\":\"Lunch Menu\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"HEALTHY FISH\",\"menu_item_description\":\"whole grain fusilli sautu00e9ed with select pieces of sustainably raised salmon, fresh asparagus, tomato sauce and fresh basil\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"RAVIOLI\",\"menu_item_description\":\"ravioli filled with ricotta with your choice of gorgonzola cream OR pomodoro sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"RIGATONI\",\"menu_item_description\":\"rigatoni baked with a creamy bolognese sauce, provolone and parmesan cheese\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN WITH BROCCOLI\",\"menu_item_description\":\"grilled chicken breast served over orecchiette pasta and sautu00e9ed broccoli tossed in a lemon garlic cream sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CAPELLINI\",\"menu_item_description\":\"choice of: pomodoro sauce with garlic, fresh basil and olive oil OR basil pesto with shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"HEALTHY CHICKEN\",\"menu_item_description\":\"whole grain fusilli sautu00e9ed with sliced grilled chicken, fresh zucchini, tomato sauce and fresh basil\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"BOLOGNESE\",\"menu_item_description\":\"rigatoni tossed with rich beef, pork and porcini mushroom sauce, topped with shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"TRI COLOR CAPELLINI\",\"menu_item_description\":\"capellini with baby kale, cherry tomatoes, asparagus and capers with olive oil, garlic, chili flakes and shaved asiago\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"MOZZARELLA PANINI\",\"menu_item_description\":\"mozzarella, vine tomatoes, sun-dried tomatoes and basil pesto\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN PANINI\",\"menu_item_description\":\"grilled chicken, provolone, organic arugula drizzled with balsamic vinegar, sun-dried tomatoes and basil pesto\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"ARTICHOKE & MUSHROOM PIZZA\",\"menu_item_description\":\"artichokes, mushrooms, tomatoes, red onions and mozzarella over tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"CHICKEN PESTO PIZZA\",\"menu_item_description\":\"grilled chicken, basil pesto, mozzarella, crispy bacon, asiago and sun-dried tomatoes\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"MARGHERITA PIZZA\",\"menu_item_description\":\"mozzarella, sliced tomatoes, fresh basil and tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"PEPPERONI PIZZA\",\"menu_item_description\":\"pepperoni, mozzarella and zesty tomato sauce\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]}]}],\"id\":\"Umsxc2dNS1BCdUkrdW5QMDdaRlBSSmpLNm5zckNLNlQ3cUxWalQrZWN2WlNFcFhRNEpOb0IwZDlRV2FTaEc0Zg==\"}}}",
    	"{\"response\":{\"api\":{\"status\":200,\"api_version\":\"2.1\",\"format\":\"json\",\"api_key\":\"51a5b6d4-dcb3-11e8-8d62-525400552a35\"},\"result\":{\"restaurant_info\":{\"restaurant_name\":\"Sunset Grille\",\"brief_description\":\"Sunset Grille has stunning views of the ocean, authentic seafood, and drinks right on St. Augustine Beach.\",\"full_description\":\"\",\"location_id\":\"\",\"mobile\":\"0\",\"address_1\":\"421 A1A Beach Blvd\",\"address_2\":\"\",\"city_town\":\"St. Augustine\",\"state_province\":\"FL\",\"postal_code\":\"32084\",\"country\":\"US\",\"phone\":\"(904) 471-5555\",\"fax\":\"\",\"longitude\":\"-81.2676190\",\"latitude\":\"29.8544830\",\"business_type\":\"independent\",\"utc_offset\":\"-5.00\",\"website_url\":\"http:\\/\\/www.sunsetgrillea1a.com\"},\"environment_info\":{\"cuisine_type_primary\":\"Seafood\",\"cuisine_type_secondary\":\"\",\"smoking_allowed\":\"0\",\"takeout_available\":\"0\",\"seating_qty\":null,\"max_group_size\":null,\"pets_allowed\":\"0\",\"wheelchair_accessible\":\"0\",\"age_level_preference\":\"\",\"dress_code\":\"\",\"delivery_available\":\"0\",\"delivery_radius\":null,\"delivery_fee\":null,\"catering_available\":\"0\",\"reservations\":\"\",\"alcohol_type\":\"\",\"music_type\":\"\"},\"operating_days\":[{\"day_of_week\":\"1\",\"open_time\":\"11:00:00\",\"close_time\":\"02:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"2:00AM\",\"day\":\"Monday\",\"day_short\":\"Mon\"},{\"day_of_week\":\"2\",\"open_time\":\"11:00:00\",\"close_time\":\"02:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"2:00AM\",\"day\":\"Tuesday\",\"day_short\":\"Tue\"},{\"day_of_week\":\"3\",\"open_time\":\"11:00:00\",\"close_time\":\"02:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"2:00AM\",\"day\":\"Wednesday\",\"day_short\":\"Wed\"},{\"day_of_week\":\"4\",\"open_time\":\"11:00:00\",\"close_time\":\"02:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"2:00AM\",\"day\":\"Thursday\",\"day_short\":\"Thu\"},{\"day_of_week\":\"5\",\"open_time\":\"11:00:00\",\"close_time\":\"02:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"2:00AM\",\"day\":\"Friday\",\"day_short\":\"Fri\"},{\"day_of_week\":\"6\",\"open_time\":\"11:00:00\",\"close_time\":\"02:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"2:00AM\",\"day\":\"Saturday\",\"day_short\":\"Sat\"},{\"day_of_week\":\"7\",\"open_time\":\"11:00:00\",\"close_time\":\"02:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"2:00AM\",\"day\":\"Sunday\",\"day_short\":\"Sun\"}],\"operating_days_printable\":{\"1\":{\"day_name\":\"Monday\",\"display\":\"11:00AM-2:00AM\"},\"2\":{\"day_name\":\"Tuesday\",\"display\":\"11:00AM-2:00AM\"},\"3\":{\"day_name\":\"Wednesday\",\"display\":\"11:00AM-2:00AM\"},\"4\":{\"day_name\":\"Thursday\",\"display\":\"11:00AM-2:00AM\"},\"5\":{\"day_name\":\"Friday\",\"display\":\"11:00AM-2:00AM\"},\"6\":{\"day_name\":\"Saturday\",\"display\":\"11:00AM-2:00AM\"},\"7\":{\"day_name\":\"Sunday\",\"display\":\"11:00AM-2:00AM\"}},\"logo_urls\":[],\"seating_locations\":[{\"seating_location\":\"indoor\"},{\"seating_location\":\"deck\"},{\"seating_location\":\"rooftop\"}],\"accepted_currencies\":[{\"accepted_currency\":\"USD\"}],\"parking\":{\"street_free\":\"0\",\"street_metered\":\"0\",\"private_lot\":\"0\",\"garage\":\"0\",\"valet\":\"0\"},\"settings\":{\"social\":false},\"menus\":[{\"menu_name\":\"Lunch Menu\",\"menu_description\":\"\",\"menu_note\":\"\",\"currency_symbol\":\"\",\"language\":\"\",\"menu_duration_name\":\"lunch\",\"menu_duration_time_start\":\"\",\"menu_duration_time_end\":\"\",\"menu_groups\":[{\"group_name\":\"Soups & Appetizers\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Soups\",\"menu_item_description\":\"N.E. Clam Chowder, Crab Bisque, Minorcan Seafood Chowder(GF), New Minorcan (1\\/2 N.E. and 1\\/2 Minorcan)\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Cup\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.99\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bowl\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.99\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Crab Legs\",\"menu_item_description\":\"1 1\\/2 lb. filled with succulent crabmeat, served with drawn butter - Market Price\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Gulf Coast Peel \\u2018n Eat Shrimp\",\"menu_item_description\":\"served hot or cold\",\"menu_item_price\":\"11.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Steamed Oysters\",\"menu_item_description\":\"Fresh select oysters steamed and served with drawn butter and cocktail sauce - (When Available) market\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Calamari\",\"menu_item_description\":\"With homemade marinara sauce\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Coconut Shrimp\",\"menu_item_description\":\"Shrimp coated with fresh coconut and deep fried, served with a Caribbean pi\\u00f1a colada dipping sauce\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Datil Pepper Wings\",\"menu_item_description\":\"1 1\\/2 lbs. of jumbo wings mild, medium or hot. Our sauce will leave you wanting more! Our mild has a kick!\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Traditional Buffalo Wings\",\"menu_item_description\":\"1 1\\/2 lbs. of jumbo wings coated with a traditional HOT buffalo wing sauce\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Tenders\",\"menu_item_description\":\"All white meat chicken served with honey mustard\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Artichoke\",\"menu_item_description\":\"Artichoke hearts quartered, lightly breaded and deep fried to a golden brown and served with marinara\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Mozzarella Sticks\",\"menu_item_description\":\"With homemade marinara sauce\",\"menu_item_price\":\"6.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Spinach, Artichoke & Crab Dip\",\"menu_item_description\":\"Fresh spinach, artichokes, crabmeat, 3 cheeses and baked until bubbling\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Nacho Diablo Dip\",\"menu_item_description\":\"A savory bean dip with cheddar cheese baked until bubbling, garnished with sour cream and jalape\\u00f1os and served with tortilla chips and salsa\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Seafood Pizza\",\"menu_item_description\":\"Crabmeat, fish, scallops, shrimp and a mushroom b\\u00e9chamel sauce topped with melted mozzarella\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Smoked Mahi Fish Dip\",\"menu_item_description\":\"Served with crunchy pita chips.\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fresh Fish Bites\",\"menu_item_description\":\"Bite size Mahi Mahi and Grouper lightly breaded, deep fried and served with our homemade tartar sauce.\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Oysters\",\"menu_item_description\":\"1\\/2 dozen oysters topped with fresh spinach, artichokes, crabmeat and three cheese blend, then broiled.\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Pickles\",\"menu_item_description\":\"Served with chipotle ranch\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Award Winning Chowder Fries\",\"menu_item_description\":\"Crispy fries topped with our award winning clam chowder, cheddar and bacon.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Bowl\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"8.99\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Cup\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.99\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Buffalo Chicken Dip\",\"menu_item_description\":\"Buffalo chicken diced, combined with 4 cheeses & baked. A creamy AND spicy combination!\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ahi Tuna App\",\"menu_item_description\":\"Sesame seared and sliced served on a bed of fried spinach and finished with a soy ginger glaze\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Sandwiches & Burgers\",\"group_note\":\"\",\"group_description\":\"All of our burgers are 1\\/2 lb. 100% Black Angus beef. All burgers and sandwiches are served with our crispy french fries.\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"The Ultimate Fish Sandwich\",\"menu_item_description\":\"Fish filet deep fried and topped with melted American cheese, grilled onions, shredded cabbage, special sauce and tomatoes\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The Ultimate Chicken Sandwich\",\"menu_item_description\":\"Grilled or blackened with bacon, melted Swiss, beer battered onion rings, lettuce and tomato served with ranch dressing\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The Ultimate Beach Burger\",\"menu_item_description\":\"A 1\\/2 lb. burger topped with cabbage, tomato & our kicked up citrus jalapeno special sauce\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The Ultimate Steak Sandwich\",\"menu_item_description\":\"Sliced filet mignon, grilled onions, bacon and melted provolone cheese on a toasted ciabatta roll. The size of our hand cut filet mignon may vary therefore we cannot guarantee a requested temperature\",\"menu_item_price\":\"12.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Maui Wowie Mahi Sandwich\",\"menu_item_description\":\"Grilled or blackened Mahi topped with finely shredded cabbage, our \\u201ckicked up\\u201d citrus jalape\\u00f1o special sauce and tomato\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Shrimp Po\\u2019 Boy\",\"menu_item_description\":\"Shrimp battered and deep fried to a golden brown served with chipotle ranch\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Fish Filet Sandwich\",\"menu_item_description\":\"A white flaky filet lightly coated and deep fried to a golden brown\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Mahi Mahi Melt\",\"menu_item_description\":\"Grilled or blackened topped with melted Swiss, lettuce and tomato\",\"menu_item_price\":\"11.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Seafood Salad Melt\",\"menu_item_description\":\"Our new seafood salad includes lobster, shrimp and crabmeat and is served on a grilled ciabatta bun with melted cheese.\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sea BLT\",\"menu_item_description\":\"Fried catfish served on a grilled ciabatta bun with bacon lettuce and tomato served with homemade tartar sauce.\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"A1A Steakhouse Burger\",\"menu_item_description\":\"Topped with our homemade Vidalia onion and steak sauce pickle relish and crumbled bleu cheese\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Frickle Burger\",\"menu_item_description\":\"1\\/2 lb. burger topped with fried pickles and chipotle ranch\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Paradise Burger\",\"menu_item_description\":\"American cheese, lettuce and tomato, Heinz 57 and french fried potatoes; Jimmy\\u2019s favorite\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The BIG Daddy Burger\",\"menu_item_description\":\"Open wide! This burger is stacked with bacon, beer battered onion rings and American cheese\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Grilled Cheeseburger\",\"menu_item_description\":\"A juicy burger with four slices of melted American cheese on grilled bread\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"All-American Burger\",\"menu_item_description\":\"1\\/2 lb. 100% Black Angus burger topped with lettuce and tomato\",\"menu_item_price\":\"8.49\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Philly Cheesesteak\",\"menu_item_description\":\"Thinly shaved steak grilled and topped with melted provolone \\u201cwit or wit out\\u201d grilled onions\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Pita Darios\",\"menu_item_description\":\"Sliced chicken breast, fresh spinach, feta cheese, oregano & homemade Kalamata olive dressing served on a grilled pita\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Homemade Chicken Salad Pita\",\"menu_item_description\":\"A generous serving of all white meat chicken salad on a grilled pita, served with lettuce and tomato\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Turkey Sandwich\",\"menu_item_description\":\"Hand carved oven roasted turkey served on a toasted bun with lettuce, tomato and housemade avocado ranch dressing\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"add bacon\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"0.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"add cheese\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"0.50\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"Tacos and Mas!\",\"group_note\":\"\",\"group_description\":\"All tacos served with beans and rice.\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Chicken Tacos\",\"menu_item_description\":\"A mix of seasoned chicken, cheddar, peppers and onions served on two warm flour tortillas layered with our bean and cheese dip, cabbage, crispy corn tortilla strips and cheddar and topped with chipotle ranch.\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Mahi Tacos\",\"menu_item_description\":\"Seasoned mahi served on two warm flour tortillas layered with our bean and cheese dip, cabbage, crispy corn tortilla strips and topped with chipotle ranch\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ahi Tuna Tacos\",\"menu_item_description\":\"Sesame seared rare and topped with sweet Asian slaw and spicy cusabi sauce\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Fish Tacos\",\"menu_item_description\":\"Lightly breaded fish deep fried to a golden brown, served on two warm flour tortillas layered with our bean and cheese dip, cabbage, crispy corn tortilla strips and topped with chipotle ranch\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fish \\u2018n Chips Tacos\",\"menu_item_description\":\"Lightly breaded fish deep friend to a golden brown served on two warm flour tortillas topped with cabbage, crispy corn tortilla strips and our housemade malt vinegar tartar sauce, served with French fries\",\"menu_item_price\":\"11.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Quesadilla\",\"menu_item_description\":\"Grilled flour tortilla stuffed with a mix of seasoned chicken, mozzarella, cheddar, peppers and onions\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Seafood Enchilada\",\"menu_item_description\":\"Flour tortilla stuffed with fish, shrimp, scallops and cheddar cheese topped with our homemade enchilada sauce.\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Salads\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Sunset Salad\",\"menu_item_description\":\"Mixed greens, cucumbers, roasted red peppers, sunflower seeds, dried cranberries and Mandarin oranges\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Caesar Salad\",\"menu_item_description\":\"Crisp romaine lettuce tossed with homemade Caesar dressing, croutons and Parmesan\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Goat Cheese Salad\",\"menu_item_description\":\"Fried goat cheese medallions on a bed of mixed greens, dried cranberries, Mandarin oranges, sunflower seeds, artichoke hearts and roasted red peppers served with a homemade Key lime vinaigrette\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Greek Salad\",\"menu_item_description\":\"Feta cheese, artichoke hearts, red peppers, cucumbers and oregano on a bed of mixed greens served with our homemade Kalamata olive vinaigrette\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ultimate Seafood Salad\",\"menu_item_description\":\"Our new seafood salad includes lobster, shrimp and crabmeat and is served over mixed greens, cucumbers, red peppers and dried cranberries served with our Key lime vinaigrette.\",\"menu_item_price\":\"14.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Salad Sunset Salad\",\"menu_item_description\":\"Our homemade chicken salad served over mixed greens, cucumbers, red peppers, sunflower seeds, dried cranberries and Mandarin oranges\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Steak & Bleu Cheese Salad\",\"menu_item_description\":\"Filet mignon medallions saut\\u00e9ed with mushrooms and onions, topped with crumbled Gorgonzola served on a bed of mixed greens and red peppers with homemade bleu cheese dressing\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Baja Fresh Mahi Ensalada\",\"menu_item_description\":\"8 oz. Mahi filet grilled or blackened, served on a bed of mixed greens with beans, rice and sweet corn topped with crumbled tortilla chips and served with fresh salsa and our homemade avocado ranch dressing\",\"menu_item_price\":\"12.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ahi Tuna Salad\",\"menu_item_description\":\"Sesame seared and sliced served on top of a bed of mixed greens and sweet Asian slaw drizzled with Cusabi dressing\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Seafood Bruschetta Salad\",\"menu_item_description\":\"Chilled rock shrimp and scallops combined with chopped tomatoes, artichoke hearts and fresh basil and tossed in a balsamic vinaigrette served on a bed of mixed greens and topped with crumbled feta cheese\",\"menu_item_price\":\"11.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Entr\\u00e9es\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Crab Legs\",\"menu_item_description\":\"1 1\\/2 lbs. of snow crab filled with succulent meat served with drawn butter - Market Price\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Shrimp Platter\",\"menu_item_description\":\"Grilled, blackened or fried served with two sides\",\"menu_item_price\":\"14.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Coconut Crusted Shrimp\",\"menu_item_description\":\"Jumbo shrimp coated with fresh coconut and deep fried served with a Caribbean Pi\\u00f1a Colada dipping sauce\",\"menu_item_price\":\"15.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fish \\u2018n Chips\",\"menu_item_description\":\"A white flaky filet lightly coated and deep fried to a golden brown served with coleslaw and malt vinegar\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Seafood Lover\\u2019s\",\"menu_item_description\":\"Filet of fish, shrimp and scallops lightly breaded and deep fried to a golden brown\",\"menu_item_price\":\"19.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Award-Winning Baby Back Ribs\",\"menu_item_description\":\"Dry rubbed, slow roasted and basted with BBQ sauce. Oh so tender!\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Full Rack\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"22.99\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Half Rack\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"13.99\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"Homemade Desserts\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Homemade Key Lime Pie\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Homemade Peanut Butter Pie\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The Ultimate Homemade Brownie\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sweet Potato Pie\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"On the Side\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"French Fries\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Rice\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Beans & Rice\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Coleslaw\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"French Green Beans\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sweet Corn\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Side Salad\",\"menu_item_description\":\"(available as a choice for entree\\u2019s. If substituting a salad for french fries with a sandwich, there is an additional charge.)\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Children\\u2019s Menu\",\"group_note\":\"Served with a choice of sides: French fries or sweet corn (Pasta entrees do not include a choice of side items)\",\"group_description\":\"(ages 10 & under please)\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Pasta & Sauce\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Grilled Cheese\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fish \\u2018n Chips\",\"menu_item_description\":\"\",\"menu_item_price\":\"5.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Mac \\u2018n Cheese\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Tenders\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ravioli & Sauce\",\"menu_item_description\":\"\",\"menu_item_price\":\"3.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Corn Dog Nuggets\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Shrimp\",\"menu_item_description\":\"\",\"menu_item_price\":\"6.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chocolate Milk\",\"menu_item_description\":\"\",\"menu_item_price\":\"2.75\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]}]},{\"menu_name\":\"Dinner Menu\",\"menu_description\":\"\",\"menu_note\":\"\",\"currency_symbol\":\"USD\",\"language\":\"en\",\"menu_duration_name\":\"dinner\",\"menu_duration_time_start\":\"\",\"menu_duration_time_end\":\"\",\"menu_groups\":[{\"group_name\":\"Soups & Appetizers\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Soup\",\"menu_item_description\":\"England Clam Chowder, Crab Bisque, Minorcan Seafood Chowder, New Minorcan (1\\/2 N.E. 1\\/2 Minorcan)\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Cup\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.99\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bowl\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.99\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Oysters\",\"menu_item_description\":\"\\u00bd dozen oysters topped with fresh spinach, artichokes, crabmeat & three cheeses, then broiled.\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Gulf Coast Peel \\u2018n Eat Shrimp\",\"menu_item_description\":\"\\u00bd lb. served hot or cold.\",\"menu_item_price\":\"11.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Spinach, Artichoke & Crab Dip\",\"menu_item_description\":\"Fresh spinach, artichokes, crabmeat and 3 cheeses, baked \\u2019til bubbling.\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ahi Tuna App\",\"menu_item_description\":\"Seared, sliced and finished with a light soy ginger glaze. Served rare only. Great for sharing!\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Seared Scallops\",\"menu_item_description\":\"Served over a bed of fresh fried spinach with a soy ginger glaze and wasabi.\",\"menu_item_price\":\"11.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Coconut Crusted Shrimp\",\"menu_item_description\":\"Shrimp coated with fresh coconut, deep fried and served with our signature Caribbean pi\\u00f1a colada dipping sauce.\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Calamari\",\"menu_item_description\":\"With homemade marinara sauce.\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fresh Fish Bites\",\"menu_item_description\":\"A combination of mahi and grouper lightly breaded and deep fried to a golden brown.\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Datil Pepper Wings\",\"menu_item_description\":\"1 \\u00bd lbs. of jumbo wings, mild, medium or hot. Our sauce will leave you wanting more! Our mild has a kick!\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Traditional Buffalo Wings\",\"menu_item_description\":\"1 \\u00bd lbs. of jumbo wings coated with our homemade traditional HOT buffalo wing sauce.\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Buffalo Chicken Dip\",\"menu_item_description\":\"Creamy and spicy served with corn tortilla chips.\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Nacho Diablo Dip\",\"menu_item_description\":\"A creamy bean dip with melted cheddar cheese and jalape\\u00f1os, baked until bubbling and topped with sour cream. Served with corn tortilla chips and salsa.\",\"menu_item_price\":\"8.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Seafood Bruschetta\",\"menu_item_description\":\"Shrimp, scallops and chopped tomatoes with a balsamic glaze.\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Mozzarella Sticks\",\"menu_item_description\":\"With homemade marinara sauce.\",\"menu_item_price\":\"6.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Pickles\",\"menu_item_description\":\"With chipotle ranch.\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chowder Fries\",\"menu_item_description\":\"Crispy fries topped with our award winning clam chowder, cheddar and bacon.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Cup\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.99\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bowl\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"8.99\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"Sandwiches & Burgers\",\"group_note\":\"\",\"group_description\":\"All burgers and sandwiches are served with our crispy French fries.\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"A1A Steakhouse Burger\",\"menu_item_description\":\"Topped with our homemade vidalia onion steak sauce pickle relish and crumbled bleu cheese.\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"All-American Burger\",\"menu_item_description\":\"\\u00bd lb. 100% Black Angus burger topped with lettuce and tomato.\",\"menu_item_price\":\"8.49\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The Ultimate Chicken Sandwich\",\"menu_item_description\":\"Grilled or blackened, topped with bacon, melted Swiss cheese, beer battered onion rings, lettuce, tomato and ranch dressing.\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The Ultimate Steak Sandwich\",\"menu_item_description\":\"Filet mignon, melted provolone, grilled onions and bacon on toasted ciabatta bread. The size of our hand-cut filet mignon may vary, therefore we cannot guarantee a requested temperature.\",\"menu_item_price\":\"12.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The Ultimate Fish Sandwich\",\"menu_item_description\":\"A filet of white flaky fish deep fried and topped with melted American cheese, grilled onions, shredded cabbage, special sauce and tomatoes.\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Maui Wowie Mahi Sandwich\",\"menu_item_description\":\"Grilled or blackened topped with shredded cabbage, tomato and our kicked up citrus jalape\\u00f1o sauce.\",\"menu_item_price\":\"10.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Salads\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Sunset Salad\",\"menu_item_description\":\"Mixed greens, cucumbers, red peppers, sunflower seeds, dried cranberries and Mandarin oranges.\",\"menu_item_price\":\"7.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Add To Your Salads\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Grilled\\/blackened steak, Mahi Mahi or shrimp\",\"menu_item_option_additional_cost\":\"7.00\",\"selected\":\"0\"},{\"menu_item_option_name\":\"Grilled\\/blackened chicken or shrimp\",\"menu_item_option_additional_cost\":\"6.00\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Goat Cheese Salad\",\"menu_item_description\":\"Fried goat cheese medallions served on a bed of fresh mixed greens, dried cranberries, Mandarin oranges, sunflower seeds, marinated artichoke hearts and red peppers served with a homemade Key lime vinaigrette.\",\"menu_item_price\":\"9.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Add To Your Salads\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Grilled\\/blackened steak, Mahi Mahi or shrimp\",\"menu_item_option_additional_cost\":\"7.00\",\"selected\":\"0\"},{\"menu_item_option_name\":\"Grilled\\/blackened chicken or shrimp\",\"menu_item_option_additional_cost\":\"6.00\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Steak & Bleu Cheese Salad\",\"menu_item_description\":\"Filet mignon medallions saut\\u00e9ed with mushrooms and onions, topped with crumbled Gorgonzola, served on a bed of fresh mixed greens and red peppers with homemade bleu cheese dressing. The size of our hand cut filet may vary, therefore we cannot guarantee a requested temperature.\",\"menu_item_price\":\"12.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Baja Fresh Mahi Mahi Ensalada\",\"menu_item_description\":\"8 oz. Mahi Mahi filet grilled or blackened, served on a bed of mixed greens with beans, rice and sweet corn topped with crumbled tortilla chips and served with fresh salsa and our homemade avocado ranch dressing.\",\"menu_item_price\":\"12.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ultimate Seafood Salad\",\"menu_item_description\":\"Our new seafood salad includes lobster, shrimp and crabmeat and is served over mixed greens, cucumbers, red peppers and dried cranberries served with our Key lime vinaigrette.\",\"menu_item_price\":\"14.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ahi Tuna Salad\",\"menu_item_description\":\"Sesame seared and sliced served on top of a bed of mixed greens and sweet Asian slaw drizzled with Cusabi dressing.\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Seafood Bruschetta Salad\",\"menu_item_description\":\"Chilled rock shrimp and scallops combined with chopped tomatoes, artichoke hearts and fresh basil and tossed in a balsamic vinaigrette served on a bed of mixed greens and topped with crumbled feta cheese.\",\"menu_item_price\":\"11.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Entr\\u00e9es - Surf\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Award-Winning Shrimp and Grits\",\"menu_item_description\":\"Shrimp saut\\u00e9ed with butter, garlic, tomatoes, scallions and bacon, served over cheesy fried grits.\",\"menu_item_price\":\"18.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fish 'n Chips\",\"menu_item_description\":\"A white, flaky fillet lightly coated and deep fried to a golden brown, served with cole slaw and malt vinegar.\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Shrimp Platter\",\"menu_item_description\":\"Grilled, blackened or fried shrimp, served with your choice of two sides.\",\"menu_item_price\":\"14.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Coconut Shrimp\",\"menu_item_description\":\"Shrimp coated with fresh coconut, deep fried and served with our signature Caribbean pi\\u00f1a colada dipping sauce, served with your choice of two sides.\",\"menu_item_price\":\"15.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Seafood Lover's\",\"menu_item_description\":\"Filet of fish, shrimp and scallops served with your choice of two sides.\",\"menu_item_price\":\"19.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Crab Legs\",\"menu_item_description\":\"1 \\u00bd lbs. of snow crab \\u2013 filled with succulent meat. Served with your choice of two sides. Market Price.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fresh Tuna\",\"menu_item_description\":\"Seared and finished with a light soy ginger glaze, served on a bed of saut\\u00e9ed fresh spinach with sesame ginger rice. Garnished with fried ginger and wasabi. Suggested temperature rare to medium rare.\",\"menu_item_price\":\"24.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ahi Tuna Tacos\",\"menu_item_description\":\"Sesame seared rare and topped with sweet Asian slaw and a spicy cusabi sauce. Served with beans and rice.\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Grouper\",\"menu_item_description\":\"Fresh grouper, seared or blackened, topped with our creamy shrimp and crab sauce, served with your choice of two sides. Available with Mahi.\",\"menu_item_price\":\"22.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Island Grouper\",\"menu_item_description\":\"Fresh grouper, seared or Jerk seasoned, topped with our passion fruit buerre blanc & toasted macadamia nuts, served with your choice of two sides. Available with Mahi. Grilled or blackened.\",\"menu_item_price\":\"22.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Macadamia Crusted Mahi Mahi\",\"menu_item_description\":\"Coated with roasted macadamia nuts and topped with Grand Marnier Passion Fruit Buerre Blanc, served with your choice of two sides. Available with Grouper.\",\"menu_item_price\":\"22.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Entr\\u00e9es - Turf\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Steak Augustine\",\"menu_item_description\":\"Sliced filet mignon saut\\u00e9ed with mushrooms and onions served over a toasted ciabatta roll and topped with our balsamic demi-glace and crumbled bleu cheese.\",\"menu_item_price\":\"15.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"12 oz. New York Strip\",\"menu_item_description\":\"Hand cut, served with our demi glace and your choice of two sides. Ask your server about adding shrimp or crab legs to your steak entr\\u00e9e!\",\"menu_item_price\":\"22.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Award-Winning Baby Back Ribs\",\"menu_item_description\":\"Dry rubbed, slow roasted and basted with BBQ sauce; the meat is oh-so-tender! Served with your choice of two sides.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Full Rack\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"22.99\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Half Rack\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"13.99\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Parmesan\",\"menu_item_description\":\"Juicy chicken breast lightly breaded, saut\\u00e9ed and topped with marinara sauce and melted mozzarella cheese, served with cheese ravioli.\",\"menu_item_price\":\"13.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Pasta\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Award-Winning Lobster Ravioli\",\"menu_item_description\":\"Pasta pockets stuffed with lobster, saut\\u00e9ed in a creamy shrimp and crab sauce. Lobster, crab and shrimp, all in one delectable bite!\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Full order\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"20.99\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Half order\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"12.99\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Lobster Mac and Cheese\",\"menu_item_description\":\"Cold water lobster and 3 cheeses topped with toasted bread crumbs.\",\"menu_item_price\":\"23.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Shrimp & Scallops\",\"menu_item_description\":\"Shrimp and scallops saut\\u00e9ed with garlic, white wine, pesto, bacon, mushrooms, tomatoes, scallions and parsley, tossed with mozzarella, Parmesan and a touch of cream over penne pasta.\",\"menu_item_price\":\"19.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Shrimp & Scallops\",\"menu_item_description\":\"Shrimp and scallops saut\\u00e9ed with garlic, white wine, pesto, bacon, mushrooms, tomatoes, scallions and parsley, tossed with mozzarella, Parmesan and a touch of cream over penne pasta.\",\"menu_item_price\":\"19.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Spicy Shrimp Sriracha\",\"menu_item_description\":\"Rice noodles tossed with shrimp saut\\u00e9ed with chili oil, sriracha, green onions, cabbage, julienned carrots and red peppers.\",\"menu_item_price\":\"15.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Tortellini Alfredo\",\"menu_item_description\":\"Fresh Tortellini tossed with a creamy Parmesan sauce.\",\"menu_item_price\":\"12.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Addition\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Add grilled or blackened chicken or shrimp\",\"menu_item_option_additional_cost\":\"6.00\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Sides\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Baked potato\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Load Up your baked potato:\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Add cheese\",\"menu_item_option_additional_cost\":\"1.50\",\"selected\":\"0\"},{\"menu_item_option_name\":\"Add cheese & bacon\",\"menu_item_option_additional_cost\":\"2.50\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Baked Sweet Potato\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"French Green Beans\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Grit Cakes with Spinach & Cheese\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sweet Corn\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Beans & Rice\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Cole Slaw\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Side Salad\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Desserts\",\"group_note\":\"\",\"group_description\":\"Our homemade desserts will make you happy you saved room!\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Key Lime Pie\",\"menu_item_description\":\"Sweet, tangy, light and airy \\u2013 a homemade Florida classic.\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Peanut Butter Pie\",\"menu_item_description\":\"Creamy peanut butter and chocolate come together in our homemade recipe you can\\u2019t buy in a store.\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"The Ultimate Brownie\",\"menu_item_description\":\"Three layers of chocolate goodness homemade and served warm with a gooey center.\",\"menu_item_price\":\"6.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Ice Cream Smoothies\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Chocolate\",\"menu_item_description\":\"\",\"menu_item_price\":\"5.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Vanilla\",\"menu_item_description\":\"\",\"menu_item_price\":\"5.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Pi\\u00f1a Colada\",\"menu_item_description\":\"\",\"menu_item_price\":\"5.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Strawberry\",\"menu_item_description\":\"\",\"menu_item_price\":\"5.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Passion Fruit\",\"menu_item_description\":\"\",\"menu_item_price\":\"5.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Frozen Delights and Sunset Specialties\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Captivating Coladas\",\"menu_item_description\":\"Pi\\u00f1a, strawberry or passion fruit.\",\"menu_item_price\":\"7.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Delectable Daiquiris\",\"menu_item_description\":\"Strawberry or passion fruit.\",\"menu_item_price\":\"7.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Marvelous Margaritas\",\"menu_item_description\":\"Original, gold, strawberry or blue moon.\",\"menu_item_price\":\"7.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Frozen Mudslide\",\"menu_item_description\":\"Island Oasis ice cream, Kahl\\u00faa, vodka and Bailey\\u2019s Irish Cream.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Mucho Mojitos\",\"menu_item_description\":\"A classic Cuban muddler. Bacardi Light rum, mashed limes, mint and sugar with a splash of club soda, all garnished with fresh mint.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Try it in your favorite flavor!\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Mango, pomegranate, strawberry or raspberry.\",\"menu_item_option_additional_cost\":null,\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Cocktails\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Original Hawaiian Mai Tai\",\"menu_item_description\":\"Light rum, Triple Sec, sour and pineapple with a Myers float.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Bikini\",\"menu_item_description\":\"Malibu rum, midori and pineapple juice.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Rum Runner on the Rocks\",\"menu_item_description\":\"Light rum, dark rum, banana liquor, blackberry brandy, grenadine, pineapple and orange juices, garnished with cherries.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Try Our 4 Orange Vodka\",\"menu_item_description\":\"Premium orange flavored vodka distilled from Florida oranges.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sunset Lemonade\",\"menu_item_description\":\"4 orange vodka, lemonade and cranberry juice.\",\"menu_item_price\":\"7.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Miami Mojito\",\"menu_item_description\":\"4 orange vodka, mint leaves, lime juice, passion fruit puree & club soda.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Pink Flamingo\",\"menu_item_description\":\"4 orange vodka, triple sec, lime juice & cranberry juice.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Martinis\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Sunset Bella-Tini\",\"menu_item_description\":\"Award winning! Absolut vodka, Cointreau orange liqueur, freshly squeezed limes, cranberry and passion fruit juice.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Cucumber Martini\",\"menu_item_description\":\"Organic Cucumber vodka, fresh cucumber puree and a splash of simple syrup.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Key Lime Martini\",\"menu_item_description\":\"A Florida favorite! Absolut vodka, vanilla liqueur, melon liqueur, lime juice and cream, rimmed with graham crackers and a lime wedge.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ultimate Cosmos\",\"menu_item_description\":\"Absolut Citron, Citronage orange liquor, lime and cranberry juices.\",\"menu_item_price\":\"8.00\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Wine\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Lindeman's Bin 65 Chardonnay (Australia)\",\"menu_item_description\":\"A delightful chardonnay nose with touches of melon,tropical fruit, peaches and nectarines. Full-bodied and soft with bright flavors of pineapple and fig. The palate is creamy and smooth with touches of spice that define the long, stylish finish. Ideally suited to salad, chicken and seafood.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"6.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"18.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Hogue Chardonnay (Washington State)\",\"menu_item_description\":\"Fresh pear and apple aromatics with toasty notes and creamy oak spice, vanilla bean and lemon meringue; pairs well with our Lobster Ravioli, Tortellini Alfredo or Caribbean Mahi Mahi.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"7.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"24.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Kendall Jackson Chardonnay (California)\",\"menu_item_description\":\"Green apples, peaches, honey and vanilla balance flavors of baked apples, lemon, pineapple and mango. A hint of spiced nuts and toasted oak round out the lingering finish.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"8.00\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"28.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Forestville Pino Grigio (California)\",\"menu_item_description\":\"Slightly dry with citrus, melon and peach flavors. A fantastic addition to any of our seafood dishes.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"6.00\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"18.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Alta Luna Pinto Grigio (Italy - Trentino Region)\",\"menu_item_description\":\"Floral notes and nuances of fresh peach and golden apple. Ths wine is elegantly structured and well balanced with a crisp refreshing character and a long finish. A delicious accompaniment to salads, appetizers and seafood. Suggested pairing with our Sunset Grouper or Goat Cheese Salad.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"7.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"24.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Chateau Ste. Michelle Riesllng (Washington)\",\"menu_item_description\":\"Mouth-watering flavors of ripe peach and juicy pear make this Riesling a wonderful complement to Peel \\u2018n Eat Shrimp, Calamari or Crab Legs\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"7.00\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"22.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Nobilo Sauvignon Blanc (New Zealand)\",\"menu_item_description\":\"A quintessential Marlborough Sauvignon Blanc. After a day at the beach, enjoy your Sunset Salad or Coconut Shrimp with style. Nobilo offers crisp, citrus notes that will enhance the taste of any meal.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"7.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"24.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Forestville Merlot (California)\",\"menu_item_description\":\"Rich fruit flavors of ripe plum, cherry and currants with oak and soft tannins. This wine pairs well with our Steaks, Ribs and blackened Seafood Entr\\u00e9es.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"6.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"18.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Ruta 22 Malbec (Argentina)\",\"menu_item_description\":\"Our award-winning Baby Back Ribs deserve this treat. Ruta 22 expresses itself with raspberry, blackberry and plum jam notes that are sure to please the palate.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"7.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"24.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Mark West Pinot Noir (California)\",\"menu_item_description\":\"Ripe blueberry, black cherry and plum balanced by rich vanilla notes. Pairs well with our Steak and Rib dishes.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"8.00\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"28.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Josh Cellars Cabernet Sauvignon (California)\",\"menu_item_description\":\"Organically grown with layers of black currant and spicy berry make this Cabernet a sure crowd-pleaser and creates a perfect pairing with any steak dish.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"8.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"26.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Lineman's Bin 45 Cabernet Sauvignon (Australia)\",\"menu_item_description\":\"Cassis and bramblefruit with dark fruit on the nose. A medium- to full-bodied cabernet sauvignon with dark berry fruit and cassis. Hints of chocolate and mint with a long juicy finish. Great with beef or burgers.\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Glass\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"6.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Bottle\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"18.00\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"Children's Menu (ages 10 and under, please)\",\"group_note\":\"\",\"group_description\":\"Served with a choice of French fries or sweet corn. (Pasta entries do not include a choice of side items.)\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Pasta & Sauce\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Grilled Cheese\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fish 'n Chips\",\"menu_item_description\":\"\",\"menu_item_price\":\"5.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Mac 'n Cheese\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Tenders\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Ravioli & Sauce\",\"menu_item_description\":\"\",\"menu_item_price\":\"3.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Corn Dog Nuggets\",\"menu_item_description\":\"\",\"menu_item_price\":\"4.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Shrimp\",\"menu_item_description\":\"\",\"menu_item_price\":\"6.99\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chocolate Milk\",\"menu_item_description\":\"\",\"menu_item_price\":\"2.75\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]}]}],\"id\":\"Smp1aTZWSndROEZUS3pBNU9MV1JBWGR2THBoUkNOMy9qNjBudjlVdDV2NFhrejhuWVZJcTRsTW5YUFlTWmNxcg==\"}}}",    	
    	"{\"response\":{\"api\":{\"status\":200,\"api_version\":\"2.1\",\"format\":\"json\",\"api_key\":\"51a5b6d4-dcb3-11e8-8d62-525400552a35\"},\"result\":{\"restaurant_info\":{\"restaurant_name\":\"The Wing Company\",\"brief_description\":\"Fresh Wings, Never Frozen!\",\"full_description\":\"\",\"location_id\":\"\",\"mobile\":\"0\",\"address_1\":\"7 Larkspur Drive\",\"address_2\":\"\",\"city_town\":\"Palmyra\",\"state_province\":\"PA\",\"postal_code\":\"17078\",\"country\":\"US\",\"phone\":\"717-832-3066\",\"fax\":\"\",\"longitude\":\"-76.599392\",\"latitude\":\"40.276777\",\"business_type\":\"independent\",\"utc_offset\":\"-5.00\",\"website_url\":\"http://www.wingcompany.net\"},\"environment_info\":{\"cuisine_type_primary\":\"Chicken\",\"cuisine_type_secondary\":\"\",\"smoking_allowed\":\"0\",\"takeout_available\":\"1\",\"seating_qty\":null,\"max_group_size\":null,\"pets_allowed\":\"0\",\"wheelchair_accessible\":\"1\",\"age_level_preference\":\"\",\"dress_code\":\"none\",\"delivery_available\":\"0\",\"delivery_radius\":null,\"delivery_fee\":null,\"catering_available\":\"1\",\"reservations\":\"0\",\"alcohol_type\":\"none\",\"music_type\":\"\"},\"operating_days\":[{\"day_of_week\":\"1\",\"open_time\":\"11:00:00\",\"close_time\":\"21:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:00PM\",\"day\":\"Monday\",\"day_short\":\"Mon\"},{\"day_of_week\":\"2\",\"open_time\":\"11:00:00\",\"close_time\":\"21:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:00PM\",\"day\":\"Tuesday\",\"day_short\":\"Tue\"},{\"day_of_week\":\"3\",\"open_time\":\"11:00:00\",\"close_time\":\"21:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:00PM\",\"day\":\"Wednesday\",\"day_short\":\"Wed\"},{\"day_of_week\":\"4\",\"open_time\":\"11:00:00\",\"close_time\":\"21:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"9:00PM\",\"day\":\"Thursday\",\"day_short\":\"Thu\"},{\"day_of_week\":\"5\",\"open_time\":\"11:00:00\",\"close_time\":\"22:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"10:00PM\",\"day\":\"Friday\",\"day_short\":\"Fri\"},{\"day_of_week\":\"6\",\"open_time\":\"11:00:00\",\"close_time\":\"22:00:00\",\"open_time_ampm\":\"11:00AM\",\"close_time_ampm\":\"10:00PM\",\"day\":\"Saturday\",\"day_short\":\"Sat\"},{\"day_of_week\":\"7\",\"open_time\":\"12:00:00\",\"close_time\":\"21:00:00\",\"open_time_ampm\":\"12:00PM\",\"close_time_ampm\":\"9:00PM\",\"day\":\"Sunday\",\"day_short\":\"Sun\"}],\"operating_days_printable\":{\"1\":{\"day_name\":\"Monday\",\"display\":\"11:00AM-9:00PM\"},\"2\":{\"day_name\":\"Tuesday\",\"display\":\"11:00AM-9:00PM\"},\"3\":{\"day_name\":\"Wednesday\",\"display\":\"11:00AM-9:00PM\"},\"4\":{\"day_name\":\"Thursday\",\"display\":\"11:00AM-9:00PM\"},\"5\":{\"day_name\":\"Friday\",\"display\":\"11:00AM-10:00PM\"},\"6\":{\"day_name\":\"Saturday\",\"display\":\"11:00AM-10:00PM\"},\"7\":{\"day_name\":\"Sunday\",\"display\":\"12:00PM-9:00PM\"}},\"logo_urls\":[],\"seating_locations\":[{\"seating_location\":\"indoor\"}],\"accepted_currencies\":[{\"accepted_currency\":\"USD\"}],\"parking\":{\"street_free\":\"0\",\"street_metered\":\"0\",\"private_lot\":\"1\",\"garage\":\"0\",\"valet\":\"0\"},\"settings\":{\"social\":false},\"menus\":[{\"menu_name\":\"The Wing Company\",\"menu_description\":\"\",\"menu_note\":\"\",\"currency_symbol\":\"USD\",\"language\":\"en\",\"menu_duration_name\":\"all\",\"menu_duration_time_start\":\"\",\"menu_duration_time_end\":\"\",\"menu_groups\":[{\"group_name\":\"Appetizers\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[{\"group_options_name\":\"Flavors & Cheese\",\"menu_group_option_information\":\"\",\"menu_group_option_min_selected\":null,\"menu_group_option_max_selected\":null,\"option_items\":[{\"menu_group_option_name\":\"Flavors for Fries\",\"menu_group_option_additional_cost\":\"0.50\",\"selected\":\"0\"},{\"menu_group_option_name\":\"Cheese for Fries\",\"menu_group_option_additional_cost\":\"0.75\",\"selected\":\"0\"}]}],\"menu_items\":[{\"menu_item_name\":\"Fresh Cut Fries\",\"menu_item_description\":\"Try a flavor for just 50 cents - Cajun, Old Bay, Ranch or ADD CHEESE for 75 cents\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Single\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"2.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Large\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Family\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.95\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Beer Battered Onion Wings\",\"menu_item_description\":\"M.O. sauce for dipping\",\"menu_item_price\":\"5.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Mozzarella Sticks\",\"menu_item_description\":\"marinara sauce for dipping\",\"menu_item_price\":\"5.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Jalapeno Poppers\",\"menu_item_description\":\"Sour cream sauce for dipping\",\"menu_item_price\":\"5.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Mushrooms\",\"menu_item_description\":\"\",\"menu_item_price\":\"5.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"The Original\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Barn Yard Rice\\u2122\",\"menu_item_description\":\"A tasty blend of rice, chicken and tomato with special seasonings made daily for your delight! Spicy upon request -- how hot??\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Single\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"2.25\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Large\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"4.75\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Family\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.95\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"Extras\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Bleu Cheese\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Single\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"0.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Large\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Family\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.95\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Ranch\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Single\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"0.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Large\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Family\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.95\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Celery\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Single\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"0.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Large\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Family\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.95\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Extra Sauce\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Single\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"0.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Large\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Family\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"5.95\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"Wings\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Wings\",\"menu_item_description\":\"Regular or Boneless | The 6 and 12 Wing Orders include bleu cheese, celery & Barn Yard Rice, The 6, 24, 50 and 100 Wing Orders have a limit of two (2) choices per order/$1 for each additional order\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"6\",\"menu_item_size_description\":\"How Hot? Mild - Medium - Hot - XXX\\r\\nFlavors: Cajun, Caribbean Jerk, Carlic Herb, Garlic Parmesan, General Tso's, Honey BBQ, Honey Hot, Honey Mustard, Hot Taco, Lemon Pepper, Old Bay, Parmesan, Ranch, Smokey BBQ, Smokey Buffalo, Teriyaki and--new--Gator!\",\"menu_item_size_price\":\"6.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"12\",\"menu_item_size_description\":\"How Hot? Mild - Medium - Hot - XXX\\r\\nFlavors: Cajun, Caribbean Jerk, Carlic Herb, Garlic Parmesan, General Tso's, Honey BBQ, Honey Hot, Honey Mustard, Hot Taco, Lemon Pepper, Old Bay, Parmesan, Ranch, Smokey BBQ, Smokey Buffalo, Teriyaki and--new--Gator!\",\"menu_item_size_price\":\"11.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"24\",\"menu_item_size_description\":\"How Hot? Mild - Medium - Hot - XXX\\r\\nFlavors: Cajun, Caribbean Jerk, Carlic Herb, Garlic Parmesan, General Tso's, Honey BBQ, Honey Hot, Honey Mustard, Hot Taco, Lemon Pepper, Old Bay, Parmesan, Ranch, Smokey BBQ, Smokey Buffalo, Teriyaki and--new--Gator!\",\"menu_item_size_price\":\"20.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"50\",\"menu_item_size_description\":\"How Hot? Mild - Medium - Hot - XXX\\r\\nFlavors: Cajun, Caribbean Jerk, Carlic Herb, Garlic Parmesan, General Tso's, Honey BBQ, Honey Hot, Honey Mustard, Hot Taco, Lemon Pepper, Old Bay, Parmesan, Ranch, Smokey BBQ, Smokey Buffalo, Teriyaki and--new--Gator!\",\"menu_item_size_price\":\"38.95\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"100\",\"menu_item_size_description\":\"How Hot? Mild - Medium - Hot - XXX\\r\\nFlavors: Cajun, Caribbean Jerk, Carlic Herb, Garlic Parmesan, General Tso's, Honey BBQ, Honey Hot, Honey Mustard, Hot Taco, Lemon Pepper, Old Bay, Parmesan, Ranch, Smokey BBQ, Smokey Buffalo, Teriyaki and--new--Gator!\",\"menu_item_size_price\":\"72.95\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"Sandwiches\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Beef on Weck\",\"menu_item_description\":\"\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Fries\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Add Fries\",\"menu_item_option_additional_cost\":\"2.00\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Pulled Pork\",\"menu_item_description\":\"\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Add Fries\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Fries\",\"menu_item_option_additional_cost\":\"2.00\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Chicken Parmesan\",\"menu_item_description\":\"\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Add Fries\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Fries\",\"menu_item_option_additional_cost\":\"2.00\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Buffalo Chicken Bleu\",\"menu_item_description\":\"\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Add Fries\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Fries\",\"menu_item_option_additional_cost\":\"2.00\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Pit Beef\",\"menu_item_description\":\"\",\"menu_item_price\":\"7.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Add Fries\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Fries\",\"menu_item_option_additional_cost\":\"2.00\",\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Combos\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Combo Sandwich\",\"menu_item_description\":\"Any Combo Sandwich includes 6 wings and fresh cut fries\",\"menu_item_price\":\"14.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[{\"item_options_name\":\"Sandwich Type\",\"menu_item_option_information\":\"\",\"menu_item_option_min_selected\":null,\"menu_item_option_max_selected\":null,\"option_items\":[{\"menu_item_option_name\":\"Beef on Weck\",\"menu_item_option_additional_cost\":null,\"selected\":\"0\"},{\"menu_item_option_name\":\"Pulled Pork\",\"menu_item_option_additional_cost\":null,\"selected\":\"0\"},{\"menu_item_option_name\":\"Chicken Parmesan\",\"menu_item_option_additional_cost\":null,\"selected\":\"0\"},{\"menu_item_option_name\":\"Buffalo Chicken Blue\",\"menu_item_option_additional_cost\":null,\"selected\":null},{\"menu_item_option_name\":\"Pit Beef\",\"menu_item_option_additional_cost\":null,\"selected\":\"0\"}]}],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Drinks\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Drinks\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"16 oz\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"2.00\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"32 oz\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"2.75\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"Ice Cream\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Hershey's Ice Cream\",\"menu_item_description\":\"Cone or Dish\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"Single Scoop\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"2.50\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Double Schoop\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.25\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]},{\"menu_item_name\":\"Milk Shakes\",\"menu_item_description\":\"\",\"menu_item_price\":null,\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[{\"menu_item_size_name\":\"16 oz\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"3.25\",\"menu_item_size_calories\":null},{\"menu_item_size_name\":\"Monster Shake\",\"menu_item_size_description\":\"\",\"menu_item_size_price\":\"4.75\",\"menu_item_size_calories\":null}],\"menu_item_images\":[]}]},{\"group_name\":\"More...\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"Chicken Tenders (5)\",\"menu_item_description\":\"with fresh cut fries & sauce for dipping\",\"menu_item_price\":\"9.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Fried Shrimp\",\"menu_item_description\":\"with fresh cut fries & sauce for dipping\",\"menu_item_price\":\"9.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Buffalo Shrimp\",\"menu_item_description\":\"with fresh cut fries & sauce for dipping\",\"menu_item_price\":\"9.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"Sampler\",\"menu_item_description\":\"5 Wings, 4 Shrimp, 2 Tenders, Fresh Cut Fries, Sauce & Barn Yard Fries (TM)\",\"menu_item_price\":\"13.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]},{\"group_name\":\"Mac's Meal\",\"group_note\":\"\",\"group_description\":\"\",\"menu_group_options\":[],\"menu_items\":[{\"menu_item_name\":\"12 and Under Wings\",\"menu_item_description\":\"Includes five (5) wings, fresh cut fries, sauce and Barn Yard Rice (TM)\",\"menu_item_price\":\"6.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"12 and Under Chicken Tenders\",\"menu_item_description\":\"Includes two (2) chicken tenders, fresh cut fries, sauce and Barn Yard Rice (TM)\",\"menu_item_price\":\"6.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":\"0\",\"vegetarian\":\"0\",\"vegan\":\"0\",\"kosher\":\"0\",\"halal\":\"0\",\"gluten_free\":\"0\",\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]},{\"menu_item_name\":\"12 and Under Shrimp\",\"menu_item_description\":\"Includes Four (4) shrimp, fresh cut fries, sauce and Barn Yard Rice (TM)\",\"menu_item_price\":\"6.95\",\"menu_item_calories\":null,\"menu_item_heat_index\":null,\"menu_item_allergy_information\":\"\",\"menu_item_allergy_information_allergens\":\"\",\"special\":null,\"vegetarian\":null,\"vegan\":null,\"kosher\":null,\"halal\":null,\"gluten_free\":null,\"tax_id\":null,\"tax_name\":\"\",\"tax_rate\":\"\",\"is_default\":\"\",\"menu_item_options\":[],\"menu_item_sizes\":[],\"menu_item_images\":[]}]}]}],\"id\":\"dEJibWcwcThHQzFRbUJHL0YwbmZ0akVqRGxCM0oxVGxNdURQVUdtUVlHYnA4U0g4UzlFYVBqU0JEdTdyNFo1Sw==\"}}}"
    };
    
    // Per review, ArrayList of entities from reviews
    // Per restaurant, ArrayList of reviews
    // Overall, ArrayList of all the restaurants
    static ArrayList<ArrayList<ArrayList<EntityClass>>> GOAL_MATCHES = new ArrayList<ArrayList<ArrayList<EntityClass>>>();
    
    
    public static void main(String[] args) throws FileNotFoundException, ParseException
    {    	
    	BackendClass backend = new BackendClass();
    	
        MATCHING_ALGS.add(new EntityClass("Case Insensitive Matching", 0.0));
        MATCHING_ALGS.add(new EntityClass("Levenstein Edit Distance", 1.0));
        MATCHING_ALGS.add(new EntityClass("Levenstein Edit Distance", 2.0));
        MATCHING_ALGS.add(new EntityClass("Levenstein Edit Distance", 3.0));
        MATCHING_ALGS.add(new EntityClass("Levenstein Edit Distance", 4.0));
        MATCHING_ALGS.add(new EntityClass("Levenstein Edit Distance", 5.0));
        MATCHING_ALGS.add(new EntityClass("Jaro Similarity Metric", 0.5));
        MATCHING_ALGS.add(new EntityClass("Jaro Similarity Metric", 0.6));
        MATCHING_ALGS.add(new EntityClass("Jaro Similarity Metric", 0.7));
        MATCHING_ALGS.add(new EntityClass("Jaro Similarity Metric", 0.8));
        MATCHING_ALGS.add(new EntityClass("Jaro Similarity Metric", 0.9));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (2-gram)", 0.5));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (2-gram)", 0.6));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (2-gram)", 0.7));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (2-gram)", 0.8));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (2-gram)", 0.9));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (3-gram)", 0.5));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (3-gram)", 0.6));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (3-gram)", 0.7));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (3-gram)", 0.8));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (3-gram)", 0.9));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (word-gram)", 0.5));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (word-gram)", 0.6));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (word-gram)", 0.7));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (word-gram)", 0.8));
        MATCHING_ALGS.add(new EntityClass("Jaccard Similarity (word-gram)", 0.9));

    	// Get all of the reviews for each business
		for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
			

			// Get the business IDs, then the reviews, and send them to Google
			ArrayList<RestaurantClass> businessIds = backend.FindBusinessId(TEST_RESTAURANTS[i]);
			ArrayList<ReviewClass> reviews = new ArrayList<ReviewClass>();
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
			
    	// Get the menu items for each restaurant
    	ArrayList<ArrayList<String[]>> menuItems = new ArrayList<ArrayList<String[]>>();
    	for (int i = 0; i < TEST_RESTAURANTS.length; i ++)
    	{
    		menuItems.add(backend.GetMenuItems(MENU_RESULTS[i]));
    	}
		
		// Count the matches we get for each matching algorithm
    	// Also categorize as true pos/false pos/false neg in Matches
    	// True pos - match with a menu item and menu item is in GROUND_TRUTH
    	// False pos - match with a menu item and menu item is not in GROUND_TRUTH
    	// False neg - didn't match with a menu item but menu item is in GROUND_TRUTH
    	boolean shouldMatch = false;
    	// First number how many matches we have, second is total entities
    	BigDecimal[] entityAccuracy = { BigDecimal.ZERO, BigDecimal.ZERO };
    	// Go through each restaurant
    	for (int i = 0; i < GOAL_MATCHES.size(); i ++)
    	{
    		ArrayList<ArrayList<EntityClass>> restaurant = GOAL_MATCHES.get(i);
    		ArrayList<String[]> restaurantItems = menuItems.get(i);
    		String[][][] restaurantGroundTruth = GROUND_TRUTH[i];
    		String[][] restEntities = GOAL_ENTITIES[i];
    		// For each restaurant, go through each review
    		for (int j = 0; j < restaurant.size(); j ++)
    		{
    			// For each review, go through each entity
    			ArrayList<EntityClass> review = restaurant.get(j);
    			String[][] reviewGroundTruth = restaurantGroundTruth[j];
    			String[] reviewEntities = restEntities[j];
    			
    			// See what entities we picked out are also extracted by Google
    			for (int m = 0; m < reviewEntities.length; m ++)
    			{
    				String reviewEntity = reviewEntities[m];
    				// If reviewEntity is in review, then found by Google, so correct match increments
    				if (Contains(review, reviewEntity))
    				{
    					entityAccuracy[0] = entityAccuracy[0].add(BigDecimal.ONE);
    				}
    				// Increase total number of entities found
    				entityAccuracy[1] = entityAccuracy[1].add(BigDecimal.ONE);
    			}
    			
    			for (int l = 0; l < restaurantItems.size(); l ++)
    			{
    				// Go through each menu item
    				String restaurantItem = restaurantItems.get(l)[0];
    				for (int k = 0; k < review.size(); k ++)
    				{
    					// For each entity, check if you get a match with any menu item
        				String menuItem = review.get(k).GetWord();
    					
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
    					if (LevensteinEditDistance(menuItem, restaurantItem, 1))
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
    					if (LevensteinEditDistance(menuItem, restaurantItem, 2))
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
    					if (LevensteinEditDistance(menuItem, restaurantItem, 3))
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
    					if (LevensteinEditDistance(menuItem, restaurantItem, 4))
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
    					if (LevensteinEditDistance(menuItem, restaurantItem, 5))
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
    					if (JaroSimilarity(menuItem, restaurantItem, 0.5))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[6][0] = Matches[6][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[6][1] = Matches[6][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[6][2] = Matches[6][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaroSimilarity(menuItem, restaurantItem, 0.6))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[7][0] = Matches[7][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[7][1] = Matches[7][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[7][2] = Matches[7][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaroSimilarity(menuItem, restaurantItem, 0.7))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[8][0] = Matches[8][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[8][1] = Matches[8][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[8][2] = Matches[8][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaroSimilarity(menuItem, restaurantItem, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[9][0] = Matches[9][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[9][1] = Matches[9][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[9][2] = Matches[9][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaroSimilarity(menuItem, restaurantItem, 0.9))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[10][0] = Matches[10][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[10][1] = Matches[10][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[10][2] = Matches[10][2].add(BigDecimal.ONE);
    						}
    					}
    					String[] tokens1 = Tokenize(menuItem, 2, false);
    					String[] tokens2 = Tokenize(restaurantItem, 2, false);
    					if (JaccardSimilarity(tokens1, tokens2, 0.5))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[11][0] = Matches[11][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[11][1] = Matches[11][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[11][2] = Matches[11][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.6))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[12][0] = Matches[12][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[12][1] = Matches[12][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[12][2] = Matches[12][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.7))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[13][0] = Matches[13][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[13][1] = Matches[13][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[13][2] = Matches[13][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[14][0] = Matches[14][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[14][1] = Matches[14][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[14][2] = Matches[14][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.9))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[15][0] = Matches[15][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[15][1] = Matches[15][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[15][2] = Matches[15][2].add(BigDecimal.ONE);
    						}
    					}
    					tokens1 = Tokenize(menuItem, 3, false);
    					tokens2 = Tokenize(restaurantItem, 3, false);
    					if (JaccardSimilarity(tokens1, tokens2, 0.5))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[16][0] = Matches[16][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[16][1] = Matches[16][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[16][2] = Matches[16][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.6))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[17][0] = Matches[17][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[17][1] = Matches[17][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[17][2] = Matches[17][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.7))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[18][0] = Matches[18][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[18][1] = Matches[18][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[18][2] = Matches[18][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[19][0] = Matches[19][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[19][1] = Matches[19][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[19][2] = Matches[19][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.9))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[20][0] = Matches[20][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[20][1] = Matches[20][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[20][2] = Matches[20][2].add(BigDecimal.ONE);
    						}
    					}
    					tokens1 = Tokenize(menuItem, 1, true);
    					tokens2 = Tokenize(restaurantItem, 1, true);
    					if (JaccardSimilarity(tokens1, tokens2, 0.5))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[21][0] = Matches[21][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[21][1] = Matches[21][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[21][2] = Matches[21][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.6))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[22][0] = Matches[22][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[22][1] = Matches[22][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[22][2] = Matches[22][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.7))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[23][0] = Matches[23][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[23][1] = Matches[23][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[23][2] = Matches[23][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.8))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[24][0] = Matches[24][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[24][1] = Matches[24][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[24][2] = Matches[24][2].add(BigDecimal.ONE);
    						}
    					}
    					if (JaccardSimilarity(tokens1, tokens2, 0.9))
    					{
    						// If we have a match, it's a true positive if restaurantItem is
    						// in the ground truth for that reviews
    						if (shouldMatch)
    						{
    							Matches[25][0] = Matches[25][0].add(BigDecimal.ONE);
    						}
    						// False positive otherwise
    						else
    						{
    							Matches[25][1] = Matches[25][1].add(BigDecimal.ONE);
    						}
    					}
    					else
    					{
    						// If we don't have a match, it's a false negative if restaurantItem
    						// is in the ground truth for that review
    						if (shouldMatch)
    						{
    							Matches[25][2] = Matches[25][2].add(BigDecimal.ONE);
    						}
    					}
    				}
    			}
    		}
    	}
		
		// Calculate the precision/recall/F1 score for each and output as table
    	// precision = true pos / (true pos + false pos)
    	// recall = true pos / (true pos + false neg)
    	// F1 = 2 * precision * recall / (precision + recall)
    	BigDecimal precision;
    	BigDecimal recall;
    	BigDecimal f1;
    	BigDecimal two = new BigDecimal(2);
    	String leftAlignFormat = "%-35s |     %.1f     |  %-4f   |  %-4f   |  %-4f%n";
    	System.out.println("MATCHING ALGORITHM                  |  THRESHOLD  |  PRECISION  |  RECALL     |  F1 Score");
    	for (int i = 0; i < MATCHING_ALGS.size(); i ++)
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
    		System.out.format(leftAlignFormat, MATCHING_ALGS.get(i).GetWord(), MATCHING_ALGS.get(i).GetSentiment(), precision, recall, f1);
    	}
    	
    	// Output Google entity extraction results
    	System.out.println("\n\n");
    	System.out.println("Google correctly extracted" + entityAccuracy[0] + "out of" + entityAccuracy[1] + "entities.");
    	System.out.println("This is an accuracy of" + entityAccuracy[0].divide(entityAccuracy[1], 5, RoundingMode.HALF_UP));
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
    
    public static boolean Contains(ArrayList<EntityClass> list, String word)
    {
    	// See if word is contained in entity list
    	for (int i = 0; i < list.size(); i ++)
    	{
    		EntityClass ent = list.get(i);
    		if (word.equalsIgnoreCase(ent.GetWord()))
    		{
    			return true;
    		}
    	}
    	return false;
    }
}
