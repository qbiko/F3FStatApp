package pl.f3f_klif.f3fstatapp.activities;

import android.content.Intent;
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
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.UpdateEventRoundStrategy;
import pl.f3f_klif.f3fstatapp.handlers.StartListHandler;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;

public class EventGroupsActivity extends AppCompatActivity {

    private final int CANCEL_SUBMENU_ID = 100000;
    private final int SEND_SUBMENU_ID = 100001;
    private final int CANCEL_GROUP_ID = 100000;
    private final int SEND_GROUP_ID = 100001;

    private Event event;
    private Round round;
    private long roundId;

    public static boolean active = false;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
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

        if(isCurrentFlyFragment){
            menu.findItem(R.id.action_event_groups).setTitle(getString(R.string.back_to_round_view, roundId));
            menu.findItem(R.id.action_current_fly).setVisible(false);
            menu.findItem(R.id.action_event_groups).setVisible(true);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(false);
            menu.findItem(R.id.end_round).setVisible(false);
            menu.findItem(CANCEL_SUBMENU_ID).setVisible(false);
        }

        if(isRoundFragment){
            menu.findItem(R.id.action_current_fly).setVisible(true);
            menu.findItem(R.id.action_event_groups).setVisible(false);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(true);
            menu.findItem(R.id.end_round).setVisible(true);
            menu.findItem(CANCEL_SUBMENU_ID).setVisible(true);
        }

        if(isRoundOrderFragment){
            menu.findItem(R.id.action_event_groups).setTitle("Start rundy " + roundId);
            menu.findItem(R.id.action_current_fly).setVisible(false);
            menu.findItem(R.id.action_event_groups).setVisible(true);
            menu.findItem(R.id.action_event_pilots_order).setVisible(false);
            menu.findItem(R.id.action_cancel_round).setVisible(false);
            menu.findItem(R.id.end_round).setVisible(false);
            menu.findItem(CANCEL_SUBMENU_ID).setVisible(false);
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
                            .doStrategy(new StrategyScope(itemId, roundId, getApplicationContext(), round.index));
                return true;
            case SEND_SUBMENU_ID:
                if(round.getGroup(itemId) != null)
                    new SendGroupStrategy()
                            .doStrategy(new StrategyScope(itemId, roundId, getApplicationContext(), round.index));
                Toast
                        .makeText(
                                getApplicationContext(),
                                "Grupa wysłana na serwer",
                                Toast.LENGTH_SHORT).show();
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
                                    .doStrategy(new StrategyScope(itemId, roundId, getApplicationContext(), round.index));
                        return true;
                    case R.id.end_round:
                        new UpdateEventRoundStrategy().doStrategy(new StrategyScope(roundId,
                                getApplicationContext(), round.index));
                        round.setState(RoundState.FINISHED);
                        return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
