package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Round {
    @Id
    public long Id;

    public ToMany<Group> Groups;

    public Round(){
        Groups = new ToMany<>(this, Round_.Groups);
    }

    public List<Group> getGroups() { return this.Groups.subList(0, Groups.size());}

}
