package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToOne;

@Entity
public class Group {
    @io.objectbox.annotation.Id
    long Id;
    public Group(){ }
    public ToOne<Group> Group;
}
