package com.gb.androidapponjava.model;

public class CheckBoxesModel {
    private final boolean isCheckBoxHumidity;
    private final boolean isCheckBoxPressure;
    private final boolean isCheckBoxWindSpeed;

    public CheckBoxesModel(boolean isCheckBoxHumidity, boolean isCheckBoxPressure, boolean isCheckBoxWindSpeed) {
        this.isCheckBoxHumidity = isCheckBoxHumidity;
        this.isCheckBoxPressure = isCheckBoxPressure;
        this.isCheckBoxWindSpeed = isCheckBoxWindSpeed;
    }

    public boolean isCheckBoxHumidity() {
        return isCheckBoxHumidity;
    }

    public boolean isCheckBoxPressure() {
        return isCheckBoxPressure;
    }

    public boolean isCheckBoxWindSpeed() {
        return isCheckBoxWindSpeed;
    }
}
