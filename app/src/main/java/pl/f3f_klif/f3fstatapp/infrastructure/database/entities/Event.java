package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Event {
    @Id
    long Id;

    public Event(){ }
    public Event(long id){
        Id = id;
    }

    public long getId() {
        return this.Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

}
