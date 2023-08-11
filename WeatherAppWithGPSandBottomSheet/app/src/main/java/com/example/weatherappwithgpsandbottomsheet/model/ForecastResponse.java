package com.example.weatherappwithgpsandbottomsheet.model;

public class ForecastResponse {
    private final String cityName;
    private final float temperature;
    private final String condition;
    private final float feelsLike;
    private final float humidity;
    private final float windSpeed;

    public ForecastResponse(String cityName, float temperature, String condition, float feelsLike, float humidity, float windSpeed) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.condition = condition;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getCityName() {
        return cityName;
    }

    public float getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }
}
