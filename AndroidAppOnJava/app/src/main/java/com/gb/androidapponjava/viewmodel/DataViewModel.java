package com.gb.androidapponjava.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.model.CheckBoxesModel;
import com.gb.androidapponjava.model.CitiesModel;
import com.gb.androidapponjava.model.ForecastAnswerModel;
import com.gb.androidapponjava.model.WeatherHistoryModel;
import com.gb.androidapponjava.model.WeatherParameterModel;
import com.gb.androidapponjava.modules.Constants;
import com.gb.androidapponjava.modules.OpenWeather;
import com.gb.androidapponjava.modules.WeatherRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataViewModel extends AndroidViewModel {

    private final MutableLiveData<CitiesModel> citiesLiveData;
    private final MutableLiveData<CheckBoxesModel> checkBoxesLiveData;
    private final MutableLiveData<WeatherParameterModel> weatherParameterLiveData;
    private final MutableLiveData<WeatherHistoryModel> weatherHistoryLiveData;
    private final MutableLiveData<ForecastAnswerModel> forecastAnswerLiveData;
    private final MutableLiveData<Uri> toBrowserEvent;
    private OpenWeather openWeather;

    public DataViewModel(@NonNull Application application) {
        super(application);
        citiesLiveData = new MutableLiveData<>(createCitiesModel());
        checkBoxesLiveData = new MutableLiveData<>(createCheckBoxesData());
        weatherParameterLiveData = new MutableLiveData<>(createWeatherParameterModel());
        weatherHistoryLiveData = new MutableLiveData<>(createWeatherHistoryModel());
        forecastAnswerLiveData = new MutableLiveData<>();
        toBrowserEvent = new MutableLiveData<>();
        initRetrofit();
    }

    public MutableLiveData<CitiesModel> getCitiesLiveData() {
        return citiesLiveData;
    }

    public MutableLiveData<CheckBoxesModel> getCheckBoxesLiveData() {
        return checkBoxesLiveData;
    }

    public MutableLiveData<WeatherParameterModel> getWeatherParameterLiveData() {
        return weatherParameterLiveData;
    }

    public MutableLiveData<WeatherHistoryModel> getWeatherHistoryLiveData() {
        return weatherHistoryLiveData;
    }

    public MutableLiveData<ForecastAnswerModel> getForecastAnswerLiveData() {
        return forecastAnswerLiveData;
    }

    public MutableLiveData<Uri> getToBrowserEvent() {
        return toBrowserEvent;
    }

    private CitiesModel createCitiesModel() {
        Context context = getApplication();
        List<String> cities = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.cities)));
        return new CitiesModel(cities.get(0), cities);
    }

    private CheckBoxesModel createCheckBoxesData() {
        return new CheckBoxesModel(false,
                false,
                false);
    }

    private WeatherParameterModel createWeatherParameterModel() {
        return new WeatherParameterModel(Constants.CELSIUS_STRING, Constants.CELSIUS_ATTRIBUTE);
    }

    private WeatherHistoryModel createWeatherHistoryModel() {
        return new WeatherHistoryModel(new ArrayList<>());
    }

    private CitiesModel getCitiesModel() {
        return citiesLiveData.getValue();
    }

    private CheckBoxesModel getCheckBoxesModel() {
        return checkBoxesLiveData.getValue();
    }

    private WeatherParameterModel getWeatherParameterModel() {
        return weatherParameterLiveData.getValue();
    }

    private WeatherHistoryModel getWeatherHistoryModel() {
        return weatherHistoryLiveData.getValue();
    }

    public List<String> getListCities() {
        return getCitiesModel().getCities();
    }

    public String getCityName() {
        return getCitiesModel().getCityName();
    }

    public void saveCitiesData(String newCityName, boolean isAddingNewCity) {
        List<String> citiesList = getListCities();
        if (isAddingNewCity) {
            citiesList.add(newCityName);
        }
        citiesLiveData.postValue(new CitiesModel(newCityName, citiesList));
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
        checkBoxesLiveData.postValue(new CheckBoxesModel(isHumidityPressed,
                isPressurePressed, isWindSpeedPressed));
    }

    public String getStringWeatherParameter() {
        return getWeatherParameterModel().getStringWeatherParameter();
    }

    public String getAttribute() {
        return getWeatherParameterModel().getWeatherAttribute();
    }

    public void saveWeatherParameter(String stringWeatherParameter, String weatherAttribute) {
        weatherParameterLiveData.postValue(new WeatherParameterModel(stringWeatherParameter,
                weatherAttribute));
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_FOR_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    public void loadWeather() {
        openWeather.loadWeatherWithParameter(getCityName(), Constants.API_KEY, getStringWeatherParameter())
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            forecastAnswerLiveData.postValue(new ForecastAnswerModel(
                                            response.body().getId(),
                                            response.body().getMain().getTemp(),
                                            response.body().getMain().getHumidity(),
                                            response.body().getMain().getPressure(),
                                            response.body().getWind().getSpeed(),
                                            getAttribute(),
                                            true,
                                            response.code(),
                                            response.body().getWeather()[0].getIcon()
                                    )
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        forecastAnswerLiveData.postValue(new ForecastAnswerModel(
                                        Constants.DEFAULT_CITY_INDEX,
                                        Constants.DEFAULT_TEMPERATURE,
                                        Constants.DEFAULT_HUMIDITY,
                                        Constants.DEFAULT_PRESSURE,
                                        Constants.DEFAULT_WIND_SPEED,
                                        getAttribute(),
                                        false,
                                        404,
                                        "01d"
                                )
                        );
                    }
                });
    }


    public void onShowWeatherOnInternetClick() {
        if (forecastAnswerLiveData.getValue() == null) {
            return;
        }
        int cityIndex = forecastAnswerLiveData.getValue().getCityIndex();
        Uri uri = Uri.parse(Constants.URL_FOR_BROWSER + cityIndex);
        toBrowserEvent.postValue(uri);
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
        citiesLiveData.postValue(new CitiesModel(cities.get(position), cities));
    }

    public List<String> getWeatherHistoryList() {
        return getWeatherHistoryModel().getWeatherHistoryList();
    }

    public void saveWeatherHistory(String city, String value) {
        String weatherHistoryString = city + " " + value;
        List<String> weatherHistoryList = getWeatherHistoryList();
        if (!weatherHistoryList.contains(weatherHistoryString)) {
            weatherHistoryList.add(weatherHistoryString);
        }
        weatherHistoryLiveData.postValue(new WeatherHistoryModel(weatherHistoryList));
    }

    public void clearCitiesData() {
        citiesLiveData.postValue(createCitiesModel());
    }

    public void clearHistoryWeatherData() {
        weatherHistoryLiveData.postValue(createWeatherHistoryModel());
    }

    public void clearAllData() {
        citiesLiveData.postValue(createCitiesModel());
        checkBoxesLiveData.postValue(createCheckBoxesData());
        weatherParameterLiveData.postValue(createWeatherParameterModel());
        weatherHistoryLiveData.postValue(createWeatherHistoryModel());
    }

    public void loadWeatherImage(ImageView imageView) {
        Uri uri = Uri.parse(String.format(Constants.URL_IMAGE,
                getForecastAnswerLiveData().getValue().getIconValue()));
        Picasso.get()
                .load(uri)
                .resize(180, 180)
                .into(imageView);
    }

    public void clearUriData() {
        toBrowserEvent.postValue(null);
    }

}
