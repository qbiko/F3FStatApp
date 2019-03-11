package pl.f3f_klif.f3fstatapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.adapters.RoundListAdapter;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.mapper.RoundMapper;
import pl.f3f_klif.f3fstatapp.utils.F3FRound;

public class EventRoundsActivity extends AppCompatActivity {

    @BindView(R.id.event_name_text_view)
    TextView eventNameTextView;
    @BindView(R.id.event_location_text_view)
    TextView eventLocationTextView;
    @BindView(R.id.event_start_date_text_view)
    TextView eventStartDateTextView;
    @BindView(R.id.event_type_text_view)
    TextView eventTypeTextView;
    @BindView(R.id.rounds_list_view)
    ListView roundsListView;

    private RoundListAdapter roundListAdapter;
    private List<F3FRound> F3FRounds;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_rounds);
        ButterKnife.bind(this);

        event = DatabaseRepository.getEvent();
        eventNameTextView.setText(event.getName());
        eventLocationTextView.setText(event.getLocation());
        eventTypeTextView.setText(event.getType());
        eventStartDateTextView.setText(event.getStartDate().toString());

        F3FRounds = RoundMapper.ToViewModel(DatabaseRepository.getRounds());
        eventNameTextView.setText(f3FEvent.getName());
        eventLocationTextView.setText(f3FEvent.getLocation());
        eventTypeTextView.setText(f3FEvent.getType());
        eventStartDateTextView.setText(f3FEvent.getStartDate().toString());

        F3FRounds = RoundMapper.ToViewModel(DatabaseRepository.GetRounds());
        pilots = new ArrayList<>();
        ListView listViewButton = (ListView)findViewById(R.id.rounds_list_view);

        listViewButton.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventRoundsActivity.this, EventGroupsActivity.class);
                intent.putExtra("round", F3FRounds.get(position));
                EventRoundsActivity.this.startActivity(intent);
            }
        });

        ((AppCompatActivity)this).getSupportActionBar().setTitle("Rundy");

        for(int i = 3; i<lines.length; i++) {
            if(!lines[i].isEmpty()) {
                pilots.add(new Pilot(lines[i]));
            }
        }

        roundListAdapter = new RoundListAdapter(F3FRounds,EventRoundsActivity.this);
        roundsListView.setAdapter(roundListAdapter);
    }



    @OnClick(R.id.add_round_button)
    void onAddRoundButtonClick() {
        long roundIndex = DatabaseRepository.createRound(event.getPilots());
        F3FRounds.add(new F3FRound(roundIndex, event.getPilots(), "nie rozpoczeta"));
        roundListAdapter.notifyDataSetChanged();
    }
}
