package pl.f3f_klif.f3fstatapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class F3FRound implements Parcelable {

    private long roundId;
    private List<Pilot> startingList;
    private String status;

    public F3FRound(long roundId, List<Pilot> startingList, String status) {
        this.roundId = roundId;
        this.startingList = startingList;
        this.status = status;
    }

    public F3FRound(Parcel parcel) {
        this.roundId = parcel.readLong();
        this.startingList = new ArrayList<>();
        parcel.readList(startingList,Pilot.class.getClassLoader());
        this.status = parcel.readString();
    }

    public long getRoundId() {
        return roundId;
    }

    public List<Pilot> getStartingList() {
        return startingList;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(roundId);
        parcel.writeList(startingList);
        parcel.writeString(status);
    }

    public static final Creator<F3FRound> CREATOR = new Creator<F3FRound>() {
        @Override
        public F3FRound createFromParcel(Parcel in) {
            return new F3FRound(in);
        }

        @Override
        public F3FRound[] newArray(int size) {
            return new F3FRound[size];
        }
    };
}
