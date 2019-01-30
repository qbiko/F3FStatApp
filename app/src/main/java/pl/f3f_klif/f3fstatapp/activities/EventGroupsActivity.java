package pl.f3f_klif.f3fstatapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.adapters.PilotListAdapter;
import pl.f3f_klif.f3fstatapp.utils.Pilot;
import pl.f3f_klif.f3fstatapp.utils.Round;

public class EventGroupsActivity extends AppCompatActivity {

    private List<Pilot> pilots;
    private PilotListAdapter pilotListAdapter;

    @BindView(R.id.pilots_groups_list_view)
    ListView pilotsGroupsListView;
    @BindView(R.id.round_name_text_view)
    TextView roundNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_groups);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        Round round = intent.getExtras().getParcelable("round");

        roundNameTextView.setText("Runda " + String.valueOf(round.getRoundId()+1));

        pilots = round.getStartingList();
        pilotListAdapter = new PilotListAdapter(pilots, getApplicationContext());
        pilotsGroupsListView.setAdapter(pilotListAdapter);
        pilotListAdapter.notifyDataSetChanged();
    }
}
