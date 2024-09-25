package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.funfitnessblender.R;
import com.example.funfitnessblender.adapters.ClientNamesAdapter;
import com.example.funfitnessblender.models.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SessionActivity extends AppCompatActivity {

    private ImageView backBtn;
    private ListView listView;
    private ClientNamesAdapter clientNamesAdapter;
    private List<Client> clientList;
    private List<Client> filteredClientList; // List to hold filtered clients
    private DatabaseReference databaseReference;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etSearch = findViewById(R.id.etSearch);
        listView = findViewById(R.id.listview);
        clientList = new ArrayList<>();
        filteredClientList = new ArrayList<>();
        clientNamesAdapter = new ClientNamesAdapter(this, filteredClientList);  // Use the filtered list for the adapter
        listView.setAdapter(clientNamesAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("client");

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Add TextWatcher to the search EditText
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchClients(s.toString());  // Call search function on text change
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client selectedClient = filteredClientList.get(position);  // Use the filtered list
                Intent intent = new Intent(SessionActivity.this, SessionDetails.class);
                intent.putExtra("clientId", selectedClient.getId());  // Assuming `getId()` method exists in `Client` class
                intent.putExtra("name", selectedClient.getFirstName() + " " + selectedClient.getLastName());

                intent.putExtra("program", selectedClient.getProgram());

                startActivity(intent);
            }
        });

        //Function to fetch the clients
        fetchClients();
    }

    // Fetch clients and filter based on "Joined" status and "Personal training" in program
    private void fetchClients() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clientList.clear();
                filteredClientList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Client client = snapshot.getValue(Client.class);

                    // Check if the client matches the filter criteria
                    if (client != null && ("Joined".equalsIgnoreCase(client.getInterest())) &&
                            (client.getProgram().toLowerCase().contains("personal training") ||
                                    client.getProgram().toLowerCase().contains("autism fitness"))) {

                        clientList.add(client);  // Populate the clientList with all clients that meet the criteria
                        filteredClientList.add(client);  // Add to the filtered list for initial display
                    }
                }

                // Notify the adapter of data change
                clientNamesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }


    private void searchClients(String searchText) {
        filteredClientList.clear();

        if (searchText.isEmpty()) {
            filteredClientList.addAll(clientList);  // Show all clients if search is empty
        } else {
            for (Client client : clientList) {
                if (client.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                        client.getLastName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredClientList.add(client);  // Add matching clients to the filtered list
                }
            }
        }

        // Sort the filtered list by date (most recent first)
        Collections.sort(filteredClientList, (o1, o2) -> {
            if (o1.getDate() == null || o2.getDate() == null) {
                return 0;
            }
            return o2.getDate().compareTo(o1.getDate());
        });

        clientNamesAdapter.notifyDataSetChanged();
    }

}
