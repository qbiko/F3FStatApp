package pl.f3f_klif.f3fstatapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class F3FPilot implements Parcelable {
    private long id;
    private int startNumber;
    private String firstName;
    private String lastName;
    private String pilotClass;
    private String ama;
    private String fai;
    private String faiLicense;
    private String teamName;
    private int orderNumber;
    private int groupNumber;

    public F3FPilot(String requestLine) {
        String[] requestValues = requestLine.split(",");
        id = Long.parseLong(requestValues[0].replace("\"", ""));
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

    public F3FPilot(long f3fId, String firstName, String lastName) {
        id = f3fId;
        firstName = firstName;
        lastName = lastName;
    }

    public F3FPilot(Parcel parcel) {
        id = parcel.readLong();
        startNumber = parcel.readInt();
        firstName = parcel.readString();
        lastName = parcel.readString();
        pilotClass = parcel.readString();
        ama = parcel.readString();
        fai = parcel.readString();
        faiLicense = parcel.readString();
        teamName = parcel.readString();
        orderNumber = parcel.readInt();
        groupNumber = parcel.readInt();
    }

    public long getId() {return id;}

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

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeInt(startNumber);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(pilotClass);
        parcel.writeString(ama);
        parcel.writeString(fai);
        parcel.writeString(faiLicense);
        parcel.writeString(teamName);
        parcel.writeInt(orderNumber);
        parcel.writeInt(groupNumber);
    }

    public static final Creator<F3FPilot> CREATOR = new Creator<F3FPilot>() {
        @Override
        public F3FPilot createFromParcel(Parcel in) {
            return new F3FPilot(in);
        }

        @Override
        public F3FPilot[] newArray(int size) {
            return new F3FPilot[size];
        }
    };
}
