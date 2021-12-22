package com.lucasteo.runningtracker.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.view_helper.GroupByDateTrackAdapter;
import com.lucasteo.runningtracker.view_helper.SpacingItemDecorator;
import com.lucasteo.runningtracker.view_model.MainViewModel;


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
        return new StatsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel =
                new ViewModelProvider(requireActivity()).get(MainViewModel.class);
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
        RecyclerView recyclerView = requireView().findViewById(R.id.itemList);

        GroupByDateTrackAdapter adapter = new GroupByDateTrackAdapter(requireActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SpacingItemDecorator spacingItemDecorator =
                new SpacingItemDecorator(
                        getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing)
                );
        recyclerView.addItemDecoration(spacingItemDecorator);

        viewModel.getAllGroupByDayTrack().observe(requireActivity(), items -> adapter.setData(items));

    }
}