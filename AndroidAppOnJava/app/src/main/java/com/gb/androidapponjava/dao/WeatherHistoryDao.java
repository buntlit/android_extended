package com.gb.androidapponjava.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gb.androidapponjava.model.WeatherHistory;

import java.util.List;

@Dao
public
interface WeatherHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertWeatherHistory(WeatherHistory weatherHistory);

    @Query("DELETE FROM weather_history_table")
    void deleteAll();

    @Query("SELECT * FROM weather_history_table ORDER BY date ASC")
    LiveData<List<WeatherHistory>> getFiltratedListByDate();

    @Query("SELECT * FROM weather_history_table ORDER BY city ASC")
    LiveData<List<WeatherHistory>> getFiltratedListByCity();

    @Query("SELECT * FROM weather_history_table ORDER BY temperature ASC")
    LiveData<List<WeatherHistory>> getFiltratedListByTemperature();
}
