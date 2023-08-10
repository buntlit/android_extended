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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentSettingsBinding;
import com.gb.androidapponjava.modules.Constants;
import com.gb.androidapponjava.viewmodel.DataViewModel;

public class SettingsFragment extends Fragment {
    private DataViewModel viewModel;
    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getLiveDataCities().observe(getViewLifecycleOwner(), model -> {
        });
        viewModel.getLiveDataCheckBoxes().observe(getViewLifecycleOwner(), model -> {
        });
        viewModel.getLiveDataSettings().observe(getViewLifecycleOwner(), model -> {
        });
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

        binding.buttonBack.setOnClickListener(view -> {
            Navigation.findNavController(requireView()).popBackStack();
        });

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
                .equals(Constants.LOCAL_RUS)) {
            binding.localisation.check(R.id.localisationRus);
        } else {
            binding.localisation.check(R.id.localisationEng);
        }
    }

    private void processingSwitchLocalisation(int indexRadioButton) {
        String locale = "";
        switch (indexRadioButton) {
            case (R.id.localisationRus):
                locale = Constants.LOCAL_RUS;
                break;
            case (R.id.localisationEng):
                locale = Constants.LOCAL_ENG;
                break;
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale));
        viewModel.changeCitiesLocale(locale);
    }

    private void setSwitchWeatherParameter() {
        switch (viewModel.getStringWeatherParameter()) {
            case (Constants.CELSIUS_STRING):
                binding.weatherParameter.check(R.id.celsius);
                break;
            case (Constants.KELVIN_STRING):
                binding.weatherParameter.check(R.id.kelvin);
                break;
            case (Constants.FAHRENHEIT_STRING):
                binding.weatherParameter.check(R.id.fahrenheit);
                break;
        }
    }

    private void processingSwitchWeatherParameter(int indexRadioButton) {
        String stringWeatherParameter = "";
        String weatherAttribute = "";
        switch (indexRadioButton) {
            case (R.id.celsius):
                stringWeatherParameter = Constants.CELSIUS_STRING;
                weatherAttribute = Constants.CELSIUS_ATTRIBUTE;
                break;
            case (R.id.kelvin):
                stringWeatherParameter = Constants.KELVIN_STRING;
                weatherAttribute = Constants.KELVIN_ATTRIBUTE;
                break;
            case (R.id.fahrenheit):
                stringWeatherParameter = Constants.FAHRENHEIT_STRING;
                weatherAttribute = Constants.FAHRENHEIT_ATTRIBUTE;
                break;
        }
        viewModel.saveWeatherParameter(stringWeatherParameter, weatherAttribute);
    }

}
