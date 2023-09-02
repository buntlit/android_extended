package com.gb.androidapponjava.model;

public class WeatherParameterModel {
    private final String stringWeatherParameter;
    private final String weatherAttribute;

    public WeatherParameterModel(String stringWeatherParameter, String weatherAttribute) {
        this.stringWeatherParameter = stringWeatherParameter;
        this.weatherAttribute = weatherAttribute;
    }

    public String getStringWeatherParameter() {
        return stringWeatherParameter;
    }

    public String getWeatherAttribute() {
        return weatherAttribute;
    }
}
