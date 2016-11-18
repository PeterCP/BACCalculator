package dev.petercp.baccalculator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DriverEntry {

    static class Table {
        static final String
                TABLE_NAME = "drivers",

                ID = "id",
                LICENSE = "license",
                NAME = "name",
                FIRST_SURNAME = "first_surname",
                LAST_SURNAME = "last_surname",
                GENDER = "gender",
                PLATE = "plate",
                DATE = "date",
                BAC = "bac";

        static final String[] PROJECTION = {
                ID, LICENSE, NAME, FIRST_SURNAME, LAST_SURNAME, GENDER, PLATE, DATE, BAC,
        };

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                LICENSE + " INTEGER NOT NULL, " +
                NAME + " TEXT NOT NULL, " +
                FIRST_SURNAME + " TEXT NOT NULL, " +
                LAST_SURNAME + " TEXT NOT NULL, " +
                GENDER + " TEXT NOT NULL, " +
                PLATE + " TEXT NOT NULL, " +
                DATE + " TEXT NOT NULL, " +
                BAC + " DOUBLE NOT NULL" +
        ")";

        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault());

    private Long id;
    private int license;
    private String name, firstSurname, lastSurname, gender, plate;
    private Date date;
    private double bac;

    public DriverEntry(Cursor cursor) {
        id = cursor.getLong(0);
        license = cursor.getInt(1);
        name = cursor.getString(2);
        firstSurname = cursor.getString(3);
        lastSurname = cursor.getString(4);
        gender = cursor.getString(5);
        plate = cursor.getString(6);
        try {
            date = FORMAT.parse(cursor.getString(7));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        bac = cursor.getDouble(8);
    }

    public DriverEntry(int license, String name, String firstSurname, String lastSurname,
                       String gender, String plate, Date date, double bac) {
        id = null;
        this.license = license;
        this.name = name;
        this.firstSurname = firstSurname;
        this.lastSurname = lastSurname;
        this.gender = gender;
        this.plate = plate;
        this.date = date;
        this.bac = bac;
    }

    public boolean save(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Table.LICENSE, license);
        values.put(Table.NAME, name);
        values.put(Table.FIRST_SURNAME, firstSurname);
        values.put(Table.LAST_SURNAME, lastSurname);
        values.put(Table.GENDER, gender);
        values.put(Table.PLATE, plate);
        values.put(Table.DATE, FORMAT.format(date));
        values.put(Table.BAC, bac);

        if (id != null) {
            String[] whereArgs = { String.valueOf(license) };
            int rowsAffected = db.update(Table.TABLE_NAME, values, Table.LICENSE + " = ?", whereArgs);
            return rowsAffected == 1;
        } else {
            long id = db.insert(Table.TABLE_NAME, null, values);
            return license == id;
        }
    }

    public long getId() {
        return id;
    }

    public int getLicense() {
        return license;
    }

    public String getName() {
        return name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public String getLastSurname() {
        return lastSurname;
    }

    public String getGender() {
        return gender;
    }

    public String getPlate() {
        return plate;
    }

    public Date getDate() {
        return date;
    }

    public double getBac() {
        return bac;
    }
}
