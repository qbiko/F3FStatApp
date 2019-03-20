package pl.f3f_klif.f3fstatapp.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.annimon.stream.*;

import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.fragments.CurrentFlyFragment;
import pl.f3f_klif.f3fstatapp.groups.fragments.RoundFragment;
import pl.f3f_klif.f3fstatapp.groups.fragments.RoundOrderFragment;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.CancelGroupStrategy;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.CancelRoundStrategy;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.SendGroupStrategy;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.SendRoundStrategy;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.StrategyScope;
import pl.f3f_klif.f3fstatapp.handlers.StartListHandler;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;
import pl.f3f_klif.f3fstatapp.utils.UsbService;

public class EventGroupsActivity extends AppCompatActivity {

    private StartListHandler startListHandler;
    private UsbService usbService;

    private final int CANCEL_SUBMENU_ID = 100000;
    private final int SEND_SUBMENU_ID = 100001;
    private final int CANCEL_GROUP_ID = 100000;
    private final int SEND_GROUP_ID = 100001;

    private Event event;
    private Round round;
    private long roundId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_groups);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        roundId = intent.getExtras().getLong("roundId");
        round = DatabaseRepository.getEvent().getRound(roundId);
        event = DatabaseRepository.getEvent();
        if(savedInstanceState == null){
            if(round.state == null)
                showFragment(RoundOrderFragment.newInstance(round));
            else{
                switch (round.state){
                    case NOT_STARTED:
                    case CANCELED:
                        showFragment(RoundOrderFragment.newInstance(round));
                        break;
                    case STARTED:
                    case FINISHED:
                        showFragment(RoundFragment.newInstance(round));
                        break;
                }
            }

        }

        startListHandler = new StartListHandler(this, round);
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

        List<Group> groups = round.getGroups();
        int index = 1;

        SubMenu cancelSubMenu = null;
        SubMenu sendSubMenu = null;

        if(menu.findItem(CANCEL_SUBMENU_ID) == null)
        {
            cancelSubMenu = menu.addSubMenu(CANCEL_GROUP_ID, CANCEL_SUBMENU_ID, 0, R.string.cancel_groups);
            for (Group group: groups) {
                cancelSubMenu.add(CANCEL_GROUP_ID,(int)group.id, 0, getString(R.string.cancel_group,
                        index));
                index++;
            }
        }
        index = 1;
        if(menu.findItem(SEND_SUBMENU_ID) == null)
        {
            sendSubMenu = menu.addSubMenu(SEND_GROUP_ID, SEND_SUBMENU_ID, 0, R.string.send_groups);
            for (Group group: groups) {
                sendSubMenu.add(SEND_GROUP_ID,(int)group.id, 0,
                        getString(R.string.send_group, index));
                index++;
            }
        }

        if(isCurrentFlyFragment){
            menu.findItem(R.id.action_event_groups).setTitle(getString(R.string.back_to_round_view, roundId));
            menu.findItem(R.id.action_current_fly).setVisible(false);
            menu.findItem(R.id.action_event_groups).setVisible(true);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(false);
            menu.findItem(R.id.action_send_results_to_server).setVisible(false);
            menu.findItem(CANCEL_SUBMENU_ID).setVisible(false);
            menu.findItem(SEND_SUBMENU_ID).setVisible(false);
        }

        if(isRoundFragment){
            menu.findItem(R.id.action_current_fly).setVisible(true);
            menu.findItem(R.id.action_event_groups).setVisible(false);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(true);
            menu.findItem(R.id.action_send_results_to_server).setVisible(true);
            menu.findItem(CANCEL_SUBMENU_ID).setVisible(true);
            menu.findItem(SEND_SUBMENU_ID).setVisible(true);
        }

        if(isRoundOrderFragment){
            menu.findItem(R.id.action_event_groups).setTitle("Start rundy " + roundId);
            menu.findItem(R.id.action_current_fly).setVisible(false);
            menu.findItem(R.id.action_event_groups).setVisible(true);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(false);
            menu.findItem(R.id.action_send_results_to_server).setVisible(false);
            menu.findItem(CANCEL_SUBMENU_ID).setVisible(false);
            menu.findItem(SEND_SUBMENU_ID).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(item.getGroupId()){
            case CANCEL_SUBMENU_ID:
                if(round.getGroup(itemId) != null)
                    new CancelGroupStrategy()
                            .doStrategy(new StrategyScope(itemId, roundId, getApplicationContext()));
                return true;
            case SEND_SUBMENU_ID:
                if(round.getGroup(itemId) != null)
                    new SendGroupStrategy()
                            .doStrategy(new StrategyScope(itemId, roundId, getApplicationContext()));
                return true;
            default:
                switch (itemId){
                    case R.id.action_current_fly:
                        showFragment(CurrentFlyFragment.newInstance(round,0));
                        return true;
                    case R.id.action_event_pilots_order:
                        showFragment(RoundOrderFragment.newInstance(round));
                        return true;
                    case R.id.action_event_groups:
                        Optional<Round> roundOptional = Stream.of(event.getRounds())
                                .filter(i -> ( i.state == RoundState.NOT_STARTED
                                        || i.state == RoundState.STARTED ) && i.id < round.id )
                                .findFirst();
                        if(roundOptional.isEmpty()){
                            showFragment(RoundFragment.newInstance(round));
                            return true;
                        }
                        Toast
                                .makeText(
                                        getApplicationContext(),
                                        "Nie można wystartować kolejnej rundy, gdy inna jest nie zakończona",
                                        Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_cancel_round:
                        if(round.getGroup(itemId) != null)
                            new CancelRoundStrategy()
                                    .doStrategy(new StrategyScope(itemId, roundId, getApplicationContext()));
                        return true;
                    case R.id.action_send_results_to_server:
                        new SendRoundStrategy()
                                .doStrategy(new StrategyScope(roundId, getApplicationContext()));
                        return true;
                }
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
