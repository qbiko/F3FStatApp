package pl.f3f_klif.f3fstatapp.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.adapters.PilotListAdapter;
import pl.f3f_klif.f3fstatapp.groups.fragments.CurrentFlyFragment;
import pl.f3f_klif.f3fstatapp.groups.fragments.RoundFragment;
import pl.f3f_klif.f3fstatapp.groups.fragments.RoundOrderFragment;
import pl.f3f_klif.f3fstatapp.handlers.StartListHandler;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;
import pl.f3f_klif.f3fstatapp.utils.Pilot;
import pl.f3f_klif.f3fstatapp.utils.F3FRound;
import pl.f3f_klif.f3fstatapp.utils.UsbService;

import static pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState.NotStarted;

public class EventGroupsActivity extends AppCompatActivity {

    private List<Pilot> _pilots;
    private PilotListAdapter pilotListAdapter;
    private StartListHandler startListHandler;
    private UsbService usbService;

    private long RoundId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_groups);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        F3FRound round = intent.getExtras().getParcelable("round");
        RoundId = round.getRoundId();

        if(savedInstanceState == null){
            Round roundDb = DatabaseRepository.GetRound(RoundId);
            if(roundDb.State == null)
                showFragment(RoundOrderFragment.newInstance(RoundId));
            else{
                switch (roundDb.State){
                    case NotStarted:
                    case Canceled:
                        showFragment(RoundOrderFragment.newInstance(RoundId));
                        break;
                    case Started:
                    case Finished:
                        showFragment(RoundFragment.newInstance(RoundId));
                        break;
                }
            }

        }

        startListHandler = new StartListHandler(this);
    }

    public void showFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction
                .replace(R.id.container, fragment, "fragment")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.menu_round, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isCurrentFlyFragment = getSupportFragmentManager()
                .findFragmentByTag("fragment") instanceof CurrentFlyFragment;
        boolean isRoundFragment = getSupportFragmentManager()
                .findFragmentByTag("fragment") instanceof RoundFragment;
        boolean isRoundOrderFragment = getSupportFragmentManager()
                .findFragmentByTag("fragment") instanceof RoundOrderFragment;

        if(isCurrentFlyFragment){
            menu.findItem(R.id.action_event_groups).setTitle("Wróć do widoku rundy " + RoundId);
            menu.findItem(R.id.action_current_fly).setVisible(false);
            menu.findItem(R.id.action_event_groups).setVisible(true);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(false);
            menu.findItem(R.id.action_send_results_to_server).setVisible(false);
        }

        if(isRoundFragment){
            menu.findItem(R.id.action_current_fly).setVisible(true);
            menu.findItem(R.id.action_event_groups).setVisible(false);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(true);
            menu.findItem(R.id.action_send_results_to_server).setVisible(true);
        }

        if(isRoundOrderFragment){
            menu.findItem(R.id.action_event_groups).setTitle("Start rundy " + RoundId);
            menu.findItem(R.id.action_current_fly).setVisible(false);
            menu.findItem(R.id.action_event_groups).setVisible(true);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(false);
            menu.findItem(R.id.action_send_results_to_server).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Round round = DatabaseRepository.GetRound(RoundId);
        switch (item.getItemId()){
            case R.id.action_current_fly:
                showFragment(CurrentFlyFragment.newInstance(RoundId,0));
                return true;
            case R.id.action_event_pilots_order:
                showFragment(RoundOrderFragment.newInstance(RoundId));
                return true;
            case R.id.action_event_groups:
                showFragment(RoundFragment.newInstance(RoundId));
                return true;
            case R.id.action_cancel_round:
                round.State = RoundState.Canceled;
                DatabaseRepository.UpdateRound(round);
                return true;
            case R.id.action_send_results_to_server:
                round.State = RoundState.Finished;
                DatabaseRepository.UpdateRound(round);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            usbService.setHandler(startListHandler);
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
