package pl.f3f_klif.f3fstatapp.groups.factory;

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Account;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;

public class ReqestParamsFactory {

    public static RequestParams Create(
            String eventType,
            Account account,
            Event event,
            Pilot pilot,
            Result result,
            long groupId,
            long roundId){
        RequestParams params = new RequestParams();
        params.put("login", account.getMail());
        params.put("password", account.getPassword());
        params.put("function", "postScore");
        params.put("event_id", event.getF3fId());
        params.put("pilot_id", pilot.f3fId);
        params.put("seconds", result.getTotalFlightTime());
        params.put("round", roundId);
        params.put("group", groupId);
        params.put("penalty", result.getPenalty());

        if(eventType == "F3F Slope Race (Plus Scoring)"){
            ArrayList<Float> laps = (ArrayList<Float>) result.getLapsTime();
            int index = 1;
            for (Float time: laps) {
                params.put(String.format("sub%s", index), time);
                index++;
            }
        }

        return params;
    }
}
