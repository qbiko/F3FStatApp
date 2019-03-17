package pl.f3f_klif.f3fstatapp.groups.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woxthebox.draglistview.BoardView;

import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.callbacks.RoundBoardCallback;
import pl.f3f_klif.f3fstatapp.groups.listeners.RoundBoardListener;
import pl.f3f_klif.f3fstatapp.groups.services.GroupCreator;
import pl.f3f_klif.f3fstatapp.groups.services.models.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;

public class RoundFragment extends Fragment {

    private BoardView _boardView;
    private Round round;
    private List<pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group> _groups;
    private boolean assignMode = false;
    private int flightNumber;
    private Result result;
    public static RoundFragment newInstance(Round round) {
        return new RoundFragment(round);
    }

    public static RoundFragment newInstance(Round round, int flightNumber,
                                            Result result) {
        return new RoundFragment(round, flightNumber, result);
    }

    @SuppressLint("ValidFragment")
    public RoundFragment(Round round){
        this.round = round;
        _groups = this.round.getGroups();
        this.round.setState(RoundState.STARTED);
    }

    @SuppressLint("ValidFragment")
    public RoundFragment(Round round, int flightNumber, Result result){
        this.round = round;
        assignMode = true;
        this.flightNumber = flightNumber;
        this.result = result;
        _groups = this.round.getGroups();

        this.round.setState(RoundState.STARTED);
    }

    public RoundFragment(){}

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

        View view = inflater.inflate(R.layout.round_layout, container,false);
        _boardView = view.findViewById(R.id.round_view);
        _boardView.setSnapToColumnsWhenScrolling(true);
        _boardView.setSnapToColumnWhenDragging(true);
        _boardView.setSnapDragItemToTouch(true);
        _boardView.setSnapToColumnInLandscape(false);
        _boardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        _boardView.setBoardListener(RoundBoardListener.GetBoardListener(_boardView, _groups));
        _boardView.setBoardCallback(RoundBoardCallback.GetBoardCallback);
        _boardView.setDragEnabled(false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String roundTitle = assignMode
                ? "Runda " + round.getId() + ": przypisz wynik do pilota"
                : "Runda " + round.getId();

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(roundTitle);

        addGroups();
    }

    private void addGroups(){
        _groups = round.getGroups();
        int groupIndex = 1;
        for (pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group pilotsGroup: _groups) {
             createGroup(String.format("Grupa %s", groupIndex), pilotsGroup.getPilots(),
                     round,
                     pilotsGroup.id);
             groupIndex++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createGroup(String groupName, List<Pilot> pilots, Round round, long groupId){
        Group group = GroupCreator
                .createRoundGroup(
                        getActivity(),
                        groupName,
                        pilots,
                        flightNumber,
                        result,
                        round,
                        groupId,
                        assignMode);

        _boardView.addColumn(
                group.itemAdapter,
                group.header,
                group.header,
                group.hasFixedItemSize);
    }

}
