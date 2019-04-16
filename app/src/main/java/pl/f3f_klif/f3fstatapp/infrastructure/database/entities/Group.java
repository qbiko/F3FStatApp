package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import pl.f3f_klif.f3fstatapp.infrastructure.database.DatabaseRepository;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;

@Entity
public class Group {
    @Id
    public long id;

    public String PilotsListJson;

    public Group(){ }
    public Group(String pilotsListJson){
        PilotsListJson = pilotsListJson;
    }

    public Group(List<Pilot> pilots){
        PilotsListJson = new Gson().toJson(GetIds(pilots));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Pilot> getPilots() {

        try{
            ArrayList<Long> ids = new Gson().fromJson(PilotsListJson, new TypeToken<List<Long>>(){}.getType());
            List<Pilot> list = new ArrayList<>();
            List<Pilot> dbPilots = DatabaseRepository
                    .getEvent()
                    .getPilots();

            for (Long id : ids) {
                list.add(com.annimon.stream.Stream
                        .of(dbPilots)
                        .filter(i -> i.id == id)
                        .findFirst()
                        .get());
            }
            return list;

        }
        catch(Exception e){
        }

        return new ArrayList<>();
    }

    public List<Pilot> getPilots(List<Long> ids) {

        try{
            return com.annimon.stream.Stream
                    .of(DatabaseRepository
                            .getEvent()
                            .getPilots())
                    .filter(i -> ids.contains(i.id))
                    .toList();
        }
        catch(Exception e){
        }

        return new ArrayList<>();
    }

    public List<Pilot> reorderPilots(int oldPosition, int newPosition) {

        try{
            List<Long> ids =  new Gson().fromJson(PilotsListJson, new TypeToken<List<Long>>(){}.getType());
            Long id = ids.get(oldPosition);
            ids.remove(oldPosition);
            ids.add(newPosition, id);
            PilotsListJson = new Gson().toJson(ids);
            update();
            return getPilots(ids);
        }
        catch(Exception e){

        }

        return new ArrayList<>();
    }

    public List<Pilot> addPilot(int position, Pilot pilot) {

        try{
            List<Long> ids =  new Gson().fromJson(PilotsListJson, new TypeToken<List<Long>>(){}.getType());
            ids.add(position, pilot.id);
            PilotsListJson = new Gson().toJson(ids);
            update();
            return getPilots(ids);
        }
        catch(Exception e){

        }

        return new ArrayList<>();
    }

    public Pilot removePilot(int position) {

        try{
            List<Long> ids =  new Gson().fromJson(PilotsListJson, new TypeToken<List<Long>>(){}.getType());
            Long removedPilot = ids.get(position);
            ids.remove(position);
            PilotsListJson = new Gson().toJson(ids);
            update();
            return com.annimon.stream.Stream
                    .of(DatabaseRepository
                            .getEvent()
                            .getPilots())
                    .filter(i -> i.id == removedPilot)
                    .findFirst().get();
        }
        catch(Exception e){

        }

        return new Pilot();
    }

    private List<Long> GetIds(List<Pilot> pilots){
        List<Long> list = new ArrayList<>();
        for (Pilot pilot : pilots) {
            list.add(pilot.id);
        }
        return list;
    }


    private void update() {
        Box<Group> groupBox = ObjectBox.get().boxFor(Group.class);
        groupBox.put(this);
    }
}
