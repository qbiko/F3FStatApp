package pl.f3f_klif.f3fstatapp.groups.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;

public class RoundFragment extends Fragment {

    private BoardView _boardView;
    private long roundId;
    private List<pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group> _groups;
    private boolean assignMode = false;
    private int flightNumber;
    private float flightTimeResult = 0f;
    public static RoundFragment newInstance(long roundId) {
        return new RoundFragment(roundId);
    }

    public static RoundFragment newInstance(long roundId, int flightNumber, float flightTimeResult) {
        return new RoundFragment(roundId, flightNumber, flightTimeResult);
    }

    @SuppressLint("ValidFragment")
    public RoundFragment(long roundId){
        this.roundId = roundId;
        _groups = DatabaseRepository.getGroups(this.roundId);
        _groups = DatabaseRepository.getGroups(this.roundId);
        Round round = DatabaseRepository.getRound(this.roundId);
        round.state = RoundState.STARTED;
        DatabaseRepository.updateRound(round);
    }

    @SuppressLint("ValidFragment")
    public RoundFragment(long roundId, int flightNumber, float flightTimeResult){
        this.roundId = roundId;
        assignMode = true;
        this.flightNumber = flightNumber;
        this.flightTimeResult = flightTimeResult;
        _groups = DatabaseRepository.getGroups(this.roundId);

        Round round = DatabaseRepository.getRound(this.roundId);
        round.state = RoundState.STARTED;
        DatabaseRepository.updateRound(round);
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
                ? "Runda " + roundId + ": przypisz wynik do pilota"
                : "Runda " + roundId;

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
                .createRoundGroup(
                        getActivity(),
                        groupName,
                        pilots,
                        flightNumber,
                        flightTimeResult,
                        roundId,
                        groupId,
                        assignMode);

        _boardView.addColumn(
                group.itemAdapter,
                group.header,
                group.header,
                group.hasFixedItemSize);
    }

}
