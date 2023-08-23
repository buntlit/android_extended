package com.gb.androidapponjava.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.model.Model;
import com.gb.androidapponjava.modules.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DataViewModel extends AndroidViewModel {

    MutableLiveData<Model> liveDataCities;
    MutableLiveData<Model> liveDataCheckBoxes;
    MutableLiveData<Model> liveDataSettings;
    MutableLiveData<Model> liveDataWeatherHistory;

    public DataViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Model> getLiveDataCities() {
        if (liveDataCities == null) {
            liveDataCities = new MutableLiveData<>();
            liveDataCities.setValue(createCitiesModel());
        }
        return liveDataCities;
    }

    public MutableLiveData<Model> getLiveDataCheckBoxes() {
        if (liveDataCheckBoxes == null) {
            liveDataCheckBoxes = new MutableLiveData<>();
            liveDataCheckBoxes.setValue(createCheckBoxesData());
        }
        return liveDataCheckBoxes;
    }

    public MutableLiveData<Model> getLiveDataSettings() {
        if (liveDataSettings == null) {
            liveDataSettings = new MutableLiveData<>();
            liveDataSettings.setValue(createSettingsModel());
        }
        return liveDataSettings;
    }

    public MutableLiveData<Model> getLiveDataWeatherHistory() {
        if (liveDataWeatherHistory == null) {
            liveDataWeatherHistory = new MutableLiveData<>();
            liveDataWeatherHistory.setValue(createWeatherHistoryModel());
        }
        return liveDataWeatherHistory;
    }

    private Model createCitiesModel() {
        Context context = getApplication();
        List<String> cities = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.cities)));
        return new Model(cities.get(0), cities);
    }

    private Model createCheckBoxesData() {
        return new Model(false, false, false);
    }

    private Model createSettingsModel() {
        return new Model(new Model.WeatherParameter(Constants.CELSIUS_STRING, Constants.CELSIUS_ATTRIBUTE));
    }

    private Model createWeatherHistoryModel() {
        return new Model(new ArrayList<>());
    }

    private Model getCitiesModel() {
        return liveDataCities.getValue();
    }

    private Model getCheckBoxesModel() {
        return liveDataCheckBoxes.getValue();
    }

    private Model getSettingsModel() {
        return liveDataSettings.getValue();
    }

    private Model getWeatherHistoryModel() {
        return liveDataWeatherHistory.getValue();
    }

    public List<String> getListCities() {
        return getCitiesModel().getCities();
    }

    public String getCityName() {
        return getCitiesModel().getCityName();
    }

    public void saveCitiesData(String cityName, boolean isAddingNewCity) {
        Model cityModel = getCitiesModel();
        if (isAddingNewCity) {
            List<String> citiesList = getListCities();
            citiesList.add(cityName);
            cityModel.setCities(citiesList);
        }
        cityModel.setCityName(cityName);
        liveDataCities.setValue(cityModel);
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
        getWeatherParameter().setStringWeatherParameter(stringWeatherParameter);
        getWeatherParameter().setWeatherAttribute(weatherAttribute);
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
        getCitiesModel().setCities(cities);
        saveCitiesData(getListCities().get(position), false);
    }

    public List<String> getWeatherHistoryList() {
        return getWeatherHistoryModel().getWeatherHistoryList();
    }

    private void setLiveDataWeatherHistory(List<String> weatherHistoryList) {
        getWeatherHistoryModel().setWeatherHistoryList(weatherHistoryList);
    }

    public void addWeatherHistory(String city, String value) {
        String weatherHistoryString = city + " " + value;
        List<String> weatherHistoryList = getWeatherHistoryList();
        if (!weatherHistoryList.contains(weatherHistoryString)) {
            weatherHistoryList.add(weatherHistoryString);
            setLiveDataWeatherHistory(weatherHistoryList);
        }
    }

    public void clearCitiesData() {
        liveDataCities.setValue(createCitiesModel());
    }

    public void clearHistoryWeatherData() {
        liveDataWeatherHistory.setValue(createWeatherHistoryModel());
    }

    public void clearAllData() {
        liveDataCities.setValue(createCitiesModel());
        liveDataCheckBoxes.setValue(createCheckBoxesData());
        liveDataSettings.setValue(createSettingsModel());
        liveDataWeatherHistory.setValue(createWeatherHistoryModel());
    }

}
