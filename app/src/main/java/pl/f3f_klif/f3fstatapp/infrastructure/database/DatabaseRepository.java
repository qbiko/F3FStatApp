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
    private static final int maxPilotsNumberInGroup = 12;
    private static Box<Event> eventBox;
    private static Event event;
    private static Box<Round> roundBox;
    private static long f3fId;
    public static void init(int f3fId, int groupsCount, String[] lines) {
        DatabaseRepository.f3fId = f3fId;
        eventBox = ObjectBox.get().boxFor(Event.class);
        event = eventBox.query().equal(Event_.f3fId, f3fId).build().findFirst();
        if(event == null){
            createEvent(f3fId, groupsCount, lines);
        }
        event = eventBox.query().equal(Event_.f3fId, f3fId).build().findFirst();
    }

    public static Event getEvent() {
        return event;
    }

    public static Round getRound(long roundId) {
        return event.getRound(roundId);
    }

    public static Round updateRound(Round updatedRound){
        Box<Round> roundBox = ObjectBox.get().boxFor(Round.class);
        roundBox.put(updatedRound);
        return updatedRound;
    }

    public static List<Round> getRounds() {
        if(event != null)
            return event.getRounds();
        return new ArrayList<>();
    }

    public static Group getGroup(long roundId, long groupId) {
        List<Group> groups = event.getRound(roundId).getGroups();
        for (Group group:groups) {
            if(group.Id == groupId)
                return group;
        }
        return null;
    }

    public static List<Group> getGroups(long roundId) {
        return event.getRound(roundId).getGroups();
    }

    public static Long createEvent(int f3fId, int groupsCount, String[] lines){
        return eventBox.put(new Event(f3fId, groupsCount, lines));
    }

    public static long createRound(List<Pilot> f3FPilots) {
        long roundId = 0;
        Round round = new Round();

        if(f3FPilots.size() < maxPilotsNumberInGroup) {
            round.groups.add(new Group(f3FPilots));
        }
        else {
            List<List<Pilot>> pilotsGroups = Lists.partition(f3FPilots, maxPilotsNumberInGroup);
            for (List<Pilot> groupPilots : pilotsGroups) {
                round.groups.add(new Group(groupPilots));
            }
        }

        event.rounds.add(round);
        eventBox.put(event);
        event = eventBox.query().equal(Event_.f3fId, f3fId).build().findFirst();

        roundId = event.getRounds().get(event.getRounds().size()-1).id;

        return roundId;
    }

    public static Group updateGroup(Group updatedGroup){
        Box<Group> groupBox = ObjectBox.get().boxFor(Group.class);
        groupBox.put(updatedGroup);
        return updatedGroup;
    }

    public static Pilot updatePilot(Pilot updatedPilot){
        Box<Pilot> pilotBox = ObjectBox.get().boxFor(Pilot.class);
        pilotBox.put(updatedPilot);
        return updatedPilot;
    }



}
