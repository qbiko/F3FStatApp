package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.objectbox.Box;
import io.objectbox.BoxStore;
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

public class SendGroupStrategy implements Strategy {

    @Override
    public void doStrategy(long groupId, long roundId) {
        Event event = DatabaseRepository.getEvent();
        Round round = event.getRound(roundId);
        Group group = round.getGroup(groupId);
        List<Pilot> pilots = group.getPilots();

        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
        if(!accountBox.isEmpty()) {
            Account account = accountBox.getAll().get(0);

            for (Pilot pilot:pilots) {
                Result result = pilot.getResult(roundId);
                RequestParams params = ReqestParamsFactory
                        .Create(event.getType(),account, event,  pilot, result, groupId, roundId);

                sendSinglePilot(params);
            }
        }
    }

    private void sendSinglePilot(RequestParams params){

        F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseText = new String(responseBody);
                if(isSuccess(responseText)) {
                    //String.format("Pilot %s %s wysłany",pilot.FirstName, pilot.LastName); walnac jakiegos loga czy cos
                }
                else {
                    //String.format("Pilot %s %s wysłany",pilot.FirstName, pilot.LastName); walnac jakiegos loga czy cos
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });
    }
}
