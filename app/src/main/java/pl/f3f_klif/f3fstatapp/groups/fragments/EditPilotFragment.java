package pl.f3f_klif.f3fstatapp.groups.fragments;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.math3.util.Precision;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.SendPilotStrategy;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.StrategyScope;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.utils.UsbServiceBaseFragment;

@SuppressLint("ValidFragment")
public class EditPilotFragment extends Fragment {
    EditText editOrder;
    EditText editPenalty;
    EditText editSeconds;
    Button saveButton;
    Pilot pilot;
    Round round;
    Context context;
    long groupId;
    int order;
    @SuppressLint("ValidFragment")
    public EditPilotFragment(
            Pilot pilot,
            Round round,
            Context context,
            int order,
            long groupId){
        this.pilot = pilot;
        this.round = round;
        this.context = context;
        this.order = order;
        this.groupId = groupId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pilot_edit, container,false);
        editSeconds = (EditText) view.findViewById(R.id.edit_seconds_editText);
        editPenalty = (EditText) view.findViewById(R.id.edit_penalty_editText);
        saveButton = (Button) view.findViewById(R.id.edit_confirm_button);
        Result pilotResult = pilot.getResult(round.getId());

        editSeconds.setText(String.valueOf(pilotResult.getTotalFlightTime()));
        editPenalty.setText(String.valueOf((int)pilotResult.getPenalty()));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String seconds = editSeconds.getText().toString();
                if(seconds != null){

                    pilotResult.setTotalFlightTime(Precision.round(Float.valueOf(seconds),2));
                    pilotResult.setDnf(false);
                    pilotResult.setDNS(false);
                }
                String penalty = editPenalty.getText().toString();
                if(penalty != null){
                    pilotResult.setPenalty(Float.valueOf(penalty));
                }

                pilot.putResult(pilotResult);
                new SendPilotStrategy().doStrategy(pilot, pilotResult, new StrategyScope(groupId, round.id, context, round.index), order);
                showFragment(RoundFragment.newInstance(round));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Edycja pilota");
    }

    public void showFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction
                .replace(R.id.container, fragment, "fragment")
                .commit();
    }
}
