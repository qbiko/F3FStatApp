package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;

@Entity
public class Pilot  implements Parcelable {
    @Id
    public long id;

    public long f3fId;
    private int startNumber;
    public String firstName;
    public String lastName;
    private String pilotClass;
    private String ama;
    private String fai;
    private String faiLicense;
    private String teamName;
    ToMany<Result> results;

    public Pilot(){ }
    public Pilot(long f3fId, String firstName, String lastName){
        this.f3fId = f3fId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.results = new ToMany<>(this, Pilot_.results);
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
    }

    public Pilot(Parcel parcel) {
        id = parcel.readLong();
        f3fId = parcel.readLong();
        startNumber = parcel.readInt();
        firstName = parcel.readString();
        lastName = parcel.readString();
        pilotClass = parcel.readString();
        ama = parcel.readString();
        fai = parcel.readString();
        faiLicense = parcel.readString();
        teamName = parcel.readString();
    }

    public long getF3fId() {
        return f3fId;
    }
    public long getId() {
        return id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(f3fId);
        parcel.writeInt(startNumber);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(pilotClass);
        parcel.writeString(ama);
        parcel.writeString(fai);
        parcel.writeString(faiLicense);
        parcel.writeString(teamName);
    }

    public static final Parcelable.Creator<Pilot> CREATOR = new Parcelable.Creator<Pilot>() {
        @Override
        public Pilot createFromParcel(Parcel in) {
            return new Pilot(in);
        }

        @Override
        public Pilot[] newArray(int size) {
            return new Pilot[size];
        }
    };

    public void addResult(Result result) {
/*        Box<Result> resultBox = ObjectBox.get().boxFor(Result.class);
        resultBox.put(result);*/
        results.add(result);
        update();
    }

    public void putResult(Result result) {
        Box<Result> resultBox = ObjectBox.get().boxFor(Result.class);
        resultBox.put(result);
    }

    private void update() {
        Box<Pilot> pilotBox = ObjectBox.get().boxFor(Pilot.class);
        pilotBox.put(this);
    }

    public Result getResult(long roundId) {
        for (Result result : results) {
            if(result.getRoundId() == roundId)
                return result;
        }
        return null;
    }
}