package pl.f3f_klif.f3fstatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.activities.EventActivity;
import pl.f3f_klif.f3fstatapp.activities.PilotRunActivity;
import pl.f3f_klif.f3fstatapp.utils.Pilot;

public class PilotListAdapter extends BaseAdapter {

    private List<Pilot> pilots;
    private Context context;

    public PilotListAdapter(List<Pilot> pilots, Context context) {
        this.pilots = pilots;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pilots.size();
    }

    @Override
    public Object getItem(int i) {
        return pilots.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.pilot_list_layout, null);

        TextView orderNumberTextView = view.findViewById(R.id.pilot_order_number);
        TextView firstNameTextView = view.findViewById(R.id.pilot_firstname);
        TextView lastNameTextView = view.findViewById(R.id
                .pilot_lastname);
        TextView pilotClassTextView = view.findViewById(R.id.pilot_class);
        TextView pilotAmaTextView = view.findViewById(R.id.pilot_ama);
        TextView pilotFaiTextView = view.findViewById(R.id.pilot_fai);
        TextView pilotFaiLicenseTextView = view.findViewById(R.id.pilot_fai_license);
        TextView pilotTeamNameTextView = view.findViewById(R.id.pilot_team_name);
        Button startRunPilotButton = view.findViewById(R.id.start_run_pilot);

        orderNumberTextView.setText(String.valueOf(pilots.get(i).getStartNumber()));
        firstNameTextView.setText(pilots.get(i).getFirstName());
        lastNameTextView.setText(pilots.get(i).getLastName());
        pilotClassTextView.setText(pilots.get(i).getPilotClass());
        pilotAmaTextView.setText(pilots.get(i).getAma());
        pilotFaiTextView.setText(pilots.get(i).getFai());
        pilotFaiLicenseTextView.setText(pilots.get(i).getFaiLicense());
        pilotTeamNameTextView.setText(pilots.get(i).getTeamName());

        startRunPilotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PilotRunActivity.class);
                intent.putExtra("pilot", pilots.get(i));
                context.startActivity(intent);
            }
        });

        return view;
    }

    public Pilot getPilot(int position) {
        return (Pilot)getItem(position);
    }
}

