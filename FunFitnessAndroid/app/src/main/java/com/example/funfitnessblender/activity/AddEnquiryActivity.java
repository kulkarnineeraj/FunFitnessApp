package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.funfitnessblender.models.Client;
import com.google.android.material.textfield.TextInputEditText;

import com.example.funfitnessblender.R;
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
import java.util.List;

public class AddEnquiryActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName,etAmount, etDate, etAge, etEmail, etAddress, etParentFirstName, etBirthDate, etPhone, etReferralName;
    private AutoCompleteTextView autoCompleteStatus, autoCompleteProgram,  autoCompleteInterest, autoCompleteReferral,autoCompleteArea;
    private Button btnSave;

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
        setContentView(R.layout.activity_add_enquiry);

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
        areaDatabaseReference = firebaseDatabase.getReference("Areas");

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etDate = findViewById(R.id.etDate);
        etAge = findViewById(R.id.etAge);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etParentFirstName = findViewById(R.id.etParentFirstName);
        etBirthDate = findViewById(R.id.etBirthDate);
        etPhone = findViewById(R.id.etPhone);
        etAmount = findViewById(R.id.etAmount);
        etReferralName = findViewById(R.id.etReferralName);
        autoCompleteStatus = findViewById(R.id.autoCompleteStatus);
        autoCompleteProgram = findViewById(R.id.autoCompleteProgram);
        autoCompleteArea = findViewById(R.id.autoCompleteArea);
        autoCompleteInterest = findViewById(R.id.autoCompleteInterest);
        autoCompleteReferral = findViewById(R.id.autoCompleteReferral);
        backBtn =findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEnquiryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSave = findViewById(R.id.btnSave);

        etDate.setOnClickListener(v -> showDatePickerDialog());

        etBirthDate.setOnClickListener(v -> showBirthDatePickerDialog());

        // Set up adapters for dropdowns
        setupDropdowns();

        // Set onClickListener for save button
        btnSave.setOnClickListener(v -> saveClient());
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
                        AddEnquiryActivity.this,
                        android.R.layout.simple_dropdown_item_1line,
                        programNames
                );
                autoCompleteProgram.setAdapter(programAdapter);
                autoCompleteProgram.setOnClickListener(view -> autoCompleteProgram.showDropDown());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddEnquiryActivity.this, "Failed to load programs", Toast.LENGTH_SHORT).show();
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
                        AddEnquiryActivity.this,
                        android.R.layout.simple_dropdown_item_1line,
                        areaNames
                );
                autoCompleteArea.setAdapter(areaAdapter);
                autoCompleteArea.setOnClickListener(view -> autoCompleteArea.showDropDown());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddEnquiryActivity.this, "Failed to load areas", Toast.LENGTH_SHORT).show();
                Log.e("FetchAreas", "Error fetching areas", databaseError.toException());
            }
        });
    }


    private void saveClient() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        //String date = etDate.getText().toString().trim();
        // Convert the date to a suitable format for sorting
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = "";
        String formattedBirthDate = "";

        try {
            Date date = inputFormat.parse(etDate.getText().toString().trim());
            Date bdate = inputFormat.parse(etBirthDate.getText().toString().trim());
            formattedDate = outputFormat.format(date);
            formattedBirthDate = outputFormat.format(bdate);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Please Select Proper Date", Toast.LENGTH_SHORT).show();
            return;
        }
        String age = etAge.getText().toString().trim();
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

        if (firstName.isEmpty() || lastName.isEmpty() ||  age.isEmpty() ||
                phone.isEmpty() ||    program.isEmpty()  ) {
            Toast.makeText(this, "Please fill all the (*) fields ", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = clientDatabaseReference.push().getKey();
        Client client = new Client(id, firstName, lastName, formattedDate, age, email, address, parentFirstName, formattedBirthDate, phone,amount, status, program, area, interest, referral, referralName);
        clientDatabaseReference.child(id).setValue(client).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddEnquiryActivity.this, "Client saved successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddEnquiryActivity.this, EnquiryListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AddEnquiryActivity.this, "Failed to save client", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEnquiryActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showBirthDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEnquiryActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    etBirthDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
