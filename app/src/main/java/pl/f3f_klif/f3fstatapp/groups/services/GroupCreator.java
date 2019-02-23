package pl.f3f_klif.f3fstatapp.groups.services;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.infrastructure.ItemAdapter;
import pl.f3f_klif.f3fstatapp.groups.services.models.Group;
import pl.f3f_klif.f3fstatapp.utils.Pilot;

public class GroupCreator {

    public static Group Create(Context context, String groupName, List<Pilot> pilots){
        final ArrayList<Pair<Long, String>> mItemArray = new ArrayList<>();
        long index = 0;
        for (Pilot pilot: pilots) {
            mItemArray
                    .add(new Pair<>(
                            index,
                            String.format("%s %s", pilot.getFirstName(), pilot.getLastName())
                            )
                    );
            index++;
        }

        ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.group_pilot_item, R.id.item_layout, true);
        final View header = View.inflate(context, R.layout.group_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(groupName);
        ((TextView) header.findViewById(R.id.item_count)).setText("" + pilots.size());

        return new Group(listAdapter, header, false);
    }
}