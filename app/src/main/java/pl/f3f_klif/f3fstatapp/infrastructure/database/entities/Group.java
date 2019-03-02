package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import io.objectbox.annotation.Id;
@Entity
public class Group {
    @Id
    long Id;

    public String PilotsListJson;

    public Group(){ }

    public List<Pilot> getPilots() {

        try{
            return (List<Pilot>)new JSONParser().parse(PilotsListJson);
        }
        catch(Exception e){

        }

        return new ArrayList<>();
    }

}
