package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import pl.f3f_klif.f3fstatapp.R;
import pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Account;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.RoundState;

import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.isSuccess;

public class CancelRoundStrategy implements Strategy {

    @Override
    public void doStrategy(StrategyScope scope) {
        Event event = DatabaseRepository.getEvent();
        Round round = event.getRound(scope.roundId);
        List<Group> groups = round.getGroups();
        for (Group group: groups) {
            new CancelGroupStrategy()
                    .doStrategy(new StrategyScope(group.id, scope.roundId, scope.context));
        }

        Account account = DatabaseRepository.getAccount();
        RequestParams params = new RequestParams();
        params.put("login", account.getMail());
        params.put("password", account.getPassword());
        params.put("function", "updateEventRoundStatus");
        params.put("event_id", event.getF3fId());
        params.put("round_number", round.getId());
        params.put("event_round_score_status", 0);

        updateRoundStatus(params, scope);
        round.setState(RoundState.CANCELED);
    }

    private void updateRoundStatus(RequestParams params, StrategyScope scope){

        F3XVaultApiClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseText = new String(responseBody);
                if(isSuccess(responseText)) {
                    Toast.makeText(scope.context, R.string.update_round_status_success, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(scope.context, R.string.update_round_status_failure, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });
    }
}
