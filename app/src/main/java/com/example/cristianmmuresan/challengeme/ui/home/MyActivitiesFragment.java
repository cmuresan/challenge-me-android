package com.example.cristianmmuresan.challengeme.ui.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.data.Activity;

import java.util.ArrayList;
import java.util.List;

public class MyActivitiesFragment extends Fragment {

    private RecyclerView recyclerViewRules;
    private View fragmentView;

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

    private void setupRecyclerView(View view) {
        recyclerViewRules = (RecyclerView) view.findViewById(R.id.recycler_view_rules);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewRules.setLayoutManager(linearLayoutManager);
        ActivitiesAdapter rulesAdapter = new ActivitiesAdapter(getActivity(), getRules());
        recyclerViewRules.setAdapter(rulesAdapter);
    }

    private ArrayList<Activity> getRules() {
        return new ArrayList<>();
    }
}
