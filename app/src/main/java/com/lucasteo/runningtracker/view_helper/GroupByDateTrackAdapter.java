package com.lucasteo.runningtracker.view_helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.model.pojo.GroupByDateTrackPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * track adapter for recycler view
 */
public class GroupByDateTrackAdapter extends RecyclerView.Adapter<GroupByDateTrackAdapter.GroupByDateTrackViewHolder> {

    // variables
    private List<GroupByDateTrackPojo> data;
    private final Context context;
    private final LayoutInflater layoutInflater;

    // constructor
    public GroupByDateTrackAdapter(Context context){
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public GroupByDateTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.group_by_date_track_item, parent, false);
        return new GroupByDateTrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupByDateTrackViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<GroupByDateTrackPojo> newData){
        if (data != null){
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    static class GroupByDateTrackViewHolder extends RecyclerView.ViewHolder{

        View itemView;

        TextView dateView;
        TextView distanceView;
        TextView recordView;
        TextView avgSpeedView;
        TextView maxSpeedView;

        public GroupByDateTrackViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            dateView = itemView.findViewById(R.id.dateTextVIew);
            distanceView = itemView.findViewById(R.id.distanceTextView);
            recordView = itemView.findViewById(R.id.recordTextView);
            avgSpeedView = itemView.findViewById(R.id.avgSpeedTextView);
            maxSpeedView = itemView.findViewById(R.id.maxSpeedTextView);

        }

        void bind(final GroupByDateTrackPojo groupByDateTrackPojo){
            if (groupByDateTrackPojo != null){

                String meter = itemView.getResources().getString(R.string.meter);
                String meterPerSec = itemView.getResources().getString(R.string.meter_per_second);

                dateView.setText(groupByDateTrackPojo.getRecord_date());
                distanceView.setText(String.format("%s " + meter, groupByDateTrackPojo.getTotal_distance()));
                recordView.setText(String.valueOf(groupByDateTrackPojo.getNumber_of_records()));
                avgSpeedView.setText(String.format("%s " + meterPerSec, groupByDateTrackPojo.getAverage_speed()));
                maxSpeedView.setText(String.format("%s " + meterPerSec, groupByDateTrackPojo.getMaximum_speed()));
            }
        }
    }
}
