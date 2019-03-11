package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;
import io.objectbox.relation.ToMany;

@Entity
public class Round implements Parcelable {
    @Id
    public long id;

    public long roundId;

    public ToMany<Group> groups;

    @Convert(converter = RoleConverter.class, dbType = Integer.class)
    public RoundState state;

    public Round(){
        state = RoundState.NOT_STARTED;
        groups = new ToMany<>(this, Round_.groups);
    }

    public Round(long roundId, RoundState state) {
        this.roundId = roundId;
        this.state = state;
    }

    public Round(Parcel parcel) {
        this.roundId = parcel.readLong();
        this.state = RoundState.valueOf(parcel.readString());
/*        this.startingList = new ArrayList<>();
        parcel.readList(startingList, Pilot.class.getClassLoader());
        this.status = parcel.readString();*/
    }

    public long getRoundId() {
        return roundId;
    }

    public List<Group> getGroups() { return this.groups.subList(0, groups.size());}

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(roundId);
        parcel.writeString(state.name());
    }

    public static final Creator<Round> CREATOR = new Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel in) {
            return new Round(in);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };
}
