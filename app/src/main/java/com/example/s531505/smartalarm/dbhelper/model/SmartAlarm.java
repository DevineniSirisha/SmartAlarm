package com.example.s531505.smartalarm.dbhelper.model;



public class SmartAlarm {
    public static final String TABLE_NAME = "smartalarm";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ALARM_NAME = "alarm_name";
    public static final String COLUMN_ALARM_NOTES = "alarm_notes";
    public static final String COLUMN_RADIUS = "radius";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id, radius;
    private double latitude, longitude;
    private String name, notes, destination;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ALARM_NAME + " TEXT,"
                    + COLUMN_ALARM_NOTES + " TEXT,"
                    + COLUMN_RADIUS + " INTEGER,"
                    + COLUMN_DESTINATION + " TEXT,"
                    + COLUMN_LATITUDE + " REAL,"
                    + COLUMN_LONGITUDE + " REAL,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public SmartAlarm() {
    }

    public SmartAlarm(int id, String name, String notes, int radius, String destination, double latitude, double longitude, String timestamp) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.radius = radius;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
