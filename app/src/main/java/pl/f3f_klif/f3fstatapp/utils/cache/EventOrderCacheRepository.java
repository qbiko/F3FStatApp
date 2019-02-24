package pl.f3f_klif.f3fstatapp.utils.cache;

import android.util.LruCache;

import pl.f3f_klif.f3fstatapp.models.EventOrder;
import pl.f3f_klif.f3fstatapp.models.Group;
import pl.f3f_klif.f3fstatapp.models.Pilot;
import pl.f3f_klif.f3fstatapp.models.Round;

public class EventOrderCacheRepository {
    private int cacheSize = 4 * 1024 * 1024;
    private String Key;
    private LruCache<String, EventOrder> _eventOrderCache;

    public EventOrderCacheRepository(String key) {
        Key = key;
        _eventOrderCache = new LruCache<String, EventOrder>(cacheSize);

    }

    public EventOrder AddRound(Round round){
        return GetOrInit(Key)
                .AddRound(round);
    }

    public EventOrder AddGroup(Group group, String RoundName){
        return GetOrInit(Key)
                .AddGroupToRound(group, RoundName);
    }

    public EventOrder AddPilot(Pilot pilot, String RoundName, int groupId){
        return GetOrInit(Key)
                .AddPilotToGroup(pilot, RoundName, groupId);
    }

    public EventOrder ReorderPilot(int fromGroup, int toGroup, int fromPilotPosition, int toPilotPosition){
        return GetOrInit(Key)
                .ReorderPilots(fromGroup, toGroup, fromPilotPosition, toPilotPosition);
    }

    public EventOrder GetOrInit(String key){
        EventOrder eventOrder = Get();
        if(eventOrder == null){
            eventOrder = new EventOrder();
            _eventOrderCache.put(key, eventOrder);
        }
        return eventOrder;
    }

    public EventOrder Get(){
        return _eventOrderCache.get(Key);
    }

}
