package com.example.farmshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private DatabaseHelper dbHelper;
    private Context context;
    private TextView totalAmountTextView;
    private Button checkout;

    // Constructor to initialize the CartAdapter
    public CartAdapter(List<CartItem> cartItems, DatabaseHelper dbHelper, Context context, TextView totalAmountTextView, Button checkout) {
        this.cartItems = cartItems;
        this.dbHelper = dbHelper;
        this.context = context;
        this.totalAmountTextView = totalAmountTextView;
        this.checkout = checkout;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartview, parent, false);
        return new CartViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        // Get the data model based on position
        CartItem cartItem = cartItems.get(position);

        // Bind data to the ViewHolder
        holder.bind(cartItem);

        // Set up click listeners for quantity buttons and delete button
        holder.setupQuantityButtons(cartItem);
        holder.setupDeleteButton(cartItem);

        // Calculate the total amount and update the totalAmountTextView
        int totalAmount = calculateTotalAmount(cartItems);
        totalAmountTextView.setText("Ksh " + String.valueOf(totalAmount));

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalAmount = calculateTotalAmount(cartItems);

                // Open PayActivity and pass total amount
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra("TOTAL_AMOUNT", totalAmount);
                context.startActivity(intent);
            }
        });

    }
    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ViewHolder class to represent each item in the RecyclerView
    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView cartItemImage;
        private TextView cartItemPrice;
        private TextView cartItemName;
        private TextView cartItemQuantity;
        private TextView increaseButton;
        private TextView decreaseButton;
        private ImageButton deleteButton;


        // Constructor to initialize the ViewHolder
        public CartViewHolder(View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.cart_foodimage);
            cartItemPrice = itemView.findViewById(R.id.cart_foodprice);
            cartItemQuantity = itemView.findViewById(R.id.qrt);
            increaseButton = itemView.findViewById(R.id.addQuantity);
            decreaseButton = itemView.findViewById(R.id.subQuantity);
            deleteButton = itemView.findViewById(R.id.delete);
            cartItemName = itemView.findViewById(R.id.cart_foodname);
        }

        // Bind data to the ViewHolder
        public void bind(CartItem cartItem) {
            // Set cart item details in the views
            cartItemPrice.setText("Ksh " + cartItem.getPrice());
            cartItemQuantity.setText(String.valueOf(cartItem.getQuantity()));
            cartItemName.setText(cartItem.getItemImage());

            // Construct the resource identifier for the item image based on its name
            int imageResourceId = itemView.getResources().getIdentifier(cartItem.getItemImage(), "drawable", itemView.getContext().getPackageName());


            // Set the item image
            cartItemImage.setImageResource(imageResourceId);
        }


        // Set up click listeners for quantity buttons
        public void setupQuantityButtons(final CartItem cartItem) {
            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newQuantity = cartItem.getQuantity() + 1;
                    // Update the quantity in the database
                    dbHelper.updateCartItemQuantity(cartItem.getUserEmail(), cartItem.getItemImage(), newQuantity);
                    cartItem.setQuantity(newQuantity); // Update the local cartItem
                    cartItemQuantity.setText(String.valueOf(newQuantity));

                    // Notify the total amount
                    int totalAmount = calculateTotalAmount(cartItems);
                    totalAmountTextView.setText("Ksh "+String.valueOf(totalAmount));

                    notifyItemChanged(getAdapterPosition());
                }
            });

            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newQuantity = cartItem.getQuantity() - 1;
                    if (newQuantity >= 1) {
                        // Update the quantity in the database
                        dbHelper.updateCartItemQuantity(cartItem.getUserEmail(), cartItem.getItemImage(), newQuantity);
                        cartItem.setQuantity(newQuantity); // Update the local cartItem
                        cartItemQuantity.setText(String.valueOf(newQuantity));

                        // Notify the total amount
                        int totalAmount = calculateTotalAmount(cartItems);
                        totalAmountTextView.setText("Ksh "+String.valueOf(totalAmount));


                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });
        }

        public void setupDeleteButton(final CartItem cartItem) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove the item from the cart
                    if (dbHelper.deleteCartItem(cartItem.getUserEmail(), cartItem.getItemImage())) {
                        // Item deleted successfully
                        cartItems.remove(cartItem);
                        notifyDataSetChanged(); // Refresh the RecyclerView
                        int totalAmount = calculateTotalAmount(cartItems);
                        totalAmountTextView.setText(String.valueOf(totalAmount));
                        // Optionally, show a confirmation message
                        Toast.makeText(context, cartItem.getItemName()+" removed from the cart.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Failed to delete the item
                        Toast.makeText(context, "Failed to remove "+cartItem.getItemName()+" from the cart.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private int calculateTotalAmount(List<CartItem> items) {
        int total = 0;
        for (CartItem item : items) {
            int price = Integer.parseInt(item.getPrice().replaceAll("[^0-9]", ""));
            total += price * item.getQuantity();
        }
        return total;
    }
}
