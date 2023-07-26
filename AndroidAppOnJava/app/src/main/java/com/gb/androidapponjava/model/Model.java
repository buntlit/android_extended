package com.gb.androidapponjava.model;

import java.util.List;

public class Model {
    private String cityName;
    private int selectedCityIndex;
    private List<String> cities;

    private boolean isCheckBoxHumidity;
    private boolean isCheckBoxPressure;
    private boolean isCheckBoxWindSpeed;

    private boolean isDarkTheme;
    private boolean isRuLocale;
    private boolean isPortraitLandscape;

    private int cityIndexForApi;
    private float temperature;
    private float humidity;
    private float pressure;
    private float windSpeed;
    private String weatherParameter;

        public Model(String cityName, int selectedCityIndex, List<String> cities
            , boolean isCheckBoxHumidity, boolean isCheckBoxPressure, boolean isCheckBoxWindSpeed
            , boolean isDarkTheme, boolean isRuLocale, boolean isPortraitLandscape, int cityIndexForApi,
                     float temperature, float humidity, float pressure, float windSpeed, String weatherParameter) {
        this.cityName = cityName;
        this.selectedCityIndex = selectedCityIndex;
        this.cities = cities;
        this.isCheckBoxHumidity = isCheckBoxHumidity;
        this.isCheckBoxPressure = isCheckBoxPressure;
        this.isCheckBoxWindSpeed = isCheckBoxWindSpeed;
        this.isDarkTheme = isDarkTheme;
        this.isRuLocale = isRuLocale;
        this.isPortraitLandscape = isPortraitLandscape;
        this.cityIndexForApi = cityIndexForApi;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.weatherParameter = weatherParameter;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getSelectedCityIndex() {
        return selectedCityIndex;
    }

    public void setSelectedCityIndex(int selectedCityIndex) {
        this.selectedCityIndex = selectedCityIndex;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public boolean isCheckBoxHumidity() {
        return isCheckBoxHumidity;
    }

    public void setCheckBoxHumidity(boolean checkBoxHumidity) {
        isCheckBoxHumidity = checkBoxHumidity;
    }

    public boolean isCheckBoxPressure() {
        return isCheckBoxPressure;
    }

    public void setCheckBoxPressure(boolean checkBoxPressure) {
        isCheckBoxPressure = checkBoxPressure;
    }

    public boolean isCheckBoxWindSpeed() {
        return isCheckBoxWindSpeed;
    }

    public void setCheckBoxWindSpeed(boolean checkBoxWindSpeed) {
        isCheckBoxWindSpeed = checkBoxWindSpeed;
    }
    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public boolean isRuLocale() {
        return isRuLocale;
    }

    public void setRuLocale(boolean ruLocale) {
        isRuLocale = ruLocale;
    }

    public boolean isPortraitLandscape() {
        return isPortraitLandscape;
    }

    public void setPortraitLandscape(boolean portraitLandscape) {
        isPortraitLandscape = portraitLandscape;
    }

    public int getCityIndexForApi() {
        return cityIndexForApi;
    }

    public void setCityIndexForApi(int cityIndexForApi) {
        this.cityIndexForApi = cityIndexForApi;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherParameter() {
        return weatherParameter;
    }

    public void setWeatherParameter(String weatherParameter) {
        this.weatherParameter = weatherParameter;
    }

}
