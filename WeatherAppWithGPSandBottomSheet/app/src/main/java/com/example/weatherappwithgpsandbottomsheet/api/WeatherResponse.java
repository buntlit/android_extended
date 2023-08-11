package com.example.weatherappwithgpsandbottomsheet.api;

public class WeatherResponse {
    private Location location;
    private Current current;

    public Location getLocation() {
        return location;
    }

    public Current getCurrent() {
        return current;
    }
}
