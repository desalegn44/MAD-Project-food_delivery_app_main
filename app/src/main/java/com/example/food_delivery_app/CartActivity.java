package com.example.food_delivery_app;

import android.app.AlertDialog;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView subtotalAmount, deliveryFeeAmount, taxAmount, totalAmount;
    private CartManager cartManager;
    private DecimalFormat decimalFormat;

    private String orderNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        cartManager = CartManager.getInstance();
        decimalFormat = new DecimalFormat("#,##0.00");

        cartItemsContainer = findViewById(R.id.cartItemsContainer);
        subtotalAmount = findViewById(R.id.subtotalAmount);
        deliveryFeeAmount = findViewById(R.id.deliveryFeeAmount);
        taxAmount = findViewById(R.id.taxAmount);
        totalAmount = findViewById(R.id.totalAmount);

        ImageButton backButton = findViewById(R.id.backButton);
        Button placeOrderButton = findViewById(R.id.placeOrderButton);
        Button trackOrderButton = findViewById(R.id.trackOrderButton);

        // Track button disabled until order is placed
        trackOrderButton.setEnabled(false);

        backButton.setOnClickListener(v -> finish());

        placeOrderButton.setOnClickListener(v -> showOrderConfirmation());

        trackOrderButton.setOnClickListener(v -> navigateToTracking());

        loadCartItems();
        updateTotals();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    private void loadCartItems() {
        cartItemsContainer.removeAllViews();
        List<CartItem> items = cartManager.getCartItems();

        if (items.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("Your cart is empty");
            emptyText.setTextSize(16);
            emptyText.setPadding(0, 100, 0, 0);
            cartItemsContainer.addView(emptyText);
        } else {
            for (int i = 0; i < items.size(); i++) {
                View itemView = createCartItemView(items.get(i), i);
                cartItemsContainer.addView(itemView);
            }
        }
    }

    private View createCartItemView(CartItem item, int position) {
        View itemView = LayoutInflater.from(this)
                .inflate(R.layout.cart_item, cartItemsContainer, false);

        TextView itemImage = itemView.findViewById(R.id.itemImage);
        TextView itemName = itemView.findViewById(R.id.itemName);
        TextView itemQuantity = itemView.findViewById(R.id.itemQuantity);
        TextView itemPrice = itemView.findViewById(R.id.itemPrice);
        Button btnDecrease = itemView.findViewById(R.id.btnDecrease);
        Button btnIncrease = itemView.findViewById(R.id.btnIncrease);
        Button btnRemove = itemView.findViewById(R.id.btnRemove);

        itemName.setText(item.getName());
        itemQuantity.setText(String.valueOf(item.getQuantity()));
        itemPrice.setText("+ " + decimalFormat.format(item.getTotalPrice()));
        itemImage.setText(getEmojiForItem(item.getName()));

        btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.decreaseQuantity();
                refreshCart();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            item.increaseQuantity();
            refreshCart();
        });

        btnRemove.setOnClickListener(v -> {
            cartManager.removeItem(position);
            refreshCart();
        });

        return itemView;
    }

    private void refreshCart() {
        loadCartItems();
        updateTotals();
    }

    private void updateTotals() {
        double subtotal = cartManager.calculateSubtotal();

        // Free delivery for orders above 500 ETB
        double deliveryFee = subtotal >= 500 ? 0 : 60;
        double tax = subtotal * 0.15;
        double total = subtotal + deliveryFee + tax;

        subtotalAmount.setText(decimalFormat.format(subtotal) + " ETB");
        deliveryFeeAmount.setText(decimalFormat.format(deliveryFee) + " ETB");
        taxAmount.setText(decimalFormat.format(tax) + " ETB");
        totalAmount.setText(decimalFormat.format(total) + " ETB");
    }

    private void showOrderConfirmation() {
        if (cartManager.getItemCount() == 0) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Order")
                .setMessage("Do you want to place this order?")
                .setPositiveButton("Yes", (dialog, which) -> placeOrder())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void placeOrder() {
        orderNumber = "FD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Toast.makeText(this,
                "Order #" + orderNumber + " placed successfully!",
                Toast.LENGTH_LONG).show();

        cartManager.clearCart();
        refreshCart();

        Button trackOrderButton = findViewById(R.id.trackOrderButton);
        trackOrderButton.setEnabled(true);
        trackOrderButton.setBackgroundColor(
                getResources().getColor(android.R.color.holo_green_dark)
        );
    }

    private void navigateToTracking() {
        if (orderNumber.isEmpty()) {
            Toast.makeText(this, "Please place an order first", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, OrderTrackingActivity.class);
        intent.putExtra("ORDER_NUMBER", orderNumber);

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 15);
        intent.putExtra("ESTIMATED_TIME",
                sdf.format(calendar.getTime()) + " (15min)");

        intent.putExtra("DRIVER_NAME", "Abebe Kebede");
        intent.putExtra("DRIVER_PHONE", "+251911223344");
        intent.putExtra("VEHICLE", "Dodai Model T6+ [electric]");

        startActivity(intent);
    }

    private String getEmojiForItem(String name) {
        name = name.toLowerCase();
        if (name.contains("burger")) return "🍔";
        if (name.contains("pizza")) return "🍕";
        if (name.contains("cola") || name.contains("coke")) return "🥤";
        if (name.contains("chicken")) return "🍗";
        if (name.contains("cheese")) return "🧀";
        if (name.contains("juice")) return "🧃";
        if (name.contains("sushi")) return "🍣";
        return "🍽️";
    }
}
