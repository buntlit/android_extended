package com.gb.androidapponjava.modules;

public class WeatherRequest {
    private Main main;
    private Wind wind;
    private Weather[] weather;
    private int id;

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
}
