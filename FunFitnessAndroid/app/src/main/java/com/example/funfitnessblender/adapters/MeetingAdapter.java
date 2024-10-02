package com.example.funfitnessblender.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.funfitnessblender.DetailedMeetingActivity;
import com.example.funfitnessblender.R;
import com.example.funfitnessblender.models.Meeting;

import java.util.List;

public class MeetingAdapter extends ArrayAdapter<Meeting> {

    private Context context;
    private List<Meeting> meetings;

    public MeetingAdapter(Context context, List<Meeting> meetings) {
        super(context, R.layout.list_item_meeting, meetings);
        this.context = context;
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_meeting, parent, false);
        }

        Meeting meeting = getItem(position);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvMeetingDate = convertView.findViewById(R.id.tvMeetingDate);
        TextView tvCompany = convertView.findViewById(R.id.tvCompany);

        if (meeting != null) {
            tvName.setText(meeting.getPersonName());
            tvMeetingDate.setText(meeting.getDate());
            tvCompany.setText(meeting.getCompany());
        }

        // Add click listener for the item
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DetailedMeetingActivity.class);
            // Pass the meeting details via intent
            intent.putExtra("meetingId", meeting.getMeetingId());
            intent.putExtra("personName", meeting.getPersonName());
            intent.putExtra("date", meeting.getDate());
            intent.putExtra("company", meeting.getCompany());
            intent.putExtra("status", meeting.getStatus());
            intent.putExtra("potential", meeting.getPotential());
            intent.putExtra("motive", meeting.getMotive());
            intent.putExtra("result", meeting.getResult());
            getContext().startActivity(intent);
        });

        return convertView;
    }


    public void updateList(List<Meeting> updatedList) {
        this.meetings.clear();  // Clear the old list
        this.meetings.addAll(updatedList);  // Add the new filtered list
        notifyDataSetChanged();  // Refresh the adapter
    }







}
