package com.gb.androidapponjava.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gb.androidapponjava.databinding.ActivityChooseCityBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityChooseCityBinding activityChooseCityBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChooseCityBinding = ActivityChooseCityBinding.inflate(getLayoutInflater());
        setContentView(activityChooseCityBinding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityChooseCityBinding = null;
    }
}
