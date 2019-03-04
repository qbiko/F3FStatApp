package pl.f3f_klif.f3fstatapp.groups.listeners;

import android.widget.TextView;

import com.woxthebox.draglistview.BoardView;

import java.util.ArrayList;
import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;

public class RoundBoardListener {
    public static BoardView.BoardListener GetBoardListener(final BoardView boardView, final List<Group> groups){
        return new BoardView.BoardListener() {
        @Override
        public void onItemDragStarted(int column, int row) {
        }

        @Override
        public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
            if (fromColumn != toColumn) {
                Group oldGroup = groups.get(fromColumn);
                Pilot removedPilot = oldGroup.removePilot(fromRow);
                DatabaseRepository.UpdateGroup(oldGroup);

                Group newGroup = groups.get(toColumn);
                newGroup.addPilot(toRow, removedPilot);
                DatabaseRepository.UpdateGroup(newGroup);
                return;
            }

            if (fromRow != toRow) {
                Group targetGroup = groups.get(fromColumn);
                targetGroup.reorderPilots(fromRow, toRow);
                DatabaseRepository.UpdateGroup(targetGroup);
            }
        }

        @Override
        public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
        }

        @Override
        public void onItemChangedColumn(int oldColumn, int newColumn) {
            TextView itemCount1 = boardView.getHeaderView(oldColumn).findViewById(R.id.item_count);
            itemCount1.setText(String.valueOf(boardView.getAdapter(oldColumn).getItemCount()));
            TextView itemCount2 = boardView.getHeaderView(newColumn).findViewById(R.id.item_count);
            itemCount2.setText(String.valueOf(boardView.getAdapter(newColumn).getItemCount()));
        }

        @Override
        public void onFocusedColumnChanged(int oldColumn, int newColumn) {
        }

        @Override
        public void onColumnDragStarted(int position) {
        }

        @Override
        public void onColumnDragChangedPosition(int oldPosition, int newPosition) {
        }

        @Override
        public void onColumnDragEnded(int position) {
        }};

    }
}
