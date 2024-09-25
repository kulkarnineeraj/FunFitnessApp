package com.example.funfitnessblender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

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

        return convertView;
    }
}
