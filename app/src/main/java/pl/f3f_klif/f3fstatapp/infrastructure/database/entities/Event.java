package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

import static pl.f3f_klif.f3fstatapp.api.F3XVaultApiClient.SIMPLE_DATE_FORMAT;

@Entity
public class Event {

    @Id
    long id;

    int f3fId;
    String name;
    String location;
    Date startDate;
    Date endDate;
    String type;
    String minGroupAmount;

    int groupsCount;
    public ToMany<Round> rounds;
    ToMany<Pilot> pilots;

    public Event(){ }
    public Event(int f3fId, int groupsCount, String[] lines){
        this.f3fId = f3fId;
        this.groupsCount = groupsCount;
        rounds = new ToMany<>(this, Event_.rounds);

        String[] requestValues = lines[1].split(",");
        name = requestValues[1].replace("\"", "");
        location = requestValues[2].replace("\"", "");
        try {
            startDate = SIMPLE_DATE_FORMAT.parse(requestValues[3].replace("\"", ""));
            endDate = SIMPLE_DATE_FORMAT.parse(requestValues[4].replace("\"", ""));
        } catch(ParseException ex) {
            startDate = new Date();
            endDate = new Date();
        }
        type = requestValues[5].replace("\"", "");

        pilots = new ToMany<>(this, Event_.pilots);

        for(int i = 3; i<lines.length; i++) {
            if(!lines[i].isEmpty()) {
                pilots.add(new Pilot(lines[i]));
            }
        }
    }

    public List<Round> getRounds() { return this.rounds;}

    public Round getRound(long id) { return this.rounds.getById(id);}

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getF3fId() {
        return this.f3fId;
    }

    public void setF3fId(int f3fId) {
        this.f3fId = f3fId;
    }

    public long getGroupsCount() {
        return this.groupsCount;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getType() {
        return type;
    }

    public List<Pilot> getPilots() {
        return pilots;
    }
}
