package com.example.funfitnessblender;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.funfitnessblender.adapters.MeetingAdapter; // Use the MeetingAdapter instead
import com.example.funfitnessblender.models.Meeting; // Use the Meeting model instead
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MeettingListFragment extends Fragment {

    private ImageView backBtn;
    private EditText etSearch;
    private ListView listView;
    private MeetingAdapter adapter;
    private List<Meeting> meetingList, filteredList;
    private DatabaseReference databaseReference;

    public MeettingListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetting_list, container, false);

        listView = view.findViewById(R.id.listview);
        backBtn = view.findViewById(R.id.backBtn);
        etSearch = view.findViewById(R.id.etSearch);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("meetings");

        // Initialize the lists and adapter
        meetingList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new MeetingAdapter(getContext(), filteredList);

        listView.setAdapter(adapter);

        // Load data from Firebase
        loadMeetingData();

        // Back button functionality
        backBtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed(); // Trigger back navigation
            }
        });

        // Add TextWatcher for search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to handle this for now
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the list when the search input changes
                filterMeetingList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    // If search input is cleared, reload the original data
                    filterMeetingList("");
                }
            }
        });

        return view;
    }

    private void loadMeetingData() {
        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    meetingList.clear();  // Clear the list before loading new data
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Meeting meeting = snapshot.getValue(Meeting.class);
                        if (meeting != null) {
                            meetingList.add(meeting);
                        }
                    }

                    // Sort the list by date (latest meeting at the top)
                    Collections.sort(meetingList, new Comparator<Meeting>() {
                        @Override
                        public int compare(Meeting o1, Meeting o2) {
                            return o2.getDate().compareTo(o1.getDate()); // latest date on top
                        }
                    });

                    // Initially show all data
                    filteredList.clear();
                    filteredList.addAll(meetingList);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();

                    // Adjust ListView height based on content
                    setListViewHeightBasedOnChildren(listView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MeetingListFragment", "Failed to retrieve data", databaseError.toException());
                }
            });
        } else {
            Log.e("MeetingListFragment", "DatabaseReference is null");
        }
    }

    // Filter meeting list based on search query
    private void filterMeetingList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(meetingList); // Show all data if the query is empty
        } else {
            for (Meeting meeting : meetingList) {
                if (meeting.getPersonName().toLowerCase().contains(query.toLowerCase()) ||
                        meeting.getCompany().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(meeting); // Add the matching items to filtered list
                }
            }
        }

        // Notify the adapter to refresh the displayed data
        adapter.notifyDataSetChanged();

        // Adjust ListView height dynamically
        setListViewHeightBasedOnChildren(listView);
    }

    // Method to dynamically set ListView height based on the number of items
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        MeetingAdapter adapter = (MeetingAdapter) listView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0); // Measure the view
            totalHeight += listItem.getMeasuredHeight(); // Add the height of each item
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout(); // Apply the new height
    }
}
