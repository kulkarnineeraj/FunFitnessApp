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

import com.example.funfitnessblender.adapters.MarketingAdapter;
import com.example.funfitnessblender.models.Marketing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MarketingListFragment extends Fragment {

    private ImageView backBtn;
    private EditText etSearch;
    private ListView listView;
    private MarketingAdapter adapter;
    private List<Marketing> marketingList, filteredList;
    private DatabaseReference databaseReference;

    public MarketingListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marketing_list, container, false);

        listView = view.findViewById(R.id.marketingList);
        backBtn = view.findViewById(R.id.backBtn);
        etSearch = view.findViewById(R.id.etSearch);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("marketing");

        // Initialize the lists and adapter
        marketingList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new MarketingAdapter(getContext(), filteredList);

        listView.setAdapter(adapter);

        // Load data from Firebase
        loadMarketingData();

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
                filterMarketingList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    // If search input is cleared, reload the original data
                    filterMarketingList("");
                }
            }
        });

        return view;
    }

    private void loadMarketingData() {
        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    marketingList.clear();  // Clear the list before loading new data
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Marketing marketing = snapshot.getValue(Marketing.class);
                        if (marketing != null) {
                            marketingList.add(marketing);
                        }
                    }

                    // Sort the list by month (latest at the top)
                    Collections.sort(marketingList, new Comparator<Marketing>() {
                        @Override
                        public int compare(Marketing o1, Marketing o2) {
                            return Integer.compare(o2.getMonthAsNumber(), o1.getMonthAsNumber()); // latest month on top
                        }
                    });

                    // Initially show all data
                    filteredList.clear();
                    filteredList.addAll(marketingList);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();

                    // Adjust ListView height based on content
                    setListViewHeightBasedOnChildren(listView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MarketingListFragment", "Failed to retrieve data", databaseError.toException());
                }
            });
        } else {
            Log.e("MarketingListFragment", "DatabaseReference is null");
        }
    }

    // Filter marketing list based on search query
    private void filterMarketingList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(marketingList); // Show all data if the query is empty
        } else {
            for (Marketing marketing : marketingList) {
                if (marketing.getStrategy().toLowerCase().contains(query.toLowerCase()) ||
                        marketing.getMonth().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(marketing); // Add the matching items to filtered list
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
        MarketingAdapter adapter = (MarketingAdapter) listView.getAdapter();
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
