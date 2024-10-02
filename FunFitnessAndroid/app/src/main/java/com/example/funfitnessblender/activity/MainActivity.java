package com.example.funfitnessblender.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funfitnessblender.MeettingActivity;
import com.example.funfitnessblender.R;

import com.example.funfitnessblender.models.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView totalTV, newTV, joinedTV, leftTV,tvNew,tvJoined;

    private RelativeLayout enquirycountRL;
    private DatabaseReference clientDatabaseReference;

    private LinearLayout addEnquiryLL,adminLL,sessionLL,addSessionLL,addTransactionLL,enquiryListLL,transactionListLL,feesDueLL,meetingLL,joinedMemberLL,incomeReportLL,marketingLL;
    private ConstraintLayout  adminCL, reportsCL, CL5, CL6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        addEnquiryLL = findViewById(R.id.addEnquiryLL);
        addEnquiryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEnquiryActivity.class);
                startActivity(intent);
            }
        });



        sessionLL = findViewById(R.id.sessionLL);
        sessionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SessionActivity.class);
                startActivity(intent);
            }
        });

        incomeReportLL = findViewById(R.id.incomeReportLL);
        incomeReportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IncomeReportActivity.class);
                startActivity(intent);
            }
        });


        joinedMemberLL = findViewById(R.id.joinedMemberLL);
        joinedMemberLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinedMemberListActivity.class);
                startActivity(intent);
            }
        });

        // To view the members
        enquiryListLL = findViewById(R.id.enquiryListLL);
        enquiryListLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EnquiryListActivity.class);
                startActivity(intent);
            }
        });

        adminLL = findViewById(R.id.adminLL);
        adminLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });


        newTV = findViewById(R.id.newTV);
        newTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EnquiryListActivity.class);
                startActivity(intent);
            }
        });

        tvNew = findViewById(R.id.tvNew);
        tvNew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EnquiryListActivity.class);
                startActivity(intent);
            }
        });


        joinedTV = findViewById(R.id.joinedTV);
        joinedTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinedMemberListActivity.class);
                startActivity(intent);
            }
        });

        tvJoined = findViewById(R.id.tvJoined);
        tvJoined.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinedMemberListActivity.class);
                startActivity(intent);
            }
        });






        meetingLL = findViewById(R.id.meetingLL);
        meetingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MeettingActivity.class);
                startActivity(intent);
            }
        });

        marketingLL = findViewById(R.id.marketingLL);
        marketingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MarketingActivity.class);
                startActivity(intent);
            }
        });


        feesDueLL = findViewById(R.id.feesDueLL);
        feesDueLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeesDueActivity.class);
                startActivity(intent);
            }
        });

        addTransactionLL = findViewById(R.id.addTransactionLL);
        addTransactionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivity(intent);
            }
        });



        transactionListLL = findViewById(R.id.transactionListLL);
        transactionListLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllTransactionActivity.class);
                startActivity(intent);
            }
        });



        totalTV = findViewById(R.id.totalTV);
        newTV = findViewById(R.id.newTV);

        leftTV = findViewById(R.id.leftTV);

        clientDatabaseReference = FirebaseDatabase.getInstance().getReference("client");

        loadClientStatusCounts();




    }

    private void loadClientStatusCounts() {
        clientDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalCount = 0;
                int newEnquiryCount = 0;
                int joinedCount = 0;
                int leftCount = 0;

                for (DataSnapshot clientSnapshot : snapshot.getChildren()) {
                    Client client = clientSnapshot.getValue(Client.class);
                    if (client != null) {
                        totalCount++;
                        String status = client.getInterest();

                        // Count based on status
                        if ("New Enquiry".equalsIgnoreCase(status)) {
                            newEnquiryCount++;
                        } else if ("Joined".equalsIgnoreCase(status)) {
                            joinedCount++;
                        } else if ("Left".equalsIgnoreCase(status)) {
                            leftCount++;
                        }
                    }
                }
                totalTV.setText(String.valueOf(totalCount));
                newTV.setText(String.valueOf(newEnquiryCount));
                joinedTV.setText(String.valueOf(joinedCount));
                leftTV.setText(String.valueOf(leftCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
