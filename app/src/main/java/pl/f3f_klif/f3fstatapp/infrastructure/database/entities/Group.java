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

}
