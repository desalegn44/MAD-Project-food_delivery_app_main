package com.example.food_delivery_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * RestaurantListActivity
 * ----------------------
 * This activity displays all available restaurants.
 * Users can:
 *  - Search restaurants by name or tag
 *  - Filter restaurants by category
 *  - Select a restaurant to view its menu
 */
public class RestaurantListActivity extends AppCompatActivity {

    // Search input field
    private TextInputEditText searchInput;

    // Container layout for restaurant cards
    private LinearLayout restaurantsContainer;

    // List holding restaurant card references for filtering
    private List<RestaurantCard> restaurantCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        // Initialize back button
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> onBackPressed());
        }

        // Initialize views
        searchInput = findViewById(R.id.searchInput);
        restaurantsContainer = findViewById(R.id.restaurantsLayout);

        /*
         * NOTE:
         * Restaurants are filtered by hiding/showing CardViews
         * instead of dynamically adding or removing views.
         */

        // Setup search functionality
        setupSearch();

        // Setup restaurant button click actions
        setupRestaurantButtons();

        // Setup food category filters
        setupCategoryFilters();

        // Initialize restaurant data list
        initializeRestaurantCards();
    }

    /**
     * Initializes restaurant card data
     */
    private void initializeRestaurantCards() {

        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnSunny),
                findViewById(R.id.tvSunnyName),
                "Sunny Burger and Pizza",
                "sunny",
                "Pizza,Burger"
        ));

        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnRome),
                findViewById(R.id.tvRomeName),
                "Rome 1960 Chicken, Pizza and Burger",
                "rome",
                "Chicken,Pizza,Burger"
        ));

        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnHTown),
                findViewById(R.id.tvHTownName),
                "H Town Burger",
                "htown",
                "Burger"
        ));

        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnVenezia),
                findViewById(R.id.tvVeneziaName),
                "Venezia – Italian Restaurant",
                "venezia",
                "Italian,Pasta"
        ));

        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnTokyo),
                findViewById(R.id.tvTokyoName),
                "Tokyo Sushi House",
                "tokyo",
                "Sushi,Japanese"
        ));

        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnNapoli),
                findViewById(R.id.tvNapoliName),
                "Napoli Pizza House",
                "napoli",
                "Pizza,Italian"
        ));

        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnJuice),
                findViewById(R.id.tvJuiceName),
                "Fresh Juice Bar",
                "juice",
                "Juice,Drinks"
        ));
    }

    /**
     * Sets up search bar functionality
     */
    private void setupSearch() {
        if (searchInput != null) {

            // Real-time text filtering
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterRestaurants(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Handle keyboard search action
            searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    String query = v.getText().toString().trim();

                    if (!TextUtils.isEmpty(query)) {
                        // Navigate to full search results screen
                        navigateToSearchResults(query);
                    }
                    return true;
                }
            });

            // Optional clear functionality placeholder
            searchInput.setOnTouchListener((v, event) -> false);
        }
    }

    /**
     * Sets click listeners for restaurant buttons
     */
    private void setupRestaurantButtons() {

        setupButton(R.id.btnSunny, "Sunny Burger and Pizza", "sunny");
        setupButton(R.id.btnRome, "Rome 1960 Chicken, Pizza and Burger", "rome");
        setupButton(R.id.btnHTown, "H Town Burger", "htown");
        setupButton(R.id.btnVenezia, "Venezia – Italian Restaurant", "venezia");
        setupButton(R.id.btnTokyo, "Tokyo Sushi House", "tokyo");
        setupButton(R.id.btnNapoli, "Napoli Pizza House", "napoli");
        setupButton(R.id.btnJuice, "Fresh Juice Bar", "juice");
    }

    /**
     * Initializes category filter buttons
     */
    private void setupCategoryFilters() {
        setupCategory(R.id.categoryPizza, "Pizza");
        setupCategory(R.id.categoryBurger, "Burger");
        setupCategory(R.id.categoryDrinks, "Drinks");
        setupCategory(R.id.categorySushi, "Sushi");
        setupCategory(R.id.categoryDessert, "Dessert");
    }

    /**
     * Sets click behavior for each category
     */
    private void setupCategory(int categoryId, String category) {

        TextView categoryView = findViewById(categoryId);

        if (categoryView != null) {
            categoryView.setOnClickListener(v -> {

                // Reset all category styles
                resetCategoryBackgrounds();

                // Highlight selected category
                categoryView.setBackgroundResource(R.drawable.category_bg_selected);

                // Filter restaurants
                filterByCategory(category);
            });
        }
    }

    /**
     * Resets all category backgrounds to default
     */
    private void resetCategoryBackgrounds() {

        int[] categoryIds = {
                R.id.categoryPizza,
                R.id.categoryBurger,
                R.id.categoryDrinks,
                R.id.categorySushi,
                R.id.categoryDessert
        };

        for (int id : categoryIds) {
            TextView categoryView = findViewById(id);
            if (categoryView != null) {
                categoryView.setBackgroundResource(R.drawable.category_bg);
            }
        }
    }

    /**
     * Sets navigation for restaurant selection
     */
    private void setupButton(int buttonId, String restaurantName, String restaurantId) {

        Button button = findViewById(buttonId);

        if (button != null) {
            button.setOnClickListener(v -> {

                // Navigate to restaurant menu screen
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantMenuActivity.class);
                intent.putExtra("RESTAURANT_NAME", restaurantName);
                intent.putExtra("RESTAURANT_ID", restaurantId);
                startActivity(intent);
            });
        }
    }

    /**
     * Filters restaurants based on search text
     */
    private void filterRestaurants(String query) {

        if (TextUtils.isEmpty(query)) {
            showAllRestaurants();
            return;
        }

        query = query.toLowerCase().trim();

        for (RestaurantCard restaurant : restaurantCards) {

            boolean matches =
                    restaurant.name.toLowerCase().contains(query) ||
                    restaurant.tags.toLowerCase().contains(query);

            // Access parent CardView and update visibility
            View cardView = (View) restaurant.button.getParent().getParent();
            cardView.setVisibility(matches ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Filters restaurants by selected category
     */
    private void filterByCategory(String category) {

        category = category.toLowerCase();

        for (RestaurantCard restaurant : restaurantCards) {

            boolean matches = restaurant.tags.toLowerCase().contains(category);

            View cardView = (View) restaurant.button.getParent().getParent();
            cardView.setVisibility(matches ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Displays all restaurants
     */
    private void showAllRestaurants() {

        for (RestaurantCard restaurant : restaurantCards) {
            View cardView = (View) restaurant.button.getParent().getParent();
            cardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Navigates to SearchResultsActivity
     */
    private void navigateToSearchResults(String query) {

        Intent intent = new Intent(RestaurantListActivity.this, SearchResultsActivity.class);
        intent.putExtra("SEARCH_QUERY", query);
        startActivity(intent);
    }

    /**
     * Helper model class for restaurant card information
     */
    static class RestaurantCard {

        Button button;
        TextView nameView;
        String name;
        String id;
        String tags;

        RestaurantCard(Button button, TextView nameView,
                       String name, String id, String tags) {

            this.button = button;
            this.nameView = nameView;
            this.name = name;
            this.id = id;
            this.tags = tags;
        }
    }
}
