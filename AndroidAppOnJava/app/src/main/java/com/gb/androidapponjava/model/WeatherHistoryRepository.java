package com.gb.androidapponjava.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gb.androidapponjava.dao.WeatherHistoryDao;
import com.gb.androidapponjava.db.WeatherHistoryRoomDatabase;

import java.util.List;

public class WeatherHistoryRepository {
    private final WeatherHistoryDao weatherHistoryDao;
    private LiveData<List<WeatherHistory>> allWeatherHistory;

    public WeatherHistoryRepository(Application application) {
        WeatherHistoryRoomDatabase db = WeatherHistoryRoomDatabase.getDatabase(application);
        weatherHistoryDao = db.weatherHistoryDao();
        allWeatherHistory = weatherHistoryDao.getFiltratedListByDate();
    }

    public void insert(WeatherHistory weatherHistory) {
        WeatherHistoryRoomDatabase.databaseWriteExecutor.execute(() ->
                weatherHistoryDao.insertWeatherHistory(weatherHistory)
        );
    }

    public void deleteAll() {
        WeatherHistoryRoomDatabase.databaseWriteExecutor.execute(weatherHistoryDao::deleteAll);
    }

    public LiveData<List<WeatherHistory>> getWeatherHistory() {
        return weatherHistoryDao.getFiltratedListByDate();
    }

    public LiveData<List<WeatherHistory>> getWeatherHistoryFilteredByDate() {
        return weatherHistoryDao.getFiltratedListByDate();
    }

    public LiveData<List<WeatherHistory>> getWeatherHistoryFilteredByCity() {
        return weatherHistoryDao.getFiltratedListByCity();
    }


    public LiveData<List<WeatherHistory>> getWeatherHistoryFilteredByTemperature() {
        return weatherHistoryDao.getFiltratedListByTemperature();
    }
}
