package com.lucasteo.runningtracker.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.animation.ComponentAnimator;
import com.lucasteo.runningtracker.calculation.SpeedStatus;
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

        viewModel.getSpeedStatus().observe(requireActivity(), new Observer<SpeedStatus>() {
            @Override
            public void onChanged(SpeedStatus status) {
                int textResid = R.string.status_sleeping;
                int imageResid = R.drawable.ic_baseline_hotel_24;

                if (status != null) {
                    switch (status){
                        case STANDING:
                            textResid = R.string.status_standing;
                            imageResid = R.drawable.ic_baseline_nature_people_24;
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

                animator.textViewFadeSetText(statusTextView, animDuration, 0, textResid);
                animator.imageViewFadeSetResource(statusImageView, animDuration, 250, imageResid);

            }
        });

        // change button text dynamically
        viewModel.getServiceStatus().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    serviceBtn.setText(R.string.btnStop);
//                    viewModel.getSpeedStatus().observe(requireActivity(), speedStatusObserver);
                } else {
                    serviceBtn.setText(R.string.btnStart);
                    viewModel.setValueSpeedStatus(null);
//                    viewModel.getSpeedStatus().removeObserver(speedStatusObserver);

                }
            }
        });

    }

}