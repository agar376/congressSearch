package com.example.shobhitagarwal.congressapi;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by ShobhitAgarwal on 22/11/16.
 */

public class LegislatorAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] jsonString;

    public LegislatorAdapter(Activity context, String[] jsonString) {
        super(context, R.layout.legislator_summary, jsonString);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.jsonString = jsonString;
    }

    public View getView(int position, View view, ViewGroup parent) {
        try {
            JSONObject rawData = new JSONObject(jsonString[position]);
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.legislator_summary, null, true);

            TextView bid = (TextView) rowView.findViewById(R.id.legislator_summary_id);
            TextView btitle = (TextView) rowView.findViewById(R.id.legislator_summary_title);
            ImageView img = (ImageView) rowView.findViewById(R.id.legislator_pic);

            String bioguide_id = (!rawData.isNull("bioguide_id")) ? rawData.getString("bioguide_id") : "i";
            String img_url = "http://theunitedstates.io/images/congress/225x275/" + bioguide_id + ".jpg";

            String party = (!rawData.isNull("party")) ? rawData.getString("party") : "I";
            String state = (!rawData.isNull("state_name")) ? rawData.getString("state_name") : "N.A.";
            String district = (!rawData.isNull("district")) ? rawData.getString("district") : "N.A.";
            btitle.setText("(" + party.toUpperCase() + ")" + state + " - District " + district);

            String lname = (!rawData.isNull("last_name")) ? rawData.getString("last_name") : "";
            String fname = (!rawData.isNull("first_name")) ? rawData.getString("first_name") : "";
            bid.setText(lname + ", " + fname);

            LoadImage loadImg = new LoadImage(img_url, "", context, img);
            loadImg.execute();

            return rowView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onItemClick(int position) {

        Intent intent = new Intent(context, Details.class);
        String title = "Legislator Info";

        intent.putExtra("database", title);
        intent.putExtra("jsonString", jsonString[position]);
        context.startActivity(intent);

    }
}