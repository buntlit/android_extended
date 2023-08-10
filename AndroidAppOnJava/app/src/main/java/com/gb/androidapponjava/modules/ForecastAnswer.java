package com.gb.androidapponjava.modules;

public class ForecastAnswer {
    private final int cityIndex;
    private final float temperature;
    private final float humidity;
    private final float pressure;
    private final float windSpeed;
    private final String weatherAttribute;
    private final boolean isResponse;
    private final int responseCode;

    public ForecastAnswer(int cityIndex, float temperature, float humidity, float pressure,
                          float windSpeed, String weatherAttribute, boolean isResponse, int responseCode) {
        this.cityIndex = cityIndex;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.weatherAttribute = weatherAttribute;
        this.isResponse = isResponse;
        this.responseCode = responseCode;
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
}
