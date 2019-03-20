package pl.f3f_klif.f3fstatapp.infrastructure.database;

import android.content.Context;

import java.util.List;

import io.objectbox.Box;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Account;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Pilot;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Round;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.Event;

public class DatabaseRepository {
    private static Box<Event> eventBox;
    private static Box<Account> accountBox;
    private static Event event;
    private static Account account;

    private static void initRounds() {
        Box<Round> roundBox = ObjectBox.get().boxFor(Round.class);
        List<Round> rounds = roundBox.getAll();
        event.fillRounds(rounds);
    }

    public static void initNew(int f3fId, int minGroupAmount, String[] lines, Context context) {
        ObjectBox.clear(context);
        eventBox = ObjectBox.get().boxFor(Event.class);
        accountBox = ObjectBox.get().boxFor(Account.class);
        createEvent(f3fId, minGroupAmount, lines);
        event = eventBox.query().build().findFirst();
    }

    public static Event getEvent() {
        return event;
    }

    public static Account getAccount() {
        return account;
    }

    private static Long createEvent(int f3fId, int minGroupAmount, String[] lines){
        event = new Event(f3fId, minGroupAmount, lines);
        return eventBox.put(event);
    }

    public static Account createAccount(String mail, String password){
        account = new Account(mail, password);
        accountBox.put(account);
        return account;
    }

    public static boolean restoreAndInit() {
        boolean result = true;

        accountBox = ObjectBox.get().boxFor(Account.class);
        eventBox = ObjectBox.get().boxFor(Event.class);


        if(account == null) {
            if(!accountBox.isEmpty()) {
                account = accountBox.getAll().get(0);
            }
            else {
                result = false;
            }
        }

        if(event == null) {
            if(!eventBox.isEmpty()) {
                event = eventBox.getAll().get(0);
                DatabaseRepository.initRounds();
            }
            else {
                result = false;
            }
        }

        return result;
    }

    public static void cleanAccount() {
        account = null;
        accountBox.removeAll();
    }
}
