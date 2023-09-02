package com.gb.androidapponjava.model;

import java.util.List;

public class CitiesModel {
    private final String cityName;
    private final List<String> cities;

    public CitiesModel(String cityName, List<String> cities) {
        this.cityName = cityName;
        this.cities = cities;
    }

    public String getCityName() {
        return cityName;
    }

    public List<String> getCities() {
        return cities;
    }
}
