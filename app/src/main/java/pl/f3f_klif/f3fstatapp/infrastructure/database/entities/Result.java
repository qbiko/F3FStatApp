package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

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
    private float windAvg;
    private float windDirAvg;
    private float totalFlightTime;
    private float climbOut;
    private String lapsTimeJson;
    private boolean dns;
    private boolean dnf;
    private int orderNumber;
    private int groupNumber;
    private long roundId;

    public Result() {}
    public Result(int flightNumber, long roundId) {
        this.flightNumber = flightNumber;
        this.roundId = roundId;
        //lapsTime = new ArrayList<>();
    }

    public List<Float> GetLapsTime(){
        
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
}
