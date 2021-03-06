package pl.f3f_klif.f3fstatapp.infrastructure.database;

import android.content.Context;

import io.objectbox.BoxStore;
import pl.f3f_klif.f3fstatapp.infrastructure.database.entities.MyObjectBox;

public class ObjectBox {

    private static BoxStore boxStore;

    public static void init(Context context) {
        if(boxStore == null) {
            boxStore = MyObjectBox.builder()
                    .androidContext(context.getApplicationContext())
                    .build();
        }
    }



    public static BoxStore get() {
        return boxStore;
    }

    static void clear(Context context) {
        boxStore.close();
        boxStore.deleteAllFiles();
        boxStore = null;

        init(context);
    }
}
