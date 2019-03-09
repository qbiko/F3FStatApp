package pl.f3f_klif.f3fstatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.activities.EventGroupsActivity;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.utils.F3FRound;

public class RoundListAdapter extends BaseAdapter {

    private List<F3FRound> F3FRounds;
    private Context context;

    public RoundListAdapter(List<F3FRound> F3FRounds, Context context) {
        this.F3FRounds = F3FRounds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return F3FRounds.size();
    }

    @Override
    public Object getItem(int i) {
        return F3FRounds.get(i);
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
        nameTextView.setText("Runda " + String.valueOf(F3FRounds.get(i).getRoundId()));
        Round round = DatabaseRepository.GetRound(F3FRounds.get(i).getRoundId());

        statusTextView.setText(GetStatus(round));
        return view;
    }

    public String GetStatus(Round round){
        if(round.State == null)
            return "Nie rozpoczęta";

        switch(round.State){
            case Finished:
                return "Zakończona";
            case Started:8
                return "Trwa";
            case Canceled:
                return "Anulowana";
            case NotStarted:
                return "Nie rozpoczęta";
                default:
                    return "";
        }
    }
}

