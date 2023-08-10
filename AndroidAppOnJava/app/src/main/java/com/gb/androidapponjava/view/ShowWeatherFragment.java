package com.gb.androidapponjava.view;

import static com.gb.androidapponjava.databinding.FragmentWeatherBinding.inflate;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentWeatherBinding;
import com.gb.androidapponjava.modules.ForecastAnswer;
import com.gb.androidapponjava.viewmodel.DataViewModel;

public class ShowWeatherFragment extends Fragment {

    private DataViewModel viewModel;
    private FragmentWeatherBinding binding;
    private ForecastAnswer answer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        requireActivity().setTitle(R.string.weather);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLiveDataCities().observe(getViewLifecycleOwner(), model ->
                setWeather()
        );
        viewModel.getLiveDataCheckBoxes().observe(getViewLifecycleOwner(), model ->
                showExtras()
        );
        viewModel.getLiveDataSettings().observe(getViewLifecycleOwner(), model -> {
        });
        handleViews();
    }

    private void setWeather() {
        answer = viewModel.getWeather();
        binding.city.setText(viewModel.getCityName());
        if (answer == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.alert_dialog_title).setMessage(R.string.internet_error)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel_dialog_button, (dialogInterface, i) -> {
                            }
                    )
                    .setPositiveButton(R.string.go_to_internet_settings, (dialogInterface, i) ->
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .show();
        } else if (answer.isResponse()) {
            setWeatherValues();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.alert_dialog_title).setMessage(getString(R.string.server_error) + answer.getResponseCode())
                    .setCancelable(false).setNegativeButton(R.string.cancel_dialog_button,
                            (dialogInterface, i) -> {
                            }
                    )
                    .setPositiveButton(R.string.show_default, (dialogInterface, i) ->
                            setWeatherValues()
                    )
                    .show();
        }
    }

    private void setWeatherValues() {
        binding.temperatureValue.setText(answer.getTemperature() + answer.getWeatherAttribute());
        binding.humidityValue.setText(String.valueOf(answer.getHumidity()));
        binding.pressureValue.setText(String.valueOf(answer.getPressure()));
        binding.windSpeedValue.setText(String.valueOf(answer.getWindSpeed()));
        viewModel.addWeatherHistory(
                (String) binding.city.getText(),
                (String) binding.temperatureValue.getText()
        );
    }

    private void showExtras() {
        showBackButton();
        if (viewModel.isHumidityCheckBoxPressed()) {
            binding.humidityLinear.setVisibility(View.VISIBLE);
        } else {
            binding.humidityLinear.setVisibility(View.GONE);
        }
        if (viewModel.isPressureCheckBoxPressed()) {
            binding.pressureLinear.setVisibility(View.VISIBLE);
        } else {
            binding.pressureLinear.setVisibility(View.GONE);
        }
        if (viewModel.isWindSpeedCheckBoxPressed()) {
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
                Navigation.findNavController(requireView()).popBackStack()
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
        Navigation.findNavController(requireView()).navigate(R.id.settingsFragment);
    }

    private void startBrowserWithWeather() {
        final String address = "https://openweathermap.org/city/";
        Uri uri = Uri.parse(address + answer.getCityIndex());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}