package com.gb.androidapponjava;

import java.io.Serializable;
import java.util.List;

public class Parcel implements Serializable {
    private String cityName;
    private int cityIndex;
    private List cities;

    public String getCityName() {
        return cityName;
    }

    public int getCityIndex() {
        return cityIndex;
    }

    public List getCities() {
        return cities;
    }

    public void setCities(List cities) {
        this.cities = cities;
    }

    public Parcel(String cityName, int cityIndex, List cities) {
        this.cityName = cityName;
        this.cityIndex = cityIndex;
        this.cities = cities;
    }
}
