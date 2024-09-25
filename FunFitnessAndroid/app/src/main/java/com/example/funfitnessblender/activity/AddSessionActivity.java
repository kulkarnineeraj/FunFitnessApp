package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funfitnessblender.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddSessionActivity extends AppCompatActivity {

    private ImageView backBtn;
    private AutoCompleteTextView autoCompleteStatus;
    private TextView tvName, tvProgram;
    private TextInputEditText etDate;
    private String clientId, program;

    // Firebase database reference
    private DatabaseReference sessionDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);

               // Hide the action bar and status bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize Firebase database reference
        sessionDatabase = FirebaseDatabase.getInstance().getReference("sessions");

        etDate = findViewById(R.id.etDate);
        backBtn = findViewById(R.id.backBtn);
        tvName = findViewById(R.id.tvName);
        tvProgram = findViewById(R.id.tvProgram);
        autoCompleteStatus = findViewById(R.id.autoCompleteStatus);

        // Back button functionality
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AddSessionActivity.this, SessionActivity.class);
            startActivity(intent);
        });

        // Retrieve data from Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name") != null ? intent.getStringExtra("name") : "N/A";
        clientId = intent.getStringExtra("clientId") != null ? intent.getStringExtra("clientId") : "N/A";
        program = intent.getStringExtra("program") != null ? intent.getStringExtra("program") : "N/A";

        // Set data to views
        tvName.setText(name);
        tvProgram.setText(program);

        // Date picker dialog for selecting date
        etDate.setOnClickListener(v -> showDatePickerDialog());

        // Status Dropdown
        String[] statusOptions = {"Done", "Not Done"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusOptions);
        autoCompleteStatus.setAdapter(statusAdapter);
        autoCompleteStatus.setOnClickListener(view -> autoCompleteStatus.showDropDown());

        // Save button functionality
        findViewById(R.id.btnSave).setOnClickListener(v -> saveSessionData());
    }

    // Show the date picker dialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddSessionActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format("%d-%02d-%02d", year1, (monthOfYear + 1), dayOfMonth);
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    // Save session data to Firebase
    private void saveSessionData() {
        String sessionDate = etDate.getText().toString();
        String status = autoCompleteStatus.getText().toString();

        // Validate fields
        if (sessionDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (status.isEmpty()) {
            Toast.makeText(this, "Please select a status", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data to save
        String sessionId = sessionDatabase.push().getKey(); // Generate a unique key for each session
        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("sessionId", sessionId);
        sessionData.put("clientId", clientId);
        sessionData.put("program", program);
        sessionData.put("sessionDate", sessionDate);
        sessionData.put("status", status);

        // Save the data to Firebase under  unique session ID
        if (sessionId != null) {
            sessionDatabase.child(sessionId).setValue(sessionData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddSessionActivity.this, "Session saved successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity after saving
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddSessionActivity.this, "Failed to save session", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
