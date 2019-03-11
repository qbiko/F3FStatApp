package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

public enum RoundState {
    NotStarted(0),
    Started(1),
    Finished(2),
    Canceled(3);

    final int id;

    RoundState(int id) {
        this.id = id;
    }
}
