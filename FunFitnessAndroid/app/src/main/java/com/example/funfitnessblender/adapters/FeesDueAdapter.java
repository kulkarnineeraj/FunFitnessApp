package com.example.funfitnessblender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.funfitnessblender.R;
import com.example.funfitnessblender.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FeesDueAdapter extends ArrayAdapter<Transaction> {
    private Context context;
    private List<Transaction> transactions;

    public FeesDueAdapter(Context context, ArrayList<Transaction> list) {
        super(context, 0, list);
        this.context = context;
        this.transactions = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_transaction, parent, false);
        }

        Transaction transaction = transactions.get(position);


        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvDueDate = convertView.findViewById(R.id.tvDate);
        TextView tvProgram = convertView.findViewById(R.id.tvProgram);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);



        // Set data from transaction object
        tvName.setText(transaction.getClientName());
        tvAmount.setText(transaction.getReceivedAmount());
        tvDueDate.setText(transaction.getToDate());
        tvProgram.setText(transaction.getProgram());

        // Set program icon and status arrow if needed
        // ivProgram.setImageResource(transaction.getProgramIcon());
        // ivStatus.setImageResource(R.drawable.arrowicon); // or another icon based on status

        return convertView;
    }
}
