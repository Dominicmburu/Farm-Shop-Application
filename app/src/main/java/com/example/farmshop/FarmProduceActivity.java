package com.example.farmshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FarmProduceActivity extends AppCompatActivity implements CustomAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ItemModel> listItems;

    private ItemModel itemModelObject;
    private FloatingActionButton cartPage;

    private String[] produceType = {
            "Cabbage", "Carrots", "Pineapple", "Bananas", "Tomatoes",
            "Strawberries", "Potatoes", "Wheat", "Rice", "Eggs", "Spinach", "Milk", "Honey", "BellPeppers",
            "Onions", "Avocados"
    };

    private String[] produceDescription = {
            "Cabbage is a leafy green or purple vegetable that is often used in salads, coleslaw, and stir-fries. It's known for its crisp texture and slightly peppery flavor.",
            "Carrots are root vegetables that come in various colors, most commonly orange. They are sweet and crunchy and are used in salads, soups, and as a snack.",
            "Pineapple is a tropical fruit with a sweet and tangy flavor. It is commonly eaten fresh, used in fruit salads, or as a topping for pizzas and desserts.",
            "Bananas are creamy and sweet fruits that are commonly consumed as a snack or used in smoothies and desserts.",
            "Tomatoes are red or yellow fruits (often used as vegetables) with a juicy and slightly tangy taste. They are used in salads, sauces, and sandwiches.",
            "Strawberries are sweet and juicy red berries often used in desserts, jams, and as a topping for various dishes.",
            "Potatoes are starchy tubers that can be mashed, roasted, or fried. They are a versatile ingredient in various dishes.",
            "Wheat is a staple cereal grain used to make flour for bread, pasta, and a variety of baked goods.",
            "Rice is a staple food in many cultures and comes in various varieties. It is often used as a side dish or base for many dishes.",
            "Eggs are a versatile food product often used in baking, frying, and as an ingredient in many dishes.",
            "Spinach is a leafy green vegetable with a mild, slightly earthy flavor. It is used in salads, smoothies, and cooked dishes.",
            "Milk is a dairy product that serves as a base for various dairy items such as cheese, yogurt, and butter.",
            "Honey is a natural sweetener produced by bees from flower nectar. It is used in a wide range of recipes, from desserts to marinades.",
            "Bell Peppers come in various colors (red, green, yellow, or orange) and are often used in salads, stir-fries, and as a crunchy snack.",
            "Onions are pungent vegetables that are used in a wide range of savory dishes to add flavor and aroma.",
            "Avocados are creamy fruits with a mild, nutty flavor. They are commonly used in guacamole, salads, and as a topping for toast."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_produce);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "").toString();
        Toast.makeText(this, "Welcome "+email, Toast.LENGTH_SHORT).show();

        recyclerView=findViewById(R.id.recyclerViewItemList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        cartPage = findViewById(R.id.cartPage);

        // Populate listItems with ItemModel objects (product data including image URLs)
        populateListItems();

        // Inside your onCreate method
        adapter = new CustomAdapter(this, listItems);
        recyclerView.setAdapter(adapter);

        if(adapter != null) {
            ((CustomAdapter) adapter).setOnItemClickListener(this);

        }

        cartPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });

    }

    // Handle item clicks in the RecyclerView
    // Navigate to ItemDetailsActivity with product details
    public void onItemClick(int position) {
        ItemModel selectedProduct = listItems.get(position);
        Intent intent = new Intent(FarmProduceActivity.this, ItemDetailsActivity.class);

        // Pass individual fields of the selected product
        intent.putExtra("productName", selectedProduct.getName());
        intent.putExtra("productPrice", selectedProduct.getPrice());
        intent.putExtra("productDescription", selectedProduct.getDescription());
        intent.putExtra("imageResourceName", selectedProduct.getName().toLowerCase());

        startActivity(intent);
    }



    // Populate listItems with product data
    private void populateListItems() {
        listItems = new ArrayList<>();

        for (int i = 0; i < produceType.length; i++) {
            String name = produceType[i];
            String description = produceDescription[i];

            // Generate a random price for the product
            int price = new Random().nextInt(151) + 50;

            // Create an image resource identifier based on the product type
            int imageResource = getResources().getIdentifier(
                    name.toLowerCase(), "drawable", getPackageName()
            );

            // Assign an ID based on the position (1-based)
            int id = i + 1;

            ItemModel item = new ItemModel(imageResource, name, "Ksh " + price, description, id);
            listItems.add(item);
        }
    }



}