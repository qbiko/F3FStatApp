package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Group;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.isSuccess;

public class SendGroupStrategy implements Strategy {

    @Override
    public void doStrategy(long groupId, long roundId) {
        Event event = DatabaseRepository.getEvent();
        Group group = DatabaseRepository.getGroup(roundId, groupId);
        Round round = DatabaseRepository.getRound(roundId);
        List<Group> groups = DatabaseRepository.getGroups(roundId);
        List<Round> rounds = DatabaseRepository.getRounds();
        List<Pilot> pilots = group.getPilots();

        for (Pilot pilot:pilots) {
            RequestParams params = new RequestParams();
            params.put("login", "ssarnecki34@gmail.com");
            params.put("password", "989865aa");
            params.put("function", "postScore");
            params.put("event_id", event.getF3fId());
            params.put("pilot_id", pilot.f3fId);
            params.put("seconds", pilot.flightTimeResult);
            params.put("round", rounds.indexOf(round) + 1);
            params.put("group", groups.indexOf(group) + 1);

            sendSinglePilot(params);
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
