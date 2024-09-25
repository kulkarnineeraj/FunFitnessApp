package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.funfitnessblender.R;
import com.example.funfitnessblender.adapters.AreaAdapter;
import com.example.funfitnessblender.models.Area;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddAreaActivity extends AppCompatActivity {

    private EditText etArea;
    private ImageView addBtn, backBtn;
    private ListView listViewArea;

    private List<Area> areaList;
    private AreaAdapter adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area); // Ensure this is the correct layout

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize UI components
        etArea = findViewById(R.id.etArea);
        addBtn = findViewById(R.id.addBtn);
        backBtn = findViewById(R.id.backBtn); // Add this line to initialize the back button
        listViewArea = findViewById(R.id.listviewArea);

        // Initialize data structures
        areaList = new ArrayList<>();
        adapter = new AreaAdapter(this, areaList);
        listViewArea.setAdapter(adapter);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Areas");

        // Set click listener for the add button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArea();
            }
        });

        // Fetch existing areas from Firebase
        fetchAreas();

        // Set click listener for the back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAreaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addArea() {
        String areaName = etArea.getText().toString().trim();

        if (!areaName.isEmpty()) {
            String id = databaseReference.push().getKey();
            if (id != null) {
                // Create a new Area object
                Area area = new Area(id, areaName);
                // Store the area in Firebase
                databaseReference.child(id).setValue(areaName)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Area added", Toast.LENGTH_SHORT).show();
                            etArea.setText(""); // Clear the input field
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to add area", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Failed to generate ID", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter an area2 name", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAreas() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                areaList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = dataSnapshot.getKey();
                    String areaName = dataSnapshot.getValue(String.class);
                    Area area = new Area(id, areaName);
                    areaList.add(area);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AddAreaActivity.this, "Failed to load areas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


