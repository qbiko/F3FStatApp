package pl.f3f_klif.f3fstatapp.groups.services.models;

import android.view.View;

import pl.f3f_klif.f3fstatapp.groups.infrastructure.ItemAdapter;

public class Group {
    public ItemAdapter ItemAdapter;
    public View Header;
    public boolean HasFixedItemSize;

    public Group(ItemAdapter itemAdapter, View header, boolean hasFixedItemSize){
        ItemAdapter = itemAdapter;
        Header = header;
        HasFixedItemSize = hasFixedItemSize;
    }
}
