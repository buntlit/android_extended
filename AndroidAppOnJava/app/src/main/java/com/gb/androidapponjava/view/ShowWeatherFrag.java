package com.gb.androidapponjava.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentWeatherBinding;
import com.gb.androidapponjava.model.Model;
import com.gb.androidapponjava.viewmodel.DataViewModel;

import java.util.Locale;

public class ShowWeatherFrag extends Fragment {

    private DataViewModel dataViewModel;
    private FragmentWeatherBinding fragmentWeatherBinding;
    private MutableLiveData<Model> liveData;

    final String CELSIUS = "celsius";
    final String KELVIN = "kelvin";
    final String FAHRENHEIT = "fahrenheit";
    private String weatherLetter = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentWeatherBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false);
        dataViewModel = new ViewModelProvider(getActivity()).get(DataViewModel.class);
        liveData = dataViewModel.getLiveData();
        dataViewModel.getWeather(setCitiNameForApi(), setWeatherParameterForApi());
        getActivity().setTitle(R.string.weather);
        fragmentWeatherBinding.setShowWeatherViewmodel(dataViewModel);
        fragmentWeatherBinding.executePendingBindings();
        return fragmentWeatherBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        liveData.observe(getActivity(), new Observer<Model>() {
            @Override
            public void onChanged(Model model) {
                setWeather();
                changeData();
            }
        });

    }

    private void setWeather() {

        fragmentWeatherBinding.temperatureValue.setText(String.valueOf(liveData.getValue().getTemperature()) + weatherLetter);
        fragmentWeatherBinding.humidityValue.setText(String.valueOf(liveData.getValue().getHumidity()));
        fragmentWeatherBinding.pressureValue.setText(String.valueOf(liveData.getValue().getPressure()));
        fragmentWeatherBinding.windSpeedValue.setText(String.valueOf(liveData.getValue().getWindSpeed()));
    }

    private void changeData() {
        fragmentWeatherBinding.city.setText(liveData.getValue().getCityName());
        showExtras();
        fragmentWeatherBinding.buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment settingsFragment = null;
                try {
                    settingsFragment = SettingsFrag.class.newInstance();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (java.lang.InstantiationException e) {
                    throw new RuntimeException(e);
                }

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(getId(), settingsFragment).addToBackStack("frag");
                fragmentTransaction.commit();
            }
        });
        fragmentWeatherBinding.showWeatherOnInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri address = Uri.parse("https://openweathermap.org/city/" + liveData.getValue().getCityIndexForApi());
                startActivity(new Intent(Intent.ACTION_VIEW, address));
            }
        });

        fragmentWeatherBinding.buttonBackToChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private String setCitiNameForApi() {
        String name;
        if (Locale.getDefault().getLanguage().equals("ru") && liveData.getValue().getSelectedCityIndex() < getResources().getStringArray(R.array.cities).length) {
            setLocale("en");
            name = getResources().getStringArray(R.array.cities)[liveData.getValue().getSelectedCityIndex()];
            setLocale("ru");
        } else {
            name = liveData.getValue().getCityName();
        }
        return name;
    }

    private String setWeatherParameterForApi() {
        switch (liveData.getValue().getWeatherParameter()) {
            case CELSIUS:
                weatherLetter = "C";
                return "&units=metric";
            case KELVIN:
                weatherLetter = "K";
                return "";
            case FAHRENHEIT:
                weatherLetter = "F";
                return "&units=imperial";
        }
        return "";
    }

    private void setLocale(String region) {
        Locale locale = new Locale(region);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void showExtras() {
        if (liveData.getValue().isCheckBoxHumidity()) {
            fragmentWeatherBinding.humidityLinear.setVisibility(View.VISIBLE);
        } else {
            fragmentWeatherBinding.humidityLinear.setVisibility(View.GONE);
        }
        if (liveData.getValue().isCheckBoxPressure()) {
            fragmentWeatherBinding.pressureLinear.setVisibility(View.VISIBLE);
        } else {
            fragmentWeatherBinding.pressureLinear.setVisibility(View.GONE);
        }
        if (liveData.getValue().isCheckBoxWindSpeed()) {
            fragmentWeatherBinding.windSpeedLinear.setVisibility(View.VISIBLE);
        } else {
            fragmentWeatherBinding.windSpeedLinear.setVisibility(View.GONE);
        }
    }
}
