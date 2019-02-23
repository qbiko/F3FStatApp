package pl.f3f_klif.f3fstatapp.groups.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woxthebox.draglistview.BoardView;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.callbacks.RoundBoardCallback;
import pl.f3f_klif.f3fstatapp.groups.listeners.RoundBoardListener;
import pl.f3f_klif.f3fstatapp.groups.services.GroupCreator;
import pl.f3f_klif.f3fstatapp.groups.services.models.Group;

public class RoundFragment extends Fragment {

    private BoardView _boardView;
    private int RoundNumber;
    private int GroupsCount = 2;

    public static RoundFragment newInstance() {
        return new RoundFragment();
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


        Group group = GroupCreator
                .Create(getActivity(), "Grupa 1");

        _boardView.addColumn(
                        group.ItemAdapter,
                        group.Header,
                        group.Header,
                        group.HasFixedItemSize);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_round, menu);
    }

}
