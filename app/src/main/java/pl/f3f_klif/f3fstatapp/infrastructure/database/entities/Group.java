package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Group implements Parcelable {
    @Id
    public long id;

    public ToMany<Pilot> pilots;

    public Group(){ }
    public Group(List<Pilot> pilots){
        this.pilots = new ToMany<>(this, Group_.pilots);
        this.pilots.addAll(pilots);
    }

    public Group(Parcel parcel) {
        id = parcel.readLong();
        pilots = new ToMany<>(this, Group_.pilots);
        parcel.readList(pilots, Pilot.class.getClassLoader());
    }


    public List<Pilot> getPilots() {
        return pilots;
    }

    public void reorderPilots(int oldPosition, int newPosition) {
        Pilot pilot = pilots.get(oldPosition);
        pilots.remove(oldPosition);
        pilots.add(newPosition, pilot);
    }

    public void addPilot(int position, Pilot pilot) {
        pilots.add(position, pilot);
    }

    public Pilot removePilot(int position) {
        return pilots.remove(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeList(pilots);
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
