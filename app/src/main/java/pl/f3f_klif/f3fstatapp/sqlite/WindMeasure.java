package pl.f3f_klif.f3fstatapp.sqlite;

import android.database.Cursor;

import java.sql.Timestamp;

import static pl.f3f_klif.f3fstatapp.sqlite.WindSQLiteDbHandler.KEY_EVENT_ID;
import static pl.f3f_klif.f3fstatapp.sqlite.WindSQLiteDbHandler.KEY_ID;
import static pl.f3f_klif.f3fstatapp.sqlite.WindSQLiteDbHandler.KEY_MEASURE_TIME;
import static pl.f3f_klif.f3fstatapp.sqlite.WindSQLiteDbHandler.KEY_PILOT_ID;
import static pl.f3f_klif.f3fstatapp.sqlite.WindSQLiteDbHandler.KEY_ROUND_ID;
import static pl.f3f_klif.f3fstatapp.sqlite.WindSQLiteDbHandler.KEY_WIND_DIRECTION;
import static pl.f3f_klif.f3fstatapp.sqlite.WindSQLiteDbHandler.KEY_WIND_SPEED;

public class WindMeasure {
    private int id;
    private Timestamp measureTime;
    private float windSpeed;
    private int windDirection;
    private int pilotId;
    private int eventId;
    private int roundId;

    public WindMeasure(Timestamp measureTime, float windSpeed, int windDirection, int eventId,
                       int roundId) {
        this.measureTime = measureTime;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.eventId = eventId;
        this.roundId = roundId;
    }

    public WindMeasure(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
        String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MEASURE_TIME));
        this.measureTime = new Timestamp(Long.parseLong(timestamp));
        this.windSpeed = cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_WIND_SPEED));
        this.windDirection = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_WIND_DIRECTION));
        this.pilotId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PILOT_ID));
        this.eventId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EVENT_ID));
        this.roundId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ROUND_ID));
    }

    public int getId() {
        return id;
    }

    public Timestamp getMeasureTime() {
        return measureTime;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public int getPilotId() {
        return pilotId;
    }

    public int getEventId() {
        return eventId;
    }

    public int getRoundId() {
        return roundId;
    }
}
