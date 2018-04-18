package com.example.m1.tripproj.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.m1.tripproj.Models.Trip;
import com.example.m1.tripproj.Models.User;

import java.util.ArrayList;

public class DatabaseAdapter {

    Context context;

    public DatabaseAdapter(Context context){
        this.context = context;
    }

    public ContentValues putUserValues(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.user_columns[1], user.getName());
        values.put(DatabaseHelper.user_columns[2], user.getEmail());
        values.put(DatabaseHelper.user_columns[3], user.getPassword());
        return values;
    }

    public void addUser(User user) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putUserValues(user);
        db.insert(DatabaseHelper.TABLE_USER, null, values);

        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, DatabaseHelper.user_columns, null, null, null, null, null);
        cursor.moveToFirst();
        user.setId(Integer.parseInt(cursor.getString(0)));
        Log.i("from DB: ", user.getId() + "");
        db.close();
    }

    public ContentValues putTripValues(Trip trip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.trip_columns[1] , trip.getTripName());
        values.put(DatabaseHelper.trip_columns[2] , trip.getStartPoint());
        values.put(DatabaseHelper.trip_columns[3] , trip.getEndPoint());
        values.put(DatabaseHelper.trip_columns[4] , trip.getDate());
        values.put(DatabaseHelper.trip_columns[5] , trip.getTime());
        values.put(DatabaseHelper.trip_columns[6] , trip.getDirection());
        values.put(DatabaseHelper.trip_columns[7] , trip.getNotes());
        values.put(DatabaseHelper.trip_columns[8] , trip.getStartLatitude());
        values.put(DatabaseHelper.trip_columns[9] , trip.getStartLongitude());
        values.put(DatabaseHelper.trip_columns[10] , trip.getEndLatitude());
        values.put(DatabaseHelper.trip_columns[11] , trip.getEndLongitude());
        values.put(DatabaseHelper.trip_columns[12] , trip.getStatus());
        values.put(DatabaseHelper.trip_columns[13] , trip.getUserId());
        return values;
    }

    public void addTrip(Trip trip){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putTripValues(trip);
        db.insert(DatabaseHelper.TABLE_TRIP ,null , values);
        db.close();
    }

    public void setTripStatus(Trip trip, String tripStatus) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.trip_columns[12] , tripStatus);
        db.update(DatabaseHelper.TABLE_TRIP, values, DatabaseHelper.trip_columns[0] + " = ?", new String[]{String.valueOf(trip.getId())});
        db.close();
    }

    public void updateUserData(User user) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putUserValues(user);
        db.update(DatabaseHelper.TABLE_USER, values, DatabaseHelper.user_columns[0] + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void editTrip(Trip trip) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = putTripValues(trip);
        db.update(DatabaseHelper.TABLE_TRIP, values, DatabaseHelper.trip_columns[0] + " = ?", new String[]{String.valueOf(trip.getId())});
        db.close();
    }

    public void deleteUser(User user) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_USER, DatabaseHelper.user_columns[0] + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void deleteTrip(Trip trip) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_TRIP, DatabaseHelper.trip_columns[0] + " = ?", new String[]{String.valueOf(trip.getId())});
        db.close();
    }

    public boolean checkUser(String email) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {DatabaseHelper.user_columns[0]}; // user_id
        String selectionCriteria = DatabaseHelper.user_columns[2] + " = ?";  // email
        String[] selectionArgs = {email};

        /*query user table with conditions (cols to return, cols for where clause, values for where clause, group the rows,
         filter by row groups, sort order) */
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, columns, selectionCriteria, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {return true;}
        return false;
    }

    public User checkUser(String email, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {DatabaseHelper.user_columns[0]}; // user_id
        String selectionCriteria = DatabaseHelper.user_columns[2] + " = ?" + " AND " + DatabaseHelper.user_columns[3] + " = ?"; // email, pass
        String[] selectionArgs = {email, password};

        /*query user table with conditions (cols to return, cols for where clause, values for where clause, group the rows,
         filter by row groups, sort order) */

        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, columns, selectionCriteria, selectionArgs, null, null, null);
        boolean cur = cursor.moveToFirst();
        User user = null;
        if(cur) {
            user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setId(cursor.getInt(0));
        }

        cursor.close();
        db.close();

        return user;
    }

    public int selectUserId(String email, String pass) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectionCriteria = DatabaseHelper.user_columns[2] + " = ? " + " AND " + DatabaseHelper.user_columns[3] + " = ? ";
        String[] selectionArgs = {email, pass};
        String[] cols = {DatabaseHelper.user_columns[0]};
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, cols, selectionCriteria, selectionArgs, null, null, null);
//        cursor.moveToFirst();
        Log.i("test","inside database "+cursor.moveToFirst());
        int userId = cursor.getInt(0);
        cursor.close();
        db.close();

        return userId;
    }

    public ArrayList<Trip> getUpcomingTrips(int userId) {
        Log.i("test","hhhhhhhhhhhhhh" + userId);
        String sortOrder = DatabaseHelper.trip_columns[5] + " ASC";  // trip_time
        ArrayList<Trip> TripList = new ArrayList<Trip>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectionCriteria = DatabaseHelper.trip_columns[13] + " = ?";
        String[] selectionArgs = {userId + ""};

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRIP, DatabaseHelper.trip_columns,
                selectionCriteria, selectionArgs, null, null, sortOrder);

        // Traversing through all trip rows and adding to a list
        while (cursor.moveToNext()){
            if(cursor.getString(12).equalsIgnoreCase("upcoming")) {
                Trip trip = new Trip();
                trip.setId(Integer.parseInt(cursor.getString(0)));
                trip.setTripName(cursor.getString(1));
                trip.setStartPoint(cursor.getString(2));
                trip.setEndPoint(cursor.getString(3));
                trip.setDate(cursor.getString(4));
                trip.setTime(cursor.getString(5));
                trip.setDirection(cursor.getString(6));
                trip.setNotes(cursor.getString(7));
                trip.setStartLatitude(cursor.getDouble(8));
                trip.setStartLongitude(cursor.getDouble(9));
                trip.setEndLatitude(cursor.getDouble(10));
                trip.setEndLongitude(cursor.getDouble(11));
                trip.setStatus(cursor.getString(12));
                trip.setUserId(cursor.getInt(13));
                // Adding trip record to the list
                TripList.add(trip);
            }
        }
        cursor.close();
        db.close();
        return TripList;
    }

    public ArrayList<Trip> getHistoryTrips(int userId) {
        String sortOrder = DatabaseHelper.trip_columns[5] + " ASC";  // trip_time
        ArrayList<Trip> trips = new ArrayList<Trip>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectionCriteria = DatabaseHelper.trip_columns[13] + " = ?";
        String[] selectionArgs = {userId + ""};

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRIP, DatabaseHelper.trip_columns,
                selectionCriteria, selectionArgs, null, null, sortOrder);

        while (cursor.moveToNext()){
            if(cursor.getString(12).equalsIgnoreCase("done")) {
                Trip trip = new Trip();
                trip.setId(Integer.parseInt(cursor.getString(0)));
                trip.setTripName(cursor.getString(1));
                trip.setStartPoint(cursor.getString(2));
                trip.setEndPoint(cursor.getString(3));
                trip.setDate(cursor.getString(4));
                trip.setTime(cursor.getString(5));
                trip.setDirection(cursor.getString(6));
                trip.setNotes(cursor.getString(7));
                trip.setStartLatitude(cursor.getDouble(8));
                trip.setStartLongitude(cursor.getDouble(9));
                trip.setEndLatitude(cursor.getDouble(10));
                trip.setEndLongitude(cursor.getDouble(11));
                trip.setStatus(cursor.getString(12));
                trip.setUserId(cursor.getInt(13));
                trips.add(trip);
            }
        }
        cursor.close();
        db.close();
        return trips;
    }

    public ArrayList<Trip> getCancelledTrips(int userId) {
        String sortOrder = DatabaseHelper.trip_columns[5] + " ASC";  // trip_time
        ArrayList<Trip> trips = new ArrayList<Trip>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectionCriteria = DatabaseHelper.trip_columns[13] + " = ?";
        String[] selectionArgs = {userId + ""};

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRIP, DatabaseHelper.trip_columns,
                selectionCriteria, selectionArgs, null, null, sortOrder);

        while (cursor.moveToNext()){
            if(cursor.getString(12).equalsIgnoreCase("cancelled")) {
                Trip trip = new Trip();
                trip.setId(Integer.parseInt(cursor.getString(0)));
                trip.setTripName(cursor.getString(1));
                trip.setStartPoint(cursor.getString(2));
                trip.setEndPoint(cursor.getString(3));
                trip.setDate(cursor.getString(4));
                trip.setTime(cursor.getString(5));
                trip.setDirection(cursor.getString(6));
                trip.setNotes(cursor.getString(7));
                trip.setStartLatitude(cursor.getDouble(8));
                trip.setStartLongitude(cursor.getDouble(9));
                trip.setEndLatitude(cursor.getDouble(10));
                trip.setEndLongitude(cursor.getDouble(11));
                trip.setStatus(cursor.getString(12));
                trip.setUserId(cursor.getInt(13));
                trips.add(trip);
            }
        }
        cursor.close();
        db.close();
        return trips;
    }

    public int getTripId(Trip trip) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectionCriteria = DatabaseHelper.trip_columns[1] + " = ?" + " AND " +
                DatabaseHelper.trip_columns[2] + " = ?" + " AND " + DatabaseHelper.trip_columns[3] +
                " = ?" + " AND " + DatabaseHelper.trip_columns[4] + " = ?" + " AND " + DatabaseHelper.trip_columns[5] + " = ?";
        String[] selectionArgs = {trip.getTripName(), trip.getStartPoint(), trip.getEndPoint(), trip.getDate(), trip.getTime()};

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRIP, DatabaseHelper.trip_columns, selectionCriteria, selectionArgs, null, null, null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        Log.i("idFromDBAdapter: ", id + "");
        cursor.close();
        db.close();
        return id;
    }

    public Trip getTrip(int i) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] selectionArgs = {i + ""};
        String selectionCriteria = DatabaseHelper.trip_columns[0] + " = ? ";
        Cursor cursor = db.query(DatabaseHelper.TABLE_TRIP, DatabaseHelper.trip_columns, selectionCriteria, selectionArgs, null, null, null);
        cursor.moveToFirst();
        Trip trip = new Trip();
        trip.setId(i);
        trip.setTripName(cursor.getString(1));
        trip.setStartPoint(cursor.getString(2));
        trip.setEndPoint(cursor.getString(3));
        trip.setDate(cursor.getString(4));
        trip.setTime(cursor.getString(5));
        trip.setDirection(cursor.getString(6));
        trip.setNotes(cursor.getString(7));
        trip.setStartLatitude(cursor.getDouble(8));
        trip.setStartLongitude(cursor.getDouble(9));
        trip.setEndLatitude(cursor.getDouble(10));
        trip.setEndLongitude(cursor.getDouble(11));
        trip.setStatus(cursor.getString(12));
        trip.setUserId(cursor.getInt(13));
        cursor.close();
        db.close();
        return trip;
    }

}

