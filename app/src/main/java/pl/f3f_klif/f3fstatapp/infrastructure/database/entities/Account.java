package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import pl.f3f_klif.f3fstatapp.infrastructure.database.ObjectBox;

@Entity
public class Account {

    @Id
    long id;
    private String mail;
    private String password;

    private boolean windDir;
    private boolean windSpeed;

    public Account() { }

    public Account(String mail, String password, boolean windDir, boolean windSpeed) {
        this.mail = mail;
        this.password = password;
        this.windDir = windDir;
        this.windSpeed = windSpeed;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public boolean isWindDir() {
        return windDir;
    }

    public void setWindDir(boolean windDir) {
        this.windDir = windDir;
        update();
    }

    public boolean isWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(boolean windSpeed) {
        this.windSpeed = windSpeed;
        update();
    }

    private void update() {
        ObjectBox.get().boxFor(Account.class).put(this);
    }
}
