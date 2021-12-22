package com.lucasteo.runningtracker.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.animation.ComponentAnimator;
import com.lucasteo.runningtracker.calculation.CustomMath;
import com.lucasteo.runningtracker.calculation.SpeedStatus;
import com.lucasteo.runningtracker.model.entity.Track;
import com.lucasteo.runningtracker.view_model.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // main
    private MainViewModel viewModel;

    // UI components
    private Button serviceBtn;
    private TextView statusTextView;
    private TextView speedTextView;
    private ImageView statusImageView;


    // animation
    private final ComponentAnimator animator = new ComponentAnimator();
    private final int animDuration = 250;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment home.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // look for UI components
        serviceBtn = requireView().findViewById(R.id.button);
        statusTextView = requireView().findViewById(R.id.statusTextView);
        speedTextView = requireView().findViewById(R.id.speedTextView);
        statusImageView = requireView().findViewById(R.id.statusImageView);

        // button onClick
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // switch button display text and controlling service
                MainActivity mainActivity = (MainActivity)requireActivity();
                if (viewModel.getValueServiceStatus()){
                    mainActivity.stopTrackerService();
                } else {
                    mainActivity.runTrackerService();
                }
                viewModel.toggleServiceStatus();
            }
        });

//        viewModel.getSpeedStatus().observe(requireActivity(), new Observer<SpeedStatus>() {
//            @Override
//            public void onChanged(SpeedStatus status) {
//                int textResid = R.string.status_sleeping;
//                int imageResid = R.drawable.ic_baseline_hotel_24;
//
//                if (status != null) {
//                    switch (status){
//                        case STANDING:
//                            textResid = R.string.status_standing;
//                            imageResid = R.drawable.ic_baseline_nature_people_24;
//                            break;
//                        case WALKING:
//                            textResid = R.string.status_walking;
//                            imageResid = R.drawable.ic_baseline_elderly_24;
//                            break;
//                        case JOGGING:
//                            textResid = R.string.status_jogging;
//                            imageResid = R.drawable.ic_baseline_directions_walk_24;
//                            break;
//                        case RUNNING:
//                            textResid = R.string.status_running;
//                            imageResid = R.drawable.ic_baseline_directions_run_24;
//                            break;
//                        case CYCLING:
//                            textResid = R.string.status_cycling;
//                            imageResid = R.drawable.ic_baseline_directions_bike_24;
//                            break;
//                        case DRIVING:
//                            textResid = R.string.status_too_fast;
//                            imageResid = R.drawable.ic_baseline_directions_car_24;
//                            break;
//                    }
//                }
//
//                animator.textViewFadeSetText(statusTextView, animDuration, 0, textResid);
//                animator.imageViewFadeSetResource(statusImageView, animDuration, 200, imageResid);
//
//            }
//        });

        // observe service status to change UI
        viewModel.getServiceStatus().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){

                    serviceBtn.setText(R.string.btnStop);
                    updateGUI(view, SpeedStatus.STANDING, 0);

                } else {
                    serviceBtn.setText(R.string.btnStart);
//                    viewModel.setValueSpeedStatus(null);
                    viewModel.setValueStopMoving(false);

                    updateGUI(view,null, 0);
                }
            }
        });

        // observe stop moving
        viewModel.getStopMoving().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){

                    updateGUI(view, SpeedStatus.STANDING, 0);

                }
            }
        });

        // observe track data
        viewModel.getLastTrack().observe(requireActivity(), new Observer<Track>() {
            @Override
            public void onChanged(Track track) {

                // display speed
                double speed = CustomMath.round(track.getSpeed(), 2);
//                String text = getSpeedString(view, speed);
//                animator.textViewFadeSetText(speedTextView, animDuration, 400, text);

//                if (viewModel.getValueServiceStatus() && viewModel.isJustStarted()){
//                    viewModel.setJustStarted(false);
//                } else {
//                }
                if (viewModel.getValueServiceStatus() && !viewModel.getValueStopMoving()){
                    Log.d("runningTracker", "onChanged: " + viewModel.getValueServiceStatus() + viewModel.getValueStopMoving());
                    updateGUI(view, SpeedStatus.valueOf(track.getActivity()), speed);
                }

            }
        });

    }

    // display helper functions

    public void updateGUI(View view , SpeedStatus status, double speed){

        int textResid = R.string.status_sleeping;
        int imageResid = R.drawable.ic_baseline_hotel_24;
        String speedText = getEmptySpeedString(view);

        if (status != null) {
            speedText = getSpeedString(view, speed);
            switch (status){
                case STANDING:
                    textResid = R.string.status_standing;
                    imageResid = R.drawable.ic_baseline_nature_people_24;
                    speedText = getZeroSpeedString(view);
                    break;
                case WALKING:
                    textResid = R.string.status_walking;
                    imageResid = R.drawable.ic_baseline_elderly_24;
                    break;
                case JOGGING:
                    textResid = R.string.status_jogging;
                    imageResid = R.drawable.ic_baseline_directions_walk_24;
                    break;
                case RUNNING:
                    textResid = R.string.status_running;
                    imageResid = R.drawable.ic_baseline_directions_run_24;
                    break;
                case CYCLING:
                    textResid = R.string.status_cycling;
                    imageResid = R.drawable.ic_baseline_directions_bike_24;
                    break;
                case DRIVING:
                    textResid = R.string.status_too_fast;
                    imageResid = R.drawable.ic_baseline_directions_car_24;
                    break;
            }
        }

        if (!statusTextView.getText().equals(view.getResources().getString(textResid))){
            animator.textViewFadeSetText(statusTextView, animDuration, 0, textResid);
            animator.imageViewFadeSetResource(statusImageView, animDuration, 200, imageResid);
        }
        if (!speedTextView.getText().equals(speedText)){
            animator.textViewFadeSetText(speedTextView, animDuration, 400, speedText);
        }

    }

    public String getSpeedString(View view, double speed){
        return speed + " " +
                view.getResources().getString(R.string.meter_per_second);
    }

    public String getEmptySpeedString(View view){
        return view.getResources().getString(R.string.empty_speed) + " " +
                view.getResources().getString(R.string.meter_per_second);
    }

    public String getZeroSpeedString(View view){
        return view.getResources().getString(R.string.zero_speed) + " " +
                view.getResources().getString(R.string.meter_per_second);
    }

}