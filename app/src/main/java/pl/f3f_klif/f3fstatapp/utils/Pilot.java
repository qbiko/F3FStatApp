package pl.f3f_klif.f3fstatapp.utils;

public class Pilot {
    private long id;
    private int orderNumber;
    private String firstName;
    private String lastName;
    private String pilotClass;
    private String ama;
    private String fai;
    private String faiLicense;
    private String teamName;

    public Pilot(String requestLine) {
        String[] requestValues = requestLine.split(",");
        id = Long.parseLong(requestValues[0].replace("\"", ""));
        orderNumber = Integer.parseInt(requestValues[1].replace("\"", ""));
        firstName = requestValues[2].replace("\"", "");
        lastName = requestValues[3].replace("\"", "");
        pilotClass = requestValues[4].replace("\"", "");
        ama = requestValues[5].replace("\"", "");
        fai = requestValues[6].replace("\"", "");
        faiLicense = requestValues[7].replace("\"", "");
        teamName = requestValues[8].replace("\"", "");
    }

    public int getOrderNumber() {
        return orderNumber;
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
}
