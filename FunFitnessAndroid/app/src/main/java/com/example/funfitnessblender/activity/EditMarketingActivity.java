package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.funfitnessblender.DetailedMarketingActivity;
import com.example.funfitnessblender.DetailedMeetingActivity;
import com.example.funfitnessblender.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditMarketingActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteMonth;
    private TextInputEditText etMarketingStrategy, etExpense, etResponseDetails;
    private AppCompatButton btnUpdate, marketingListButton; // Declare marketingListButton
    private ImageView backBtn;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference marketingDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_marketing);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseDatabase = FirebaseDatabase.getInstance();
        marketingDatabaseReference = firebaseDatabase.getReference("marketing");

        // Retrive client data from previous activity
        // Retrieve client data from previous activity
        String marketingId = getIntent().getStringExtra("marketingId");

        if (marketingId == null) {
            Toast.makeText(this, "Marketing ID is missing!", Toast.LENGTH_SHORT).show();
            return; // Stop further execution
        }

        String month = getIntent().getStringExtra("month");
        String strategy = getIntent().getStringExtra("strategy");
        String expense = getIntent().getStringExtra("expense");
        String responseDetails = getIntent().getStringExtra("responseDetails");


        autoCompleteMonth = findViewById(R.id.autoCompleteMonth);
        etMarketingStrategy = findViewById(R.id.etStrategy);
        etExpense = findViewById(R.id.etExpense);
        etResponseDetails =findViewById(R.id.etResponse);
        btnUpdate = findViewById(R.id.btnUpdate);
        backBtn = findViewById(R.id.backBtn);


        autoCompleteMonth.setText(month != null ? month : "");
        etMarketingStrategy.setText(strategy != null ? strategy : "");
        etExpense.setText(expense != null ? expense : "");
        etResponseDetails.setText(responseDetails != null ? responseDetails : "");


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMarketingData(marketingId);
            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the current activity and returns to the previous one
            }
        });

        // Setup dropdowns
        setupDropdowns();


    }


    private void setupDropdowns() {
        String[] monthOptions = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, monthOptions);

        autoCompleteMonth.setAdapter(monthAdapter);
        autoCompleteMonth.setOnClickListener(view -> autoCompleteMonth.showDropDown()); // Show dropdown on click
    }




    public void updateMarketingData(String marketingId){




        String month = autoCompleteMonth.getText().toString().trim();
        String strategy = etMarketingStrategy.getText().toString().trim();
        String expense = etExpense.getText().toString().trim();
        String responseDetails = etResponseDetails.getText().toString().trim();

        HashMap MarketingDetails = new HashMap();


        MarketingDetails.put("month", month);
        MarketingDetails.put("strategy", strategy);
        MarketingDetails.put("expense", expense);
        MarketingDetails.put("responseDetails", responseDetails);


        marketingDatabaseReference.child(marketingId).updateChildren(MarketingDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditMarketingActivity.this, "Marketing updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditMarketingActivity.this, DetailedMarketingActivity.class);

                intent.putExtra("marketingId", marketingId); // Pass the client ID
                intent.putExtra("month", month);
                intent.putExtra("strategy", strategy);
                intent.putExtra("expense", expense);
                intent.putExtra("responseDetails", responseDetails);

                startActivity(intent);
            } else {
                Toast.makeText(EditMarketingActivity.this, "Failed to update Marketing", Toast.LENGTH_SHORT).show();
            }
        });


    }



}