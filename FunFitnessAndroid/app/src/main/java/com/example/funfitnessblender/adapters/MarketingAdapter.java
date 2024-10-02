package com.example.funfitnessblender.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funfitnessblender.DetailedMarketingActivity;
import com.example.funfitnessblender.R;
import com.example.funfitnessblender.models.Marketing;

import java.util.List;

public class MarketingAdapter extends ArrayAdapter<Marketing> {
    public MarketingAdapter(Context context, List<Marketing> marketingList) {
        super(context, 0, marketingList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_marketing, parent, false);
        }

        Marketing marketing = getItem(position);
        TextView monthTextView = convertView.findViewById(R.id.tvMonth);
        TextView strategyTextView = convertView.findViewById(R.id.tvStrategy);


        monthTextView.setText(marketing.getMonth());
        strategyTextView.setText(marketing.getStrategy());

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DetailedMarketingActivity.class);
            // Pass the marketing details via intent
            intent.putExtra("marketingId", marketing.getMarketingId());
            intent.putExtra("month", marketing.getMonth());
            intent.putExtra("strategy", marketing.getStrategy());
            intent.putExtra("expense", marketing.getExpense());
            intent.putExtra("responseDetails", marketing.getResponseDetails());
            getContext().startActivity(intent);
        });

        return convertView;
    }
}