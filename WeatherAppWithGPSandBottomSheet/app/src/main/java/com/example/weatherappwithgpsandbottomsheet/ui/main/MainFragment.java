package com.example.weatherappwithgpsandbottomsheet.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherappwithgpsandbottomsheet.databinding.FragmentMainBinding;
import com.example.weatherappwithgpsandbottomsheet.model.ForecastResponse;

public class MainFragment extends Fragment {

    private MainViewModel viewModel;
    private FragmentMainBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.chooseWeather.setOnClickListener(button -> {
            MainBottomSheetDialogFragment dialogFragment = MainBottomSheetDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "dialog");
        });
        viewModel.getModelLiveData().observe(getViewLifecycleOwner(), model -> {
            viewModel.getWeather();
        });

        viewModel.getResponseLiveData().observe(getViewLifecycleOwner(), this::setWeather);
    }

    private void setWeather(ForecastResponse response) {
        if (response != null) {
            binding.extras.setVisibility(View.VISIBLE);
            binding.city.setText(response.getCityName());
            binding.temperatureValue.setText(String.valueOf(response.getTemperature()));
            binding.condition.setText(response.getCondition());
            binding.feels.setText(String.valueOf(response.getFeelsLike()));
            binding.humidityValue.setText(String.valueOf(response.getHumidity()));
            binding.windSpeedValue.setText(String.valueOf(response.getWindSpeed()));
        } else {
            binding.extras.setVisibility(View.GONE);
        }
    }
}