package pl.f3f_klif.f3fstatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;

public class PilotListAdapter extends BaseAdapter {

    private List<Pilot> f3FPilots;
    private Context context;

    public PilotListAdapter(List<Pilot> f3FPilots, Context context) {
        this.f3FPilots = f3FPilots;
        this.context = context;
    }

    @Override
    public int getCount() {
        return f3FPilots.size();
    }

    @Override
    public Object getItem(int i) {
        return f3FPilots.get(i);
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

        orderNumberTextView.setText(String.valueOf(f3FPilots.get(i).getStartNumber()));
        firstNameTextView.setText(f3FPilots.get(i).getFirstName());
        lastNameTextView.setText(f3FPilots.get(i).getLastName());
        pilotClassTextView.setText(f3FPilots.get(i).getPilotClass());
        pilotAmaTextView.setText(f3FPilots.get(i).getAma());
        pilotFaiTextView.setText(f3FPilots.get(i).getFai());
        pilotFaiLicenseTextView.setText(f3FPilots.get(i).getFaiLicense());
        pilotTeamNameTextView.setText(f3FPilots.get(i).getTeamName());

        return view;
    }

    public Pilot getPilot(int position) {
        return (Pilot) getItem(position);
    }
}

