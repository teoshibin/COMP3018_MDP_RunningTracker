package com.lucasteo.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;

import com.lucasteo.runningtracker.model.Track;
import com.lucasteo.runningtracker.service.TrackerService;
import com.lucasteo.runningtracker.viewHelper.TrackAdapter;
import com.lucasteo.runningtracker.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel =
                new ViewModelProvider(this,
                        ViewModelProvider
                                .AndroidViewModelFactory
                                .getInstance(this.getApplication())
                ).get(MainViewModel.class);

//        model = new ViewModelProvider(this).get(MainViewModel.class);

//        this.startService(new Intent(this, TrackerService.class));
//        this.bindService(new Intent(this, TrackerService.class), serviceConnection, Context.BIND_AUTO_CREATE);

        // TODO request user permission


//        RecyclerView recyclerView = findViewById(R.id.itemList);
//        final TrackAdapter adapter = new TrackAdapter(this);
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        viewModel.getAllTracks().observe(this, tracks -> {
//            adapter.setData(tracks);
//        });

    }

    public void btnOnClick(View view) {

        viewModel.insert(new Track(3, 20));
    }
}