package pl.f3f_klif.f3fstatapp.groups.infrastructure;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.fragments.RoundFragment;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;

public class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private int flightNumber;
    private Result result;
    private Round round;
    private long groupId;
    private boolean assignMode;

    public ItemAdapter(
            ArrayList<Pair<Long, String>> list,
            int layoutId,
            int grabHandleId,
            boolean dragOnLongPress,
            int flightNumber,
            Result result,
            Round round,
            long groupId,
            boolean assignMode) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        this.flightNumber = flightNumber;
        this.result = result;
        this.round = round;
        this.groupId = groupId;
        this.assignMode = assignMode;
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
            if(assignMode) {
                Toast.makeText(view.getContext(), "Item clicked" + this.mText + this.mItemId, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean onItemLongClicked(View view) {
            if(assignMode) {
                Pilot pilot = round.getGroup(groupId).getPilots().get((int)this.mItemId);
                Toast.makeText(view.getContext(),
                        "Wynik został zapisany pilotowi: " + pilot.getFirstName() + " " + pilot.getLastName(),
                        Toast.LENGTH_SHORT).show();
                pilot.addResult(result);
                //wyciagnij grupe dla danej rundy
                //mItemId to id pilota
                //przypisz wynik pilotowi
                //Toast.makeText(view.getContext(), "Item long clicked"+this.mText+this.mItemId+mLayoutId, Toast.LENGTH_SHORT).show();
                showFragment(RoundFragment.newInstance(round), view);
            }

            return true;
        }
    }

    public void showFragment(Fragment fragment, View view){
        FragmentManager manager = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .replace(R.id.container, fragment, "fragment")
                .commit();
    }
}
