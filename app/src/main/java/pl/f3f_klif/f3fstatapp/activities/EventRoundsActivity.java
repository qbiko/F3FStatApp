package pl.f3f_klif.f3fstatapp.activities;

import android.content.Intent;
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
import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.adapters.RoundListAdapter;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;

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
    private List<Round> rounds;
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

        rounds = event.getRounds();

        ListView listViewButton = findViewById(R.id.rounds_list_view);

        listViewButton.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventRoundsActivity.this, EventGroupsActivity.class);
                intent.putExtra("roundId", rounds.get(position).id);
                EventRoundsActivity.this.startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("Rundy");

        roundListAdapter = new RoundListAdapter(rounds,EventRoundsActivity.this);
        roundsListView.setAdapter(roundListAdapter);
    }



    @OnClick(R.id.add_round_button)
    void onAddRoundButtonClick() {
        Box<Round> roundBox = ObjectBox.get().boxFor(Round.class);
        roundBox.put(event.createRound());
        roundListAdapter.notifyDataSetChanged();
    }
}
