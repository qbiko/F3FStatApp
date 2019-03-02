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
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.adapters.PilotListAdapter;
import pl.f3f_klif.f3fstatapp.utils.F3FEvent;
import pl.f3f_klif.f3fstatapp.utils.Pilot;

public class EventActivity extends AppCompatActivity {

    private List<Pilot> pilots;
    private PilotListAdapter pilotListAdapter;
    private F3FEvent f3FEvent;

    @BindView(R.id.pilots_list_view)
    ListView pilotsListView;
    @BindView(R.id.event_title_text_view)
    TextView eventTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        pilots = new ArrayList<>();

        Intent intent = getIntent();
        String responseText = intent.getStringExtra("responseText");

        String[] lines = responseText.split(System.getProperty("line.separator"));

        f3FEvent = new F3FEvent(lines[1]);
        eventTitleTextView.setText(f3FEvent.getName());

        for(int i = 3; i<lines.length; i++) {
            if(!lines[i].isEmpty()) {
                pilots.add(new Pilot(lines[i]));
            }
        }
        pilotListAdapter = new PilotListAdapter(pilots, getApplicationContext());
        pilotsListView.setAdapter(pilotListAdapter);
        pilotListAdapter.notifyDataSetChanged();
    }
}
