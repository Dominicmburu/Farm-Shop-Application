package com.example.farmshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.ViewHolder> {
    private List<Developer> developerList;

    public DeveloperAdapter(List<Developer> developerList) {
        this.developerList = developerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.developers_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Developer developer = developerList.get(position);
        holder.bind(developer);
    }

    @Override
    public int getItemCount() {
        return developerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView devImage;
        private TextView devName, devRole, devDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            devImage = itemView.findViewById(R.id.dev_image);
            devName = itemView.findViewById(R.id.dev_name);
            devRole = itemView.findViewById(R.id.dev_role);
            devDescription = itemView.findViewById(R.id.dev_description);
        }

        public void bind(Developer developer) {
            devName.setText(developer.getName());
            devRole.setText(developer.getRole());
            devDescription.setText(developer.getDescription());

            // Load the developer's image from the drawable resources
            Context context = itemView.getContext();
            int imageResourceId = context.getResources().getIdentifier(
                    developer.getName().toLowerCase(),
                    "drawable",
                    context.getPackageName()
            );

            if (imageResourceId != 0) {
                devImage.setImageResource(imageResourceId);
            } else {
                // Handle the case where the image is not found in resources
                // You can set a placeholder or default image here
                devImage.setImageResource(R.drawable.baseline_photo_camera_back_24);
            }
        }
    }
}
