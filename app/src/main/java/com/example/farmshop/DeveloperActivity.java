package com.example.farmshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DeveloperActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        List<Developer> developerList = new ArrayList<>();
        developerList.add(new Developer("CodeCraftPro", "Team Leader", "A passionate technology leader with a strong vision for our project. CodeCraftPro has an extensive background in software development and leads the team with dedication and enthusiasm."));
        developerList.add(new Developer("SteveGathi", "Developer", "SteveGathi is an experienced Android developer with a knack for writing clean and efficient code. He brings innovative solutions to the table and is always up for challenging tasks."));
        developerList.add(new Developer("GedionKiprotich", "Designer", "Gedion is a creative designer with a keen eye for beautiful user interfaces. With a deep understanding of user experience, he ensures our app is visually appealing and user-friendly."));
        developerList.add(new Developer("TilliKiptiken", "Tester", "Tilli is a dedicated tester who meticulously ensures our app is bug-free. With a rigorous testing process, Tilli guarantees a smooth and error-free user experience."));
        developerList.add(new Developer("Mayai", "Marketing", "Mayai is our marketing expert, dedicated to promoting our app to the world. With a deep understanding of digital marketing strategies, Mayai spreads the word about our app and engages with our audience."));


        recyclerView = findViewById(R.id.recyclerViewDeveloperList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a custom adapter for developers and set it to the RecyclerView
        DeveloperAdapter adapter = new DeveloperAdapter(developerList);
        recyclerView.setAdapter(adapter);
    }
}