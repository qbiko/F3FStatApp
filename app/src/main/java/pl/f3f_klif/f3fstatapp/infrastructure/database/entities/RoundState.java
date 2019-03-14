package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import pl.f3f_klif.f3fstatapp.R;

public enum RoundState {
    NOT_STARTED(R.string.not_started),
    STARTED(R.string.started),
    FINISHED(R.string.finished),
    CANCELED(R.string.canceled);

    final int stateKey;

    RoundState(int stateKey) {
        this.stateKey = stateKey;
    }

    public int getStateKey() {
        return stateKey;
    }
}
