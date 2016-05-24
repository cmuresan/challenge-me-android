package com.example.cristianmmuresan.challengeme.ui.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cristianmmuresan.challengeme.Globals;
import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.data.Activity;
import com.example.cristianmmuresan.challengeme.ui.home.ActivitiesAdapter;
import com.example.cristianmmuresan.challengeme.util.ProgressDialogUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MyActivitiesFragment extends Fragment {

    private RecyclerView recyclerViewRules;
    private View fragmentView;
    private ActivitiesAdapter mRulesAdapter;
    private ProgressDialogUtil mProgressDialogUtil;

    public MyActivitiesFragment() {
        // Required empty public constructor
    }

    public static MyActivitiesFragment newInstance() {
        MyActivitiesFragment fragment = new MyActivitiesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_activities, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Setup RecyclerView
        setupRecyclerView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivities();
    }

    private void setupRecyclerView(View view) {
        recyclerViewRules = (RecyclerView) view.findViewById(R.id.recycler_view_rules);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewRules.setLayoutManager(linearLayoutManager);
        mRulesAdapter = new ActivitiesAdapter(getActivity());
        recyclerViewRules.setAdapter(mRulesAdapter);
    }

    private void getActivities() {
        mProgressDialogUtil = new ProgressDialogUtil(getActivity());
        mProgressDialogUtil.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Activity");
        query.whereEqualTo("user", Globals.iUser.getEmail());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mProgressDialogUtil.dismiss();
                if(e==null){
                    List<Activity> activities = new ArrayList<>();
                    for(ParseObject po : list){
                        Activity act = new Activity(po);
                        activities.add(act);
                    }
                    mRulesAdapter.setActivities(activities);
                }else{
                    Toast.makeText(getActivity(), "There was a problem fetching the data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
