package pl.f3f_klif.f3fstatapp.mapper;

import java.util.ArrayList;
import java.util.List;

import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.utils.F3FPilot;

public class PilotMapper {
    public static List<Pilot> ToDbModel(List<F3FPilot> f3FPilots){ //<-wyjebac to do mappera
        List<Pilot> dbpilots = new ArrayList<>();
        for (F3FPilot f3FPilot : f3FPilots) {
            dbpilots.add(new Pilot(f3FPilot.getId(), f3FPilot.getFirstName(), f3FPilot.getLastName()));
        }

        return dbpilots;
    }

    public static List<F3FPilot> ToViewModel(List<Pilot> pilots){ //<-wyjebac to do mappera
        List<F3FPilot> showpilots = new ArrayList<>();
        for (Pilot pilot: pilots) {
            showpilots.add(new F3FPilot(pilot.f3fId, pilot.firstName, pilot.lastName));
        }

        return showpilots;
    }
}
