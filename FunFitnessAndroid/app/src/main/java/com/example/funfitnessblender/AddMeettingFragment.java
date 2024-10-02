package com.example.funfitnessblender;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.button.MaterialButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddMeettingFragment extends Fragment {

    private TextInputEditText etDate, etPersonName, etCompanyName, etMotive, etResult;
    private AutoCompleteTextView autoCompleteStatus, autoCompletePotential;
    private Button btnSave;

    private DatabaseReference databaseReference;

    private ImageView backBtn;

    public AddMeettingFragment() {
        // Required empty public constructor
    }

    public static AddMeettingFragment newInstance() {
        return new AddMeettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("meetings");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_meetting, container, false);

        etDate = view.findViewById(R.id.etDate);
        etPersonName = view.findViewById(R.id.etName);
        etCompanyName = view.findViewById(R.id.etCompany);
        etMotive = view.findViewById(R.id.etMotive);
        etResult = view.findViewById(R.id.etResult);
        autoCompleteStatus = view.findViewById(R.id.autoCompleteStatus);
        autoCompletePotential = view.findViewById(R.id.autoCompletePotential);
        btnSave = view.findViewById(R.id.btnSave);

        etDate.setOnClickListener(v -> showDatePickerDialog());

        // Setup dropdowns
        setupDropdowns();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeetingData();
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
        String[] statusOptions = {"Schedule", "Executed"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, statusOptions);
        autoCompleteStatus.setAdapter(statusAdapter);

        // Potential dropdown options
        String[] potentialOptions = {"High", "Low", "Follow Up"};
        ArrayAdapter<String> potentialAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, potentialOptions);
        autoCompletePotential.setAdapter(potentialAdapter);
    }

    private void saveMeetingData() {
        // Convert the date to a suitable format for sorting
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = "";
        try {
            Date date = inputFormat.parse(etDate.getText().toString().trim());
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Invalid Date Format", Toast.LENGTH_SHORT).show();
            return;
        }

        String personName = etPersonName.getText() != null ? etPersonName.getText().toString().trim() : "";
        String companyName = etCompanyName.getText() != null ? etCompanyName.getText().toString().trim() : "";
        String motive = etMotive.getText() != null ? etMotive.getText().toString().trim() : "";
        String result = etResult.getText() != null ? etResult.getText().toString().trim() : "";
        String status = autoCompleteStatus.getText() != null ? autoCompleteStatus.getText().toString().trim() : "";
        String potential = autoCompletePotential.getText() != null ? autoCompletePotential.getText().toString().trim() : "";

        // Create a unique key for each meeting
        String meetingId = databaseReference.push().getKey();

        if (meetingId == null) {
            Toast.makeText(getContext(), "Failed to generate meeting ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map of the meeting data
        Map<String, Object> meetingData = new HashMap<>();
        meetingData.put("meetingId", meetingId);
        meetingData.put("date", formattedDate);
        meetingData.put("personName", personName);
        meetingData.put("company", companyName);
        meetingData.put("motive", motive);
        meetingData.put("result", result);
        meetingData.put("status", status);
        meetingData.put("potential", potential);

        // Save to Firebase
        databaseReference.child(meetingId).setValue(meetingData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Redirect to MeetingListFragment after successful save
                Toast.makeText(getContext(), "Meeting saved successfully", Toast.LENGTH_SHORT).show();
                redirectToMeetingListFragment();
            } else {
                Toast.makeText(getContext(), "Failed to save meeting", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToMeetingListFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with MeetingListFragment
        fragmentTransaction.replace(R.id.frame_layout, new MeettingListFragment());

        // Optionally add this transaction to the back stack, so the user can navigate back
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
