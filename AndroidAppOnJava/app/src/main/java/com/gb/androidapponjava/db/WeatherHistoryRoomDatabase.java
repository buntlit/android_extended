package com.gb.androidapponjava.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gb.androidapponjava.dao.WeatherHistoryDao;
import com.gb.androidapponjava.model.WeatherHistory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {WeatherHistory.class}, version = 1, exportSchema = false)
public abstract class WeatherHistoryRoomDatabase extends RoomDatabase {
    public abstract WeatherHistoryDao weatherHistoryDao();

    private static volatile WeatherHistoryRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static WeatherHistoryRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherHistoryRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WeatherHistoryRoomDatabase.class, "weather_history_table")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(()->{
                WeatherHistoryDao dao = INSTANCE.weatherHistoryDao();
                dao.deleteAll();

                WeatherHistory weatherHistory = new WeatherHistory(1661870592, "Korolev", 10);
                dao.insertWeatherHistory(weatherHistory);
            });
        }
    };
}
