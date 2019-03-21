package pl.f3f_klif.f3fstatapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;


public class WindSQLiteDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wind_measurement_f3f.sqlite";
    private static final String TABLE_NAME = "wind";
    static final String KEY_ID = "id";
    static final String KEY_MEASURE_TIME = "measureTime";
    static final String KEY_WIND_SPEED = "windSpeed";
    static final String KEY_WIND_DIRECTION = "windDirection";
    static final String KEY_PILOT_ID = "pilotId";
    static final String KEY_EVENT_ID = "eventId";
    static final String KEY_ROUND_ID = "roundId";
    static final String KEY_GROUP_ID = "groupId";
    private static final String[] COLUMNS = { KEY_ID, KEY_MEASURE_TIME, KEY_WIND_SPEED,
            KEY_WIND_DIRECTION, KEY_PILOT_ID, KEY_EVENT_ID, KEY_ROUND_ID, KEY_GROUP_ID };

    static final DateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WindSQLiteDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME +
                " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_MEASURE_TIME + " TEXT, " +
                KEY_WIND_SPEED + " REAL, " +
                KEY_WIND_DIRECTION + " INTEGER, " +
                KEY_PILOT_ID + " INTEGER, " +
                KEY_EVENT_ID + " INTEGER, " +
                KEY_ROUND_ID + " INTEGER )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public List<WindMeasure> allWindMeasures() {
        List<WindMeasure> players = new LinkedList<>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        WindMeasure windMeasure;

        if (cursor.moveToFirst()) {
            do {
                windMeasure = new WindMeasure(cursor);
                players.add(windMeasure);
            } while (cursor.moveToNext());
        }

        return players;
    }

    public void addWindMeasure(WindMeasure windMeasure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String dateTime = ISO_8601_FORMAT.format(windMeasure.getMeasureTime());
        values.put(KEY_MEASURE_TIME, dateTime);
        values.put(KEY_WIND_SPEED, windMeasure.getWindSpeed());
        values.put(KEY_WIND_DIRECTION, windMeasure.getWindDirection());
        values.put(KEY_PILOT_ID, windMeasure.getPilotId());
        values.put(KEY_EVENT_ID, windMeasure.getEventId());
        values.put(KEY_ROUND_ID, windMeasure.getRoundId());

        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public void addWindMeasures(List<WindMeasure> windMeasures, int pilotId) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (WindMeasure windMeasure : windMeasures) {
            ContentValues values = new ContentValues();

            String dateTime = ISO_8601_FORMAT.format(windMeasure.getMeasureTime());
            values.put(KEY_MEASURE_TIME, dateTime);
            values.put(KEY_WIND_SPEED, windMeasure.getWindSpeed());
            values.put(KEY_WIND_DIRECTION, windMeasure.getWindDirection());
            values.put(KEY_PILOT_ID, pilotId);
            values.put(KEY_EVENT_ID, windMeasure.getEventId());
            values.put(KEY_ROUND_ID, windMeasure.getRoundId());

            db.insert(TABLE_NAME,null, values);
        }

        db.close();
    }

}

