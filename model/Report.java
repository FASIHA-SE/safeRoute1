package com.example.saferoute.model;

public class Report {

    private String location;
    private String description;
    private long timestamp;

    public Report() {}

    public Report(String location, String description, long timestamp) {
        this.location = location;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }
}