package com.lucasteo.runningtracker.viewHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.model.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private List<Track> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public TrackAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.db_layout_view, parent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Track> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView kindView;

        TrackViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.nameView);
            kindView = itemView.findViewById(R.id.colourView);
        }

        void bind(final Track track) {

            if (track != null) {
                nameView.setText(String.valueOf(track.getTrackID()));
                kindView.setText(String.valueOf(track.getDistance()));
            }
        }

    }
}
