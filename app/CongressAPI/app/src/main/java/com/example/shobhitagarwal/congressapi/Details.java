package com.example.shobhitagarwal.congressapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String title = getIntent().getStringExtra("database");
        String jsonString = getIntent().getStringExtra("jsonString");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (title.equalsIgnoreCase("Legislator Info")) {
            try {
                JSONObject rawData = new JSONObject(jsonString);

                String web = "#";
                if (!rawData.isNull("website") && !rawData.getString("website").trim().isEmpty()) {
                    web = rawData.getString("website");
                }

                String fb = "#";
                if (!rawData.isNull("facebook_id") && !rawData.getString("facebook_id").trim().isEmpty()) {
                    fb = rawData.getString("facebook_id");
                }

                String tw = "#";
                if (!rawData.isNull("twitter_id") && !rawData.getString("twitter_id").trim().isEmpty()) {
                    tw = rawData.getString("twitter_id");
                }

                setImageLinks(R.id.fbLeg, "https://facebook.com/" + fb);
                setImageLinks(R.id.twLeg, "https://twitter.com/" + tw);
                setImageLinks(R.id.webLeg, web);

                String bioguide_id = (!rawData.isNull("bioguide_id")) ? rawData.getString("bioguide_id") : "N.A.";
                ;
                if (bioguide_id != null && !bioguide_id.trim().isEmpty()) {
                    String img = "https://theunitedstates.io/images/congress/original/" + bioguide_id + ".jpg";

                    ImageView imgview = (ImageView) findViewById(R.id.legImg);
                    LoadImage loadImg = new LoadImage(img, "legImg", this, imgview);
                    loadImg.execute();
                }

                final ImageView fav = (ImageView) findViewById(R.id.favLeg);

                SharedPreferences favorite = this.getSharedPreferences("legislator", Context.MODE_PRIVATE);
                if (favorite.contains(bioguide_id)) {
                    fav.setImageResource(R.drawable.gold_star);
                }

                final String id = bioguide_id;
                final Activity activity = this;
                final String value = jsonString;
                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RestJSON.addToFavorites(activity, "legislator", id, value, fav);
                    }
                });

                String party = (!rawData.isNull("party")) ? rawData.getString("party") : "N.A.";
                ;
                String ptext = "Independent";
                if (party != null && !party.trim().isEmpty()) {
                    party = party.toLowerCase();
                    if (party.equalsIgnoreCase("r")) {
                        ptext = "Republican";
                    } else if (party.equalsIgnoreCase("d")) {
                        ptext = "Democrat";
                    }
                } else {
                    party = "i";
                }
                String party_img = "drawable/" + party;

                ImageView partyImage = (ImageView) findViewById(R.id.chamberImg);
                int imageResource = getResources().getIdentifier(party_img, null, getPackageName());

                Drawable image = getResources().getDrawable(imageResource);
                partyImage.setImageDrawable(image);

                TextView chamberText = (TextView) findViewById(R.id.chamberText);
                chamberText.setText(ptext);

                String rep = (!rawData.isNull("title")) ? rawData.getString("title") : "";
                String lname = (!rawData.isNull("last_name")) ? rawData.getString("last_name") : "";
                String fname = (!rawData.isNull("first_name")) ? rawData.getString("first_name") : "";
                rep = (rep != null) ? rep : "";
                if (lname != null && !lname.trim().isEmpty() && fname != null && !fname.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.name);
                    t.setText(rep + ". " + lname + ", " + fname);
                }

                String email = (!rawData.isNull("oc_email")) ? rawData.getString("oc_email") : "N.A.";
                if (email != null && !email.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.email);
                    t.setText(email);
                }

                String chamber = (!rawData.isNull("chamber")) ? rawData.getString("chamber") : "N.A.";
                if (chamber != null && !chamber.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.chamber);
                    t.setText(chamber.substring(0, 1).toUpperCase() + chamber.substring(1));
                }

                String contact = (!rawData.isNull("phone")) ? rawData.getString("phone") : "N.A.";
                if (contact != null && !contact.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.contact);
                    t.setText(contact);
                }

                String fax = (!rawData.isNull("fax")) ? rawData.getString("fax") : "N.A.";
                if (fax != null && !fax.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.fax);
                    t.setText(fax);
                }

                String state = (!rawData.isNull("state")) ? rawData.getString("state") : "N.A.";
                if (state != null && !state.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.state);
                    t.setText(state);
                }

                String office = (!rawData.isNull("office")) ? rawData.getString("office") : "N.A.";
                if (office != null && !office.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.office);
                    t.setText(office);
                }

                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");

                String birthday = (!rawData.isNull("birthday")) ? rawData.getString("birthday") : "N.A.";
                if (birthday != null && !birthday.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.birthday);
                    t.setText(format2.format(format1.parse(birthday)));
//                    t.setText(birthday);
                }

                String ts = (!rawData.isNull("term_start")) ? rawData.getString("term_start") : "N.A.";
                Date start = null;
                Date end = null;
                if (ts != null && !ts.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.start_term);
                    start = format1.parse(ts);
                    t.setText(format2.format(start));
                }

                String te = (!rawData.isNull("term_end")) ? rawData.getString("term_end") : "N.A.";
                if (te != null && !te.trim().isEmpty()) {
                    TextView t = (TextView) findViewById(R.id.end_term);
                    end = format1.parse(te);
                    t.setText(format2.format(end));
                }

                int progress = 0;
                TextView termText = (TextView) findViewById(R.id.progress);
                if (start != null && end != null && start.getTime() != end.getTime()) {
                    long now = System.currentTimeMillis();
                    long s = start.getTime();
                    long e = end.getTime();
                    if (s >= e || now >= e) {
                        progress = 100;
                    } else if (now <= s) {
                        progress = 0;
                    } else {
                        progress = (int) ((now - s) * 100 / (e - s));
                    }
                    String prText = progress + "%";
                    termText.setText(prText);
                }

                ProgressBar term = (ProgressBar) findViewById(R.id.term);
                term.setProgress(progress);
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

class LoadImage extends AsyncTask<Void, Void, Void> {

    String img_url;
    String img_id;
    Activity activity;
    Bitmap bitMap;
    ImageView imgViewId;

    public LoadImage(String img_url, String img_id, Activity activity, ImageView imgViewId) {
        this.img_url = img_url;
        this.img_id = img_id;
        this.activity = activity;
        this.imgViewId = imgViewId;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try {
            URL url = new URL(img_url);
            bitMap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        imgViewId.setImageBitmap(bitMap);

    }
}
