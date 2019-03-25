package pl.f3f_klif.f3fstatapp.groups.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.handlers.CurrentFlyHandler;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.sqlite.WindMeasure;
import pl.f3f_klif.f3fstatapp.utils.UsbServiceBaseFragment;

public class CurrentFlyFragment extends UsbServiceBaseFragment {
    public static CurrentFlyFragment newInstance(Round round, int flightNumber) {
        CurrentFlyFragment f = new CurrentFlyFragment();
        Bundle args = new Bundle();
        args.putInt("flightNumber", flightNumber);
        CurrentFlyFragment.round = round;
        f.setArguments(args);
        return f;
    }

    @BindView(R.id.flight_number_text_view)
    TextView flightNumberTextView;
    @BindView(R.id.assign_pilot_button)
    public Button assignPilotButton;
    @BindView(R.id.dnf_button)
    public Button dnfButton;
    @BindView(R.id.start_time_result_text_view)
    public TextView startTimeResultTextView;
    @BindView(R.id.current_time_text_view)
    public TextView currentTimeTextView;
    @BindView(R.id.live_status_text_view)
    public TextView liveStatusTextView;
    @BindView(R.id.stats_container)
    public LinearLayout statsContainer;
    @BindView(R.id.current_wind_speed_text_view)
    public TextView currentWindSpeedTextView;
    @BindView(R.id.current_wind_direction_text_view)
    public TextView currentWindDirectionTextView;
    @BindView(R.id.avg_wind_speed_text_view)
    public TextView avgWindSpeedTextView;
    @BindView(R.id.avg_wind_direction_text_view)
    public TextView avgWindDirectionTextView;
    @BindView(R.id.statusImageView)
    public ImageView statusImageView;
    @BindView(R.id.penalty_points_edit)
    public EditText penaltyPointsInput;

    private int flightNumber;
    public float flightTimeResult = 0f;
    private static Round round;

    public Result result;

    public List<WindMeasure> windMeasures;
    public Timestamp currentTimestamp;

    public boolean isActiveListening;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        flightNumber = args.getInt("flightNumber", 0);

        result = new Result(flightNumber, round.getId());
        windMeasures = new ArrayList<>();

        isActiveListening = true;

        handler = new CurrentFlyHandler(this);

        //TODO save current state of flight and read frames in background
    }

    @OnClick(R.id.assign_pilot_button)
    void onAssignPilotButtonClick() {
            String inputString = penaltyPointsInput.getText().toString();
            float penaltyPoints = inputString.isEmpty() ? 0f : Float.parseFloat(inputString);
            result.setPenalty(penaltyPoints);
            showFragment(RoundFragment.newInstance(round, flightNumber, result, windMeasures));

    }

    @OnClick(R.id.cancel_button)
    void onCancelButtonClick() {
        showFragment(RoundFragment.newInstance(round));
    }

    @OnClick(R.id.dnf_button)
    void onDNFButtonClick() {
        result.setDnf(true);
        result.setTotalFlightTime(0f);
        showFragment(RoundFragment.newInstance(round, flightNumber, result, windMeasures));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_fly_fragment, container, false);
        ButterKnife.bind(this, view);

        flightNumberTextView.setText(getString(R.string.flight_number, flightNumber));

        currentTimeTextView.setText(getString(R.string.flight_current_time, 0f));

        currentWindSpeedTextView.setText(getString(R
                .string.current_wind_speed, 0f));
        avgWindSpeedTextView.setText(getString(R
                .string.avg_wind_speed, 0f));
        currentWindDirectionTextView.setText(getString(R
                .string.current_wind_direction, 0));
        avgWindDirectionTextView.setText(getString(R
                .string.avg_wind_direction, 0));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(getString(R.string.flight_number, flightNumber));
    }

    public void showFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction
                .replace(R.id.container, fragment, "fragment")
                .commit();
    }

    public void stopService() {
        isActiveListening = false;
        //usbService.stopService();
    }
}
