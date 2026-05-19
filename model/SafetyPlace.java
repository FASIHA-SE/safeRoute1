package com.example.saferoute.model;

public class SafetyPlace {

    String name, type, lat, lon;

    public SafetyPlace(String name, String type, String lat, String lon) {
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}