package pl.f3f_klif.f3fstatapp.infrastructure.database;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event_;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;

public class DatabaseRepository {
    private static final int MaxPilotsNumberInGroup = 12;
    private static Box<Event> EventBox;
    private static Event event;
    private static Box<Round> RoundBox;
    private static long F3fId;
    public static void init(int f3fId, int groupsCount, String[] lines) {
        F3fId = f3fId;
        EventBox = ObjectBox.get().boxFor(Event.class);
        event = EventBox.query().equal(Event_.f3fId, f3fId).build().findFirst();
        if(event == null){
            createEvent(f3fId, groupsCount, lines);
        }
        event = EventBox.query().equal(Event_.f3fId, f3fId).build().findFirst();
    }

    public static Event getEvent() {
        return event;
    }

    public static List<Round> getRounds() {
        if(event != null)
            return event.getRounds();
        return new ArrayList<>();
    }

    public static List<Group> getGroups(long roundId){
        return event.getRound(roundId).getGroups();
    }

    public static Long createEvent(int f3fId, int groupsCount, String[] lines){
        return EventBox.put(new Event(f3fId, groupsCount, lines));
    }

    public static long createRound(List<Pilot> f3FPilots) {
        long roundId = 0;
        Round round = new Round();

        if(f3FPilots.size() < MaxPilotsNumberInGroup) {
            round.Groups.add(new Group(f3FPilots));
        }
        else {
            List<List<Pilot>> pilotsGroups = Lists.partition(f3FPilots, MaxPilotsNumberInGroup);
            for (List<Pilot> groupPilots : pilotsGroups) {
                round.Groups.add(new Group(groupPilots));
            }
        }

        event.rounds.add(round);
        EventBox.put(event);
        event = EventBox.query().equal(Event_.f3fId, F3fId).build().findFirst();

        roundId = event.getRounds().get(event.getRounds().size()-1).Id;

        return roundId;
    }

    public static Group updateGroup(Group updatedGroup){
        Box<Group> groupBox = ObjectBox.get().boxFor(Group.class);
        groupBox.put(updatedGroup);
        return updatedGroup;
    }
}
