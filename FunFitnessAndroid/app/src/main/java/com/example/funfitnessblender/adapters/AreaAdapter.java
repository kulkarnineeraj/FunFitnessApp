package com.example.funfitnessblender.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import necessary widgets
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.funfitnessblender.R;
import com.example.funfitnessblender.activity.AddAreaActivity;
import com.example.funfitnessblender.models.Area;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AreaAdapter extends ArrayAdapter<Area> {

    private Context context;
    private List<Area> areaList;
    private DatabaseReference databaseReference;

    public AreaAdapter(Context context, List<Area> areaList) {
        super(context, 0, areaList);
        this.context = context;
        this.areaList = areaList;
        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Areas");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Inflate the view if not already done
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item2, parent, false);
        }

        // Retrieve the current area
        Area area = areaList.get(position);

        // Initialize UI components
        TextView tvItem = convertView.findViewById(R.id.tvItem);
        ImageView ivDelete = convertView.findViewById(R.id.ivDelete);

        // Set the area name
        tvItem.setText(area.getAreaName());

        // Set click listener for the delete button
        ivDelete.setOnClickListener(v -> {
            // Ensure the position is within bounds before removing the item
            if (position >= 0 && position < areaList.size()) {
                databaseReference.child(area.getId()).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Deleted " + area.getAreaName(), Toast.LENGTH_SHORT).show();
                            // Remove the item from the list only if it's still valid
                            if (position >= 0 && position < areaList.size()) {
                                areaList.remove(position);
                                //      notifyDataSetChanged();  Refresh the ListView
                                // Redirect to AddAreaActivity
                                // Intent intent = new Intent(context, AddAreaActivity.class);
                                // context.startActivity(intent);
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Failed to delete " + area.getAreaName(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(context, "Invalid position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
