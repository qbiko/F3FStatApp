package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Group {
    @Id
    public long id;
    public ToMany<Pilot> pilots;

    public Group(){ }
    public Group(List<Pilot> pilots){
        this.pilots = new ToMany<>(this, Group_.pilots);
        this.pilots.addAll(pilots);
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
}
