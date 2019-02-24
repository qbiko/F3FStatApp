package pl.f3f_klif.f3fstatapp.groups.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;
import com.woxthebox.draglistview.BoardView;

import java.util.ArrayList;
import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.callbacks.RoundBoardCallback;
import pl.f3f_klif.f3fstatapp.groups.listeners.RoundBoardListener;
import pl.f3f_klif.f3fstatapp.groups.services.GroupCreator;
import pl.f3f_klif.f3fstatapp.groups.services.models.Group;
import pl.f3f_klif.f3fstatapp.models.EventOrder;
import pl.f3f_klif.f3fstatapp.models.Round;
import pl.f3f_klif.f3fstatapp.utils.Pilot;

public class RoundFragment extends Fragment {

    private BoardView _boardView;
    private int RoundNumber;
    private int GroupsCount;
    private final int MaxPilotsNumberInGroup = 12;
    private List<Pilot> _pilots;
    public static RoundFragment newInstance(List<Pilot> pilots, int groupsNumber) {
        return new RoundFragment(pilots, groupsNumber);
    }

    public RoundFragment(){

    }

    @SuppressLint("ValidFragment")
    public RoundFragment(List<Pilot> pilots, int groupsNumber){
        _pilots = pilots;
        GroupsCount = groupsNumber;
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

        View view = inflater.inflate(R.layout.round_layout, container,false);
        _boardView = view.findViewById(R.id.round_view);
        _boardView.setSnapToColumnsWhenScrolling(true);
        _boardView.setSnapToColumnWhenDragging(true);
        _boardView.setSnapDragItemToTouch(true);
       // _boardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.column_item));
        //_boardView.setCustomColumnDragItem(new MyColumnDragItem(getActivity(), R.layout.column_drag_layout));
        _boardView.setSnapToColumnInLandscape(false);
        _boardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        _boardView.setBoardListener(RoundBoardListener.GetBoardListener(_boardView));
        _boardView.setBoardCallback(RoundBoardCallback.GetBoardCallback);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Runda"); // <-tutaj jakoś przekazać numerek rundy

        AddGroups();
    }

    private void AddGroups(){
        if(_pilots.size() < MaxPilotsNumberInGroup){
            CreateGroup("Grupa 1", _pilots);
            return;
        }

        List<List<Pilot>> pilotsGroups = Lists.partition(_pilots, MaxPilotsNumberInGroup);
        int groupIndex = 1;
        for (List<Pilot> pilotsGroup: pilotsGroups) {
             CreateGroup(String.format("Grupa %s", groupIndex), pilotsGroup);
             groupIndex++;
        }
    }

    private void CreateGroup(String groupName, List<Pilot> pilots){
        Group group = GroupCreator
                .Create(getActivity(), groupName, pilots);

        _boardView.addColumn(
                group.ItemAdapter,
                group.Header,
                group.Header,
                group.HasFixedItemSize);
    }

}
