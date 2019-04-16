package pl.f3f_klif.f3fstatapp.groups.services;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.infrastructure.ItemAdapter;
import pl.f3f_klif.f3fstatapp.groups.services.models.Group;
import pl.f3f_klif.f3fstatapp.groups.services.points.PointCounter;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.sqlite.WindMeasure;

public class GroupCreator {

    public static Group create(
            Context context,
            String groupName,
            List<pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot> pilots,
            int flightNumber,
            Result result,
            Round round,
            long groupId,
            boolean assignMode,
            FragmentTransaction transaction)
    {
        final ArrayList<Pair<Long, String>> mItemArray = new ArrayList<>();
        long index = 0;
        for (pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot pilot: pilots) {
            mItemArray
                    .add(new Pair<>(
                            index,
                            String.format("%s %s", pilot.firstName, pilot.lastName)
                            )
                    );
            index++;
        }

        ItemAdapter listAdapter = new ItemAdapter(
                mItemArray,
                R.layout.group_pilot_item,
                R.id.item_layout,
                true,
                flightNumber,
                result,
                round,
                groupId,
                assignMode,
                new ArrayList<>(),
                context,
                View.INVISIBLE,
                View.INVISIBLE,
                transaction);

        final View header = View.inflate(context, R.layout.group_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(groupName);
        ((TextView) header.findViewById(R.id.item_count)).setText(String.valueOf(pilots.size()));

        return new Group(listAdapter, header, false);
    }

    public static Group createRoundGroup(
            Context context,
            String groupName,
            List<Pilot> pilots,
            int flightNumber,
            Result result,
            Round round,
            long groupId,
            boolean assignMode,
            List<WindMeasure> windMeasures,
            FragmentTransaction transaction)
    {
        final ArrayList<Pair<Long, String>> mItemArray = new ArrayList<>();
        long index = 0;
        Map<Long, Double> points = PointCounter.GetPoints(round);
        for (Pilot pilot: pilots) {
            String resultMessage;
            Result pilotResult = pilot.getResult(round.getId());
            if(pilotResult != null && pilotResult.getTotalFlightTime() >= 0) {
                if(pilotResult.isDnf()){
                    resultMessage = String.format(
                            "%s. %s %s\nDNF",index+1, pilot.firstName, pilot.lastName);
                }
                else if(pilotResult.isDns())
                    resultMessage = String.format(
                            "%s. %s %s\nDNS",index+1, pilot.firstName, pilot.lastName);
                else{
                    resultMessage = String.format("%s. %s %s\nCzas: %s\nPunkty: %.2f\nPkt. karne: %.2f", index+1,
                            pilot.firstName, pilot.lastName, String.valueOf(Precision.round(pilotResult.getTotalFlightTime(),2)),
                            (points.get(pilot.id).floatValue()), pilotResult.getPenalty());
                }
            }
            else {
                resultMessage =
                        String.format("%s. %s %s\nCzas: -\nPunkty: -\nPkt. karne: -", index+1,
                                pilot.firstName, pilot.lastName);
            }


            mItemArray.add(new Pair<>(index, resultMessage));
            index++;
        }

        ItemAdapter listAdapter = new ItemAdapter(
                mItemArray,
                assignMode ? R.layout.group_pilot_item_assign : R.layout.group_pilot_item,
                assignMode ? R.id.item_layout_assign : R.id.item_layout,
                true,
                flightNumber,
                result,
                round,
                groupId,
                assignMode,
                windMeasures,
                context,
                assignMode ? View.INVISIBLE : View.VISIBLE,
                assignMode ? View.VISIBLE : View.INVISIBLE,
                transaction);

        final View header = View.inflate(context, R.layout.group_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(groupName);
        ((TextView) header.findViewById(R.id.item_count)).setText(String.valueOf(pilots.size()));

        return new Group(listAdapter, header, false);
    }
}
