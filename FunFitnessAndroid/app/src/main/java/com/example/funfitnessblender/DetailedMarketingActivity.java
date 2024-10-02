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

import com.example.funfitnessblender.activity.EditMarketingActivity;
import com.example.funfitnessblender.activity.EditMeetingActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailedMarketingActivity extends AppCompatActivity {

    private TextView tvMonth, tvExpense, tvStrategy, tvResponse;
    private ImageView backBtn,menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_marketing);

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
        tvMonth = findViewById(R.id.tvMonth);
        tvExpense = findViewById(R.id.tvExpense);
        tvStrategy = findViewById(R.id.tvStrategy);
        tvResponse = findViewById(R.id.tvResponse);

        // Get data from Intent
        String marketingId = getIntent().getStringExtra("marketingId");
        String month = getIntent().getStringExtra("month");
        String strategy = getIntent().getStringExtra("strategy");
        String expense = getIntent().getStringExtra("expense");
        String responseDetails = getIntent().getStringExtra("responseDetails");

        // Set the data to the TextViews
        tvMonth.setText(month);
        tvExpense.setText(expense);
        tvStrategy.setText(strategy);
        tvResponse.setText(responseDetails);

    }


    private void showPopupMenu(View view) {

        Intent intent = getIntent();

        String marketingId = getIntent().getStringExtra("marketingId");
        String month = getIntent().getStringExtra("month");
        String strategy = getIntent().getStringExtra("strategy");
        String expense = getIntent().getStringExtra("expense");
        String responseDetails = getIntent().getStringExtra("responseDetails");


        // Create a PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_items, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu_edit) {
                    // Handle Edit action
                    Intent intent = new Intent(DetailedMarketingActivity.this, EditMarketingActivity.class);

                    // Pass all data needed to edit the client
                    intent.putExtra("marketingId", getIntent().getStringExtra("marketingId")); // Pass the client ID
                    intent.putExtra("month", getIntent().getStringExtra("month"));
                    intent.putExtra("strategy", getIntent().getStringExtra("strategy"));
                    intent.putExtra("expense", getIntent().getStringExtra("expense"));
                    intent.putExtra("responseDetails", getIntent().getStringExtra("responseDetails"));



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
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailedMarketingActivity.this);
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
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("marketing");

                if (marketingId != null) {
                    databaseReference.child(marketingId).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailedMarketingActivity.this, "Marketing deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DetailedMarketingActivity.this, "Failed to delete marketing", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Optional: Show Snackbar with undo option
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Marketing deleted", Snackbar.LENGTH_LONG);
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
                Toast.makeText(DetailedMarketingActivity.this, "Undo deletion is not implemented", Toast.LENGTH_SHORT).show();
            }
        });
        // Show the PopupMenu
        popupMenu.show();
    }
}