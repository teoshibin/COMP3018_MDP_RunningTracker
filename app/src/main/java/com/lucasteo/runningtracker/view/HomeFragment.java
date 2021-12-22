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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.animation.ComponentAnimator;
import com.lucasteo.runningtracker.service.SpeedStatus;
import com.lucasteo.runningtracker.viewmodel.MainViewModel;

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
                int resid = R.string.status_sleeping;

                if (status != null) {
                    switch (status){
                        case STANDING:
                            resid = R.string.status_standing;
                            break;
                        case WALKING:
                            resid = R.string.status_walking;
                            break;
                        case JOGGING:
                            resid = R.string.status_jogging;
                            break;
                        case RUNNING:
                            resid = R.string.status_running;
                            break;
                        case CYCLING:
                            resid = R.string.status_cycling;
                            break;
                        case DRIVING:
                            resid = R.string.status_too_fast;
                            break;
                    }
                }

//                if (statusTextView.getText() != getResources().getString(resid)){
//                }
//                if (justStarted){
//                    statusTextView.setText(resid);
//                    justStarted = false;
//                } else {
                    animator.textViewFadeSetText(statusTextView, animDuration, resid);
//                }
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