package com.example.farmshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class ItemDetailsActivity extends AppCompatActivity {

    private TextView itemName, itemPrice, itemDesc;
    private FloatingActionButton buyItemBtn;
    private ImageView itemImage;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        dbHelper = new DatabaseHelper(getApplicationContext(), "FarmAppDB", null, 1);

        // Display the product details in your layout
        itemImage = findViewById(R.id.itemImageDetails);
        itemName = findViewById(R.id.itemNameDetails);
        itemPrice = findViewById(R.id.itemPriceDetails);
        itemDesc = findViewById(R.id.itemDescDetails);
        buyItemBtn = findViewById(R.id.buyItemFab);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "").toString();

        Intent intent = getIntent();
        // Set the values from the selected product
        String imageResourceName = intent.getStringExtra("imageResourceName");

        int imageResourceId = getResources().getIdentifier(imageResourceName, "drawable", getPackageName());
        if (imageResourceId != 0) {
            itemImage.setImageResource(imageResourceId);
        } else {
            itemImage.setImageResource(R.drawable.baseline_photo_camera_back_24);
        }

        itemName.setText(intent.getStringExtra("productName"));
        itemPrice.setText(intent.getStringExtra("productPrice"));
        itemDesc.setText(intent.getStringExtra("productDescription"));


        // Assuming you have the user ID, item ID, quantity, itemImage, itemPrice, and itemName
        buyItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(ItemDetailsActivity.this);
                alertDialog.setMessage("Do you want to buy "+intent.getStringExtra("productName"))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // get the product details from the intent
                                String productName = intent.getStringExtra("productName");
                                String imageResourceName = intent.getStringExtra("imageResourceName");
                                int quantity = 1; // Adjust quantity as needed
                                String priceString = intent.getStringExtra("productPrice");

                                String cleanPrice = priceString.replaceAll("[^0-9]", "");

                                // Check if the item is already in the cart
                                boolean itemInCart = dbHelper.isItemInCart(email, imageResourceName);

                                if (itemInCart) {
                                    Toast.makeText(ItemDetailsActivity.this, productName + " Alredy in the cart", Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        int price = Integer.parseInt(cleanPrice);
                                        long newRowId = dbHelper.insertCartDetails(email, productName, imageResourceName, quantity, cleanPrice);

                                        if(newRowId != -1){
                                            Toast.makeText(ItemDetailsActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ItemDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(ItemDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Later",null)
                        .show();

            }
        });

    }


}
