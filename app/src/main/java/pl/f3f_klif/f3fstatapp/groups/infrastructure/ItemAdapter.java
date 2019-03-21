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

import com.annimon.stream.Optional;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.fragments.RoundFragment;
import pl.f3f_klif.f3fstatapp.groups.infrastructure.models.PilotWithOrder;
import pl.f3f_klif.f3fstatapp.groups.services.models.PilotWithResult;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.sqlite.WindMeasure;
import pl.f3f_klif.f3fstatapp.sqlite.WindSQLiteDbHandler;

public class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private int flightNumber;
    private Result result;
    private Round round;
    private long groupId;
    private boolean assignMode;
    private List<WindMeasure> windMeasures;

    private WindSQLiteDbHandler db;

    public ItemAdapter(
            ArrayList<Pair<Long, String>> list,
            int layoutId,
            int grabHandleId,
            boolean dragOnLongPress,
            int flightNumber,
            Result result,
            Round round,
            long groupId,
            boolean assignMode,
            List<WindMeasure> windMeasures) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        this.flightNumber = flightNumber;
        this.result = result;
        this.round = round;
        this.groupId = groupId;
        this.assignMode = assignMode;
        this.windMeasures = windMeasures;
        setItemList(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);

        db = new WindSQLiteDbHandler(view.getContext());

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

                Group targetGroup = round.getGroup(groupId);
                List<PilotWithOrder> pilotsWitOrder = GetPilots(targetGroup);
                Optional<PilotWithOrder> firstPilotWithoutResult =
                        com.annimon.stream.Stream.of(pilotsWitOrder)
                        .filter(i -> i.time.isEmpty())
                        .findFirst();

                Pilot pilot = round.getGroup(groupId).getPilots().get((int)this.mItemId);

                if(firstPilotWithoutResult.isPresent() && firstPilotWithoutResult.get().id != pilot.id){
                    Optional<PilotWithOrder> currentPilot =
                            com.annimon.stream.Stream.of(pilotsWitOrder)
                                    .filter(i -> i.time.isEmpty() && i.id == pilot.id)
                                    .findFirst();
                    if(currentPilot.get().order > firstPilotWithoutResult.get().order){
                        targetGroup.reorderPilots(currentPilot.get().order, firstPilotWithoutResult.get().order);
                    }
                }
                Toast.makeText(view.getContext(),
                        "Wynik został zapisany pilotowi: " + pilot.getFirstName() + " " + pilot.getLastName(),
                        Toast.LENGTH_SHORT).show();
                pilot.addResult(result);
                db.addWindMeasures(windMeasures, (int)pilot.getF3fId());
                showFragment(RoundFragment.newInstance(round), view);
            }

            return true;
        }

        private List<PilotWithOrder> GetPilots(Group group){
            List<PilotWithOrder> pilots = new ArrayList<>();
            int order = 0;
            for (Pilot pilot: group.getPilots()) {
                Result result = pilot.getResult(round.id);
                Optional<Float> time = Optional.empty();
                if(result != null)
                    time = result.getTotalFlightTime() <= 0F
                            ? time
                            : Optional.of(result.getTotalFlightTime());

                pilots.add(new PilotWithOrder(pilot.id, time, order));
                order++;
            }
            return pilots;
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
