package pl.f3f_klif.f3fstatapp.groups.infrastructure;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;

public class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private int FlightNumber;
    private float FlightTimeResult = 0f;
    private long RoundId;
    private long GroupId;
    public ItemAdapter(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        setItemList(list);
    }

    public ItemAdapter(
            ArrayList<Pair<Long, String>> list,
            int layoutId,
            int grabHandleId,
            boolean dragOnLongPress,
            int flightNumber,
            float flightTimeResult,
            long roundId,
            long groupId) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        FlightNumber = flightNumber;
        FlightTimeResult = flightTimeResult;
        RoundId = roundId;
        GroupId = groupId;
        setItemList(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;
        holder.mText.setText(text);
        holder.itemView.setTag(mItemList.get(position));
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            mText = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onItemClicked(View view) {
            Toast.makeText(view.getContext(), "Item clicked"+this.mText+this.mItemId, Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "Wynik został zapisany pilotowi: ", Toast.LENGTH_SHORT).show();
            //wyciagnij grupe dla danej rundy
            //mItemId to id pilota
            //przypisz wynik pilotowi
            //Toast.makeText(view.getContext(), "Item long clicked"+this.mText+this.mItemId+mLayoutId, Toast.LENGTH_SHORT).show();

            return true;
        }
    }
}
