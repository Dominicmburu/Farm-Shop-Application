package com.example.farmshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<ItemModel> items;
    private Context context; // Add a reference to the context

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CustomAdapter(Context context, List<ItemModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item_cardview.xml layout here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data from items to the views in your card view
        ItemModel item = items.get(position);
        holder.bind(item);

        // Handle item clicks and navigate to ItemDetailsActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected item's data
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ItemModel selectedItem = items.get(adapterPosition);

                    if (listener != null){
                        listener.onItemClick(adapterPosition);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemName;
        private TextView itemPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize and find views in the item_cardview.xml layout
            itemImage = itemView.findViewById(R.id.imageViewItem);
            itemName = itemView.findViewById(R.id.textViewName);
            itemPrice = itemView.findViewById(R.id.textViewPrice);
        }

        public void bind(ItemModel item) {
            itemName.setText(item.getName());
            itemPrice.setText(item.getPrice());

            // Load the item's image from the drawable resources
            String formattedProductType = item.getName().toLowerCase().replace(" ", "_");  // Format product type
            int imageResourceId = context.getResources().getIdentifier(formattedProductType, "drawable", context.getPackageName());

            if (imageResourceId != 0) {
                // Load the image and resize it
                Drawable drawable = context.getResources().getDrawable(imageResourceId);
                itemImage.setImageDrawable(drawable);

                // Set the desired width and height for the image
                int desiredWidth = 150; // Set your desired width in pixels
                int desiredHeight = 150; // Set your desired height in pixels

                // Adjust the image size
                itemImage.getLayoutParams().width = desiredWidth;
                itemImage.getLayoutParams().height = desiredHeight;
                itemImage.requestLayout(); // Apply the layout changes
            } else {
                // Handle the case where the image is not found in resources
                // You can set a placeholder or default image here
                itemImage.setImageResource(R.drawable.baseline_photo_camera_back_24);
            }
        }




    }
}
