package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
@Entity
public class Pilot {
    @Id
    long Id;

    public int F3fId;

    public Pilot(){ }
}