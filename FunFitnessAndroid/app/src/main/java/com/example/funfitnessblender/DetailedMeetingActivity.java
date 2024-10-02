package com.example.funfitnessblender;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funfitnessblender.activity.DetailedDataActivity;
import com.example.funfitnessblender.activity.EditClientDetails;
import com.example.funfitnessblender.activity.EditMeetingActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailedMeetingActivity extends AppCompatActivity {

    private ImageView backBtn,menuBtn;

    private TextView tvName, tvMeetingDate, tvCompanyName, tvMeetingStatus, tvPotential, tvMotive, tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_meeting);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the current activity and returns to the previous one
            }
        });

        // Initialize TextViews
        tvName = findViewById(R.id.tvName);
        tvMeetingDate = findViewById(R.id.tvMeetingDate);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvMeetingStatus = findViewById(R.id.tvMeetingStatus);
        tvPotential = findViewById(R.id.tvPotential);
        tvMotive = findViewById(R.id.tvMotive);
        tvResult = findViewById(R.id.tvResult);

        // Get data from Intent
        String meetingId = getIntent().getStringExtra("meetingId");
        String personName = getIntent().getStringExtra("personName");
        String date = getIntent().getStringExtra("date");
        String company = getIntent().getStringExtra("company");
        String status = getIntent().getStringExtra("status");
        String potential = getIntent().getStringExtra("potential");
        String motive = getIntent().getStringExtra("motive");
        String result = getIntent().getStringExtra("result");

        // Set the data to the TextViews
        tvName.setText(personName);
        tvMeetingDate.setText(date);
        tvCompanyName.setText(company);
        tvMeetingStatus.setText(status);
        tvPotential.setText(potential);
        tvMotive.setText(motive);
        tvResult.setText(result);
    }


    private void showPopupMenu(View view) {

        Intent intent = getIntent();

        String meetingId = getIntent().getStringExtra("meetingId");
        String personName = getIntent().getStringExtra("personName");
        String date = getIntent().getStringExtra("date");
        String company = getIntent().getStringExtra("company");
        String status = getIntent().getStringExtra("status");
        String potential = getIntent().getStringExtra("potential");
        String motive = getIntent().getStringExtra("motive");
        String result = getIntent().getStringExtra("result");


        // Create a PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_items, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu_edit) {
                    // Handle Edit action
                    Intent intent = new Intent(DetailedMeetingActivity.this, EditMeetingActivity.class);

                    // Pass all data needed to edit the client
                    intent.putExtra("meetingId", getIntent().getStringExtra("meetingId")); // Pass the client ID
                    intent.putExtra("personName", getIntent().getStringExtra("personName"));
                    intent.putExtra("date", getIntent().getStringExtra("date"));
                    intent.putExtra("company", getIntent().getStringExtra("company"));
                    intent.putExtra("status", getIntent().getStringExtra("status"));
                    intent.putExtra("potential", getIntent().getStringExtra("potential"));
                    intent.putExtra("result", getIntent().getStringExtra("result"));
                    intent.putExtra("motive", getIntent().getStringExtra("motive"));


                    // Start EditClientDetails activity
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_delete) {
                    showDeleteConfirmationDialog();
                    return true;
                }
                return false;
            }

            // Show confirmation dialog before deletion
            private void showDeleteConfirmationDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailedMeetingActivity.this);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure you want to delete this item?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(); // Call delete function
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            // Delete client from Firebase
            private void deleteItem() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("meetings");

                if (meetingId != null) {
                    databaseReference.child(meetingId).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailedMeetingActivity.this, "Meeting deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DetailedMeetingActivity.this, "Failed to delete meeting", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Optional: Show Snackbar with undo option
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Meeting deleted", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            undoDelete();  // Undo the deletion (this part needs actual logic)
                        }
                    });
                    snackbar.show();
                }
            }

            // Placeholder for undo delete logic
            private void undoDelete() {
                Toast.makeText(DetailedMeetingActivity.this, "Undo deletion is not implemented", Toast.LENGTH_SHORT).show();
            }
        });
        // Show the PopupMenu
        popupMenu.show();
    }
}