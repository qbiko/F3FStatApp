package pl.f3f_klif.f3fstatapp.groups.factory;

import com.loopj.android.http.RequestParams;

import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Account;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Result;

public class RequestParamsFactory {

    public static RequestParams create(
            String eventType,
            Account account,
            Event event,
            Pilot pilot,
            Result result,
            long groupId,
            long roundId,
            int order,
            int roundIndex){
        RequestParams params = new RequestParams();
        params.put("login", account.getMail());
        params.put("password", account.getPassword());
        params.put("function", "postScore");
        params.put("event_id", event.getF3fId());
        params.put("pilot_id", pilot.f3fId);
        params.put("seconds", result.getTotalFlightTime());
        params.put("round", roundIndex);
        params.put("group", groupId);
        params.put("penalty", result.getPenalty());
        params.put("order", order);
        if(account.isWindDir())
            params.put("dir_avg", result.getWindDirAvg());
        if(account.isWindSpeed())
            params.put("wind_avg", result.getWindAvg());


        if(eventType.equals("F3F Slope Race (Plus Scoring)")){
            List<Float> laps = result.getLapsTime();
            int index = 1;
            for (Float time: laps) {
                params.put(String.format("sub%s", index), Precision.round(time,2));
                index++;
            }

            while(index<12) {
                params.put(String.format("sub%s", index), 0f);
                index++;
            }
            params.put("dnf", result.isDnf());
            params.put("dns", result.isDns());
        }

        return params;
    }
}
