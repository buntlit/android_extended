package com.gb.androidapponjava.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.model.Model;
import com.gb.androidapponjava.modules.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class DataViewModel extends AndroidViewModel {

    MutableLiveData<Model> liveDataModel;

    public DataViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<Model> getLiveData() {
        if (liveDataModel == null) {
            liveDataModel = new MutableLiveData<Model>();
            liveDataModel.setValue(createModel());
        }
        return liveDataModel;
    }

    public Model createModel() {
        Context context = getApplication();
        Boolean isRuLocale;
        if (context.getResources().getConfiguration().getLocales().get(0).toString().equals("ru_RU")) {
            isRuLocale = true;
        } else {
            isRuLocale = false;
        }
        List cities = new ArrayList(Arrays.asList(context.getResources().getStringArray(R.array.cities)));
        Float temperature = Float.valueOf(context.getResources().getString(R.string.temperature));
        Float humidity = Float.valueOf(context.getResources().getString(R.string.humidity_value));
        Float pressure = Float.valueOf(context.getResources().getString(R.string.pressure_value));
        Float windSpeed = Float.valueOf(context.getResources().getString(R.string.wind_speed_value));
        return new Model(cities.get(0).toString(), 0, cities,
                false, false, false, false,
                isRuLocale, true, 554233,
                temperature, humidity, pressure, windSpeed, "celsius");
    }

    public void getWeather(String cityName, String weatherParameter) {
        final String URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s%s";
        final String API_KEY = "2f0e5b22131acf399237f29d84fdcfeb";
        final String REQUEST_METHOD = "GET";
        final int TIMEOUT = 10000;

        try {
            final java.net.URL uri = new URL(String.format(URL, cityName, API_KEY, weatherParameter));
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
                    liveDataModel.getValue().setCityIndexForApi(weatherRequest.getId());
                    liveDataModel.getValue().setTemperature(weatherRequest.getMain().getTemp());
                    liveDataModel.getValue().setHumidity(weatherRequest.getMain().getHumidity());
                    liveDataModel.getValue().setPressure(weatherRequest.getMain().getPressure());
                    liveDataModel.getValue().setWindSpeed(weatherRequest.getWind().getSpeed());
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

    public void onCityClick(int position) {

        saveLiveData(cities.get(position), position);
    }

    void saveLiveData(String cityName, int position) {
        Model model = getModel();
        model.setCityName(cityName);
        model.setSelectedCityIndex(position);
        liveDataModel.setValue(model);
    }

    private Model getModel() { return liveDataModel.getValue();}
}
