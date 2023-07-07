package com.gb.androidapponjava;

import static com.gb.androidapponjava.ShowWeatherFragment.PARCEL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

public class ShowWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        if (savedInstanceState == null) {
            ShowWeatherFragment showWeatherFragment = ShowWeatherFragment.
                    create((Parcel) getIntent().getExtras().getSerializable(PARCEL));
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, showWeatherFragment).commit();
        }
    }
}