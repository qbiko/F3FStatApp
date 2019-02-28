package pl.f3f_klif.f3fstatapp.groups.callbacks;

import com.woxthebox.draglistview.BoardView;

public class RoundBoardCallback {
    public static BoardView.BoardCallback GetBoardCallback = new BoardView.BoardCallback() {
        @Override
        public boolean canDragItemAtPosition(int column, int dragPosition) {
            // Add logic here to prevent an item to be dragged
            return true;
        }

        @Override
        public boolean canDropItemAtPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
            // Add logic here to prevent an item to be dropped
            return true;
        }
    };
}
