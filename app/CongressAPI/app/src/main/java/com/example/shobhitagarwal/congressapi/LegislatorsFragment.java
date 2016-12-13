package com.example.shobhitagarwal.congressapi;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LegislatorsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LegislatorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LegislatorsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LegislatorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LegislatorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LegislatorsFragment newInstance(String param1, String param2) {
        LegislatorsFragment fragment = new LegislatorsFragment();
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
        View view = inflater.inflate(R.layout.fragment_legislators2, container, false);

        TabHost host = (TabHost) view.findViewById(R.id.legislatorsTabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("BY STATES");
        spec.setContent(R.id.tab1);
        spec.setIndicator("BY STATES");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("HOUSE");
        spec.setContent(R.id.tab2);
        spec.setIndicator("HOUSE");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("SENATE");
        spec.setContent(R.id.tab3);
        spec.setIndicator("SENATE");
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
        host.setOnTabChangedListener(new OnTabChangeListener() {
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
        RestJSON testAsyncTask = new RestJSON(getActivity(), "http://congresssearch-1.efcrywsxmt.us-west-2.elasticbeanstalk.com/hw8/congress.php?database=0&filter=state", R.id.legislatorsByState, getActivity(), "Legislator Info", "State");
        testAsyncTask.execute();

        // fetch data for legislators house
        testAsyncTask = new RestJSON(getActivity(), "http://congresssearch-1.efcrywsxmt.us-west-2.elasticbeanstalk.com/hw8/congress.php?database=0&filter=house", R.id.legislatorsHouse, getActivity(), "Legislator Info", "House");
        testAsyncTask.execute();

        // fetch data for legislators senate
        testAsyncTask = new RestJSON(getActivity(), "http://congresssearch-1.efcrywsxmt.us-west-2.elasticbeanstalk.com/hw8/congress.php?database=0&filter=senate", R.id.legislatorsSenate, getActivity(), "Legislator Info", "Senate");
        testAsyncTask.execute();

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
