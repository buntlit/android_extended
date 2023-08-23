package com.gb.androidapponjava.model;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.gb.androidapponjava.modules.Constants;
import com.gb.androidapponjava.modules.ForecastAnswer;
import com.gb.androidapponjava.modules.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class GetWeatherService extends Service {

    private final IBinder binder = new LocalBinder();

    public GetWeatherService() {
        super();
    }

    public class LocalBinder extends Binder{
       public GetWeatherService getWeatherService(){
            return GetWeatherService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ForecastAnswer getWeather(String citiName, String weatherParameter, String attribute){
        final String URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s%s";
        final String API_KEY = "2f0e5b22131acf399237f29d84fdcfeb";
        final String REQUEST_METHOD = "GET";
        final int TIMEOUT = 10000;
        final ForecastAnswer[] answer = new ForecastAnswer[1];

        try {
            final java.net.URL uri = new URL(String.format(URL, citiName, API_KEY, weatherParameter));
            Thread thread = new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod(REQUEST_METHOD);
                    urlConnection.setReadTimeout(TIMEOUT);
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode >= 200 && responseCode <= 300) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = in.lines().collect(Collectors.joining("\n"));
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        answer[0] = new ForecastAnswer(weatherRequest.getId(), weatherRequest.getMain().getTemp(),
                                weatherRequest.getMain().getHumidity(), weatherRequest.getMain().getPressure(),
                                weatherRequest.getWind().getSpeed(), attribute, true, responseCode);
                    } else {
                        answer[0] = new ForecastAnswer(Constants.DEFAULT_CITY_INDEX, Constants.DEFAULT_TEMPERATURE,
                                Constants.DEFAULT_HUMIDITY, Constants.DEFAULT_PRESSURE,
                                Constants.DEFAULT_WIND_SPEED, attribute, false, responseCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        }
        return answer[0];
    }
}