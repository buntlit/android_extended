package com.gb.androidapponjava.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.model.CitiesModel;
import com.gb.androidapponjava.model.Model;
import com.gb.androidapponjava.modules.ConstantsStrings;
import com.gb.androidapponjava.modules.ForecastAnswer;
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
import java.util.Locale;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class DataViewModel extends AndroidViewModel {

    private final MutableLiveData<CitiesModel> liveDataCities;
    private final MutableLiveData<Model> liveDataCheckBoxes;
    private final MutableLiveData<Model> liveDataSettings;
    private final MutableLiveData<ForecastAnswer> forecastLiveData;
    private final MutableLiveData<Uri> toBrowserEvent;


    public DataViewModel(@NonNull Application application) {
        super(application);
        liveDataCities = new MutableLiveData<>(createCitiesModel());
        liveDataCheckBoxes = new MutableLiveData<>(createCheckBoxesData());
        liveDataSettings = new MutableLiveData<>(createSettingsModel());
        forecastLiveData = new MutableLiveData<>();
        toBrowserEvent = new MutableLiveData<>();
        loadWeather();
    }

    public MutableLiveData<CitiesModel> getCitiesLiveData() {
        return liveDataCities;
    }

    public MutableLiveData<Uri> getToBrowserEventLiveData() {
        return toBrowserEvent;
    }

    public MutableLiveData<ForecastAnswer> getForecastLiveData() {
        return forecastLiveData;
    }

    public MutableLiveData<Model> getLiveDataCheckBoxes() {
        return liveDataCheckBoxes;
    }

    public MutableLiveData<Model> getLiveDataSettings() {
        return liveDataSettings;
    }

    public CitiesModel createCitiesModel() {
        Context context = getApplication();
        List<String> cities = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.cities)));
        return new CitiesModel(cities.get(0), cities);
    }

    public Model createCheckBoxesData() {
        return new Model(false, false, false);
    }

    public Model createSettingsModel() {
        return new Model(new Model.WeatherParameter(ConstantsStrings.CELSIUS_STRING, ConstantsStrings.CELSIUS_ATTRIBUTE));
    }

    public void loadWeather() {
        final String URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s%s";
        final String API_KEY = "2f0e5b22131acf399237f29d84fdcfeb";
        final String REQUEST_METHOD = "GET";
        final int TIMEOUT = 10000;
        try {
            final java.net.URL uri = new URL(String.format(URL, getCityName(), API_KEY, getStringWeatherParameter()));
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

                    forecastLiveData.postValue(new ForecastAnswer(
                            weatherRequest.getId(),
                            weatherRequest.getMain().getTemp(),
                            weatherRequest.getMain().getHumidity(),
                            weatherRequest.getMain().getPressure(),
                            weatherRequest.getWind().getSpeed(),
                            getAttribute()
                    ));
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
    }

    private CitiesModel getCitiesModel() {
        return liveDataCities.getValue();
    }

    private Model getCheckBoxesModel() {
        return liveDataCheckBoxes.getValue();
    }

    private Model getSettingsModel() {
        return liveDataSettings.getValue();
    }

    public List<String> getListCities() {
        return getCitiesModel().getCities();
    }

    public String getCityName() {
        return getCitiesModel().getCityName();
    }

    public void saveCitiesData(String newCityName, boolean isAddingNewCity) {
        CitiesModel cityModel = getCitiesModel();
        List<String> cities = cityModel.getCities();
        if (isAddingNewCity) {
            cities.add(newCityName);
        }
        liveDataCities.setValue(new CitiesModel(newCityName, cities));
    }

    public boolean isHumidityCheckBoxPressed() {
        return getCheckBoxesModel().isCheckBoxHumidity();
    }

    public boolean isPressureCheckBoxPressed() {
        return getCheckBoxesModel().isCheckBoxPressure();
    }

    public boolean isWindSpeedCheckBoxPressed() {
        return getCheckBoxesModel().isCheckBoxWindSpeed();
    }

    public void saveCheckBoxesData(boolean isHumidityPressed, boolean isPressurePressed, boolean isWindSpeedPressed) {
        Model checkBoxModel = getCheckBoxesModel();
        checkBoxModel.setCheckBoxPressure(isPressurePressed);
        checkBoxModel.setCheckBoxHumidity(isHumidityPressed);
        checkBoxModel.setCheckBoxWindSpeed(isWindSpeedPressed);
        liveDataCheckBoxes.setValue(checkBoxModel);
    }

    private Model.WeatherParameter getWeatherParameter() {
        return getSettingsModel().getWeatherParameter();
    }

    public String getStringWeatherParameter() {
        return getWeatherParameter().getStringWeatherParameter();
    }

    public String getAttribute() {
        return getWeatherParameter().getWeatherAttribute();
    }

    public void saveWeatherParameter(String stringWeatherParameter, String weatherAttribute) {
        getSettingsModel().getWeatherParameter().setStringWeatherParameter(stringWeatherParameter);
        getSettingsModel().getWeatherParameter().setWeatherAttribute(weatherAttribute);
    }

    public void changeCitiesLocale(String region) {
        int position = getListCities().indexOf(getCityName());
        Locale locale = new Locale(region);
        Resources resources = getApplication().getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        List<String> cities = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.cities)));
        if (cities.size() != getListCities().size()) {
            for (int i = cities.size(); i < getListCities().size(); i++) {
                cities.add(getListCities().get(i));
            }
        }
//        getCitiesModel().setCities(cities);
        saveCitiesData(getListCities().get(position), false);
    }

    public void onIntenetWeatherClick() {
        ForecastAnswer forecastAnswer = forecastLiveData.getValue();
        if (forecastAnswer == null) return;
        int cityIndex = forecastLiveData.getValue().getCityIndex();

        final String address = "https://openweathermap.org/city/";
        Uri uri = Uri.parse(address + cityIndex);
        toBrowserEvent.postValue(uri);
    }
}
