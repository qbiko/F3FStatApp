package pl.f3f_klif.f3fstatapp.infrastructure.database;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event_;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.mapper.PilotMapper;
import pl.f3f_klif.f3fstatapp.utils.F3FPilot;

public class DatabaseRepository {
    private static final int MaxPilotsNumberInGroup = 12;
    private static Box<Event> EventBox;
    private static Event Event;
    private static Box<Round> RoundBox;
    private static long F3fId;
    public static void init(int f3fId, int groupsCount, String[] lines) {
        F3fId = f3fId;
        EventBox = ObjectBox.get().boxFor(Event.class);
        Event = EventBox.query().equal(Event_.f3fId, f3fId).build().findFirst();
        if(Event == null){
            createEvent(f3fId, groupsCount, lines);
        }
        Event = EventBox.query().equal(Event_.f3fId, f3fId).build().findFirst();
    }

    public static Event getEvent() {
        return Event;
    }

    public static List<Round> getRounds() {
        if(Event != null)
            return Event.getRounds();
        return new ArrayList<>();
    }

    public static List<Group> getGroups(long roundId){
        return Event.getRound(roundId).getGroups();
    }

    public static Long createEvent(int f3fId, int groupsCount, String[] lines){
        return EventBox.put(new Event(f3fId, groupsCount, lines));
    }

    public static long createRound(List<F3FPilot> f3FPilots){
        long roundId = 0;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String pilotsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(PilotMapper.ToDbModel(f3FPilots));
            Round round = new Round();

            if(f3FPilots.size() < MaxPilotsNumberInGroup){
                round.Groups.add(new Group(pilotsJson));
            }
            else{
                List<List<F3FPilot>> pilotsGroups = Lists.partition(f3FPilots, MaxPilotsNumberInGroup);

                for (List<F3FPilot> pilotsGroup: pilotsGroups) {
                    String groupPilotsJson = mapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(PilotMapper.ToDbModel(pilotsGroup));

                    round.Groups.add(new Group(groupPilotsJson));
                }
            }

            Event.rounds.add(round);
            EventBox.put(Event);
            Event = EventBox.query().equal(Event_.f3fId, F3fId).build().findFirst();

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

    public static Group updateGroup(Group updatedGroup){
        Box<Group> groupBox = ObjectBox.get().boxFor(Group.class);
        groupBox.put(updatedGroup);
        return updatedGroup;
    }
}
