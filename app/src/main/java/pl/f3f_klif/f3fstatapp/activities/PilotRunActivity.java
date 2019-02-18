package pl.f3f_klif.f3fstatapp.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.utils.Pilot;
import pl.f3f_klif.f3fstatapp.utils.UsbService;

import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.COMPLETED_LAST_BASE;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.COMPLETED_NEXT_BASE;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.NEW_FLIGHT;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.PREPARE_TIME;
import static pl.f3f_klif.f3fstatapp.utils.FramesDictionary.START_TIME;

public class PilotRunActivity extends AppCompatActivity {


    @BindView(R.id.pilot_name_text_view)
    TextView pilotNameTextView;
    @BindView(R.id.prepare_time_result_text_view)
    TextView prepareTimeResultTextView;
    @BindView(R.id.start_time_result_text_view)
    TextView startTimeResultTextView;
    @BindView(R.id.information_about_start_text_view)
    TextView informationAboutStartTextView;
    @BindView(R.id.main_container_pilot_run)
    LinearLayout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot_run);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Pilot pilot = intent.getExtras().getParcelable("pilot");

        pilotNameTextView.setText(pilot.getName());

        mHandler = new MyHandler(this);
    }

    public static List<String> receivedFrames = new ArrayList<>();

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
    private UsbService usbService;
    private MyHandler mHandler;

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    private static class MyHandler extends Handler {
        private final WeakReference<PilotRunActivity> mActivity;

        public MyHandler(PilotRunActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    receivedFrames.add(data);
                    readMessage(data);
                    //mActivity.get().terminal.append(data);
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
            }
        }

        private void readMessage(String data) {

            String[] message = data.split(";");
            String typeOfMessage = data.split(";")[0];

            switch(typeOfMessage) {
                case NEW_FLIGHT:
                {

                    break;
                }
                case PREPARE_TIME:
                {
                    mActivity.get().prepareTimeResultTextView.setText(message[1]);
                    break;
                }
                case START_TIME:
                {
                    mActivity.get().startTimeResultTextView.setText(message[1]);
                    break;
                }
                case COMPLETED_NEXT_BASE:
                {
                    if(message[2].equals("0")) {
                        mActivity.get().informationAboutStartTextView.setText("Pilot wystartował!");
                    }
                    else {
                        Float time = Float.parseFloat(message[3])/100;
                        TextView textView = new TextView(mActivity.get());
                        textView.setText("Baza " + message[2]
                                + ": " + time.toString());
                        mActivity.get().mainContainer.addView(textView);
                    }
                    break;
                }
                case COMPLETED_LAST_BASE:
                {
                    Float time = Float.parseFloat(message[3])/100;
                    TextView textView = new TextView(mActivity.get());
                    textView.setText("Koniec biegu, ostateczny czas: " + time.toString());
                    mActivity.get().mainContainer.addView(textView);
                    break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }
}
