package pl.f3f_klif.f3fstatapp.infrastructure.database;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event_;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.mapper.PilotMapper;

public class DatabaseRepository {
    private static Box<Event> EventBox;
    private static Event Event;
    private static Box<Round> RoundBox;
    private static long F3fId;
    public static void Init(int f3fId) {
        F3fId = f3fId;
        EventBox = ObjectBox.get().boxFor(Event.class);
        Event = EventBox.query().equal(Event_.F3fId, f3fId).build().findFirst();
        if(Event == null){
            CreateEvent(f3fId);
        }
    }

    public static Event GetEvent() {
        return Event;
    }

    public static List<Round> GetRounds() {
        if(Event != null)
            return Event.getRounds();
        return new ArrayList<>();
    }

    public static List<Group> GetGroups(long roundId){
        return Event.getRound(roundId).getGroups();
    }

    public static Long CreateEvent(int f3fId){
        return EventBox.put(new Event(f3fId));
    }

    public static long CreateRound(List<pl.f3f_klif.f3fstatapp.utils.Pilot> pilots){
        long roundId = 0;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String pilotsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(PilotMapper.ToDbModel(pilots));
            Round round = new Round();
            round.Groups.add(new Group(pilotsJson));
            Event.Rounds.add(round);
            EventBox.put(Event);
            Event = EventBox.query().equal(Event_.F3fId, F3fId).build().findFirst();

            roundId = Event.getRounds().get(Event.getRounds().size()-1).Id;

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roundId;
    }
}
