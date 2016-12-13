package com.example.shobhitagarwal.congressapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class CommitteeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_details);

        String title = getIntent().getStringExtra("database");
        final String jsonString = getIntent().getStringExtra("jsonString");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (title.equalsIgnoreCase("Committee Info")) {
            try {
                JSONObject rawData = new JSONObject(jsonString);

                String email =  (!rawData.isNull("committee_id")) ? rawData.getString("committee_id") : "N.A.";
                if (email != null && !email.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.committee_id);
                    t.setText(email);
                }

                final ImageView fav = (ImageView) findViewById(R.id.favCom);

                SharedPreferences favorite = this.getSharedPreferences("committee", Context.MODE_PRIVATE);
                if(favorite.contains(email)) {
                    fav.setImageResource(R.drawable.gold_star);
                }
                final String id = email;
                final Activity activity = this;
                final String value = jsonString;
                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RestJSON.addToFavorites(activity, "committee", id, value, fav);
                    }
                });

                String chamber =  (!rawData.isNull("chamber")) ? rawData.getString("chamber") : "N.A.";
                if (chamber != null && !chamber.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.com_chamber);
                    t.setText(chamber.substring(0, 1).toUpperCase() + chamber.substring(1));
                }

                String contact =  (!rawData.isNull("phone")) ? rawData.getString("phone") : "N.A.";
                if (contact != null && !contact.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.com_contact);
                    t.setText(contact);
                }

                String fax =  (!rawData.isNull("name")) ? rawData.getString("name") : "N.A.";
                if (fax != null && !fax.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.com_name);
                    t.setText(fax);
                }

                int image_id = R.drawable.s;
                if(chamber.equalsIgnoreCase("house")) {
                    image_id = R.drawable.h;
                }
                ImageView partyImage = (ImageView) findViewById(R.id.com_chamberImg);
                partyImage.setImageDrawable(ContextCompat.getDrawable(this, image_id));

                String office = (!rawData.isNull("office")) ? rawData.getString("office") : "N.A.";
                if (office != null && !office.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.com_office);
                    t.setText(office);
                }

                String state = (!rawData.isNull("parent_committee_id")) ? rawData.getString("parent_committee_id") : "N.A.";
                if (state != null && !state.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.parent);
                    t.setText(state);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        this.finish();
        return true;
    }

    public void setImageLinks(int img_id, String l) {
        final String link = l;
        ImageView img = (ImageView) findViewById(img_id);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (link == null || link.trim().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Link Not available", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                }
            }
        });
    }

}
