package pl.f3f_klif.f3fstatapp.groups.services.models;

import java.util.Optional;

public class PilotWithResult{
    public long id;
    public Optional<Float> time;

    public PilotWithResult(long id, Optional<Float> time){
        this.id = id;
        this.time = time;
    }
}
