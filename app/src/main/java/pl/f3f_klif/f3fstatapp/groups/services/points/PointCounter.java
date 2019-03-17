package pl.f3f_klif.f3fstatapp.groups.services.points;


import com.annimon.stream.Optional;
import com.annimon.stream.OptionalDouble;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.f3f_klif.f3fstatapp.groups.services.models.PilotWithResult;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;


public class PointCounter {

    public static Map<Long, Double> GetPoints(Round round){
        Map<Long, Double> results = new HashMap<>();
        List<PilotWithResult> pilots = GetPilots(round);
        OptionalDouble pilotWithBestResult = Stream.of(pilots)
                .filter(i -> i.time.isPresent())
                .mapToDouble(i -> i.time.get())
                .min();

        if(pilotWithBestResult.isPresent())
        {
            for (PilotWithResult pilot: pilots) {
                if(pilot.time.isPresent())
                    results.put(pilot.id, (pilotWithBestResult.getAsDouble()/pilot.time.get()) * 1000);
                else
                    results.put(pilot.id, 0D);
            }

        }else{
            for (PilotWithResult pilot: pilots) {
                results.put(pilot.id, 0D);
            }
        }

        return results;
    }

    private static List<PilotWithResult> GetPilots(Round round){
        List<Group> groups = round.getGroups();
        List<PilotWithResult> pilots = new ArrayList<>();
        for (Group pilotsGroup: groups) {
            for (Pilot pilot: pilotsGroup.getPilots()) {
                Result result = pilot.getResult(round.id);
                Optional<Float> time = Optional.empty();
                if(result != null)
                    time = result.getTotalFlightTime() <= 0F
                            ? time
                            : Optional.of(result.getTotalFlightTime());

                pilots.add(new PilotWithResult(pilot.id, time));
            }
        }
        return pilots;
    }

}
