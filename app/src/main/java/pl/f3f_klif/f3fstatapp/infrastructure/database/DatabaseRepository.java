package pl.f3f_klif.f3fstatapp.infrastructure.database;

import android.content.Context;

import org.w3c.dom.Entity;

import java.util.List;

import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event_;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;

public class DatabaseRepository {
    private static Box<Event> EventBox;
    private static Event Event;

    public static void Init(int f3fId) {
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
        return Event.getRounds();
    }

    public static List<Group> GetGroups(long roundId){
        return Event.getRound(roundId).getGroups();
    }

    public static Long CreateEvent(int f3fId){
        return EventBox.put(new Event(f3fId));
    }
}
