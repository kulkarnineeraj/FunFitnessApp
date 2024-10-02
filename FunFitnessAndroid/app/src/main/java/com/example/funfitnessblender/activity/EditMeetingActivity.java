package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.funfitnessblender.DetailedMeetingActivity;
import com.example.funfitnessblender.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class EditMeetingActivity extends AppCompatActivity {

    private ImageView backBtn;
    private DatabaseReference meetingDatabaseReference;

    private FirebaseDatabase firebaseDatabase;

    private TextInputEditText etDate, etPersonName, etCompanyName, etMotive, etResult;
    private AutoCompleteTextView autoCompleteStatus, autoCompletePotential;
    private AppCompatButton btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseDatabase = FirebaseDatabase.getInstance();
        meetingDatabaseReference = firebaseDatabase.getReference("meetings");

        // Retrive client data from previous activity
        // Retrieve client data from previous activity
        String meetingId = getIntent().getStringExtra("meetingId");

        if (meetingId == null) {
            Toast.makeText(this, "Meeting ID is missing!", Toast.LENGTH_SHORT).show();
            return; // Stop further execution
        }

        String personName = getIntent().getStringExtra("personName");
        String date = getIntent().getStringExtra("date");
        String company = getIntent().getStringExtra("company");
        String status = getIntent().getStringExtra("status");
        String potential = getIntent().getStringExtra("potential");
        String result = getIntent().getStringExtra("result");
        String motive = getIntent().getStringExtra("motive");


        etDate = findViewById(R.id.etDate);
        etPersonName = findViewById(R.id.etName);
        etCompanyName = findViewById(R.id.etCompany);
        etMotive = findViewById(R.id.etMotive);
        etResult = findViewById(R.id.etResult);
        autoCompleteStatus = findViewById(R.id.autoCompleteStatus);
        autoCompletePotential = findViewById(R.id.autoCompletePotential);
        btnUpdate = findViewById(R.id.btnUpdate);

        etDate.setOnClickListener(v -> showDatePickerDialog());


        etDate.setText(date != null ? date : "");
        etPersonName.setText(personName != null ? personName : "");
        etCompanyName.setText(company != null ? company : "");
        etMotive.setText(motive != null ? motive : "");
        etResult.setText(result != null ? result : "");
        autoCompleteStatus.setText(status != null ? status : "");
        autoCompletePotential.setText(potential != null ? potential : "");



        // Set all dropdowns
        setupDropdowns();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the current activity and returns to the previous one
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMeetingData(meetingId);
            }
        });





    }

    public void updateMeetingData(String meetingId){




        String personName = etPersonName.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String company = etCompanyName.getText().toString().trim();
        String status = autoCompleteStatus.getText().toString().trim();
        String potential = autoCompletePotential.getText().toString().trim();
        String result = etResult.getText().toString().trim();
        String motive = etMotive.getText().toString().trim();

        HashMap MeetingDetails = new HashMap();


        MeetingDetails.put("personName", personName);
        MeetingDetails.put("date", date);
        MeetingDetails.put("company", company);
        MeetingDetails.put("status", status);
        MeetingDetails.put("potential", potential);
        MeetingDetails.put("result", result);
        MeetingDetails.put("motive", motive);

        meetingDatabaseReference.child(meetingId).updateChildren(MeetingDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditMeetingActivity.this, "Meeting updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditMeetingActivity.this, DetailedMeetingActivity.class);

                intent.putExtra("meetingId", meetingId);
                intent.putExtra("personName", personName);
                intent.putExtra("date", date);
                intent.putExtra("company", company);
                intent.putExtra("status", status);
                intent.putExtra("potential", potential);
                intent.putExtra("motive", motive);
                intent.putExtra("result", result);
                startActivity(intent);
            } else {
                Toast.makeText(EditMeetingActivity.this, "Failed to update Meeting", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupDropdowns() {
        // Status dropdown options
        String[] statusOptions = {"Schedule", "Executed"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusOptions);
        autoCompleteStatus.setAdapter(statusAdapter);
        autoCompleteStatus.setOnClickListener(view -> autoCompleteStatus.showDropDown());

        // Potential dropdown options
        String[] potentialOptions = {"High", "Low", "Follow Up"};
        ArrayAdapter<String> potentialAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, potentialOptions);
        autoCompletePotential.setAdapter(potentialAdapter);
        autoCompletePotential.setOnClickListener(view -> autoCompletePotential.showDropDown());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditMeetingActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format("%d-%02d-%02d", year1, (monthOfYear + 1), dayOfMonth);
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

}