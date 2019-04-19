package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient;
import pl.f3f_klif.f3fstatapp.groups.factory.RequestParamsFactory;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Account;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;

import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.isSuccess;

public class SendPilotStrategy{
    public void doStrategy(Pilot pilot, Result result, StrategyScope scope, int order) {
        Event event = DatabaseRepository.getEvent();

        Box<Account> accountBox = ObjectBox.get().boxFor(Account.class);
        if(!accountBox.isEmpty()) {
            Account account = accountBox.getAll().get(0);
            RequestParams params = RequestParamsFactory
                    .create(event.getType(),account, event,  pilot, result, scope.groupId, scope.roundId, order);

            sendSinglePilot(params, scope, pilot, event.getType(), result);
        }
        else{
            String message = String.format("Wysyłanie nie powiodło się: %d");
            Toast
                    .makeText(
                            scope.context,
                            message,
                            Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSinglePilot(RequestParams params, StrategyScope scope, Pilot pilot, String eventType, Result result){

        F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseText = new String(responseBody);
                if(isSuccess(responseText)) {

                    String message = String.format("Wysyłanie powiodło się. Wynik: %.2f",result.getTotalFlightTime());
                    Toast
                            .makeText(
                                    scope.context,
                                    message,
                                    Toast.LENGTH_SHORT).show();
                }
                else {
                    String message = String.format("Nie powiodło się wysyłanie pilota %s %s w grupie: %d", pilot.firstName, pilot.lastName, scope.groupId);
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
