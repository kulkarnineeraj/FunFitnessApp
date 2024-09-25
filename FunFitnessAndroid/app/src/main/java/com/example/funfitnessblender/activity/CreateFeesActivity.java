package com.example.funfitnessblender.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.funfitnessblender.adapters.ClientNamesAdapter;
import com.example.funfitnessblender.models.Client;
import com.example.funfitnessblender.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateFeesActivity extends AppCompatActivity {

    private ListView listView;
    private ClientNamesAdapter clientNamesAdapter;
    private List<Client> clientList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_list);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listview);
        clientList = new ArrayList<>();
        clientNamesAdapter = new ClientNamesAdapter(this, clientList);  // Use the ClientNamesAdapter
        listView.setAdapter(clientNamesAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("client");

        fetchClients();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client selectedClient = clientNamesAdapter.getClient(position);
                Intent intent = new Intent(CreateFeesActivity.this, AddTransactionActivity.class);
                intent.putExtra("clientId", selectedClient.getId());  // Assuming `getId()` method exists in `Client` class
                intent.putExtra("clientName", selectedClient.getFirstName() + " " + selectedClient.getLastName());
                intent.putExtra("clientAmount",selectedClient.getAmount());
                startActivity(intent);
            }
        });
    }

    private void fetchClients() {
        databaseReference.orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Client client = snapshot.getValue(Client.class);
                    if (client != null && client.getDate() != null) {
                        clientList.add(client);
                        Log.d("ClientFeesActivity", "Client added: " + client.getFirstName());
                    } else {
                        Log.d("ClientFeesActivity", "Enquiry is null or has no date");
                    }
                }
                Collections.sort(clientList, (o1, o2) -> {
                    if (o1.getDate() == null || o2.getDate() == null) {
                        return 0;
                    }
                    return o2.getDate().compareTo(o1.getDate());
                });
                clientNamesAdapter.notifyDataSetChanged();  // Notify adapter of data changes
                Log.d("CreateFeesActivity", "Total Enquiries: " + clientList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CreateFeesActivity", "Failed to retrieve data", databaseError.toException());
            }
        });
    }
}
