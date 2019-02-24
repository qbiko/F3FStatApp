package pl.f3f_klif.f3fstatapp.models;

import java.util.ArrayList;
import java.util.List;

public class EventOrder {
    public List<Round> Rounds;

    public EventOrder(){
        Rounds = new ArrayList<>();
    }

    public EventOrder AddRound(Round round){
        Rounds.add(round);
        return this;
    }

    public EventOrder AddGroupToRound(Group group, String RoundName){
        for (Round round: Rounds) {
            if(round.Name == RoundName)
                round.Groups.add(group);
        }

        return this;
    }

    public EventOrder AddPilotToGroup(Pilot pilot, String RoundName, int groupId){
        for (Round round: Rounds) {
            if(round.Name == RoundName)
            {
                for (Group group: round.Groups) {
                    if(group.Id == groupId)
                    {
                        group.Pilots.add(pilot);
                    }
                }
            }
        }

        return this;
    }

    public EventOrder ReorderPilots(int fromGroup, int toGroup, int fromPilotPosition, int toPilotPosition){
        if(fromGroup != toGroup)
        {
            //todo
            return this;
        }

        if(fromPilotPosition != toPilotPosition)
        {
            //todo
            return this;
        }

        return this;
    }

}
