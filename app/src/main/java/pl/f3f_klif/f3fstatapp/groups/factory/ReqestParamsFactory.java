package pl.f3f_klif.f3fstatapp.groups.factory;

import com.loopj.android.http.RequestParams;

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

        if(eventType == "F3F Slope Race (Plus Scoring)"){
            params.put("sub1", groupId);
            params.put("sub2", groupId);
            params.put("sub3", groupId);
            params.put("sub4", groupId);
            params.put("sub5", groupId);
            params.put("sub6", groupId);
            params.put("sub7", groupId);
            params.put("sub8", groupId);
            params.put("sub9", groupId);
            params.put("sub10", groupId);
            params.put("sub11", groupId);

        }

        return params;
    }
}
