package com.example.shobhitagarwal.congressapi;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Favorites extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static View vw;

    static Map<String, Integer> mapIndex;
    static ListView legListView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Favorites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favorites.
     */
    // TODO: Rename and change types and number of parameters
    public static Favorites newInstance(String param1, String param2){
        Favorites fragment = new Favorites();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        vw = view;

        TabHost host = (TabHost) view.findViewById(R.id.favoritesTabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("LEGISLATORS");
        spec.setContent(R.id.tab1);
        spec.setIndicator("LEGISLATORS");
        host.addTab(spec);

        spec = host.newTabSpec("BILLS");
        spec.setContent(R.id.tab2);
        spec.setIndicator("BILLS");
        host.addTab(spec);

        spec = host.newTabSpec("COMMITTEES");
        spec.setContent(R.id.tab3);
        spec.setIndicator("COMMITTEES");
        host.addTab(spec);

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#3c3c3c"));
            tv.setTypeface(null, Typeface.NORMAL);
            host.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        int current = host.getCurrentTab();
        TextView tv = (TextView) host.getTabWidget().getChildAt(current).findViewById(android.R.id.title);
        tv.setTypeface(null, Typeface.BOLD);

        final TabHost h = host;
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                for (int i = 0; i < h.getTabWidget().getChildCount(); i++) {
                    TextView tv = (TextView) h.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(Color.parseColor("#3c3c3c"));
                    tv.setTypeface(null, Typeface.NORMAL);
                }
                int current = h.getCurrentTab();
                TextView tv = (TextView) h.getTabWidget().getChildAt(current).findViewById(android.R.id.title);
                tv.setTypeface(null, Typeface.BOLD);
            }
        });

        host.getTabWidget().setDividerDrawable(null);
        Activity activity = getActivity();
        refreshFavList(view, activity);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static void populateFavorites(final String database, final ListView listId, final Activity activity) {
        ArrayList<String> result = new ArrayList<String>();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();

        SharedPreferences favorite = activity.getSharedPreferences(database, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = favorite.getAll();
        try {
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                result.add(entry.getValue().toString());
                jsonValues.add(new JSONObject(entry.getValue().toString()));
            }

            JSONArray dataArray = new JSONArray(result);

            if (database.equalsIgnoreCase("legislator")) {

                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                    private static final String KEY_NAME = "last_name";

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = a.getString(KEY_NAME);
                            valB = b.getString(KEY_NAME);
                        } catch (JSONException e) {
                            //do something
                        }

                        return valA.compareTo(valB);
                    }
                });

            } else if (database.equalsIgnoreCase("bill")) {
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
                    }
                });
            } else if (database.equalsIgnoreCase("committee")) {
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String title_temp = "Legislator Info";
        if (database.equalsIgnoreCase("bill")) {
            title_temp = "Bill Info";
        } else if (database.equalsIgnoreCase("committee")) {
            title_temp = "Committee Info";
        }

        try {
            final String title = title_temp;
            final Activity activityStatic = activity;
            if (title.equalsIgnoreCase("Legislator Info")) {
                final LegislatorAdapter ladapter = new LegislatorAdapter(activity, result.toArray(new String[0]));
                listId.setAdapter(ladapter);
                listId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        ladapter.onItemClick(position);
                    }});

                legListView = listId;

                String[] data = result.toArray(new String[0]);
                mapIndex = new LinkedHashMap<String, Integer>();
                for (int i = 0; i < data.length; i++) {
                    String dd = data[i];
                    try {
                        JSONObject obj = new JSONObject(dd);
                        String index = obj.getString("last_name").substring(0, 1).toUpperCase();
                        if (mapIndex.get(index) == null)
                            mapIndex.put(index, i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                LinearLayout indexLayout = (LinearLayout) vw.findViewById(R.id.side_index_fav_leg);
                indexLayout.removeAllViews();

                TextView textView;
                List<String> indexList = new ArrayList<String>(mapIndex.keySet());
                for (String index : indexList) {
                    textView = (TextView) activity.getLayoutInflater().inflate(
                            R.layout.side_index_item, null);
                    textView.setText(index);
                    textView.setOnClickListener(new Favorites());
                    indexLayout.addView(textView);
                }

            }
            else if (title.equalsIgnoreCase("Committee Info")) {
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
                final BillAdapter badapter = new BillAdapter(activity, result.toArray(new String[0]));
                listId.setAdapter(badapter);
                listId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        badapter.onItemClick(position);
                    }});
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshFavList(View view, Activity activity) {
        ListView listLeg = (ListView) view.findViewById(R.id.favoriteLegislators);
        ListView listBill = (ListView) view.findViewById(R.id.favoriteBills);
        ListView listCom = (ListView) view.findViewById(R.id.favoriteCommittees);

        populateFavorites("legislator", listLeg, activity);
        populateFavorites("bill", listBill, activity);
        populateFavorites("committee", listCom, activity);

    }

    public static void refreshPage(String database, int listId, Activity activityStatic) {
        if (vw != null) {
            ListView listLeg = (ListView) vw.findViewById(R.id.favoriteLegislators);
            if (database.equalsIgnoreCase("Committee")) {
                listLeg = (ListView) vw.findViewById(R.id.favoriteCommittees);
            } else if (database.equalsIgnoreCase("Bill")) {
                listLeg = (ListView) vw.findViewById(R.id.favoriteBills);
            }

            populateFavorites(database, listLeg, activityStatic);

        }
    }


    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        legListView.setSelection(mapIndex.get(selectedIndex.getText()));
    }
}
