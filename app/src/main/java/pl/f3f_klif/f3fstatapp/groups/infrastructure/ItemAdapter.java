package pl.f3f_klif.f3fstatapp.groups.infrastructure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Optional;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.fragments.EditPilotFragment;
import pl.f3f_klif.f3fstatapp.groups.fragments.RoundFragment;
import pl.f3f_klif.f3fstatapp.groups.infrastructure.models.PilotWithOrder;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.SendPilotStrategy;
import pl.f3f_klif.f3fstatapp.groups.strategy.menu.StrategyScope;
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
    private Context context;
    private WindSQLiteDbHandler db;
    private int editButtonsVisibility;
    private int assignAndSendButtonVisibility;
    private FragmentTransaction transaction;
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
            List<WindMeasure> windMeasures,
            Context context,
            int buttonsVisible,
            int assignAndSendButton,
            FragmentTransaction transaction) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        this.flightNumber = flightNumber;
        this.result = result;
        this.round = round;
        this.groupId = groupId;
        this.assignMode = assignMode;
        this.windMeasures = windMeasures;
        this.context = context;
        this.editButtonsVisibility = buttonsVisible;
        this.assignAndSendButtonVisibility = assignAndSendButton;
        this.transaction = transaction;
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
        holder.mText.setTextSize(20);
        holder.itemView.setTag(mItemList.get(position));
        Pilot pilot = round.getGroup(groupId).getPilots().get(position);
        Result result = pilot.getResult(round.id);
        if(holder.dnsButton!=null && editButtonsVisibility == View.VISIBLE ){
            holder.dnsButton.setEnabled(result == null);
        }

        if(holder.editButton!=null && editButtonsVisibility == View.VISIBLE){
            holder.editButton.setEnabled(result != null);
        }

        if(holder.assignAndSendButton!=null && assignAndSendButtonVisibility == View.VISIBLE ){
            holder.assignAndSendButton.setEnabled(result == null);
        }

        if(result != null)
            holder.itemView.setBackgroundColor(Color.rgb(0, 255,0));
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;
        Button dnsButton;
        Button assignAndSendButton;
        Button editButton;
        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            mText = (TextView) itemView.findViewById(R.id.text);

            dnsButton = (Button) itemView.findViewById(R.id.dnsButton);
            editButton = (Button) itemView.findViewById(R.id.editButton);
            assignAndSendButton = (Button) itemView.findViewById(R.id.assignAndSendButton);

            if(dnsButton != null){
                dnsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Group targetGroup = round.getGroup(groupId);
                        List<PilotWithOrder> pilotsWitOrder = getPilots(targetGroup);
                        Optional<PilotWithOrder> firstPilotWithoutResult =
                                com.annimon.stream.Stream.of(pilotsWitOrder)
                                        .filter(i -> i.time.isEmpty())
                                        .findFirst();

                        int order = (int)mItemId + 1;
                        Pilot pilot = round.getGroup(groupId).getPilots().get((int)mItemId);
                        if(firstPilotWithoutResult.isPresent() && firstPilotWithoutResult.get().id != pilot.id){
                            order = firstPilotWithoutResult.get().order + 1;

                            Optional<PilotWithOrder> currentPilot =
                                    com.annimon.stream.Stream.of(pilotsWitOrder)
                                            .filter(i -> i.time.isEmpty() && i.id == pilot.id)
                                            .findFirst();
                            if(currentPilot.get().order > firstPilotWithoutResult.get().order){
                                targetGroup.reorderPilots(currentPilot.get().order, firstPilotWithoutResult.get().order);
                            }
                        }
                        result = new Result(true, round.id);
                        pilot.addResult(result);
                        Toast.makeText(view.getContext(),
                                "Wynik został zapisany pilotowi: " + pilot.getFirstName() + " " + pilot.getLastName(),
                                Toast.LENGTH_SHORT).show();

                        new SendPilotStrategy().doStrategy(pilot, result, new StrategyScope(round.id,
                                context), order);
                        showFragment(RoundFragment.newInstance(round));
                    }
                });
                dnsButton.setVisibility(editButtonsVisibility);
            }

            if(editButton != null ){
                editButton.setVisibility(editButtonsVisibility);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int order = (int)mItemId + 1;
                        Pilot pilot = round.getGroup(groupId).getPilots().get((int)mItemId);
                        transaction
                                .replace(R.id.container, new EditPilotFragment(pilot, round, context, order), "fragment")
                                .commit();
                    }
                });
            }

            if(assignAndSendButton != null){
                assignAndSendButton.setVisibility(assignAndSendButtonVisibility);
                assignAndSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(assignMode) {
                            Group targetGroup = round.getGroup(groupId);
                            List<PilotWithOrder> pilotsWitOrder = getPilots(targetGroup);
                            Optional<PilotWithOrder> firstPilotWithoutResult =
                                    com.annimon.stream.Stream.of(pilotsWitOrder)
                                            .filter(i -> i.time.isEmpty())
                                            .findFirst();

                            int order = (int)mItemId + 1;

                            Pilot pilot = round.getGroup(groupId).getPilots().get((int)mItemId);

                            if(firstPilotWithoutResult.isPresent() && firstPilotWithoutResult.get().id != pilot.id){
                                order = firstPilotWithoutResult.get().order + 1;

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

                            new SendPilotStrategy().doStrategy(pilot, result, new StrategyScope(round.id,
                                    context), order);
                            showFragment(RoundFragment.newInstance(round));
                        }
                    }
                });
            }
        }

        @Override
        public void onItemClicked(View view) {
        }

        @Override
        public boolean onItemLongClicked(View view) {
            return true;
        }

        private List<PilotWithOrder> getPilots(Group group){
            List<PilotWithOrder> pilots = new ArrayList<>();
            int order = 0;
            for (Pilot pilot: group.getPilots()) {
                Result result = pilot.getResult(round.id);
                Optional<Float> time = Optional.empty();

                if(result != null)
                    time = Optional.of(result.getTotalFlightTime());


                pilots.add(new PilotWithOrder(pilot.id, time, order));
                order++;
            }
            return pilots;
        }
    }

    public void showFragment(Fragment fragment){
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .replace(R.id.container, fragment, "fragment")
                .commit();
    }

}
