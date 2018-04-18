package com.example.m1.tripproj.Models;

import android.os.Parcelable;

import java.io.Serializable;

public class Trip implements Serializable{

    private int id ;
    private String TripName ;
    private String StartPoint ;
    private String EndPoint ;
    private String date ;
    private String time ;
    private String direction ;
    private String notes ;
    private double startLongitude ;
    private double startLatitude;
    private double endLongitude ;
    private double  endLatitude ;
    private String status = "Upcoming" ;
    private int userId;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTripName() {
        return TripName;
    }
    public void setTripName(String tripName) {
        TripName = tripName;
    }

    public String getStartPoint() {
        return StartPoint;
    }
    public void setStartPoint(String startPoint) {
        StartPoint = startPoint;
    }

    public String getEndPoint() {
        return EndPoint;
    }
    public void setEndPoint(String endPoint) {
        EndPoint = endPoint;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getStartLongitude() { return startLongitude; }
    public void setStartLongitude(double startLongitude) { this.startLongitude = startLongitude; }

    public double getStartLatitude() { return startLatitude; }
    public void setStartLatitude(double startLatitude) { this.startLatitude = startLatitude; }

    public double getEndLongitude() { return endLongitude; }
    public void setEndLongitude(double endLongitude) { this.endLongitude = endLongitude; }

    public double getEndLatitude() { return endLatitude; }
    public void setEndLatitude(double endLatitude) { this.endLatitude = endLatitude; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
