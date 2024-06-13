package com.example.citywatchapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class complaintAdapter extends ArrayAdapter<String> {

    private ArrayList<String> complaintTitles;
    private Context context;

    public complaintAdapter(Context context, ArrayList<String> complaintTitles) {
        super(context, 0, complaintTitles);
        this.context = context;
        this.complaintTitles = complaintTitles;
    }

    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_complaint, parent, false);
        }

        // Get the current complaint title
        String title = getItem(position);

        // Set the title to the TextView
        TextView textViewComplaintTitle = convertView.findViewById(R.id.textViewTitle);
        textViewComplaintTitle.setText(title);

        return convertView;
    }
}