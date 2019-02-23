package pl.f3f_klif.f3fstatapp.groups.services;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.groups.infrastructure.ItemAdapter;
import pl.f3f_klif.f3fstatapp.groups.services.models.Group;

public class GroupCreator {

    public static Group Create(Context context, String groupName){
        final ArrayList<Pair<Long, String>> mItemArray = new ArrayList<>();
        int addItems = 12;
        for (long i = 0; i < addItems; i++) {
            mItemArray.add(new Pair<>(i, "Zawodnik " + i));
        }

        ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.group_pilot_item, R.id.item_layout, true);
        final View header = View.inflate(context, R.layout.group_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(groupName);
        ((TextView) header.findViewById(R.id.item_count)).setText("" + addItems);

        return new Group(listAdapter, header, false);
    }
}
