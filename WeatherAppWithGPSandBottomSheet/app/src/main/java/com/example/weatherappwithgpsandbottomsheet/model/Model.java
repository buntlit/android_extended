package com.example.weatherappwithgpsandbottomsheet.model;

public class Model {
    private String buttonResponse;
    private double latitude;
    private double longitude;

    public Model(String buttonResponse, double latitude, double longitude) {
        this.buttonResponse = buttonResponse;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getButtonResponse() {
        return buttonResponse;
    }

    public void setButtonResponse(String buttonResponse) {
        this.buttonResponse = buttonResponse;
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
}
