package com.example.shobhitagarwal.congressapi;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by ShobhitAgarwal on 22/11/16.
 */

public class BillAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] jsonString;

    public BillAdapter(Activity context, String[] jsonString) {
        super(context, R.layout.bill_summary, jsonString);

        this.context = context;
        this.jsonString = jsonString;
    }

    public View getView(int position, View view, ViewGroup parent) {
        try {
            JSONObject rawData = new JSONObject(jsonString[position]);
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.bill_summary, null, true);

            TextView bid = (TextView) rowView.findViewById(R.id.bill_summary_id);
            TextView btitle = (TextView) rowView.findViewById(R.id.bill_summary_title);
            TextView bintro = (TextView) rowView.findViewById(R.id.bill_summary_intro);

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");

            String bill_id = (!rawData.isNull("bill_id")) ? rawData.getString("bill_id") : "N.A.";
            String bill_name = (!rawData.isNull("official_title")) ? rawData.getString("official_title") : "N.A.";
            String bill_intro = (!rawData.isNull("introduced_on")) ? rawData.getString("introduced_on") : "N.A.";
            bid.setText(bill_id.toUpperCase());
            btitle.setText(bill_name);
            bintro.setText(format2.format(format1.parse(bill_intro)));

            return rowView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onItemClick(int position) {

        Intent intent = new Intent(context, BillDetails.class);
        String title = "Bill Info";

        intent.putExtra("database", title);
        intent.putExtra("jsonString", jsonString[position]);
        context.startActivity(intent);

    }
}