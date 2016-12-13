package com.example.shobhitagarwal.congressapi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShobhitAgarwal on 17/11/16.
 */

public class RestJSON extends AsyncTask<Void, Void, String> implements View.OnClickListener {
    private Context mContext;
    private String mUrl;
    private int id;
    private Activity activity;
    private String title;
    private String subtitle;

    Map<String, Integer> mapIndex;
    ListView legListView;

    public RestJSON(Context context, String url, int id, Activity activity, String title, String subtitle) {
        mContext = context;
        mUrl = url;
        this.id = id;
        this.activity = activity;
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String resultString = null;
        resultString = getJSON(mUrl);

        return resultString;
    }

    @Override
    protected void onPostExecute(String strings) {
        super.onPostExecute(strings);
        ListView listId = (ListView) activity.findViewById(id);
        ArrayList<String> result = new ArrayList<String>();
        JSONArray dataArray = new JSONArray();
        try {
            JSONObject rawData = new JSONObject(strings);
            dataArray = rawData.getJSONArray("results");

            if(title.equalsIgnoreCase("Legislator Info")) {
                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                for (int i = 0; i < dataArray.length(); i++) {
                    jsonValues.add(dataArray.getJSONObject(i));
                }
                Collections.sort( jsonValues, new Comparator<JSONObject>() {
                    //You can change "Name" with "ID" if you want to sort by ID
                    private static final String KEY_NAME = "last_name";

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = a.getString(KEY_NAME);
                            valB = b.getString(KEY_NAME);
                        }
                        catch (JSONException e) {
                            //do something
                        }

                        return valA.compareTo(valB);
                        //if you want to change the sort order, simply use the following:
                        //return -valA.compareTo(valB);
                    }
                });

                if(subtitle.equalsIgnoreCase("state")) {
                    Collections.sort( jsonValues, new Comparator<JSONObject>() {
                        //You can change "Name" with "ID" if you want to sort by ID
                        private static final String KEY_NAME = "state_name";

                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            String valA = new String();
                            String valB = new String();

                            try {
                                valA = a.getString(KEY_NAME);
                                valB = b.getString(KEY_NAME);
                            }
                            catch (JSONException e) {
                                //do something
                            }

                            return valA.compareTo(valB);
                            //if you want to change the sort order, simply use the following:
                            //return -valA.compareTo(valB);
                        }
                    });
                }
                result.clear();
                for (int i = 0; i < dataArray.length(); i++) {
                    String s = jsonValues.get(i).toString();
                    if (s == null || s.trim().isEmpty())
                        s = "N.A.";
                    result.add(s);
                }

