package pl.f3f_klif.f3fstatapp.mapper;

import java.util.ArrayList;
import java.util.List;

import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;

public class PilotMapper {
    public static List<Pilot> ToDbModel(List<pl.f3f_klif.f3fstatapp.utils.Pilot> pilots){ //<-wyjebac to do mappera
        List<Pilot> dbpilots = new ArrayList<>();
        for (pl.f3f_klif.f3fstatapp.utils.Pilot pilot: pilots) {
            dbpilots.add(new Pilot(pilot.getId(), pilot.getFirstName(), pilot.getLastName()));
        }

        return dbpilots;
    }

    public static List<pl.f3f_klif.f3fstatapp.utils.Pilot> ToViewModel(List<Pilot> pilots){ //<-wyjebac to do mappera
        List<pl.f3f_klif.f3fstatapp.utils.Pilot> showpilots = new ArrayList<>();
        for (Pilot pilot: pilots) {
            showpilots.add(new pl.f3f_klif.f3fstatapp.utils.Pilot(pilot.F3fId, pilot.FirstName, pilot.LastName));
        }

        return showpilots;
    }
}
