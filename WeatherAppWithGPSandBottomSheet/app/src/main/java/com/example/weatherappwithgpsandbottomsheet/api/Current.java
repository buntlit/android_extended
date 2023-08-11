package com.example.weatherappwithgpsandbottomsheet.api;

public class Current {
    private float temp_c;
    private Condition condition;
    private float humidity;
    private float wind_kph;
    private float feelslike_c;

    public float getTemp_c() {
        return temp_c;
    }

    public Condition getCondition() {
        return condition;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getWind_kph() {
        return wind_kph;
    }

    public float getFeelslike_c() {
        return feelslike_c;
    }
}
