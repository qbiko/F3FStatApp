package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;
import io.objectbox.relation.ToMany;

@Entity
public class Event {
    @Id
    long Id;

    @NameInDb("F3fId")
    public int F3fId;

    int GroupsCount;
    public ToMany<Round> Rounds;

    public Event(){ }
    public Event(int f3fId, int groupsCount){
        F3fId = f3fId;
        GroupsCount = groupsCount;
        Rounds = new ToMany<>(this, Event_.Rounds);
    }

    public List<Round> getRounds() { return this.Rounds.subList(0, Rounds.size());}

    public Round getRound(long id) { return this.Rounds.getById(id);}

    public long getId() {
        return this.Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public long getF3fId() {
        return this.F3fId;
    }

    public void setF3fId(int id) {
        this.F3fId = id;
    }

    public long getGroupsCount() {
        return this.GroupsCount;
    }

}
