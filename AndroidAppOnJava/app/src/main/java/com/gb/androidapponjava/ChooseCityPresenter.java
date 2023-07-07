package com.gb.androidapponjava;

public final class ChooseCityPresenter {
    private static ChooseCityPresenter instance = null;

    private boolean isCheckBoxHumidity;
    private boolean isCheckBoxPressure;
    private boolean isCheckBoxWindSpeed;
    private boolean isDarkTheme;
    private String enterCity;

    public ChooseCityPresenter() {
        isCheckBoxHumidity = false;
        isCheckBoxPressure = false;
        isCheckBoxWindSpeed = false;
        isDarkTheme = false;
        enterCity = "";
    }

    public void setCheckBoxHumidity(boolean checkBoxHumidity) {
        isCheckBoxHumidity = checkBoxHumidity;
    }

    public boolean getIsCheckBoxHumidity() {
        return isCheckBoxHumidity;
    }

    public boolean getIsCheckBoxPressure() {
        return isCheckBoxPressure;
    }

    public void setCheckBoxPressure(boolean checkBoxPressure) {
        isCheckBoxPressure = checkBoxPressure;
    }

    public boolean getIsCheckBoxWindSpeed() {
        return isCheckBoxWindSpeed;
    }

    public void setCheckBoxWindSpeed(boolean checkBoxWindSpeed) {
        isCheckBoxWindSpeed = checkBoxWindSpeed;
    }

    public String getEnterCity() {
        return enterCity;
    }

    public void setEnterCity(String enterCity) {
        if (enterCity != null) {
            this.enterCity = enterCity;
        }
    }

    public boolean getIsDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public static ChooseCityPresenter getInstance() {
        if (instance == null) {
            instance = new ChooseCityPresenter();
        }
        return instance;
    }
}
