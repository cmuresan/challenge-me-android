package com.example.cristianmmuresan.challengeme.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.data.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian M. Muresan on 5/21/2016.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivitiesViewHolder> {

    private ArrayList<Activity> mActivities;
    private Context mContext;

    public ActivitiesAdapter(Context context){
        mContext = context;

    }

    public void setActivities(List<Activity> activities){
        mActivities = new ArrayList<>(activities);
        notifyDataSetChanged();
    }
    
    @Override
    public ActivitiesAdapter.ActivitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ruleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ActivitiesViewHolder(ruleView);
    }

    @Override
    public void onBindViewHolder(ActivitiesAdapter.ActivitiesViewHolder holder, int position) {
        holder.activityName.setText(mActivities.get(position).getName());
        holder.activityTime.setText(mActivities.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mActivities == null ? 0 : mActivities.size();
    }

    public class ActivitiesViewHolder extends RecyclerView.ViewHolder{
        TextView activityName;
        TextView activityTime;

        public ActivitiesViewHolder(View itemView) {
            super(itemView);
            activityName = (TextView) itemView.findViewById(R.id.activity_name);
            activityTime = (TextView) itemView.findViewById(R.id.activity_time);
        }
    }
}
