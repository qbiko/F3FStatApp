package pl.f3f_klif.f3fstatapp.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.ButterKnife;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.adapters.PilotListAdapter;
import pl.f3f_klif.f3fstatapp.groups.fragments.CurrentFlyFragment;
import pl.f3f_klif.f3fstatapp.groups.fragments.RoundFragment;
import pl.f3f_klif.f3fstatapp.utils.Pilot;
import pl.f3f_klif.f3fstatapp.utils.F3FRound;

public class EventGroupsActivity extends AppCompatActivity {

    private List<Pilot> _pilots;
    private PilotListAdapter pilotListAdapter;
    private long GroupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_groups);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        F3FRound round = intent.getExtras().getParcelable("round");
        GroupId = round.getRoundId();

        if(savedInstanceState == null){
            showFragment(RoundFragment.newInstance(GroupId));
        }
    }

    private void showFragment(Fragment fragment){
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

        menu.findItem(R.id.action_current_fly).setVisible(!isCurrentFlyFragment);
        menu.findItem(R.id.action_event_groups).setVisible(isCurrentFlyFragment);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_current_fly:
                showFragment(CurrentFlyFragment.newInstance());
                return true;
            case R.id.action_event_groups:
                showFragment(RoundFragment.newInstance(GroupId));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
