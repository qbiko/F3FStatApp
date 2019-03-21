package pl.f3f_klif.f3fstatapp.groups.infrastructure.models;

import com.annimon.stream.Optional;

public class PilotWithOrder{
    public long id;
    public Optional<Float> time;
    public int order;

    public PilotWithOrder(long id, Optional<Float> time, int order){
        this.id = id;
        this.time = time;
        this.order = order;
    }
}
