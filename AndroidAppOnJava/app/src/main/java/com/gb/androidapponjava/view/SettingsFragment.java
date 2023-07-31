package com.gb.androidapponjava.view;

import static com.gb.androidapponjava.databinding.FragmentSettingsBinding.inflate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentSettingsBinding;
import com.gb.androidapponjava.modules.ConstantsStrings;
import com.gb.androidapponjava.viewmodel.DataViewModel;

public class SettingsFragment extends Fragment {
    private DataViewModel dataViewModel;
    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataViewModel.getLiveDataCities().observe(getViewLifecycleOwner(), model -> {});
        dataViewModel.getLiveDataCheckBoxes().observe(getViewLifecycleOwner(), model -> {});
        dataViewModel.getLiveDataSettings().observe(getViewLifecycleOwner(), model -> {});
        handleViews();
    }

    private void initView() {
        requireActivity().setTitle(R.string.settings);
        setSwitchDarkTheme();
        setSwitchLocalisation();
        setSwitchWeatherParameter();
    }

    private void handleViews() {
        binding.nightTheme.setOnCheckedChangeListener((compoundButton, b) ->
                setDarkTheme(b)
        );

        binding.buttonBack.setOnClickListener(view ->
                requireActivity().getSupportFragmentManager().
                        popBackStack(
                                ConstantsStrings.BACK_STACK_SHOW_WEATHER_FRAGMENT,
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
        );

        binding.localisation.setOnCheckedChangeListener((radioGroup, indexRadioButton) ->
                processingSwitchLocalisation(indexRadioButton)
        );

        binding.weatherParameter.setOnCheckedChangeListener((radioGroup, indexRadioButton) ->
                processingSwitchWeatherParameter(indexRadioButton)
        );
    }

    private void setSwitchDarkTheme() {
        binding.nightTheme.setChecked(AppCompatDelegate
                .getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void setDarkTheme(boolean b) {
        int mode;
        if (b) {
            mode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            mode = AppCompatDelegate.MODE_NIGHT_NO;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void setSwitchLocalisation() {
        if (requireContext().getResources().getConfiguration().getLocales().get(0).getLanguage()
                .equals(ConstantsStrings.LOCAL_RUS)) {
            binding.localisation.check(R.id.localisationRus);
        } else {
            binding.localisation.check(R.id.localisationEng);
        }
    }

    private void processingSwitchLocalisation(int indexRadioButton) {
        String locale = "";
        switch (indexRadioButton) {
            case (R.id.localisationRus):
                locale = ConstantsStrings.LOCAL_RUS;
                break;
            case (R.id.localisationEng):
                locale = ConstantsStrings.LOCAL_ENG;
                break;
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale));
        dataViewModel.changeCitiesLocale(locale);
    }

    private void setSwitchWeatherParameter() {
        switch (dataViewModel.getStringWeatherParameter()) {
            case (ConstantsStrings.CELSIUS_STRING):
                binding.weatherParameter.check(R.id.celsius);
                break;
            case (ConstantsStrings.KELVIN_STRING):
                binding.weatherParameter.check(R.id.kelvin);
                break;
            case (ConstantsStrings.FAHRENHEIT_STRING):
                binding.weatherParameter.check(R.id.fahrenheit);
                break;
        }
    }

    private void processingSwitchWeatherParameter(int indexRadioButton) {
        String stringWeatherParameter = "";
        String weatherAttribute = "";
        switch (indexRadioButton) {
            case (R.id.celsius):
                stringWeatherParameter = ConstantsStrings.CELSIUS_STRING;
                weatherAttribute = ConstantsStrings.CELSIUS_ATTRIBUTE;
                break;
            case (R.id.kelvin):
                stringWeatherParameter = ConstantsStrings.KELVIN_STRING;
                weatherAttribute = ConstantsStrings.KELVIN_ATTRIBUTE;
                break;
            case (R.id.fahrenheit):
                stringWeatherParameter = ConstantsStrings.FAHRENHEIT_STRING;
                weatherAttribute = ConstantsStrings.FAHRENHEIT_ATTRIBUTE;
                break;
        }
        dataViewModel.saveWeatherParameter(stringWeatherParameter, weatherAttribute);
    }

}
