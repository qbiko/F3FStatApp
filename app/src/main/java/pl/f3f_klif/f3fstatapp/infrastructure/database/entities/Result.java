package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Result {

    @Id
    long id;
    private int flightNumber;
    //private Timestamp dateTimeOfFlight;
    public float windAvg;
    public float windDirAvg;
    private float totalFlightTime;
    private float climbOut;
    private String lapsTimeJson;
    private boolean dns;
    private boolean dnf;
    private int orderNumber;
    private int groupNumber;
    private long roundId;

    public Result() {
    }

    public Result(int flightNumber, long roundId) {
        this.flightNumber = flightNumber;
        this.roundId = roundId;
    }

    public List<Float> getLapsTime() {
        try {
            return new Gson()
                    .fromJson(
                            lapsTimeJson,
                            new TypeToken<List<Float>>() {
                            }.getType());
        } catch (Exception e) {

        }

        return new ArrayList<>();
    }

    public void addLapTime(Float time) {
        ArrayList<Float> laps = new Gson()
                .fromJson(
                        lapsTimeJson,
                        new TypeToken<List<Float>>() {
                        }.getType());
        laps.add(time);
        lapsTimeJson = new Gson().toJson(laps);
    }

    public void setClimbOut(float climbOut) {
        this.climbOut = climbOut;
    }

    public void addLap(float lapTime) {
        //lapsTime.add(lapTime);
    }

    public void setTotalFlightTime(float totalFlightTime) {
        this.totalFlightTime = totalFlightTime;
    }

    public long getId() {
        return id;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public float getWindAvg() {
        return windAvg;
    }

    public float getWindDirAvg() {
        return windDirAvg;
    }

    public float getTotalFlightTime() {
        return totalFlightTime;
    }

    public float getClimbOut() {
        return climbOut;
    }

    public boolean isDns() {
        return dns;
    }

    public boolean isDnf() {
        return dnf;
    }

    public void setDnf(boolean dnf) {
        this.dnf = dnf;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public long getRoundId() {
        return roundId;
    }

    public String getLapsTimeJson() {
        return lapsTimeJson;
    }
}

