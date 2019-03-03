package pl.f3f_klif.f3fstatapp.mapper;
import java.util.ArrayList;
import java.util.List;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.utils.F3FRound;
import pl.f3f_klif.f3fstatapp.utils.Pilot;

public class RoundMapper {
    public static List<F3FRound> ToViewModel(List<Round> rounds){
        List<F3FRound> f3fRounds = new ArrayList<>();
        for (Round round:rounds) {
            f3fRounds.add(new F3FRound(round.Id, new ArrayList<Pilot>(), "nie rozpoczeta"));
        }
        return f3fRounds;
    }
}
