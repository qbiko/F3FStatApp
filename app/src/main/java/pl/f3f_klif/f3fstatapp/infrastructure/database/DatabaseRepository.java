package pl.f3f_klif.f3fstatapp.infrastructure.database;

import android.content.Context;

import java.util.List;

import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event_;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;

public class DatabaseRepository {
    private static Box<Event> eventBox;
    private static Event event;
    private static Box<Round> roundBox;
    private static long f3fId;

    public static boolean init() {
        eventBox = ObjectBox.get().boxFor(Event.class);
        event = eventBox.getAll().get(0);
        if(event == null){
            return false;
        }
        DatabaseRepository.f3fId = event.getF3fId();

        roundBox = ObjectBox.get().boxFor(Round.class);
        List<Round> rounds = roundBox.getAll();
        event.fillRounds(rounds);

        return true;
    }

    public static void initNew(int f3fId, int groupsCount, String[] lines, Context context) {
        DatabaseRepository.f3fId = f3fId;
        ObjectBox.clear(context);
        eventBox = ObjectBox.get().boxFor(Event.class);
        createEvent(f3fId, groupsCount, lines);
        event = eventBox.query().equal(Event_.f3fId, f3fId).build().findFirst();
    }

    public static Event getEvent() {
        return event;
    }

    private static Long createEvent(int f3fId, int groupsCount, String[] lines){
        return eventBox.put(new Event(f3fId, groupsCount, lines));
    }

    public static Pilot updatePilot(Pilot updatedPilot){
        Box<Pilot> pilotBox = ObjectBox.get().boxFor(Pilot.class);
        pilotBox.put(updatedPilot);
        return updatedPilot;
    }



}
