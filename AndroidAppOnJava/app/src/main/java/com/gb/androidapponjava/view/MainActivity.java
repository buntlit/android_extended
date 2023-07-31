package com.gb.androidapponjava.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.gb.androidapponjava.databinding.ActivityChooseCityBinding;
import com.gb.androidapponjava.viewmodel.DataViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityChooseCityBinding activityChooseCityBinding;
    private DataViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChooseCityBinding = ActivityChooseCityBinding.inflate(getLayoutInflater());
        setContentView(activityChooseCityBinding.getRoot());
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);
        viewModel.getLiveDataCities().observe(this, model ->{});
        viewModel.getLiveDataCheckBoxes().observe(this, model -> {});
        viewModel.getLiveDataSettings().observe(this, model -> {});
    }
}
