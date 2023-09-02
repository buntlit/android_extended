package com.gb.androidapponjava.model;

import java.util.List;

public class WeatherHistoryModel {
    private final List<String> weatherHistoryList;

    public WeatherHistoryModel(List<String> weatherHistoryList) {
        this.weatherHistoryList = weatherHistoryList;
    }

    public List<String> getWeatherHistoryList() {
        return weatherHistoryList;
    }
}
