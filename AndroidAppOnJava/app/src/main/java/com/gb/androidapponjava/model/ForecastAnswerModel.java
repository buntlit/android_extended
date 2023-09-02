package com.gb.androidapponjava.model;

public class ForecastAnswerModel {
    private final int cityIndex;
    private final float temperature;
    private final float humidity;
    private final float pressure;
    private final float windSpeed;
    private final String weatherAttribute;
    private final boolean isResponse;
    private final int responseCode;
    private final String iconValue;

    public ForecastAnswerModel(int cityIndex, float temperature, float humidity, float pressure,
                               float windSpeed, String weatherAttribute, boolean isResponse,
                               int responseCode, String iconValue) {
        this.cityIndex = cityIndex;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.weatherAttribute = weatherAttribute;
        this.isResponse = isResponse;
        this.responseCode = responseCode;
        this.iconValue = iconValue;
    }

    public int getCityIndex() {
        return cityIndex;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherAttribute() {
        return weatherAttribute;
    }

    public boolean isResponse() {
        return isResponse;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getIconValue() {
        return iconValue;
    }
}
