package com.gb.androidapponjava.view;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentSettingsBinding;
import com.gb.androidapponjava.model.Model;
import com.gb.androidapponjava.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SettingsFrag extends Fragment {
    private DataViewModel dataViewModel;
    private FragmentSettingsBinding fragmentSettingsBinding;
    private MutableLiveData<Model> liveData;

    final String CELSIUS = "celsius";
    final String KELVIN = "kelvin";
    final String FAHRENHEIT = "fahrenheit";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        dataViewModel = new ViewModelProvider(getActivity()).get(DataViewModel.class);
        liveData = dataViewModel.getLiveData();
        fragmentSettingsBinding.setSettingsViewmodel(dataViewModel);
        fragmentSettingsBinding.executePendingBindings();
        initView();
        return fragmentSettingsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        liveData.observe(getActivity(), new Observer<Model>() {
            @Override
            public void onChanged(Model model) {
                init();
            }
        });
    }

    private void initView() {
        getActivity().setTitle(R.string.settings);
        fragmentSettingsBinding.nightTheme.setChecked(liveData.getValue().isDarkTheme());
        setMode(liveData.getValue().isDarkTheme());

        if (liveData.getValue().isRuLocale()) {
            fragmentSettingsBinding.localisation.check(R.id.localisationRus);
        } else {
            fragmentSettingsBinding.localisation.check(R.id.localisationEng);
        }

        switch (liveData.getValue().getWeatherParameter()){
            case CELSIUS:
                fragmentSettingsBinding.weatherParameter.check(R.id.celsius);
                break;
            case KELVIN:
                fragmentSettingsBinding.weatherParameter.check(R.id.kelvin);
                break;
            case FAHRENHEIT:
                fragmentSettingsBinding.weatherParameter.check(R.id.fahrenheit);
                break;
        }
    }

    private void init() {
        fragmentSettingsBinding.nightTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                liveData.getValue().setDarkTheme(b);
                setMode(b);
            }
        });

        fragmentSettingsBinding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        fragmentSettingsBinding.localisation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String region = "";
                switch (i) {
                    case R.id.localisationRus:
                        region = "ru";
                        liveData.getValue().setRuLocale(true);
                        break;
                    case R.id.localisationEng:
                        region = "en";
                        liveData.getValue().setRuLocale(false);
                }
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(region));
                updateCities(region);

            }
        });

        fragmentSettingsBinding.weatherParameter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case (R.id.celsius):
                        liveData.getValue().setWeatherParameter(CELSIUS);
                        break;
                    case (R.id.kelvin):
                        liveData.getValue().setWeatherParameter(KELVIN);
                        break;
                    case(R.id.fahrenheit):
                        liveData.getValue().setWeatherParameter(FAHRENHEIT);
                        break;
                }
            }
        });
    }

    private void updateCities(String region) {
        Locale locale = new Locale(region);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        List<String> cities = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.cities)));
        List<String> citiesFromModel = liveData.getValue().getCities();
        if (cities.size() != citiesFromModel.size()) {
            for (int j = cities.size(); j < citiesFromModel.size(); j++) {
                cities.add(j, citiesFromModel.get(j));
            }
        }
        liveData.getValue().setCities(cities);
        liveData.getValue().setCityName(cities.get(liveData.getValue().getCityIndex()));
    }

    private void setMode(boolean isDarkTheme) {
        if (isDarkTheme == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
