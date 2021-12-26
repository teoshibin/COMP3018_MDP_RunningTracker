package com.lucasteo.runningtracker.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.view_helper.ComponentAnimator;
import com.lucasteo.runningtracker.calculation.CustomMath;
import com.lucasteo.runningtracker.calculation.SpeedStatus;
import com.lucasteo.runningtracker.model.entity.Track;
import com.lucasteo.runningtracker.model.pojo.GroupByDateTrackPojo;
import com.lucasteo.runningtracker.view_model.MainViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //==============================================================================================
    // variables
    //==============================================================================================

    //region

    // main
    private MainViewModel viewModel;

    // shared preference
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    SharedPreferences.OnSharedPreferenceChangeListener sharedPrefListener;
    private final String SHARED_PREF_KEY_GOAL_VALUE = "goalValue";
    private final int DEFAULT_GOAL_VALUE = 10000;
    private final int DEFAULT_LOWEST_GOAL_VALUE = 5000;

    // UI components
    private Button serviceBtn;

    private TextView statusTextView;
    private TextView speedTextView;
    private ImageView statusImageView;

    private ProgressBar progressBar;
    private TextView progressValueTextView;
    private TextView progressSlashTextView;
    private TextView progressGoalValueTextView;

    private ImageView flagImageButton;
    private Dialog goalDialog;
    private EditText goalDialogEditText;
    private Button goalDialogCancelButton;
    private Button goalDialogSaveButton;

    private ImageView deleteImageButton;
    private Dialog deleteDialog;
    private Button deleteDialogCancelButton;
    private Button deteteDialogDeleteButton;

    // animation
    private final ComponentAnimator animator = new ComponentAnimator();
    private final int animDuration = 250;

    // animation phase delay
    private final int phase0 = 0;
    private final int phase1 = 200;
    private final int phase2 = phase1 * 2;
    private final int phase3 = phase1 * 3;

    //endregion

    //==============================================================================================
    // constructors
    //==============================================================================================

    //region
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
    //endregion

    //==============================================================================================
    // life cycle
    //==============================================================================================

    //region

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel =
                new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // shared preference
        sharedPref = requireActivity().getSharedPreferences("runningTracker", Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();

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
        progressBar = requireView().findViewById(R.id.progress_bar);
        progressValueTextView = requireView().findViewById(R.id.progressTextView1);
        progressGoalValueTextView = requireView().findViewById(R.id.progressTextView2);
        progressSlashTextView = requireView().findViewById(R.id.progressTextView3);
        flagImageButton = requireView().findViewById(R.id.flagImageButton);
        deleteImageButton = requireView().findViewById(R.id.binImageButton);

        // animate progress slash text view
        animator.textViewFadeSetText(progressSlashTextView, animDuration, phase0, R.string.slash);

        /// Buttons ///

        // start button listener
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // switch button display text and controlling service
                MainActivity mainActivity = (MainActivity)requireActivity();
                boolean success = false;

                if (viewModel.getValueServiceStatus()){
                    mainActivity.stopTrackerService();
                    success = true;
                } else {
                    success = mainActivity.runTrackerService();
                }

                if (success){
                    viewModel.toggleServiceStatus();
                }
            }
        });

        // flag button listener
        flagImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalDialog.show();
            }
        });

        // delete button listener
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.show();
            }
        });

        /// Goal Distance Dialog ///

        // create dialog view
        goalDialog = new Dialog(requireActivity());
        goalDialog.setContentView(R.layout.goal_dialog);
        goalDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_background));
        goalDialog.setCancelable(false);

        goalDialogCancelButton = goalDialog.findViewById(R.id.dialog_cancel_btn);
        goalDialogSaveButton = goalDialog.findViewById(R.id.dialog_save_btn);
        goalDialogEditText = goalDialog.findViewById(R.id.goalEditText);
        goalDialogEditText.setInputType(InputType.TYPE_CLASS_NUMBER); // only allow number

        // goal dialog cancel button listener
        goalDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalDialog.cancel();
                goalDialogEditText.setText("");
            }
        });

        // goal dialog save button listener
        goalDialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // this is required to fix a bug where listener is not listening properly on first install
                sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefListener);

                String text = String.valueOf(goalDialogEditText.getText());

                // if not empty string
                if (text.length() > 0){

                    int value = Integer.parseInt(text);

                    // if value not too low
                    if (value >= DEFAULT_LOWEST_GOAL_VALUE){

                        sharedPrefEditor.putInt(SHARED_PREF_KEY_GOAL_VALUE, value);
                        sharedPrefEditor.commit();
                        goalDialog.dismiss();
                        goalDialogEditText.setText("");

                    } else {
                        // if value too low
                        Toast.makeText(requireActivity().getApplicationContext(),
                                requireActivity().getString(R.string.dialog_goal_value_not_large_enough) +
                                        " " + DEFAULT_LOWEST_GOAL_VALUE, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // if empty string
                    Toast.makeText(requireActivity().getApplicationContext(),
                            R.string.invalid_value, Toast.LENGTH_SHORT).show();
                }
            }
        });

        /// delete dialog ///

        deleteDialog = new Dialog(requireActivity());
        deleteDialog.setContentView(R.layout.delete_dialog);
        deleteDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_background));
        deleteDialog.setCancelable(false);

        deleteDialogCancelButton = deleteDialog.findViewById(R.id.delete_dialog_cancel_btn);
        deteteDialogDeleteButton = deleteDialog.findViewById(R.id.delete_dialog_delete_button);

        // delete dialog cancel button listener
        deleteDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.cancel();
            }
        });

        // delete dialog delete button listener
        deteteDialogDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteAll();
                deleteDialog.dismiss();
            }
        });

        /// progress bar and text view below it ///

        // progress bar & progress text goal value
        int goalValue = sharedPref.getInt(SHARED_PREF_KEY_GOAL_VALUE, DEFAULT_GOAL_VALUE);
        // init if not exist
