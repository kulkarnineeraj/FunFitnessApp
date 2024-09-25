package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.funfitnessblender.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EditClientDetails extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName,etAmount, etDate, etAge, etEmail, etAddress, etParentFirstName, etParentLastName, etPhone, etReferralName,etBirthDate;
    private AutoCompleteTextView autoCompleteStatus, autoCompleteProgram,  autoCompleteInterest, autoCompleteReferral,autoCompleteArea;
    private AppCompatButton btnSave;

    private ImageView backBtn;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference programDatabaseReference, areaDatabaseReference;
    private DatabaseReference clientDatabaseReference;

    private ArrayAdapter<String> programAdapter;
    private List<String> programList;

    private ArrayAdapter<String> areaAdapter;
    private List<String> areaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_details);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        clientDatabaseReference = firebaseDatabase.getReference("client");
        programDatabaseReference = firebaseDatabase.getReference("programs");

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etAge = findViewById(R.id.etAge);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etParentFirstName = findViewById(R.id.etParentFirstName);
        etBirthDate = findViewById(R.id.etBirthDate);
        etPhone = findViewById(R.id.etPhone);
        etReferralName = findViewById(R.id.etReferralName);
        autoCompleteStatus = findViewById(R.id.autoCompleteStatus);
        autoCompleteProgram = findViewById(R.id.autoCompleteProgram);
        autoCompleteInterest = findViewById(R.id.autoCompleteInterest);
        autoCompleteReferral = findViewById(R.id.autoCompleteReferral);
        autoCompleteArea = findViewById(R.id.autoCompleteArea);
        backBtn = findViewById(R.id.backBtn);

        // Get data from the Intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String fname = intent.getStringExtra("fname");
        String lname = intent.getStringExtra("lname");
        String area = intent.getStringExtra("area");
        String enquiryDate = intent.getStringExtra("enquiryDate");
        String program = intent.getStringExtra("program");
        String age = intent.getStringExtra("age");
        String status = intent.getStringExtra("status");
        String interest = intent.getStringExtra("interest");
        String referredBy = intent.getStringExtra("referredBy");
        String referralName = intent.getStringExtra("referredName");
        String parentFirstName = intent.getStringExtra("parentFirstName");
        String birthDate = intent.getStringExtra("birthDate");
        String mobile = intent.getStringExtra("mobile");
        String amount = intent.getStringExtra("amount");
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the current activity and returns to the previous one
            }
        });



        // Set the values to the views
        etFirstName.setText(fname != null ? fname : "");
        etLastName.setText(lname != null ? lname : "");
        etAmount.setText(amount != null ? amount : "");
        etDate.setText(enquiryDate != null ? enquiryDate : "");
        etAge.setText(age != null ? age : "");
        etEmail.setText(email != null ? email : "");
        etAddress.setText(address != null ? address : "");
        etPhone.setText(mobile != null ? mobile : "");
        etParentFirstName.setText(parentFirstName != null ? parentFirstName : "");
        etBirthDate.setText(birthDate != null ? birthDate : "");
        etReferralName.setText(referralName != null ? referralName : "");
        autoCompleteProgram.setText(program != null ? program : "");
        autoCompleteStatus.setText(status != null ? status : "");
        autoCompleteInterest.setText(interest != null ? interest : "");
        autoCompleteReferral.setText(referredBy != null ? referredBy : "");
        autoCompleteArea.setText(area != null ? area : "");

        etDate.setOnClickListener(v -> showDatePickerDialog());
        etBirthDate.setOnClickListener(v -> showBirthDatePickerDialog());

        setupDropdowns();


        // Handling Save button click
        AppCompatButton btnSave = findViewById(R.id.btnUpdate);
        btnSave.setOnClickListener(v -> updateClient(id));
    }

    private void setupDropdowns() {
        // Status Dropdown
        String[] statusOptions = {"Hot Enquiry", "Cold Enquiry"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusOptions);
        autoCompleteStatus.setAdapter(statusAdapter);
        autoCompleteStatus.setOnClickListener(view -> autoCompleteStatus.showDropDown());

        // Program Dropdown
        programList = new ArrayList<>();
        programAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, programList);
        autoCompleteProgram.setAdapter(programAdapter);
        autoCompleteProgram.setOnClickListener(view -> autoCompleteProgram.showDropDown());

        // Fetch programs from Firebase and populate the dropdown
        fetchProgramsFromDatabase();


        // Program Dropdown
        areaList = new ArrayList<>();
        areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, areaList);
        autoCompleteArea.setAdapter(areaAdapter);
        autoCompleteArea.setOnClickListener(view -> autoCompleteArea.showDropDown());

        // Fetch programs from Firebase and populate the dropdown
        fetchAreaFromDatabase();

        // Interest Dropdown
        String[] interestOptions = {"New Enquiry", "Joined", "Left"};
        ArrayAdapter<String> interestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, interestOptions);
        autoCompleteInterest.setAdapter(interestAdapter);
        autoCompleteInterest.setOnClickListener(view -> autoCompleteInterest.showDropDown());

        // Referral Dropdown
        String[] referralOptions = {"Google", "Person", "Flyer", "Street Board", "Instagram Ad"};
        ArrayAdapter<String> referralAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, referralOptions);
        autoCompleteReferral.setAdapter(referralAdapter);
        autoCompleteReferral.setOnClickListener(view -> autoCompleteReferral.showDropDown());
    }

    private void fetchProgramsFromDatabase() {
        DatabaseReference programDatabaseReference = FirebaseDatabase.getInstance().getReference("Programs");
        programDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> programNames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String programName = snapshot.getValue(String.class);
                    if (programName != null) {
                        programNames.add(programName);
                    }
                }
                ArrayAdapter<String> programAdapter = new ArrayAdapter<>(
                        EditClientDetails.this,
                        android.R.layout.simple_dropdown_item_1line,
                        programNames
                );
                autoCompleteProgram.setAdapter(programAdapter);
                autoCompleteProgram.setOnClickListener(view -> autoCompleteProgram.showDropDown());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditClientDetails.this, "Failed to load programs", Toast.LENGTH_SHORT).show();
                Log.e("FetchPrograms", "Error fetching programs", databaseError.toException());
            }
        });
    }

    private void fetchAreaFromDatabase() {
        DatabaseReference areaDatabaseReference = FirebaseDatabase.getInstance().getReference("Areas");
        areaDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> areaNames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String areaName = snapshot.getValue(String.class);
                    if (areaName != null) {
                        areaNames.add(areaName);
                    }
                }
                ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(
                        EditClientDetails.this,
                        android.R.layout.simple_dropdown_item_1line,
                        areaNames
                );
                autoCompleteArea.setAdapter(areaAdapter);
                autoCompleteArea.setOnClickListener(view -> autoCompleteArea.showDropDown());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditClientDetails.this, "Failed to load areas", Toast.LENGTH_SHORT).show();
                Log.e("FetchAreas", "Error fetching areas", databaseError.toException());
            }
        });
    }

    private void updateClient(String clientId) {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();

        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String parentFirstName = etParentFirstName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String status = autoCompleteStatus.getText().toString().trim();
        String program = autoCompleteProgram.getText().toString().trim();
        String area = autoCompleteArea.getText().toString().trim();
        String interest = autoCompleteInterest.getText().toString().trim();
        String referral = autoCompleteReferral.getText().toString().trim();
        String referralName = etReferralName.getText().toString().trim();

        // HashMap to store client details
        HashMap<String, Object> clientDetails = new HashMap<>();
        clientDetails.put("firstName", firstName);
        clientDetails.put("lastName", lastName);
        clientDetails.put("age", age);
        clientDetails.put("email", email);
        clientDetails.put("address", address);
        clientDetails.put("date", date);
        clientDetails.put("birthDate", birthDate);

        clientDetails.put("parentFirstName", parentFirstName);
        clientDetails.put("phone", phone);
        clientDetails.put("amount", amount);
        clientDetails.put("status", status);
        clientDetails.put("program", program);
        clientDetails.put("area", area);
        clientDetails.put("interest", interest);
        clientDetails.put("referral", referral);
        clientDetails.put("referralName", referralName);



        // Update in Firebase
        clientDatabaseReference.child(clientId).updateChildren(clientDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditClientDetails.this, "Client updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditClientDetails.this, DetailedDataActivity.class);
                intent.putExtra("clientId", clientId);

                intent.putExtra("name", firstName + " " + lastName);
                intent.putExtra("area", area);
                intent.putExtra("enquiryDate", date);
                intent.putExtra("program", program);
                intent.putExtra("age", age);
                intent.putExtra("status", status);
                intent.putExtra("interest", interest);
                intent.putExtra("referredBy", referral);
                intent.putExtra("referredName", referralName);
                intent.putExtra("parentFirstName", parentFirstName);
                intent.putExtra("birthDate", birthDate);
                intent.putExtra("mobile", phone);
                intent.putExtra("amount", amount);
                intent.putExtra("email", email);
                intent.putExtra("address", address);
                startActivity(intent);

            } else {
                Toast.makeText(EditClientDetails.this, "Failed to update client", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditClientDetails.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Display date in yyyy-MM-dd format to the user
                    String selectedDate = String.format("%d-%02d-%02d", year1, (monthOfYear + 1), dayOfMonth);
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }


    private void showBirthDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditClientDetails.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Display date in yyyy-MM-dd format to the user
                    String selectedDate = String.format("%d-%02d-%02d", year1, (monthOfYear + 1), dayOfMonth);
                    etBirthDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }



}