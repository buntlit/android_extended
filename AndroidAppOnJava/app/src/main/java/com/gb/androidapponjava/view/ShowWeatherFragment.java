package com.gb.androidapponjava.view;

import static com.gb.androidapponjava.databinding.FragmentWeatherBinding.inflate;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentWeatherBinding;
import com.gb.androidapponjava.modules.ConstantsStrings;
import com.gb.androidapponjava.modules.ForecastAnswer;
import com.gb.androidapponjava.viewmodel.DataViewModel;

public class ShowWeatherFragment extends Fragment {

    private DataViewModel dataViewModel;
    private FragmentWeatherBinding binding;
    private ForecastAnswer answer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        requireActivity().setTitle(R.string.weather);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataViewModel.getLiveDataCities().observe(getViewLifecycleOwner(), model ->
                setWeather()
        );
        dataViewModel.getLiveDataCheckBoxes().observe(getViewLifecycleOwner(), model ->
                showExtras()
        );
        dataViewModel.getLiveDataSettings().observe(getViewLifecycleOwner(), model -> {});
        handleViews();
    }

    private void setWeather() {
        answer = dataViewModel.getWeather();
        binding.city.setText(dataViewModel.getCityName());
        binding.temperatureValue.setText(answer.getTemperature() + answer.getWeatherAttribute());
        binding.humidityValue.setText(String.valueOf(answer.getHumidity()));
        binding.pressureValue.setText(String.valueOf(answer.getPressure()));
        binding.windSpeedValue.setText(String.valueOf(answer.getWindSpeed()));
    }

    private void showExtras() {
        showBackButton();
        if (dataViewModel.isHumidityCheckBoxPressed()) {
            binding.humidityLinear.setVisibility(View.VISIBLE);
        } else {
            binding.humidityLinear.setVisibility(View.GONE);
        }
        if (dataViewModel.isPressureCheckBoxPressed()) {
            binding.pressureLinear.setVisibility(View.VISIBLE);
        } else {
            binding.pressureLinear.setVisibility(View.GONE);
        }
        if (dataViewModel.isWindSpeedCheckBoxPressed()) {
            binding.windSpeedLinear.setVisibility(View.VISIBLE);
        } else {
            binding.windSpeedLinear.setVisibility(View.GONE);
        }
    }

    private void handleViews() {
        binding.buttonSettings.setOnClickListener(view ->
                startSettingsFragment()
        );

        binding.showWeatherOnInternet.setOnClickListener(view ->
                startBrowserWithWeather()
        );

        binding.buttonBackToChoose.setOnClickListener(view ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }

    private void showBackButton() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.buttonBackToChoose.setVisibility(View.VISIBLE);
        } else {
            binding.buttonBackToChoose.setVisibility(View.GONE);
        }
    }

    private void startSettingsFragment() {
        Fragment settingsFragment;
        try {
            settingsFragment = SettingsFragment.class.newInstance();
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(getId(), settingsFragment)
                .addToBackStack(ConstantsStrings.BACK_STACK_SHOW_WEATHER_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void startBrowserWithWeather() {
        final String address = "https://openweathermap.org/city/";
        Uri uri = Uri.parse(address + answer.getCityIndex());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
