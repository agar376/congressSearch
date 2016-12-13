package com.example.shobhitagarwal.congressapi;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by ShobhitAgarwal on 22/11/16.
 */

public class CommitteeAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] jsonString;

    public CommitteeAdapter(Activity context, String[] jsonString) {
        super(context, R.layout.committee_summary, jsonString);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.jsonString = jsonString;
    }

    public View getView(int position, View view, ViewGroup parent) {
        try {
            JSONObject rawData = new JSONObject(jsonString[position]);
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.committee_summary, null, true);

            TextView bid = (TextView) rowView.findViewById(R.id.committee_summary_id);
            TextView btitle = (TextView) rowView.findViewById(R.id.committee_summary_title);
            TextView bintro = (TextView) rowView.findViewById(R.id.committee_summary_intro);

            String committee_id = (!rawData.isNull("committee_id")) ? rawData.getString("committee_id") : "N.A.";
            String committee_name = (!rawData.isNull("name")) ? rawData.getString("name") : "N.A.";
            String chamber = (!rawData.isNull("chamber")) ? rawData.getString("chamber") : "N.A.";
            bid.setText(committee_id.toUpperCase());
            btitle.setText(committee_name);
            bintro.setText(chamber.substring(0, 1).toUpperCase() + chamber.substring(1));

            return rowView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onItemClick(int position) {

        Intent intent = new Intent(context, CommitteeDetails.class);
        String title = "Committee Info";

        intent.putExtra("database", title);
        intent.putExtra("jsonString", jsonString[position]);
        context.startActivity(intent);

    }
}