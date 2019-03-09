package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
@Entity
public class Pilot {
    @Id
    public long Id;

    public long F3fId;
    public String FirstName;
    public String LastName;
    public int FlightNumber;
    public float FlightTimeResult = 0f;

    public Pilot(){ }
    public Pilot(long f3fId, String firstName, String lastName){
        F3fId = f3fId;
        FirstName = firstName;
        LastName = lastName;
    }
}