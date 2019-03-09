package pl.f3f_klif.f3fstatapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
