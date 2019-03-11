package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import android.content.Intent;

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
    public void Do(long groupId, long roundId) {
        Event event = DatabaseRepository.GetEvent();
        Group group = DatabaseRepository.GetGroup(roundId, groupId);
        Round round = DatabaseRepository.GetRound(roundId);
        List<Group> groups = DatabaseRepository.GetGroups(roundId);
        List<Round> rounds = DatabaseRepository.GetRounds();
        List<Pilot> pilots = group.getPilots();

        for (Pilot pilot:pilots) {
            RequestParams params = new RequestParams();
            params.put("login", "ssarnecki34@gmail.com");
            params.put("password", "989865aa");
            params.put("function", "postScore");
            params.put("event_id", event.F3fId);
            params.put("pilot_id", pilot.F3fId);
            params.put("seconds", pilot.FlightTimeResult);
            params.put("round", rounds.indexOf(round) + 1);
            params.put("group", groups.indexOf(group) + 1);

            SendSinglePilot(params);
        }
    }

    private void SendSinglePilot(RequestParams params){

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
