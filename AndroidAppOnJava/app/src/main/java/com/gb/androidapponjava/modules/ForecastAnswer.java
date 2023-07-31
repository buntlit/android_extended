package com.gb.androidapponjava.modules;

public class ForecastAnswer {
    private final int cityIndex;
    private final float temperature;
    private final float humidity;
    private final float pressure;
    private final float windSpeed;
    private final String weatherAttribute;

    public ForecastAnswer(int cityIndex, float temperature, float humidity, float pressure, float windSpeed, String weatherAttribute) {
        this.cityIndex = cityIndex;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.weatherAttribute = weatherAttribute;
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
}
