package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Round {
    @Id
    long Id;
    public Round(){ }
    public ToOne<Event> Event;
}
