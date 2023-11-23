package com.example.farmshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView totalAmountTextView;
    private List<CartItem> cartItems;
    private Button checkout;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCartList);
        totalAmountTextView = findViewById(R.id.total);
        checkout = findViewById(R.id.checkout);

        // Retrieve the user's email from SharedPreferences (or any other method you use)
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // Retrieve cart items for the user
        dbHelper = new DatabaseHelper(this, "FarmAppDB", null, 1);
        cartItems = dbHelper.getCartItems(userEmail);

        // Initialize the RecyclerView and set the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems,dbHelper, this, totalAmountTextView, checkout);
        recyclerView.setAdapter(cartAdapter);
    }


}
