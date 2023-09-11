package com.example.weatherappwithgpsandbottomsheet.ui.main;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherappwithgpsandbottomsheet.R;
import com.example.weatherappwithgpsandbottomsheet.api.WeatherResponse;
import com.example.weatherappwithgpsandbottomsheet.model.ForecastResponse;
import com.example.weatherappwithgpsandbottomsheet.model.Model;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<Model> modelLiveData;
    private final MutableLiveData<ForecastResponse> responseLiveData;
    private final SharedPreferences preferences;
    private final String PREFERENCES_NAME = "MODEL";
    private final String RESPONSE_KEY = "RESPONSE";
    private final String LATITUDE_KEY = "LAT";
    private final String LONGITUDE_KEY = "LON";

    public MainViewModel(@NonNull Application application) {
        super(application);
        preferences = getApplication().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        modelLiveData = new MutableLiveData<>(createModelLiveData());
        responseLiveData = new MutableLiveData<>();
        if (preferences.contains(RESPONSE_KEY)) {
            modelLiveData.postValue(new Model(preferences.getString(RESPONSE_KEY, ""),
                    preferences.getFloat(LATITUDE_KEY, 0),
                    preferences.getFloat(LONGITUDE_KEY, 0)));
        }

    }

    public MutableLiveData<Model> getModelLiveData() {
        return modelLiveData;
    }

    public MutableLiveData<ForecastResponse> getResponseLiveData() {
        return responseLiveData;
    }

    private Model createModelLiveData() {
        return new Model("", 0, 0);
    }

    private Model getModel() {
        return getModelLiveData().getValue();
    }

    private Double getLongitude() {
        return getModel().getLongitude();
    }

    private Double getLatitude() {
        return getModel().getLatitude();
    }

    private String getButtonResponse() {
        return getModel().getButtonResponse();
    }

    public void getWeather() {
        final String URL = "https://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=no&lang=ru";
        final String API_KEY = "cba6db1af5184e1b98e152915231008";
        final String REQUEST_METHOD = "GET";
        final int TIMEOUT = 5000;
        String requestCity;
        if (!getButtonResponse().equals("")) {
            if (getButtonResponse().equals(getApplication().getString(R.string.gps))) {
                requestCity = getLatitude() + " " + getLongitude();
            } else {
                requestCity = getButtonResponse();
            }
            try {
                final java.net.URL uri = new URL(String.format(URL, API_KEY, requestCity));
                Thread thread = new Thread(() -> {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod(REQUEST_METHOD);
                        urlConnection.setConnectTimeout(TIMEOUT);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = reader.lines().collect(Collectors.joining("\n"));
                        Gson gson = new Gson();
                        final WeatherResponse weatherResponse = gson.fromJson(result, WeatherResponse.class);
                        responseLiveData.postValue(new ForecastResponse(
                                weatherResponse.getLocation().getName(),
                                weatherResponse.getCurrent().getTemp_c(),
                                weatherResponse.getCurrent().getCondition().getText(),
                                weatherResponse.getCurrent().getFeelslike_c(),
                                weatherResponse.getCurrent().getHumidity(),
                                weatherResponse.getCurrent().getWind_kph()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                });
                thread.start();
                thread.join();
            } catch (MalformedURLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveModel(String buttonResponse) {
        if (buttonResponse.equals(getApplication().getString(R.string.gps))) {
            getLocationGPS(buttonResponse);
        } else {
            saveModelLivaData(buttonResponse, getLatitude(), getLongitude());
        }
    }

    private void saveModelLivaData(String buttonResponse, Double lat, Double lon) {
        modelLiveData.postValue(new Model(buttonResponse, lat, lon));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(RESPONSE_KEY, buttonResponse);
        editor.putFloat(LATITUDE_KEY, lat.floatValue());
        editor.putFloat(LONGITUDE_KEY, lon.floatValue());
        editor.apply();
    }

    private void getLocationGPS(String buttonResponse) {

        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient providerClient = LocationServices.getFusedLocationProviderClient(getApplication());
        providerClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return this;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location ->
                saveModelLivaData(buttonResponse, location.getLatitude(), location.getLongitude())
        );
    }
}