package com.example.cristianmmuresan.challengeme.ui.home.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.ui.record.RecordActivity;

public class RecordActivityFragment extends Fragment implements View.OnClickListener {
    public RecordActivityFragment() {
        // Required empty public constructor
    }

    public static RecordActivityFragment newInstance() {
        RecordActivityFragment fragment = new RecordActivityFragment();

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
        return inflater.inflate(R.layout.fragment_record_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.record_activity_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_activity_image:
                startActivity(new Intent(getActivity(), RecordActivity.class));
                break;
        }
    }
}
