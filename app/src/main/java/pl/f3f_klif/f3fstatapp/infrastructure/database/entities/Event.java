package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;


import java.text.ParseException;
import java.util.ArrayList;
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
    int minGroupAmount;
    boolean windDir;
    boolean windSpeed;

    private ToMany<Round> rounds;
    ToMany<Pilot> pilots;

    public Event() {
        rounds = new ToMany<>(this, Event_.rounds);
        pilots = new ToMany<>(this, Event_.pilots);
    }

    public Event(int f3fId, int minGroupAmount, String[] lines, boolean windDir, boolean windSpeed){
        this.f3fId = f3fId;
        this.minGroupAmount = minGroupAmount;
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

        this.windDir = windDir;
        this.windSpeed = windSpeed;
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

    public int getMinGroupAmount() {
        return minGroupAmount;
    }

    public boolean isWindDir() {
        return windDir;
    }

    public boolean isWindSpeed() {
        return windSpeed;
    }

    public void setWindDir(boolean windDir) {
        this.windDir = windDir;
    }

    public void setWindSpeed(boolean windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Round createRound() {
        Round round = new Round();

        if(pilots.size() < minGroupAmount) {
            round.groups.add(new Group(pilots));
        }
        else {
            int numberOfPilots = pilots.size();
            int groupsCount = numberOfPilots/minGroupAmount;
            int groups[] = new int[groupsCount];
            int numberOfPilotsOutsideGroup = numberOfPilots%minGroupAmount;
            int numberOfPilotsToEveryGroup = numberOfPilotsOutsideGroup/groupsCount;
            int numberOfPilotsToLastGroup = numberOfPilotsOutsideGroup%groupsCount;
            int numberOfPilotsInGroup = minGroupAmount + numberOfPilotsToEveryGroup;
            for (int i =0; i < groupsCount; i++){
                groups[i] = numberOfPilotsInGroup;
            }
            if(numberOfPilotsToLastGroup < 2)
                groups[0] = numberOfPilotsInGroup + numberOfPilotsToLastGroup;
            else if(numberOfPilotsToLastGroup >= 2  && numberOfPilotsToLastGroup <= groupsCount){
                for (int i =0; i<numberOfPilotsToLastGroup; i++){
                    groups[i] = groups[i]+1;
                }
            }

            int startIndex = 0;
            int endIndex = 0;
            for (int groupCount:groups ) {
                endIndex = endIndex + groupCount;
                round.groups.add(new Group(pilots.subList(startIndex, endIndex)));
                startIndex = endIndex;
            }
        }
        rounds.add(round);

        return round;
    }

    public void fillRounds(List<Round> rounds) {
        this.rounds.addAll(rounds);
    }
}
