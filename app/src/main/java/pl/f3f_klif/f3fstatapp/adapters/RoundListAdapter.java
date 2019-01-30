package pl.f3f_klif.f3fstatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.activities.EventGroupsActivity;
import pl.f3f_klif.f3fstatapp.activities.EventRoundsActivity;
import pl.f3f_klif.f3fstatapp.utils.Round;

public class RoundListAdapter extends BaseAdapter {

    private List<Round> rounds;
    private Context context;

    public RoundListAdapter(List<Round> rounds, Context context) {
        this.rounds = rounds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return rounds.size();
    }

    @Override
    public Object getItem(int i) {
        return rounds.get(i);
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
        Button roundButton = view.findViewById(R.id.round_button);

        roundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventGroupsActivity.class);
                intent.putExtra("round", rounds.get(i));

                context.startActivity(intent);
            }
        });

        nameTextView.setText("Runda " + String.valueOf(rounds.get(i).getRoundId()+1));
        statusTextView.setText(rounds.get(i).getStatus());

        return view;
    }
}