                final LegislatorAdapter ladapter = new LegislatorAdapter(activity, result.toArray(new String[0]));
                listId.setAdapter(ladapter);
                listId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        ladapter.onItemClick(position);
                    }});

                legListView = listId;
                if(subtitle.equalsIgnoreCase("state")) {
                    getIndexStateList(result.toArray(new String[0]), activity);
                } else {
                    getIndexList(result.toArray(new String[0]), activity);
                }
                displayIndex(activity);

            }
            else if (title.equalsIgnoreCase("Committee Info")) {

                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                for (int i = 0; i < dataArray.length(); i++) {
                    jsonValues.add(dataArray.getJSONObject(i));
                }
                Collections.sort( jsonValues, new Comparator<JSONObject>() {
                    //You can change "Name" with "ID" if you want to sort by ID
                    private static final String KEY_NAME = "name";

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = a.getString(KEY_NAME);
                            valB = b.getString(KEY_NAME);
                        }
                        catch (JSONException e) {
                            //do something
                        }

                        return valA.compareTo(valB);
                        //if you want to change the sort order, simply use the following:
                        //return -valA.compareTo(valB);
                    }
                });

                result.clear();
                for (int i = 0; i < dataArray.length(); i++) {
                    String s = jsonValues.get(i).toString();
                    if (s == null || s.trim().isEmpty())
                        s = "N.A.";
                    result.add(s);
                }

                final CommitteeAdapter cadapter = new CommitteeAdapter(activity, result.toArray(new String[0]));
                listId.setAdapter(cadapter);
                listId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        cadapter.onItemClick(position);
                    }});

            }
            else if (title.equalsIgnoreCase("Bill Info")) {
                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                for (int i = 0; i < dataArray.length(); i++) {
                    jsonValues.add(dataArray.getJSONObject(i));
                }
                Collections.sort( jsonValues, new Comparator<JSONObject>() {
                    //You can change "Name" with "ID" if you want to sort by ID
                    private static final String KEY_NAME = "introduced_on";

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = a.getString(KEY_NAME);
                            valB = b.getString(KEY_NAME);
                        }
                        catch (JSONException e) {
                            //do something
                        }

                        return -valA.compareTo(valB);
                        //if you want to change the sort order, simply use the following:
                        //return -valA.compareTo(valB);
                    }
                });

                result.clear();
                for (int i = 0; i < dataArray.length(); i++) {
                    String s = jsonValues.get(i).toString();
                    if (s == null || s.trim().isEmpty())
                        s = "N.A.";
                    result.add(s);
                }

                final BillAdapter badapter = new BillAdapter(activity, result.toArray(new String[0]));
                listId.setAdapter(badapter);
                listId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        badapter.onItemClick(position);
                    }});

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getJSON(String url) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.connect();
            int status = c.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (Exception ex) {
            return ex.toString();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    //disconnect error
                }
            }
        }
        return null;
    }

    public static void addToFavorites(Activity activityStatic, String database, String key, String value, ImageView fav) {
        if(database == null || database.isEmpty() || key == null || key.isEmpty() || value == null || value.isEmpty()) {
            return;
        }
        SharedPreferences favorite = activityStatic.getSharedPreferences(database, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = favorite.edit();
        if(favorite.contains(key)) {
            editor.remove(key);
            Log.i("exists", database + " " + key);
            fav.setImageResource(R.drawable.grey_star);
        }
        else {
            Log.i("new", database + " " + key);
            editor.putString(key, value);
            fav.setImageResource(R.drawable.gold_star);
        }
        editor.commit();

//        ListView listId = (ListView) findViewById(R.id.favoriteLegislators);
        int listTextId = R.id.favoriteLegislators;
        if (database.equalsIgnoreCase("bill")) {
//            listId = (ListView) activityStatic.findViewById(R.id.favoriteBills);
            listTextId = R.id.favoriteBills;
        } else if (database.equalsIgnoreCase("committee")) {
//            listId = (ListView) activityStatic.findViewById(R.id.favoriteCommittees);
            listTextId = R.id.favoriteCommittees;
        }
        Favorites.refreshPage(database, listTextId, activityStatic);
    }


    private void getIndexList(String[] data, Activity activity) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < data.length; i++) {

            String dd = data[i];
            try {
                JSONObject obj = new JSONObject(dd);
                String index = obj.getString("last_name").substring(0, 1).toUpperCase();
                if (mapIndex.get(index) == null)
                    mapIndex.put(index, i);
            } catch (Exception e) {

            }
        }
    }

    private void getIndexStateList(String[] data, Activity activity) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < data.length; i++) {

            String dd = data[i];
            try {
                JSONObject obj = new JSONObject(dd);
                String index = obj.getString("state_name").substring(0, 1).toUpperCase();
                if (mapIndex.get(index) == null)
                    mapIndex.put(index, i);
            } catch (Exception e) {

            }
        }
    }

    private void displayIndex(Activity activity) {
        int index_id = R.id.side_index_state;

        if(subtitle.equalsIgnoreCase("state")) {
            index_id = R.id.side_index_state;
        }

        else if(subtitle.equalsIgnoreCase("house")) {
            index_id = R.id.side_index_house;
        }
        else if(subtitle.equalsIgnoreCase("senate")) {
            index_id = R.id.side_index_senate;
        }
        LinearLayout indexLayout = (LinearLayout) activity.findViewById(index_id);

        indexLayout.removeAllViews();
        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) activity.getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        legListView.setSelection(mapIndex.get(selectedIndex.getText()));
    }

}