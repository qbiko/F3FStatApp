package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;

@Entity
public class Round {
    @Id
    public long Id;

    public ToMany<Group> Groups;

    @Convert(converter = RoleConverter.class, dbType = Integer.class)
    public RoundState State;

    public Round(){
        State = RoundState.NotStarted;
        Groups = new ToMany<>(this, Round_.Groups);
    }

    public List<Group> getGroups() { return this.Groups.subList(0, Groups.size());}

    public static class RoleConverter implements PropertyConverter<RoundState, Integer> {
        @Override
        public RoundState convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (RoundState role : RoundState.values()) {
                if (role.id == databaseValue) {
                    return role;
                }
            }
            return RoundState.NotStarted;
        }

        @Override
        public Integer convertToDatabaseValue(RoundState entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }
}
