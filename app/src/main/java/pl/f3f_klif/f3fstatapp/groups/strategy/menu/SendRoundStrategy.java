package pl.f3f_klif.f3fstatapp.groups.strategy.menu;
import java.util.List;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;

public class SendRoundStrategy implements Strategy {

    @Override
    public void doStrategy(StrategyScope scope) {
        Event event = DatabaseRepository.getEvent();
        Round round = event.getRound(scope.roundId);
        List<Group> groups = round.getGroups();
        for (Group group: groups) {
            new SendGroupStrategy()
                    .doStrategy(new StrategyScope(group.id, scope.roundId, scope.context, round.index));
        }

        round.setState(RoundState.FINISHED);
    }
}
