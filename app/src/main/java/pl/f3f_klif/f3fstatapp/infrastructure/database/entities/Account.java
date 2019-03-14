package pl.f3f_klif.f3fstatapp.infrastructure.database.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Account {

    @Id
    long id;
    private String mail;
    private String password;

    public Account() { }

    public Account(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}
