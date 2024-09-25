package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.example.funfitnessblender.R;
import com.example.funfitnessblender.adapters.SessionAdapter;

import com.example.funfitnessblender.models.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SessionDetails extends AppCompatActivity {

    private ImageView backBtn;

    private AppCompatButton btnAdd;
    private TextView tvProgram,tvName,tvSessionCount;

    private ListView listViewSessions;
    private List<Session> sessionList;
    private SessionAdapter sessionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);
        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        tvProgram = findViewById(R.id.tvProgram);
        tvName = findViewById(R.id.tvName);
        backBtn = findViewById(R.id.backBtn);
        btnAdd = findViewById(R.id.btnAdd);
        tvSessionCount = findViewById(R.id.tvSessionCount);
        listViewSessions = findViewById(R.id.listviewSessions);



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionDetails.this, SessionActivity.class);
                startActivity(intent);
            }
        });





        Intent intent = getIntent();
        String name = intent.getStringExtra("name") != null ? intent.getStringExtra("name") : "N/A";
        String clientId = intent.getStringExtra("clientId") != null ? intent.getStringExtra("clientId") : "N/A";
        String program = intent.getStringExtra("program") != null ? intent.getStringExtra("program") : "N/A";

        // Set data to views
        tvProgram.setText(program);
        tvName.setText(name);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SessionDetails.this, AddSessionActivity.class);
                intent.putExtra("clientId", clientId);
                intent.putExtra("name",name);

                intent.putExtra("program", program);

                startActivity(intent);
            }
        });

        // Initialize session list and adapter
        sessionList = new ArrayList<>();
        sessionAdapter = new SessionAdapter(this, sessionList);
        listViewSessions.setAdapter(sessionAdapter);
        // Fetch sessions for the specific client
        fetchSessions(clientId);

        listViewSessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Session session = sessionList.get(position);
                Intent intent = new Intent(SessionDetails.this,EditSessionActivity.class);
                intent.putExtra("sessionId", session.getSessionId());
                intent.putExtra("clientId", session.getClientId());  // Assuming `getId()` method exists in `Client` class
                intent.putExtra("name", name);
                intent.putExtra("sessionDate", session.getSessionDate());
                intent.putExtra("program", session.getProgram());
                intent.putExtra("status", session.getStatus());

                startActivity(intent);

            }
        });




    }

    private void fetchSessions(String clientId) {
        // Reference to Firebase database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sessions");

        // Query to fetch sessions for the specific client
        databaseReference.orderByChild("clientId").equalTo(clientId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sessionList.clear();  // Clear previous data
                int doneSessionCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Session session = snapshot.getValue(Session.class);
                    sessionList.add(session);  // Add each session to the list
                    // Check if the session status is "Done"
                    if (session.getStatus() != null && session.getStatus().equals("Done")) {
                        doneSessionCount++;  // Increment counter for "Done" sessions
                    }
                }

                // Sort the list by session date in descending order
                sessionList.sort((s1, s2) -> s2.getSessionDate().compareTo(s1.getSessionDate()));

                // Notify the adapter that the data has changed
                sessionAdapter.notifyDataSetChanged();

                // Update tvSessionCount with the number of sessions with status "Done"
                tvSessionCount.setText(String.valueOf(doneSessionCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

}