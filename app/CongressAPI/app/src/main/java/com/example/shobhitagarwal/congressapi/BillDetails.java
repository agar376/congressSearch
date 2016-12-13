package com.example.shobhitagarwal.congressapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class BillDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        String title = getIntent().getStringExtra("database");
        String jsonString = getIntent().getStringExtra("jsonString");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (title.equalsIgnoreCase("Bill Info")) {
            try {
                JSONObject rawData = new JSONObject(jsonString);

                String bill_id = (!rawData.isNull("bill_id")) ? rawData.getString("bill_id") : "N.A.";
                if (bill_id != null && !bill_id.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.bill_id);
                    t.setText(bill_id.toUpperCase());
                }

                final ImageView fav = (ImageView) findViewById(R.id.favBill);

                SharedPreferences favorite = this.getSharedPreferences("bill", Context.MODE_PRIVATE);
                if(favorite.contains(bill_id)) {
                    fav.setImageResource(R.drawable.gold_star);
                }

                final String id = bill_id;
                final Activity activity = this;
                final String value = jsonString;
                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RestJSON.addToFavorites(activity, "bill", id, value, fav);
                    }
                });

                String email = (!rawData.isNull("official_title")) ? rawData.getString("official_title") : "N.A.";
                if (email != null && !email.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.bill_title);
                    t.setText(email);
                }

                String chamber = (!rawData.isNull("chamber")) ? rawData.getString("chamber") : "N.A.";
                if (chamber != null && !chamber.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.bill_chamber);
                    t.setText(chamber.substring(0, 1).toUpperCase() + chamber.substring(1));
                }

                String contact = (!rawData.isNull("bill_type")) ? rawData.getString("bill_type") : "N.A.";
                if (contact != null && !contact.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.bill_type);
                    t.setText(contact.toUpperCase());
                }

                JSONObject sponsor = null;
                if(!rawData.isNull("sponsor")) {
                    sponsor = rawData.getJSONObject("sponsor");
                }
                if(sponsor != null) {
                    String rep = (!sponsor.isNull("title")) ? sponsor.getString("title") : "";
                    String lname = (!sponsor.isNull("last_name")) ? sponsor.getString("last_name") : "";
                    String fname = (!sponsor.isNull("first_name")) ? sponsor.getString("first_name") : "";
                    rep = (rep != null) ? rep : "";
                    if (lname != null && !lname.trim().isEmpty() && fname != null && !fname.trim().isEmpty()) {
                        TextView t = (TextView) findViewById(R.id.bill_sponsor);
                        t.setText(rep + ". " + lname + ", " + fname);
                    }
                }

                JSONObject history = null;
                if(!rawData.isNull("history")) {
                    history = rawData.getJSONObject("history");
                }
                if(history != null) {
                    String state = (!history.isNull("active") && history.getString("active").equalsIgnoreCase("true")) ? "Active" : "New";
                    if (state != null && !state.trim().isEmpty()) {
                        TextView t = (TextView) findViewById(R.id.bill_status);
                        t.setText(state);
                    }
                }

                JSONObject urls = null;
                if(!rawData.isNull("urls")) {
                    urls = rawData.getJSONObject("urls");
                }
                if(urls != null) {
                    String office = (!urls.isNull("congress")) ? urls.getString("congress") : "N.A.";
                    if (office != null && !office.trim().isEmpty()) {
                        TextView t = (TextView) findViewById(R.id.bill_congress_url);
                        t.setText(office);
                    }
                }

                JSONObject last_version = null;
                if(!rawData.isNull("last_version")) {
                    last_version = rawData.getJSONObject("last_version");
                }
                if(last_version != null) {
                    String version = (!last_version.isNull("version_name")) ? last_version.getString("version_name") : "N.A.";
                    if (version != null && !version.trim().isEmpty()) {
                        TextView t = (TextView) findViewById(R.id.bill_version);
                        t.setText(version);
                    }

                    JSONObject burl = null;
                    if (!last_version.isNull("urls")) {
                        burl = last_version.getJSONObject("urls");
                    }
                    if (burl != null) {
                        String url = (!burl.isNull("pdf")) ? burl.getString("pdf") : "N.A.";
                        if (url != null && !url.trim().isEmpty()) {
                            TextView t = (TextView) findViewById(R.id.bill_url);
                            t.setText(url);
                        }
                    }
                }

                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");

                String birthday = (!rawData.isNull("introduced_on")) ? rawData.getString("introduced_on") : "N.A.";
                if (birthday != null && !birthday.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.bill_intro);
                    t.setText(format2.format(format1.parse(birthday)));
//                    t.setText(birthday);
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
                if (link == null || link.trim().isEmpty() || link.equalsIgnoreCase("https://facebook.com/#") || link.equalsIgnoreCase("https://twitter.com/#") || link.equalsIgnoreCase("#")) {
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

