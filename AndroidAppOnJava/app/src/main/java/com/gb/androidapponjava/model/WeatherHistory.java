package com.gb.androidapponjava.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_history_table")
public class WeatherHistory {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "temperature")
    private float temperature;

    public WeatherHistory(long date, String city, float temperature) {
        this.date = date;
        this.city = city;
        this.temperature = temperature;
    }

    public long getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public float getTemperature() {
        return temperature;
    }
}
