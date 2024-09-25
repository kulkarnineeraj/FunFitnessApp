package com.example.funfitnessblender;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.funfitnessblender.adapters.MeetingAdapter;
import com.example.funfitnessblender.models.Meeting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeettingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeettingListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView listView;
    private MeetingAdapter adapter;
    private List<Meeting> meetingList;

    private DatabaseReference databaseReference;

    public MeettingListFragment() {
        // Required empty public constructor
    }

    public static MeettingListFragment newInstance(String param1, String param2) {
        MeettingListFragment fragment = new MeettingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetting_list, container, false);

        listView = view.findViewById(R.id.listview);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("meetings");

        // Initialize the list and adapter
        meetingList = new ArrayList<>();
        adapter = new MeetingAdapter(getContext(), meetingList);

        listView.setAdapter(adapter);

        // Load data from Firebase
        loadMeetingData();

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
                            Log.d("MeettingListFragment", "Meeting: " + meeting.toString());
                            meetingList.add(meeting);
                        } else {
                            Log.d("MeettingListFragment", "Meeting is null");
                        }
                    }
                    // Sort the list by date
                    Collections.sort(meetingList, (o1, o2) -> {
                        if (o1.getDate() == null || o2.getDate() == null) {
                            return 0;
                        }
                        return o2.getDate().compareTo(o1.getDate());
                    });
                    adapter.notifyDataSetChanged();
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

}
