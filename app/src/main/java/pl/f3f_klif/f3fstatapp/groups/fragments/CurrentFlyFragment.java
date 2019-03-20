package pl.f3f_klif.f3fstatapp.groups.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.handlers.CurrentFlyHandler;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.utils.UsbService;

public class CurrentFlyFragment extends Fragment {
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

    private int flightNumber;
    public float flightTimeResult = 0f;
    private CurrentFlyHandler currentFlyHandler;
    private UsbService usbService;
    private static Round round;

    public Result result;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        flightNumber = args.getInt("flightNumber", 0);

        result = new Result(flightNumber, round.getId());

        currentFlyHandler = new CurrentFlyHandler(this);

        //TODO save current state of flight and read frames in background
    }

    @OnClick(R.id.assign_pilot_button)
    void onAssignPilotButtonClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.penalty_points);

        final EditText input = new EditText(this.getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton(R.string.assign, (dialog, which) -> {
            String inputString = input.getText().toString();
            float penaltyPoints = inputString.isEmpty() ? 0f : Float.parseFloat(inputString);
            result.setPenalty(penaltyPoints);
            showFragment(RoundFragment.newInstance(round, flightNumber, result));

        });
        builder.show();

    }

    @OnClick(R.id.cancel_button)
    void onCancelButtonClick() {
        showFragment(RoundFragment.newInstance(round));
    }

    @OnClick(R.id.dnf_button)
    void onDNFButtonClick() {
        result.setDnf(true);
        result.setTotalFlightTime(0f);
        showFragment(RoundFragment.newInstance(round, flightNumber, result));
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


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(currentFlyHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getActivity().unregisterReceiver(mUsbReceiver);
        this.getActivity().unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this.getActivity(), service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            this.getActivity().startService(startService);
        }
        Intent bindingIntent = new Intent(this.getActivity(), service);
        this.getActivity().bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        this.getActivity().registerReceiver(mUsbReceiver, filter);
    }

    public void showFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction
                .replace(R.id.container, fragment, "fragment")
                .commit();
    }
}
