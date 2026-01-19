package com.example.food_delivery_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchResultsActivity
 * ---------------------
 * This activity displays search results for both restaurants and dishes.
 * It receives a search query from the previous activity and shows matching results.
 */
public class SearchResultsActivity extends AppCompatActivity {

    // Layout containers for restaurants and dishes
    private LinearLayout restaurantsLayout;
    private LinearLayout dishesLayout;

    // TextViews for displaying messages and section titles
    private TextView noResultsText;
    private TextView restaurantsTitle;
    private TextView dishesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Initialize UI components
        restaurantsLayout = findViewById(R.id.restaurantsLayout);
        dishesLayout = findViewById(R.id.dishesLayout);
        noResultsText = findViewById(R.id.noResultsText);
        restaurantsTitle = findViewById(R.id.restaurantsTitle);
        dishesTitle = findViewById(R.id.dishesTitle);

        // Back button to return to previous screen
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());

        // Get search query sent from previous activity
        String query = getIntent().getStringExtra("SEARCH_QUERY");

        // Check if query is not empty
        if (query != null && !TextUtils.isEmpty(query)) {

            // Set search title text
            TextView searchTitle = findViewById(R.id.searchTitle);
            if (searchTitle != null) {
                searchTitle.setText("Search: \"" + query + "\"");
            }

            // Perform search operation
            performSearch(query);
        }
    }

    /**
     * Performs search on restaurants and dishes
     */
    private void performSearch(String query) {
        query = query.toLowerCase().trim();

        // Clear previous search results
        restaurantsLayout.removeAllViews();
        dishesLayout.removeAllViews();

        boolean hasRestaurantResults = false;
        boolean hasDishResults = false;

        // Search restaurants
        List<Restaurant> restaurantResults = searchRestaurants(query);
        for (Restaurant restaurant : restaurantResults) {
            addRestaurantCard(restaurant);
            hasRestaurantResults = true;
        }

        // Search dishes
        List<Dish> dishResults = searchDishes(query);
        for (Dish dish : dishResults) {
            addDishCard(dish);
            hasDishResults = true;
        }

        // Show or hide section titles
        restaurantsTitle.setVisibility(hasRestaurantResults ? View.VISIBLE : View.GONE);
        dishesTitle.setVisibility(hasDishResults ? View.VISIBLE : View.GONE);

        // Show "no results" message if nothing found
        boolean hasResults = hasRestaurantResults || hasDishResults;
        noResultsText.setVisibility(hasResults ? View.GONE : View.VISIBLE);
    }

    /**
     * Searches restaurants based on name or tags
     */
    private List<Restaurant> searchRestaurants(String query) {
        List<Restaurant> allRestaurants = getSampleRestaurants();
        List<Restaurant> results = new ArrayList<>();

        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.name.toLowerCase().contains(query) ||
                    restaurant.tags.toLowerCase().contains(query)) {
                results.add(restaurant);
            }
        }

        return results;
    }

    /**
     * Searches dishes based on name, description, or tags
     */
    private List<Dish> searchDishes(String query) {
        List<Dish> allDishes = getSampleDishes();
        List<Dish> results = new ArrayList<>();

        for (Dish dish : allDishes) {
            if (dish.name.toLowerCase().contains(query) ||
                    dish.description.toLowerCase().contains(query) ||
                    dish.tags.toLowerCase().contains(query)) {
                results.add(dish);
            }
        }

        return results;
    }

    /**
     * Creates and displays a restaurant card dynamically
     */
    private void addRestaurantCard(Restaurant restaurant) {

        // Inflate restaurant card layout
        CardView cardView = (CardView) getLayoutInflater()
                .inflate(R.layout.item_restaurant_card, null);

        // Get UI elements from card
        TextView name = cardView.findViewById(R.id.restaurantName);
        TextView tags = cardView.findViewById(R.id.restaurantTags);
        Button viewMenu = cardView.findViewById(R.id.viewMenuButton);

        // Set restaurant data
        name.setText(restaurant.name);
        tags.setText(restaurant.tags);

        // Navigate to restaurant menu
        viewMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, RestaurantMenuActivity.class);
            intent.putExtra("RESTAURANT_NAME", restaurant.name);
            intent.putExtra("RESTAURANT_ID", restaurant.id);
            startActivity(intent);
        });

        // Add card to layout
        restaurantsLayout.addView(cardView);
    }

    /**
     * Creates and displays a dish card dynamically
     */
    private void addDishCard(Dish dish) {

        // Inflate dish card layout
        CardView cardView = (CardView) getLayoutInflater()
                .inflate(R.layout.item_dish_card, null);

        // Get UI elements
        TextView name = cardView.findViewById(R.id.dishName);
        TextView description = cardView.findViewById(R.id.dishDescription);
        TextView tags = cardView.findViewById(R.id.dishTags);
        Button viewDish = cardView.findViewById(R.id.viewDishButton);

        // Set dish data
        name.setText(dish.name);
        description.setText(dish.description);
        tags.setText("Restaurant: " + dish.restaurantName);

        // Navigate to restaurant menu and scroll to selected dish
        viewDish.setOnClickListener(v -> {
            Intent intent = new Intent(this, RestaurantMenuActivity.class);
            intent.putExtra("RESTAURANT_ID", dish.restaurantId);
            intent.putExtra("DISH_ID", dish.id);
            intent.putExtra("SCROLL_TO_DISH", true);
            startActivity(intent);
        });

        // Add card to layout
        dishesLayout.addView(cardView);
    }

    /**
     * Sample restaurant data (temporary / demo data)
     */
    private List<Restaurant> getSampleRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Sunny Burger and Pizza", "sunny", "Pizza, Burger, Fast Food"));
        restaurants.add(new Restaurant("Rome 1960 Chicken, Pizza and Burger", "rome", "Chicken, Pizza, Burger, Italian"));
        restaurants.add(new Restaurant("H Town Burger", "htown", "Burger, American, Fast Food"));
        restaurants.add(new Restaurant("Venezia – Italian Restaurant", "venezia", "Italian, Pasta, Pizza, Fine Dining"));
        restaurants.add(new Restaurant("Tokyo Sushi House", "tokyo", "Sushi, Japanese, Asian, Seafood"));
        restaurants.add(new Restaurant("Napoli Pizza House", "napoli", "Pizza, Italian, Wood Fired"));
        restaurants.add(new Restaurant("Fresh Juice Bar", "juice", "Juice, Drinks, Healthy, Smoothies"));
        return restaurants;
    }

    /**
     * Sample dish data (temporary / demo data)
     */
    private List<Dish> getSampleDishes() {
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("Pepperoni Pizza", "pepperoni_pizza", "sunny", "Sunny Burger and Pizza",
                "Delicious pepperoni pizza with mozzarella cheese and tomato sauce", "Pizza, Italian"));
        dishes.add(new Dish("Margherita Pizza", "margherita_pizza", "napoli", "Napoli Pizza House",
                "Classic margherita pizza with fresh basil", "Pizza, Vegetarian"));
        dishes.add(new Dish("Cheeseburger", "cheeseburger", "sunny", "Sunny Burger and Pizza",
                "Juicy beef patty with cheese, lettuce, and tomato", "Burger, American"));
        dishes.add(new Dish("California Roll", "california_roll", "tokyo", "Tokyo Sushi House",
                "8 pieces of California roll with crab and avocado", "Sushi, Japanese, Seafood"));
        dishes.add(new Dish("Fresh Orange Juice", "orange_juice", "juice", "Fresh Juice Bar",
                "Freshly squeezed orange juice, no sugar added", "Juice, Healthy, Drink"));
        dishes.add(new Dish("Spaghetti Carbonara", "carbonara", "venezia", "Venezia – Italian Restaurant",
                "Creamy spaghetti carbonara with pancetta", "Pasta, Italian"));
        dishes.add(new Dish("BBQ Burger", "bbq_burger", "htown", "H Town Burger",
                "Burger with BBQ sauce, onion rings, and cheddar", "Burger, BBQ, American"));
        dishes.add(new Dish("Chicken Burger", "chicken_burger", "rome", "Rome 1960 Chicken, Pizza and Burger",
                "Crispy chicken burger with lettuce and mayo", "Burger, Chicken"));
        return dishes;
    }

    /**
     * Restaurant model class
     */
    static class Restaurant {
        String name;
        String id;
        String tags;

        Restaurant(String name, String id, String tags) {
            this.name = name;
            this.id = id;
            this.tags = tags;
        }
    }

    /**
     * Dish model class
     */
    static class Dish {
        String name;
        String id;
        String restaurantId;
        String restaurantName;
        String description;
        String tags;

        Dish(String name, String id, String restaurantId,
             String restaurantName, String description, String tags) {
            this.name = name;
            this.id = id;
            this.restaurantId = restaurantId;
            this.restaurantName = restaurantName;
            this.description = description;
            this.tags = tags;
        }
    }
}
