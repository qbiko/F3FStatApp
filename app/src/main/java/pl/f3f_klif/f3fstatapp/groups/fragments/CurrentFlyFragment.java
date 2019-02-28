package pl.f3f_klif.f3fstatapp.groups.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.handlers.CurrentFlyHandler;
import pl.f3f_klif.f3fstatapp.handlers.StartListHandler;
import pl.f3f_klif.f3fstatapp.utils.UsbService;

public class CurrentFlyFragment extends Fragment {
    public static CurrentFlyFragment newInstance(int flightNumber) {
        CurrentFlyFragment f = new CurrentFlyFragment();
        Bundle args = new Bundle();
        args.putInt("flightNumber", flightNumber);
        f.setArguments(args);
        return f;
    }

    @BindView(R.id.flight_number_text_view)
    TextView flightNumberTextView;
    @BindView(R.id.assign_pilot_button)
    public Button assignPilotButton;
    @BindView(R.id.prepare_time_result_text_view)
    public TextView prepareTimeResultTextView;
    @BindView(R.id.start_time_result_text_view)
    public TextView startTimeResultTextView;
    @BindView(R.id.current_time_text_view)
    public TextView currentTimeTextView;
    @BindView(R.id.live_status_text_view)
    public TextView liveStatusTextView;
    @BindView(R.id.stats_container)
    public LinearLayout statsContainer;

    private int flightNumber;
    private CurrentFlyHandler currentFlyHandler;
    private UsbService usbService;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        flightNumber = args.getInt("flightNumber", 0);

        currentFlyHandler = new CurrentFlyHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_fly_fragment, container, false);
        ButterKnife.bind(this, view);

        flightNumberTextView.setText(getString(R.string.flight_number, flightNumber));



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
}
