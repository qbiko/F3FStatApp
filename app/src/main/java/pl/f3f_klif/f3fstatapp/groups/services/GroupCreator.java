package pl.f3f_klif.f3fstatapp.groups.services;

import android.content.Context;
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

public class GroupCreator {

    public static Group create(
            Context context,
            String groupName,
            List<pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot> pilots,
            int flightNumber,
            Result result,
            Round round,
            long groupId,
            boolean assignMode)
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
                assignMode);

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
            boolean assignMode)
    {
        final ArrayList<Pair<Long, String>> mItemArray = new ArrayList<>();
        long index = 0;
        Map<Long, Double> points = PointCounter.GetPoints(round);
        for (Pilot pilot: pilots) {
            String time = "-";
            Result pilotResult = pilot.getResult(round.getId());
            if(pilotResult != null) {
                time = pilotResult.getTotalFlightTime() < 0
                            ? "-"
                            : String.valueOf(Precision.round(pilotResult.getTotalFlightTime(),2));
            }
            mItemArray
                    .add(new Pair<>(
                                    index,

                                    String.format("%s. %s %s\n Czas: %s\n Punkty: %.2f",index+1,
                                            pilot.firstName, pilot.lastName, time, (points.get(pilot.id).floatValue()))
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
                assignMode);

        final View header = View.inflate(context, R.layout.group_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(groupName);
        ((TextView) header.findViewById(R.id.item_count)).setText(String.valueOf(pilots.size()));

        return new Group(listAdapter, header, false);
    }
}
