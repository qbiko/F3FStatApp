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
    Result result;
    Round round;
    Context context;
    @SuppressLint("ValidFragment")
    public EditPilotFragment(
            Pilot pilot,
            Result result,
            Round round,
            Context context){
        this.pilot = pilot;
        this.result = result;
        this.round = round;
        this.context = context;
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
        editPenalty = (EditText) view.findViewById(R.id.edit_penalty_editText);
        editSeconds = (EditText) view.findViewById(R.id.edit_seconds_editText);
        saveButton = (Button) view.findViewById(R.id.edit_confirm_button);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int order = 1;
                if(result == null){
                    result = new Result();
                    result.roundId = round.id;
                    result.dns = false;
                    result.dnf = false;
                    pilot.addResult(result);
                }
                if(editPenalty.getText().toString() != null)
                    result.penalty = Integer.valueOf(editPenalty.getText().toString());
                if(editSeconds.getText().toString() != null)
                    result.penalty = Precision.round(Float.valueOf(editSeconds.getText().toString()),2);

                pilot.putResult(result);
                new SendPilotStrategy().doStrategy(pilot, result, new StrategyScope(round.id, context), order);
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
