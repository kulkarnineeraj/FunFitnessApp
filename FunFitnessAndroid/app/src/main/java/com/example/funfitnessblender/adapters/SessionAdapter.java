package com.example.funfitnessblender.adapters;

import com.example.funfitnessblender.R;
import com.example.funfitnessblender.models.Client;
import com.example.funfitnessblender.models.Session;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SessionAdapter extends ArrayAdapter<Session>{

    private Context context;
    private List<Session> sessions;

    public SessionAdapter(Context context, List<Session> sessions) {
        super(context, R.layout.list_item_session, sessions);
        this.context = context;
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_session, parent, false);
        }

        Session session = sessions.get(position);

        TextView tvSrNo = convertView.findViewById(R.id.tvSrNo);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);


        tvSrNo.setText(String.valueOf(position + 1));
        tvDate.setText(session.getSessionDate());
        tvStatus.setText(session.getStatus());



        return convertView;
    }
}
