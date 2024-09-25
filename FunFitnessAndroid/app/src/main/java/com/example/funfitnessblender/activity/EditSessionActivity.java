package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funfitnessblender.R;
import com.example.funfitnessblender.models.Client;
import com.example.funfitnessblender.models.Session;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditSessionActivity extends AppCompatActivity {

    private AppCompatButton btnUpdate, btnDelete;

    private ImageView backBtn;
    private AutoCompleteTextView autoCompleteStatus;
    private TextView tvName, tvProgram;
    private TextInputEditText etDate;
    private String clientId, program, sessionId, sessionDate, status, name;

    private DatabaseReference sessionDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session);

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
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Back button functionality
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(EditSessionActivity.this, SessionActivity.class);
            startActivity(intent);
        });

        // Retrieve data from Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name") != null ? intent.getStringExtra("name") : "N/A";
        clientId = intent.getStringExtra("clientId") != null ? intent.getStringExtra("clientId") : "N/A";
        program = intent.getStringExtra("program") != null ? intent.getStringExtra("program") : "N/A";
        sessionId = intent.getStringExtra("sessionId") != null ? intent.getStringExtra("sessionId") : "N/A";
        sessionDate = intent.getStringExtra("sessionDate") != null ? intent.getStringExtra("sessionDate") : "N/A";
        status = intent.getStringExtra("status") != null ? intent.getStringExtra("status") : "N/A";


        // Set data to views
        tvName.setText(name);
        tvProgram.setText(program);
        etDate.setText(sessionDate);
        autoCompleteStatus.setText(status);

        // Date picker dialog for selecting date
        etDate.setOnClickListener(v -> showDatePickerDialog());


        // Status Dropdown
        String[] statusOptions = {"Done", "Not Done"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusOptions);
        autoCompleteStatus.setAdapter(statusAdapter);
        autoCompleteStatus.setOnClickListener(view -> autoCompleteStatus.showDropDown());

        btnUpdate.setOnClickListener(v -> updateSessionData());
        // Delete button functionality
        btnDelete.setOnClickListener(v -> deleteSessionData());

    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditSessionActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format("%d-%02d-%02d", year1, (monthOfYear + 1), dayOfMonth);
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }


    private void updateSessionData() {
        // Get updated values from input fields
        String updatedDate = etDate.getText().toString().trim();
        String updatedStatus = autoCompleteStatus.getText().toString().trim();

        // Check for empty fields
        if (updatedDate.isEmpty() || updatedStatus.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Session object with updated values
        Session updatedSession = new Session(sessionId, clientId, program, updatedDate, updatedStatus);

        // Update the session data in Firebase
        sessionDatabase.child(sessionId).setValue(updatedSession)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Show a success message
                        Toast.makeText(this, "Session updated successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditSessionActivity.this, SessionDetails.class);
                        intent.putExtra("clientId", clientId);  // Assuming `getId()` method exists in `Client` class
                        intent.putExtra("name", name);

                        intent.putExtra("program", program);

                        startActivity(intent);
                    } else {
                        // Handle the error
                        Toast.makeText(this, "Failed to update session. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteSessionData() {
        // Perform delete operation in Firebase
        if (sessionId != null) {
            sessionDatabase.child(sessionId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditSessionActivity.this, "Session deleted successfully", Toast.LENGTH_SHORT).show();
                        // Redirect to session list or any other screen
                        Intent intent = new Intent(EditSessionActivity.this, SessionDetails.class);
                        intent.putExtra("clientId", clientId);
                        intent.putExtra("name", name);

                        intent.putExtra("program", program);

                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditSessionActivity.this, "Failed to delete session", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Invalid session ID", Toast.LENGTH_SHORT).show();
        }
    }

}

