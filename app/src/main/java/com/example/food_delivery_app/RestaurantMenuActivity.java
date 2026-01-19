// RestaurantMenuActivity.java
// Main activity responsible for displaying restaurant details and dynamic menu items
// Includes category filtering (All, Pizza, Burger, Drinks) and Add-to-Cart functionality
package com.example.food_delivery_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantMenuActivity extends AppCompatActivity {

    // Map to store all restaurant data using restaurant ID as key
    private Map<String, RestaurantData> restaurantMap = new HashMap<>();
    // Currently selected restaurant ID
    private String currentRestaurantId;
    // List to hold all menu items of the selected restaurant
    private List<MenuItem> allMenuItems = new ArrayList<>();

    // UI Containers for different categories
    private LinearLayout pizzaItemsContainer;
    private LinearLayout burgerItemsContainer;
    private LinearLayout drinksItemsContainer;
    private TextView categoryAll, categoryPizza, categoryBurger, categoryDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_menu);

        // Initialize restaurant data
        initializeRestaurantData();

        // Initialize UI containers
        pizzaItemsContainer = findViewById(R.id.pizzaItemsContainer);
        burgerItemsContainer = findViewById(R.id.burgerItemsContainer);
        drinksItemsContainer = findViewById(R.id.drinksItemsContainer);

        // Initialize category buttons
        categoryAll = findViewById(R.id.categoryAll);
        categoryPizza = findViewById(R.id.categoryPizza);
        categoryBurger = findViewById(R.id.categoryBurger);
        categoryDrinks = findViewById(R.id.categoryDrinks);

        // Get restaurant data from intent
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("RESTAURANT_NAME");
        currentRestaurantId = intent.getStringExtra("RESTAURANT_ID");

        // Initialize views
        TextView tvRestaurantName = findViewById(R.id.restaurantName);
        TextView tvRestaurantRating = findViewById(R.id.restaurantRating);
        TextView tvFoodType = findViewById(R.id.foodType);
        TextView tvDeliveryTime = findViewById(R.id.deliveryTime);
        TextView tvMinOrder = findViewById(R.id.minOrder);

        // Set restaurant data
        if (restaurantName != null && currentRestaurantId != null) {
            tvRestaurantName.setText(restaurantName);

            RestaurantData data = restaurantMap.get(currentRestaurantId);
            if (data != null) {
                tvRestaurantRating.setText(data.rating);
                tvFoodType.setText(data.foodType);
                tvDeliveryTime.setText(data.deliveryTime);
                tvMinOrder.setText(data.minOrder);

                // Set menu items based on restaurant
                setMenuItems(currentRestaurantId);
            }
        }

        // Setup Add to Cart buttons for static items (compatibility)
        setupAddToCartButtons();

        // Setup Cart button in toolbar
        ImageButton cartButton = findViewById(R.id.cartButton);
        if (cartButton != null) {
            cartButton.setOnClickListener(v -> {
                // Navigate to CartActivity
                Intent cartIntent = new Intent(RestaurantMenuActivity.this, CartActivity.class);
                startActivity(cartIntent);
            });
        }

        // Setup back button
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                onBackPressed();
            });
        }

        // Setup category buttons
        setupCategoryButtons();

        // Show all items by default
        showAllItems();

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    private void initializeRestaurantData() {
        // H-Town Burger
        restaurantMap.put("htown", new RestaurantData(
                "★★★★★(4.2)",
                "Burger",
                "15-20 min",
                "500 ETB",
                createHTownMenuItems()
        ));

        // Sunny Burger and Pizza
        restaurantMap.put("sunny", new RestaurantData(
                "★★★★☆(4.0)",
                "Burger & Pizza",
                "25 min",
                "450 ETB",
                createSunnyMenuItems()
        ));

        // Rome 1960
        restaurantMap.put("rome", new RestaurantData(
                "★★★★☆(4.1)",
                "Chicken, Pizza & Burger",
                "20 min",
                "500 ETB",
                createRomeMenuItems()
        ));

        // Venezia
        restaurantMap.put("venezia", new RestaurantData(
                "★★★★☆(4.3)",
                "Italian",
                "25 min",
                "550 ETB",
                createVeneziaMenuItems()
        ));

        // Tokyo
        restaurantMap.put("tokyo", new RestaurantData(
                "★★★★★(4.5)",
                "Japanese",
                "30 min",
                "600 ETB",
                createTokyoMenuItems()
        ));

        // Napoli
        restaurantMap.put("napoli", new RestaurantData(
                "★★★★☆(4.2)",
                "Pizza",
                "20 min",
                "500 ETB",
                createNapoliMenuItems()
        ));

        // Juice Bar
        restaurantMap.put("juice", new RestaurantData(
                "★★★★☆(4.0)",
                "Drinks",
                "15 min",
                "300 ETB",
                createJuiceBarMenuItems()
        ));
    }

    private List<MenuItem> createHTownMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("H-Town Special Burger", "Boasts a flavorful beef patty, fresh lettuce, juicy tomatoes, and tangy condiments", "450.00", "burger", "🍔"));
        items.add(new MenuItem("Chicken Burger", "Features a juicy chicken patty, crisp lettuce, ripe tomatoes, and savory condiments", "475.00", "burger", "🍗"));
        items.add(new MenuItem("Chef Burger", "Succulent beef patty, melted cheese, caramelized onions, and tangy special sauce", "420.00", "burger", "👨‍🍳"));
        items.add(new MenuItem("French Fries", "Crispy golden fries with seasoning", "120.00", "burger", "🍟"));
        items.add(new MenuItem("Coca-Cola", "Refreshing carbonated drink", "50.00", "drinks", "🥤"));
        items.add(new MenuItem("Milkshake", "Creamy vanilla milkshake", "150.00", "drinks", "🥛"));
        return items;
    }

    private List<MenuItem> createSunnyMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Sunny Special Pizza", "Loaded with mozzarella, pepperoni, mushrooms, and bell peppers", "550.00", "pizza", "🍕"));
        items.add(new MenuItem("BBQ Burger", "Grilled beef patty with BBQ sauce, onions, and cheese", "480.00", "burger", "🍔"));
        items.add(new MenuItem("Cheese Burger", "Classic beef burger with double cheese and special sauce", "430.00", "burger", "🍔"));
        items.add(new MenuItem("Garlic Bread", "Toasted bread with garlic butter", "180.00", "pizza", "🍞"));
        items.add(new MenuItem("Pepsi", "Cold refreshing soda", "45.00", "drinks", "🥤"));
        items.add(new MenuItem("Water", "Bottled mineral water", "30.00", "drinks", "💧"));
        return items;
    }

    private List<MenuItem> createRomeMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Roman Chicken", "Grilled chicken with Italian herbs and spices", "520.00", "burger", "🍗"));
        items.add(new MenuItem("Classic Pizza", "Traditional Italian pizza with fresh ingredients", "490.00", "pizza", "🍕"));
        items.add(new MenuItem("Italian Burger", "Burger with Italian seasoning and mozzarella", "460.00", "burger", "🍔"));
        items.add(new MenuItem("Caesar Salad", "Fresh salad with Caesar dressing", "280.00", "burger", "🥗"));
        items.add(new MenuItem("Red Wine", "Italian red wine glass", "350.00", "drinks", "🍷"));
        items.add(new MenuItem("Espresso", "Strong Italian coffee", "120.00", "drinks", "☕"));
        return items;
    }

    private List<MenuItem> createVeneziaMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Pasta Carbonara", "Creamy pasta with eggs, cheese, and bacon", "380.00", "pizza", "🍝"));
        items.add(new MenuItem("Margherita Pizza", "Classic tomato and mozzarella pizza", "450.00", "pizza", "🍕"));
        items.add(new MenuItem("Tiramisu", "Traditional Italian dessert", "280.00", "burger", "🍰"));
        items.add(new MenuItem("Lasagna", "Layered pasta with meat sauce", "420.00", "pizza", "🍝"));
        items.add(new MenuItem("White Wine", "Italian white wine glass", "320.00", "drinks", "🍷"));
        items.add(new MenuItem("Cappuccino", "Italian coffee with milk foam", "150.00", "drinks", "☕"));
        return items;
    }

    private List<MenuItem> createTokyoMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Salmon Sushi", "Fresh salmon sushi with rice", "520.00", "burger", "🍣"));
        items.add(new MenuItem("Chicken Teriyaki", "Grilled chicken with teriyaki sauce", "480.00", "burger", "🍗"));
        items.add(new MenuItem("Miso Soup", "Traditional Japanese soup", "180.00", "drinks", "🍜"));
        items.add(new MenuItem("California Roll", "Crab and avocado sushi roll", "450.00", "burger", "🍣"));
        items.add(new MenuItem("Green Tea", "Japanese green tea", "100.00", "drinks", "🍵"));
        items.add(new MenuItem("Sake", "Japanese rice wine", "400.00", "drinks", "🍶"));
        return items;
    }

    private List<MenuItem> createNapoliMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Neapolitan Pizza", "Traditional Neapolitan style pizza", "490.00", "pizza", "🍕"));
        items.add(new MenuItem("Calzone", "Folded pizza with cheese and ham", "420.00", "pizza", "🥟"));
        items.add(new MenuItem("Garlic Bread", "Toasted bread with garlic butter", "220.00", "pizza", "🍞"));
        items.add(new MenuItem("Quattro Formaggi", "Four cheese pizza", "520.00", "pizza", "🍕"));
        items.add(new MenuItem("Italian Soda", "Refreshing Italian soda", "120.00", "drinks", "🥤"));
        items.add(new MenuItem("Limoncello", "Italian lemon liqueur", "250.00", "drinks", "🍸"));
        return items;
    }

    private List<MenuItem> createJuiceBarMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Orange Juice", "Freshly squeezed orange juice", "120.00", "drinks", "🧃"));
        items.add(new MenuItem("Mango Smoothie", "Creamy mango smoothie", "180.00", "drinks", "🥭"));
        items.add(new MenuItem("Berry Blast", "Mixed berry smoothie", "200.00", "drinks", "🍓"));
        items.add(new MenuItem("Green Detox", "Kale, spinach, and apple juice", "150.00", "drinks", "🥬"));
        items.add(new MenuItem("Protein Shake", "Chocolate protein shake", "220.00", "drinks", "🥛"));
        items.add(new MenuItem("Iced Coffee", "Cold brewed coffee", "140.00", "drinks", "☕"));
        return items;
    }

    private void setMenuItems(String restaurantId) {
        RestaurantData data = restaurantMap.get(restaurantId);
        if (data == null) {
            data = restaurantMap.get("htown");
        }

        if (data == null) return;

        // Clear existing items
        allMenuItems.clear();
        allMenuItems.addAll(data.menuItems);

        // Create menu cards for all items
        createMenuCards(allMenuItems);

        // Show all items initially
        showAllItems();
    }

    private void createMenuCards(List<MenuItem> items) {
        // Clear containers
        pizzaItemsContainer.removeAllViews();
        burgerItemsContainer.removeAllViews();
        drinksItemsContainer.removeAllViews();

        // Sort items into categories
        for (MenuItem item : items) {
            CardView cardView = createMenuItemCard(item);

            switch (item.category) {
                case "pizza":
                    pizzaItemsContainer.addView(cardView);
                    break;
                case "burger":
                    burgerItemsContainer.addView(cardView);
                    break;
                case "drinks":
                    drinksItemsContainer.addView(cardView);
                    break;
            }
        }
    }

    private CardView createMenuItemCard(MenuItem item) {
        // Inflate the card layout
        CardView cardView = (CardView) LayoutInflater.from(this)
                .inflate(R.layout.menu_item_card, null);

        TextView itemName = cardView.findViewById(R.id.menuItemName);
        TextView itemDescription = cardView.findViewById(R.id.menuItemDescription);
        TextView itemPrice = cardView.findViewById(R.id.menuItemPrice);
        TextView itemEmoji = cardView.findViewById(R.id.menuItemEmoji);
        Button addToCartButton = cardView.findViewById(R.id.btnAddToCartMenuItem);

        itemName.setText(item.name);
        itemDescription.setText(item.description);
        itemPrice.setText(item.price + " ETB");
        itemEmoji.setText(item.emoji);

        addToCartButton.setOnClickListener(v -> {
            addToCart(item.name, item.price);
        });

        return cardView;
    }

    private void setupCategoryButtons() {
        // Reset all category backgrounds
        resetCategoryBackgrounds();

        // Set click listeners
        categoryAll.setOnClickListener(v -> {
            resetCategoryBackgrounds();
            categoryAll.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            categoryAll.setTextColor(getResources().getColor(android.R.color.white));
            showAllItems();
        });

        categoryPizza.setOnClickListener(v -> {
            resetCategoryBackgrounds();
            categoryPizza.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            categoryPizza.setTextColor(getResources().getColor(android.R.color.white));
            showPizzaItems();
        });

        categoryBurger.setOnClickListener(v -> {
            resetCategoryBackgrounds();
            categoryBurger.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            categoryBurger.setTextColor(getResources().getColor(android.R.color.white));
            showBurgerItems();
        });

        categoryDrinks.setOnClickListener(v -> {
            resetCategoryBackgrounds();
            categoryDrinks.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            categoryDrinks.setTextColor(getResources().getColor(android.R.color.white));
            showDrinksItems();
        });
    }

    private void resetCategoryBackgrounds() {
        int grayColor = getResources().getColor(android.R.color.darker_gray);
        int whiteColor = getResources().getColor(android.R.color.white);

        categoryAll.setBackgroundColor(grayColor);
        categoryAll.setTextColor(whiteColor);

        categoryPizza.setBackgroundColor(grayColor);
        categoryPizza.setTextColor(whiteColor);

        categoryBurger.setBackgroundColor(grayColor);
        categoryBurger.setTextColor(whiteColor);

        categoryDrinks.setBackgroundColor(grayColor);
        categoryDrinks.setTextColor(whiteColor);
    }

    private void showAllItems() {
        pizzaItemsContainer.setVisibility(View.VISIBLE);
        burgerItemsContainer.setVisibility(View.VISIBLE);
        drinksItemsContainer.setVisibility(View.VISIBLE);
    }

    private void showPizzaItems() {
        pizzaItemsContainer.setVisibility(View.VISIBLE);
        burgerItemsContainer.setVisibility(View.GONE);
        drinksItemsContainer.setVisibility(View.GONE);
    }

    private void showBurgerItems() {
        pizzaItemsContainer.setVisibility(View.GONE);
        burgerItemsContainer.setVisibility(View.VISIBLE);
        drinksItemsContainer.setVisibility(View.GONE);
    }

    private void showDrinksItems() {
        pizzaItemsContainer.setVisibility(View.GONE);
        burgerItemsContainer.setVisibility(View.GONE);
        drinksItemsContainer.setVisibility(View.VISIBLE);
    }

    // Keep existing setupAddToCartButtons method for static items
    private void setupAddToCartButtons() {
        Button btnAddCart1 = findViewById(R.id.btnAddCart1);
        Button btnAddCart2 = findViewById(R.id.btnAddCart2);
        Button btnAddCart3 = findViewById(R.id.btnAddCart3);

        if (btnAddCart1 != null) {
            btnAddCart1.setOnClickListener(v -> {
                TextView itemName = findViewById(R.id.itemName1);
                TextView itemPrice = findViewById(R.id.itemPrice1);
                String priceText = itemPrice.getText().toString().replace(" ETB", "");
                addToCart(itemName.getText().toString(), priceText);
            });
        }

        if (btnAddCart2 != null) {
            btnAddCart2.setOnClickListener(v -> {
                TextView itemName = findViewById(R.id.itemName2);
                TextView itemPrice = findViewById(R.id.itemPrice2);
                String priceText = itemPrice.getText().toString().replace(" ETB", "");
                addToCart(itemName.getText().toString(), priceText);
            });
        }

        if (btnAddCart3 != null) {
            btnAddCart3.setOnClickListener(v -> {
                TextView itemName = findViewById(R.id.itemName3);
                TextView itemPrice = findViewById(R.id.itemPrice3);
                String priceText = itemPrice.getText().toString().replace(" ETB", "");
                addToCart(itemName.getText().toString(), priceText);
            });
        }
    }

    private void addToCart(String itemName, String itemPrice) {
        CartManager.getInstance().addItem(new CartItem(itemName, itemPrice, 1));
        Toast.makeText(this, itemName + " added to cart", Toast.LENGTH_SHORT).show();
    }

    // Helper class for restaurant data
    private static class RestaurantData {
        String rating;
        String foodType;
        String deliveryTime;
        String minOrder;
        List<MenuItem> menuItems;

        RestaurantData(String rating, String foodType, String deliveryTime, String minOrder,
                       List<MenuItem> menuItems) {
            this.rating = rating;
            this.foodType = foodType;
            this.deliveryTime = deliveryTime;
            this.minOrder = minOrder;
            this.menuItems = menuItems;
        }
    }

    // Helper class for menu items
    private static class MenuItem {
        String name;
        String description;
        String price;
        String category; // "pizza", "burger", "drinks"
        String emoji;

        MenuItem(String name, String description, String price, String category, String emoji) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
            this.emoji = emoji;
        }
    }
}
