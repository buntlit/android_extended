package com.gb.androidapponjava.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.model.CheckBoxesModel;
import com.gb.androidapponjava.model.CitiesModel;
import com.gb.androidapponjava.model.ForecastAnswerModel;
import com.gb.androidapponjava.model.WeatherHistory;
import com.gb.androidapponjava.model.WeatherHistoryRepository;
import com.gb.androidapponjava.model.WeatherParameterModel;
import com.gb.androidapponjava.modules.Constants;
import com.gb.androidapponjava.modules.OpenWeather;
import com.gb.androidapponjava.modules.WeatherRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    private final MutableLiveData<ForecastAnswerModel> forecastAnswerLiveData;
    private final MutableLiveData<Uri> toBrowserEvent;
    private OpenWeather openWeather;
    private WeatherHistoryRepository repository;
    private LiveData<List<WeatherHistory>> allWeatherHistory;

    public DataViewModel(@NonNull Application application) {
        super(application);
        citiesLiveData = new MutableLiveData<>(createCitiesModel());
        checkBoxesLiveData = new MutableLiveData<>(createCheckBoxesData());
        weatherParameterLiveData = new MutableLiveData<>(createWeatherParameterModel());
        forecastAnswerLiveData = new MutableLiveData<>();
        toBrowserEvent = new MutableLiveData<>();
        repository = new WeatherHistoryRepository(application);
        allWeatherHistory = repository.getWeatherHistory();
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

    public MutableLiveData<ForecastAnswerModel> getForecastAnswerLiveData() {
        return forecastAnswerLiveData;
    }

    public LiveData<List<WeatherHistory>> getFilterWeatherHistoryByDate() {
        return repository.getWeatherHistoryFilteredByDate();
    }

    public LiveData<List<WeatherHistory>> getFilterWeatherHistoryByCity() {
        return repository.getWeatherHistoryFilteredByCity();
    }
    public LiveData<List<WeatherHistory>> getFilterWeatherHistoryByTemperature() {
        return repository.getWeatherHistoryFilteredByTemperature();
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

    private CitiesModel getCitiesModel() {
        return citiesLiveData.getValue();
    }

    private CheckBoxesModel getCheckBoxesModel() {
        return checkBoxesLiveData.getValue();
    }

    private WeatherParameterModel getWeatherParameterModel() {
        return weatherParameterLiveData.getValue();
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
                                            response.body().getWeather()[0].getIcon(),
                                            response.body().getDt()
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
                                        "01d",
                                        Calendar.getInstance().getTime().getTime()
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

    public void insertWeatherHistory(WeatherHistory weatherHistory) {
        repository.insert(weatherHistory);
    }

    public void clearCitiesData() {
        citiesLiveData.postValue(createCitiesModel());
    }

    public void clearHistoryWeatherData() {
        repository.deleteAll();
    }

    public void clearAllData() {
        clearCitiesData();
        checkBoxesLiveData.postValue(createCheckBoxesData());
        weatherParameterLiveData.postValue(createWeatherParameterModel());
        clearHistoryWeatherData();
        clearUriData();
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
