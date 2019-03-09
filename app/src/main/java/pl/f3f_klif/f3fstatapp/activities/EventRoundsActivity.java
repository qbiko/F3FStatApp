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
import pl.f3f_klif.f3fstatapp.adapters.RoundListAdapter;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.mapper.RoundMapper;
import pl.f3f_klif.f3fstatapp.utils.F3FEvent;
import pl.f3f_klif.f3fstatapp.utils.F3FRound;
import pl.f3f_klif.f3fstatapp.utils.Pilot;
import pl.f3f_klif.f3fstatapp.utils.ProcessingResponse;

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
    private List<F3FRound> F3FRounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_rounds);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String[] lines = ProcessingResponse.receiveExtraAndDividePerLine(intent);

        F3FEvent f3FEvent = new F3FEvent(lines[1]);

        eventNameTextView.setText(f3FEvent.getName());
        eventLocationTextView.setText(f3FEvent.getLocation());
        eventTypeTextView.setText(f3FEvent.getType());
        eventStartDateTextView.setText(f3FEvent.getStartDate().toString());

        F3FRounds = RoundMapper.ToViewModel(DatabaseRepository.GetRounds());
        pilots = new ArrayList<>();

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
        long roundIndex = DatabaseRepository.CreateRound(pilots);
        F3FRounds.add(new F3FRound(roundIndex, pilots, "nie rozpoczeta"));
        roundListAdapter.notifyDataSetChanged();
    }
}
