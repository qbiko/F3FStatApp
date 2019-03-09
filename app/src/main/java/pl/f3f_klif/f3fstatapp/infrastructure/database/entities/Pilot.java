package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
@Entity
public class Pilot {
    @Id
    long Id;

    public long f3fId;
    private int startNumber;
    public String firstName;
    public String lastName;
    private String pilotClass;
    private String ama;
    private String fai;
    private String faiLicense;
    private String teamName;
    private int orderNumber;
    private int groupNumber;

    public Pilot(){ }
    public Pilot(long f3fId, String firstName, String lastName){
        this.f3fId = f3fId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Pilot(String requestLine) {
        String[] requestValues = requestLine.split(",");
        f3fId = Long.parseLong(requestValues[0].replace("\"", ""));
        startNumber = Integer.parseInt(requestValues[1].replace("\"", ""));
        firstName = requestValues[2].replace("\"", "");
        lastName = requestValues[3].replace("\"", "");
        pilotClass = requestValues[4].replace("\"", "");
        ama = requestValues[5].replace("\"", "");
        fai = requestValues[6].replace("\"", "");
        faiLicense = requestValues[7].replace("\"", "");
        teamName = requestValues[8].replace("\"", "");
        orderNumber = -1;
        groupNumber = -1;
    }

    public long getF3fId() {
        return f3fId;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPilotClass() {
        return pilotClass;
    }

    public String getAma() {
        return ama;
    }

    public String getFai() {
        return fai;
    }

    public String getFaiLicense() {
        return faiLicense;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getGroupNumber() {
        return groupNumber;
    }
}