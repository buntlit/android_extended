package com.gb.androidapponjava.modules;

import android.os.Handler;
import android.widget.TextView;

import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class WeatherFromApi {

    private final String URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    private final String API_KEY = "2f0e5b22131acf399237f29d84fdcfeb";
    private final String REQUEST_METHOD = "GET";
    private final int TIMEOUT = 10000;
    private String cityName;

    public WeatherFromApi(String cityName) {
        this.cityName = cityName;
    }

    public void getWeather(TextView temperature, TextView humidity, TextView pressure, TextView windSpeed) {
        try {
            final URL uri = new URL(String.format(URL, cityName, API_KEY));
            final Handler handler = new Handler();
            Thread thread = new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod(REQUEST_METHOD);
                    urlConnection.setReadTimeout(TIMEOUT);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String result = in.lines().collect(Collectors.joining("\n"));
                    Gson gson = new Gson();
                    final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            temperature.setText(String.valueOf(weatherRequest.getMain().getTemp()));
                            humidity.setText(String.valueOf(weatherRequest.getMain().getHumidity()));
                            pressure.setText(String.valueOf(weatherRequest.getMain().getPressure()));
                            windSpeed.setText(String.valueOf(weatherRequest.getWind().getSpeed()));
                        }
                    });
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
