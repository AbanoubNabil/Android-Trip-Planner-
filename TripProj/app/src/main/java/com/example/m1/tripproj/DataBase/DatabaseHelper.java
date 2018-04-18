package com.example.m1.tripproj.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TripPlanner.db";
    static final String TABLE_USER = "user";
    static final String TABLE_TRIP = "Trip";

    static final String[] user_columns = {"user_id", "user_name", "user_email", "user_password"};
    static final String[] trip_columns = {"Trip_id", "Trip_name", "Start_point", "End_point", "Trip_Date", "Trip_Time",
            "Trip_Direction", "Trip_Notes", "startLatitude", "startLongitude" ,"endLatitude" ,"endLongitude" , "status", "userId"};

    // create tables
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + user_columns[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," + user_columns[1] + " TEXT,"
            + user_columns[2] + " TEXT," + user_columns[3] + " TEXT" + ")";

    private String CREATE_TRIP_TABLE = "CREATE TABLE " + TABLE_TRIP + "(" + trip_columns[0] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + trip_columns[1] + " TEXT," + trip_columns[2] +" TEXT," + trip_columns[3] + " TEXT," + trip_columns[4]
            + " TEXT," + trip_columns[5] + " TEXT," + trip_columns[6] + " TEXT," + trip_columns[7] + " TEXT," + trip_columns[8] +
            " REAL," + trip_columns[9]  + " REAL," + trip_columns[10]  + " REAL," + trip_columns[11]  + " REAL," +
            trip_columns[12]  + " TEXT," + trip_columns[13]  + " REAL" + ")";

    // drop tables
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER ;
    private String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRIP ;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TRIP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_TRIP_TABLE);
        onCreate(db);
    }
}
