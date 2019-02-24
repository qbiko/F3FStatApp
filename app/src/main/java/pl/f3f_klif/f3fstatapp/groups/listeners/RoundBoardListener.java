package pl.f3f_klif.f3fstatapp.groups.listeners;

import android.widget.TextView;

import com.woxthebox.draglistview.BoardView;

import pl.f3f_klif.f3fstatapp.R;

public class RoundBoardListener {
    public static BoardView.BoardListener GetBoardListener(final BoardView boardView){ return new BoardView.BoardListener() {
        @Override
        public void onItemDragStarted(int column, int row) {
            int a=1;
        }

        @Override
        public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
            if (fromColumn != toColumn || fromRow != toRow) {
                //tutaj bedzie kod do zapisu zmiany pozycji
            }
        }

        @Override
        public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
            int a = 1;
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
