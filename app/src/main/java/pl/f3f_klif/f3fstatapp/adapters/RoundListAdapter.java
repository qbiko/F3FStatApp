package pl.f3f_klif.f3fstatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;

public class RoundListAdapter extends BaseAdapter {

    private Event event;
    private Context context;

    public RoundListAdapter(Event event, Context context) {
        this.event = event;
        this.context = context;
    }

    @Override
    public int getCount() {
        return event.getRounds().size();
    }

    @Override
    public Object getItem(int i) {
        return event.getRounds().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.round_list_layout, null);

        TextView nameTextView = view.findViewById(R.id.round_name_text_view);
        TextView statusTextView = view.findViewById(R.id.round_status_text_view);
        nameTextView.setText("Runda " + String.valueOf(event.getRounds().get(i).getId()));

        statusTextView.setText(getStatus(event.getRounds().get(i).state));
        return view;
    }

    public String getStatus(RoundState state){
        if(state != null) {
            return context.getString(state.getStateKey());
        }
        return context.getString(RoundState.NOT_STARTED.getStateKey());
    }
}

