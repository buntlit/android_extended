package com.gb.androidapponjava.modules;

public class WeatherRequest {
    private Main main;
    private Wind wind;
    private Weather[] weather;
    private int id;

    private long dt;

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public int getId() {
        return id;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public long getDt() {
        return dt;
    }
}
