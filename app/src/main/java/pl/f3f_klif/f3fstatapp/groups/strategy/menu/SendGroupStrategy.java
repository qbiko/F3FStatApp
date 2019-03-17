package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import android.content.Context;
import android.widget.Toast;

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
    public void doStrategy(StrategyScope scope) {
        Event event = DatabaseRepository.getEvent();
        Round round = event.getRound(scope.roundId);
        Group group = round.getGroup(scope.groupId);
        List<Pilot> pilots = group.getPilots();

        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
        if(!accountBox.isEmpty()) {
            Account account = accountBox.getAll().get(0);

            for (Pilot pilot:pilots) {
                Result result = pilot.getResult(scope.roundId);
                if(result != null){
                    RequestParams params = ReqestParamsFactory
                            .Create(event.getType(),account, event,  pilot, result, scope.groupId, scope.roundId);

                    sendSinglePilot(params, scope, pilot, event.getType());
                }

            }
        }
    }

    private void sendSinglePilot(RequestParams params, StrategyScope scope, Pilot pilot, String eventType){

        F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseText = new String(responseBody);
                if(isSuccess(responseText)) {

                }
                else {
                    String message = String.format("Nie powiodło się wysyłanie pilota %s %s w grupie: %d", pilot.firstName, pilot.lastName, scope.groupId);
                    if(eventType.equals("F3F Slope Race (Plus Scoring)")
                            && responseText !=null
                            && responseText.contains("mandatory")){
                        message = String.format("Nie powiodło się wysyłanie pilota %s %s w grupie: %d. Brak czasów na wszystkich bazach", pilot.firstName, pilot.lastName, scope.groupId);
                    }
                    Toast
                            .makeText(
                                    scope.context,
                                    message,
                                    Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });
    }
}