//        if (!sharedPref.contains(SHARED_PREF_KEY_GOAL_VALUE)){
//            sharedPrefEditor.putInt(SHARED_PREF_KEY_GOAL_VALUE, DEFAULT_GOAL_VALUE);
//            sharedPrefEditor.commit();
//            Log.d("tag", "onCreate: set value");
//        }
        updateGoalTextView(view, goalValue);
        progressBar.setMax(goalValue);
        progressBar.setMin(0);

        // progress text
        viewModel.getAllGroupByDayTrack().observe(requireActivity(), new Observer<List<GroupByDateTrackPojo>>() {
            @Override
            public void onChanged(List<GroupByDateTrackPojo> groupByDateTrackPojos) {

                int totalDistance = 0;
                int progress = totalDistance;

                if (!groupByDateTrackPojos.isEmpty()) {

                    GroupByDateTrackPojo groupByDateTrack = groupByDateTrackPojos.get(0);

                    totalDistance = (int) Math.round(groupByDateTrack.getTotal_distance());
                    progress = Math.min(totalDistance, goalValue);
                    progressBar.setProgress(progress, true);

                }

                // goal progress
                String progressText = totalDistance + " " + view.getResources().getString(R.string.meter);
                animator.textViewFadeSetText(progressValueTextView, animDuration, phase0, progressText);

            }
        });

        // goal text
        sharedPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals(SHARED_PREF_KEY_GOAL_VALUE)){
                    int goalValue = sharedPreferences.getInt(SHARED_PREF_KEY_GOAL_VALUE, DEFAULT_GOAL_VALUE);
                    updateGoalTextView(view, goalValue);
                    progressBar.setMax(goalValue);
                }
                Log.d("tag", "onSharedPreferenceChanged: ");
            }
        };
        // for some reason this listener assignment doesn't work sometimes
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefListener);

        // observe service status to change UI
        viewModel.getServiceStatus().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){

                    serviceBtn.setText(R.string.btnStop);
                    updateGUI(view, SpeedStatus.STANDING, 0);

                } else {
                    serviceBtn.setText(R.string.btnStart);
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

                double speed = 0;
                SpeedStatus status = null;

                if (track != null){
                    speed = CustomMath.round(track.getSpeed(), 2);
                    status = SpeedStatus.valueOf(track.getActivity());
                }

                if (viewModel.getValueServiceStatus() && !viewModel.getValueStopMoving()){
                    Log.d("runningTracker", "onChanged: " + viewModel.getValueServiceStatus() + viewModel.getValueStopMoving());
                    updateGUI(view, status, speed);
                }
            }
        });

    }

    @Override
    public void onPause() {
        // clean up shared pref listener
        sharedPref.unregisterOnSharedPreferenceChangeListener(sharedPrefListener);
        super.onPause();
    }

    //endregion

    //==============================================================================================
    // GUI helper methods
    //==============================================================================================

    //region

    /**
     * update goal text view
     *
     * @param view view
     */
    public void updateGoalTextView(View view, int goalValue){
        String goalText = goalValue + " " + view.getResources().getString(R.string.meter);
        animator.textViewFadeSetText(progressGoalValueTextView, animDuration, phase0, goalText);
    }

    /**
     * update speed status
     * update speed icon status
     * update speed text view
     *
     * @param view view
     * @param status status
     * @param speed speed
     */
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
            animator.textViewFadeSetText(statusTextView, animDuration, phase1, textResid);
            animator.imageViewFadeSetResource(statusImageView, animDuration, phase2, imageResid);
        }
        if (!speedTextView.getText().equals(speedText)){
            animator.textViewFadeSetText(speedTextView, animDuration, phase3, speedText);
        }

    }

    public String getSpeedString(View view, double speed){
        return speed + " " +
                view.getResources().getString(R.string.meter_per_second);
    }

    public String getEmptySpeedString(View view){
        return view.getResources().getString(R.string.dash) + " " +
                view.getResources().getString(R.string.meter_per_second);
    }

    public String getZeroSpeedString(View view){
        return view.getResources().getString(R.string.zero) + " " +
                view.getResources().getString(R.string.meter_per_second);
    }

    //endregion

}