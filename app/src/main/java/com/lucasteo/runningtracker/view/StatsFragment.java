package com.lucasteo.runningtracker.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.model.GroupByDateTrackPojo;
import com.lucasteo.runningtracker.model.Track;
import com.lucasteo.runningtracker.viewHelper.GroupByDateTrackAdapter;
import com.lucasteo.runningtracker.viewHelper.SpacingItemDecorator;
import com.lucasteo.runningtracker.viewHelper.TrackAdapter;
import com.lucasteo.runningtracker.viewmodel.MainViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    private MainViewModel viewModel;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment stats.
     */
    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel =
                new ViewModelProvider(getActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load UI
//        RecyclerView recyclerView = getView().findViewById(R.id.itemList);
//        final TrackAdapter adapter = new TrackAdapter(getActivity());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        viewModel.getAllTracks().observe(getActivity(), new Observer<List<Track>>() {
//            @Override
//            public void onChanged(List<Track> tracks) {
//                adapter.setData(tracks);
//            }
//        });

        RecyclerView recyclerView = getView().findViewById(R.id.itemList);

        GroupByDateTrackAdapter adapter = new GroupByDateTrackAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SpacingItemDecorator spacingItemDecorator =
                new SpacingItemDecorator(
                        getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing)
                );
        recyclerView.addItemDecoration(spacingItemDecorator);

        viewModel.getAllGroupByDayTrack().observe(getActivity(), new Observer<List<GroupByDateTrackPojo>>() {
            @Override
            public void onChanged(List<GroupByDateTrackPojo> items) {
                adapter.setData(items);
            }
        });

    }
}