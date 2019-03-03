package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
@Entity
public class Pilot {
    @Id
    long Id;

    public long F3fId;
    public String FirstName;
    public String LastName;

    public Pilot(){ }
    public Pilot(long f3fId, String firstName, String lastName){
        F3fId = f3fId;
        FirstName = firstName;
        LastName = lastName;
    }
}