package pl.f3f_klif.f3fstatapp.models;

import java.util.ArrayList;
import java.util.List;

public class Round {
    public String Name;
    public List<Group> Groups;

    public Round(String name){
        Name = name;
        Groups = new ArrayList<>();
    }
}
