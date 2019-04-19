package pl.f3f_klif.f3fstatapp.groups.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.woxthebox.draglistview.BoardView;

import java.util.List;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.callbacks.RoundBoardCallback;
import pl.f3f_klif.f3fstatapp.groups.listeners.RoundBoardListener;
import pl.f3f_klif.f3fstatapp.groups.services.GroupCreator;
import pl.f3f_klif.f3fstatapp.groups.services.models.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;

public class RoundOrderFragment extends Fragment {

    private BoardView _boardView;
    private Round round;
    private List<pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group> _groups;
    private boolean dragEnabled = true;
    private int flightNumber;
    private Result result;
    public static RoundOrderFragment newInstance(Round round) {
        return new RoundOrderFragment(round);
    }

    public static RoundOrderFragment newInstance(Round round, int flightNumber,
                                                 Result result) {
        return new RoundOrderFragment(round, flightNumber, result);
    }

    @SuppressLint("ValidFragment")
    public RoundOrderFragment(Round round){
        this.round = round;
        _groups = round.getGroups();
    }

    @SuppressLint("ValidFragment")
    public RoundOrderFragment(Round round, int flightNumber, Result result){
        this.round = round;
        dragEnabled = false;
        this.flightNumber = flightNumber;
        this.result = result;
        _groups = round.getGroups();
    }

    public RoundOrderFragment(){}

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

        View view = inflater.inflate(R.layout.round_order_layout, container,false);
        _boardView = view.findViewById(R.id.round_order_view);
        _boardView.setSnapToColumnsWhenScrolling(true);
        _boardView.setSnapToColumnWhenDragging(true);
        _boardView.setSnapDragItemToTouch(true);
        _boardView.setSnapToColumnInLandscape(false);
        _boardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        _boardView.setBoardListener(RoundBoardListener.GetBoardListener(_boardView, _groups));
        _boardView.setBoardCallback(RoundBoardCallback.GetBoardCallback);
        _boardView.setDragEnabled(dragEnabled);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String roundTitle = dragEnabled
                ? "Runda " + round.getId() + ": ustaw kolejność"
                : "Runda " + round.getId() +": przypisz wynik do pilota";

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
                     round, pilotsGroup.id);
             groupIndex++;
        }
    }

    private void createGroup(String groupName, List<Pilot> pilots, Round round, long groupId){
        Group group = GroupCreator
                .create(
                        getActivity(),
                        groupName,
                        pilots,
                        flightNumber,
                        result,
                        round,
                        groupId,
                        false,
                        getFragmentManager()
                                .beginTransaction());

        _boardView.addColumn(
                group.itemAdapter,
                group.header,
                group.header,
                group.hasFixedItemSize);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(dragEnabled)
            menu.findItem(R.id.action_event_groups).setTitle("Start rundy " + round.getId());
        else
            menu.findItem(R.id.action_event_groups).setTitle("Wróć do widoku rundy " + round.getId());
    }

}
