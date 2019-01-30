package pl.f3f_klif.f3fstatapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.adapters.PilotListAdapter;
import pl.f3f_klif.f3fstatapp.adapters.RoundListAdapter;
import pl.f3f_klif.f3fstatapp.utils.Event;
import pl.f3f_klif.f3fstatapp.utils.Pilot;
import pl.f3f_klif.f3fstatapp.utils.ProcessingResponse;
import pl.f3f_klif.f3fstatapp.utils.Round;

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

    private List<Pilot> pilots;
    private RoundListAdapter roundListAdapter;
    private List<Round> rounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_rounds);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String[] lines = ProcessingResponse.receiveExtraAndDividePerLine(intent);

        Event event = new Event(lines[1]);

        eventNameTextView.setText(event.getName());
        eventLocationTextView.setText(event.getLocation());
        eventTypeTextView.setText(event.getType());
        eventStartDateTextView.setText(event.getStartDate().toString());

        rounds = new ArrayList<>();
        pilots = new ArrayList<>();

        for(int i = 3; i<lines.length; i++) {
            if(!lines[i].isEmpty()) {
                pilots.add(new Pilot(lines[i]));
            }
        }

        roundListAdapter = new RoundListAdapter(rounds,EventRoundsActivity.this);
        roundsListView.setAdapter(roundListAdapter);
    }

    @OnClick(R.id.add_round_button)
    void onAddRoundButtonClick() {
        int roundIndex = rounds.size();
        rounds.add(new Round(roundIndex, pilots, "nie rozpoczeta"));
        roundListAdapter.notifyDataSetChanged();
    }
}
