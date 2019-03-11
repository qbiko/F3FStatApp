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
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;

public class RoundOrderFragment extends Fragment {

    private BoardView _boardView;
    private long roundId;
    private List<pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group> _groups;
    private boolean dragEnabled = true;
    private int flightNumber;
    private float flightTimeResult = 0f;
    public static RoundOrderFragment newInstance(long roundId) {
        return new RoundOrderFragment(roundId);
    }

    public static RoundOrderFragment newInstance(long roundId, int flightNumber, float flightTimeResult) {
        return new RoundOrderFragment(roundId, flightNumber, flightTimeResult);
    }

    @SuppressLint("ValidFragment")
    public RoundOrderFragment(long roundId){
        this.roundId = roundId;
        _groups = DatabaseRepository.getGroups(this.roundId);
    }

    @SuppressLint("ValidFragment")
    public RoundOrderFragment(long roundId, int flightNumber, float flightTimeResult){
        this.roundId = roundId;
        dragEnabled = false;
        this.flightNumber = flightNumber;
        this.flightTimeResult = flightTimeResult;
        _groups = DatabaseRepository.getGroups(this.roundId);
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
                ? "Runda " + roundId + ": ustaw kolejność"
                : "Runda " + roundId +": przypisz wynik do pilota";

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(roundTitle);

        AddGroups();
    }

    private void AddGroups(){
        _groups = DatabaseRepository.getGroups(roundId);
        int groupIndex = 1;
        for (pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group pilotsGroup: _groups) {
             CreateGroup(String.format("Grupa %s", groupIndex), pilotsGroup.getPilots(), roundId, groupIndex);
             groupIndex++;
        }
    }

    private void CreateGroup(String groupName, List<Pilot> pilots, long roundId, long groupId){
        Group group = GroupCreator
                .create(
                        getActivity(),
                        groupName,
                        pilots,
                        flightNumber,
                        flightTimeResult,
                        roundId,
                        groupId,
                        false);

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
            menu.findItem(R.id.action_event_groups).setTitle("Start rundy " + roundId);
        else
            menu.findItem(R.id.action_event_groups).setTitle("Wróć do widoku rundy " + roundId);
    }

}
