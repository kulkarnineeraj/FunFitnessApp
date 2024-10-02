package com.example.funfitnessblender;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.funfitnessblender.activity.MainActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddMarketingFragment extends Fragment {


    private AutoCompleteTextView autoCompleteMonth;
    private TextInputEditText etMarketingStrategy, etExpense, etResponseDetails;
    private AppCompatButton btnSave, marketingListButton; // Declare marketingListButton
    private ImageView backBtn;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference marketingDatabaseReference;

    public AddMarketingFragment() {
        // Required empty public constructor
    }

    public static AddMarketingFragment newInstance() {
        return new AddMarketingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marketingDatabaseReference = FirebaseDatabase.getInstance().getReference("marketing");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_marketing, container, false);


        autoCompleteMonth = view.findViewById(R.id.autoCompleteMonth);
        etMarketingStrategy = view.findViewById(R.id.etStrategy);
        etExpense = view.findViewById(R.id.etExpense);
        etResponseDetails = view.findViewById(R.id.etResponse);
        btnSave = view.findViewById(R.id.btnSave);
        backBtn = view.findViewById(R.id.backBtn);

        // Setup dropdowns
        setupDropdowns();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMarketingData();
            }
        });

        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed(); // Trigger back navigation
            }
        });


        return view;
    }

    private void setupDropdowns() {
        // Status dropdown options
        String[] monthOptions = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, monthOptions);
        autoCompleteMonth.setAdapter(monthAdapter);


    }


    private void saveMarketingData() {
        // Convert the date to a suitable format for sorting


        String month = autoCompleteMonth.getText() != null ? autoCompleteMonth.getText().toString().trim() : "";
        String strategy = etMarketingStrategy.getText() != null ? etMarketingStrategy.getText().toString().trim() : "";
        String expense = etExpense.getText() != null ? etExpense.getText().toString().trim() : "";
        String responseDetails = etResponseDetails.getText() != null ? etResponseDetails.getText().toString().trim() : "";

        // Create a unique key for each meeting
        String marketingId = marketingDatabaseReference.push().getKey();

        if (marketingId == null) {
            Toast.makeText(getContext(), "Failed to generate marketing ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map of the meeting data
        Map<String, Object> marketingData = new HashMap<>();
        marketingData.put("marketingId", marketingId);
        marketingData.put("month", month);
        marketingData.put("strategy", strategy);
        marketingData.put("expense", expense);
        marketingData.put("responseDetails", responseDetails);


        // Save to Firebase
        marketingDatabaseReference.child(marketingId).setValue(marketingData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Redirect to MeetingListFragment after successful save
                Toast.makeText(getContext(), "Marketing saved successfully", Toast.LENGTH_SHORT).show();
                redirectToMarketingListFragment();
            } else {
                Toast.makeText(getContext(), "Failed to save Marketing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToMarketingListFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with MeetingListFragment
        fragmentTransaction.replace(R.id.frame_layout, new MarketingListFragment());

        // Optionally add this transaction to the back stack, so the user can navigate back
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

}