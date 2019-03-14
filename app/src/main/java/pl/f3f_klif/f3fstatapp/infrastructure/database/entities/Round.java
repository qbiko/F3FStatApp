package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;
import io.objectbox.relation.ToMany;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;

@Entity
public class Round {
    @Id
    public long id;
    public ToMany<Group> groups;
    @Convert(converter = RoleConverter.class, dbType = Integer.class)
    public RoundState state;

    public Round(){
        state = RoundState.NOT_STARTED;
        groups = new ToMany<>(this, Round_.groups);
    }

    public long getId() {
        return id;
    }

    public void setState(RoundState state) {
        this.state = state;
        Box<Round> roundBox = ObjectBox.get().boxFor(Round.class);
        roundBox.put(this);
    }

    public List<Group> getGroups() { return this.groups;}

    public Group getGroup(long id) { return this.groups.getById(id);}

    public static class RoleConverter implements PropertyConverter<RoundState, Integer> {
        @Override
        public RoundState convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (RoundState role : RoundState.values()) {
                if (role.stateKey == databaseValue) {
                    return role;
                }
            }
            return RoundState.NOT_STARTED;
        }

        @Override
        public Integer convertToDatabaseValue(RoundState entityProperty) {
            return entityProperty == null ? null : entityProperty.stateKey;
        }
    }
}
