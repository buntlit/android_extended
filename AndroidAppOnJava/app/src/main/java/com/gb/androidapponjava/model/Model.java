package com.gb.androidapponjava.model;

import java.util.List;

public class Model {
    private String cityName;
    private List<String> cities;

    private boolean isCheckBoxHumidity;
    private boolean isCheckBoxPressure;
    private boolean isCheckBoxWindSpeed;

    private WeatherParameter weatherParameter;

    public static class WeatherParameter{
        private String stringWeatherParameter;
        private String weatherAttribute;

        public WeatherParameter(String stringWeatherParameter, String weatherAttribute) {
            this.stringWeatherParameter = stringWeatherParameter;
            this.weatherAttribute = weatherAttribute;
        }

        public String getStringWeatherParameter() {
            return stringWeatherParameter;
        }

        public void setStringWeatherParameter(String stringWeatherParameter) {
            this.stringWeatherParameter = stringWeatherParameter;
        }

        public String getWeatherAttribute() {
            return weatherAttribute;
        }

        public void setWeatherAttribute(String weatherAttribute) {
            this.weatherAttribute = weatherAttribute;
        }
    }

    public Model(String cityName, List<String> cities) {
        this.cityName = cityName;
        this.cities = cities;
    }

    public Model(boolean isCheckBoxHumidity, boolean isCheckBoxPressure, boolean isCheckBoxWindSpeed) {
        this.isCheckBoxHumidity = isCheckBoxHumidity;
        this.isCheckBoxPressure = isCheckBoxPressure;
        this.isCheckBoxWindSpeed = isCheckBoxWindSpeed;
    }

    public Model(WeatherParameter weatherParameter) {
        this.weatherParameter = weatherParameter;
    }

    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
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
    public WeatherParameter getWeatherParameter() {
        return weatherParameter;
    }
}
