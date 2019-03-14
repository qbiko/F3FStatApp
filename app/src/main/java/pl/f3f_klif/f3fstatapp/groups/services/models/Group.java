package pl.f3f_klif.f3fstatapp.groups.services.models;

import android.view.View;

import pl.f3f_klif.f3fstatapp.groups.infrastructure.ItemAdapter;

public class Group {
    public ItemAdapter itemAdapter;
    public View header;
    public boolean hasFixedItemSize;

    public Group(ItemAdapter itemAdapter, View header, boolean hasFixedItemSize){
        this.itemAdapter = itemAdapter;
        this.header = header;
        this.hasFixedItemSize = hasFixedItemSize;
    }
}
