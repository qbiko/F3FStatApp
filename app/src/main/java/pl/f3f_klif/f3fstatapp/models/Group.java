package pl.f3f_klif.f3fstatapp.models;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public int Id;
    public List<Pilot> Pilots;

    public Group(int id){
        Id = id;
        Pilots = new ArrayList<>();
    }
}
