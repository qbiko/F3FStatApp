package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import android.util.JsonReader;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;
import io.objectbox.annotation.Id;
@Entity
public class Group {
    @Id
    long Id;

    public String PilotsListJson;

    public Group(){ }

    public List<Pilot> getPilots() throws ParseException {
        return (List<Pilot>)new JSONParser().parse(PilotsListJson);
    }

}
