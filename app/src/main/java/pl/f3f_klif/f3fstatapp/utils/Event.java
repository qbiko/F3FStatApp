package pl.f3f_klif.f3fstatapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.Settings.System.DATE_FORMAT;
import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.SIMPLE_DATE_FORMAT;

public class Event {
    private long id;
    private String name;
    private String location;
    private Date startDate;
    private Date endDate;
    private String type;

    public Event(String requestLine) {
        String[] requestValues = requestLine.split(",");
        id = Long.parseLong(requestValues[0].replace("\"", ""));
        name = requestValues[1].replace("\"", "");
        location = requestValues[2].replace("\"", "");
        try {
            startDate = SIMPLE_DATE_FORMAT.parse(requestValues[3].replace("\"", ""));
            endDate = SIMPLE_DATE_FORMAT.parse(requestValues[4].replace("\"", ""));
        } catch(ParseException ex) {
            startDate = new Date();
            endDate = new Date();
        }
        type = requestValues[5].replace("\"", "");
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getType() {
        return type;
    }
}
