package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient;
import pl.f3f_klif.f3fstatapp.groups.factory.ReqestParamsFactory;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Account;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;

import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.isSuccess;

public class CancelGroupStrategy implements Strategy {

    @Override
    public void doStrategy(StrategyScope scope) {
        Event event = DatabaseRepository.getEvent();
        Round round = event.getRound(scope.roundId);
        Group group = round.getGroup(scope.groupId);
        List<Pilot> pilots = group.getPilots();

        for (Pilot pilot : pilots) {
            Result result = pilot.getResult(scope.roundId);
            if(result != null){
                Box<Result> resultBox = ObjectBox.get().boxFor(Result.class);
                resultBox.remove(result);
            }

        }
    }
}
