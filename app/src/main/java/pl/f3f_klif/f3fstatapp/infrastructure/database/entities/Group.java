package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import java.util.List;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
@Entity
public class Group {
    @Id
    long Id;

    public String PilotsListJson;

    public Group(){ }
    public Group(String pilotsListJson){
        PilotsListJson = pilotsListJson;
    }


    public List<Pilot> getPilots() {

        try{
            ArrayList<Pilot> pilots = new Gson().fromJson(PilotsListJson, new TypeToken<List<Pilot>>(){}.getType());
            return pilots;
        }
        catch(Exception e){

        }

        return new ArrayList<>();
    }

    public List<Pilot> reorderPilots(int oldPosition, int newPosition) {

        try{
            List<Pilot> pilots = getPilots();
            Pilot pilot = pilots.get(oldPosition);
            pilots.remove(oldPosition);
            pilots.add(newPosition, pilot);
            PilotsListJson = new Gson().toJson(pilots);
            return pilots;
        }
        catch(Exception e){

        }

        return new ArrayList<>();
    }

    public List<Pilot> addPilot(int position, Pilot pilot) {

        try{
            List<Pilot> pilots = getPilots();
            pilots.add(position, pilot);
            PilotsListJson = new Gson().toJson(pilots);
            return pilots;
        }
        catch(Exception e){

        }

        return new ArrayList<>();
    }

    public Pilot removePilot(int position) {

        try{
            List<Pilot> pilots = getPilots();
            Pilot removedPilot = pilots.get(position);
            pilots.remove(position);
            PilotsListJson = new Gson().toJson(pilots);
            return removedPilot;
        }
        catch(Exception e){

        }

        return new Pilot();
    }

}
