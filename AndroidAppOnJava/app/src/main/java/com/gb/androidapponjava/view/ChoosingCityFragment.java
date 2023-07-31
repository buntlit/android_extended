package com.gb.androidapponjava.view;

import static com.gb.androidapponjava.databinding.FragmentChooseCityBinding.inflate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentChooseCityBinding;
import com.gb.androidapponjava.modules.ConstantsStrings;
import com.gb.androidapponjava.viewmodel.DataViewModel;

import java.util.regex.Pattern;

public class ChoosingCityFragment extends Fragment implements CitiesAdapter.OnItemClickListener, CheckBox.OnCheckedChangeListener {

    private DataViewModel dataViewModel;
    private FragmentChooseCityBinding binding;
    private final CitiesAdapter adapter = new CitiesAdapter(this);
    private final Pattern checkCity = Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}$");

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
        dataViewModel.getLiveDataCities().observe(getViewLifecycleOwner(), model -> {
        });
        dataViewModel.getLiveDataCheckBoxes().observe(getViewLifecycleOwner(), model -> {
        });
        dataViewModel.getLiveDataSettings().observe(getViewLifecycleOwner(), model -> {
        });
        handleViews();
    }

    private void initView() {
        requireActivity().setTitle(R.string.choosing_city_name);
        binding.checkBoxHumidity.setChecked(dataViewModel.isHumidityCheckBoxPressed());
        binding.checkBoxPressure.setChecked(dataViewModel.isPressureCheckBoxPressed());
        binding.checkBoxWindSpeed.setChecked(dataViewModel.isWindSpeedCheckBoxPressed());
        binding.recyclerViewCityList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter.updateCitiesList(dataViewModel.getListCities());
        binding.recyclerViewCityList.setAdapter(adapter);
    }

    private void handleViews() {

        binding.checkBoxHumidity.setOnCheckedChangeListener(this);
        binding.checkBoxPressure.setOnCheckedChangeListener(this);
        binding.checkBoxWindSpeed.setOnCheckedChangeListener(this);

        binding.buttonShowWeather.setOnClickListener(view ->
                processingShowWeatherButton()
        );
    }

    private void startShowWeatherFragment() {
        Fragment showWeatherFragment;
        try {
            showWeatherFragment = ShowWeatherFragment.class.newInstance();
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(getId(), showWeatherFragment)
                .addToBackStack(ConstantsStrings.BACK_STACK_CHOOSE_CITI_FRAGMENT).commit();
    }

    private void processingShowWeatherButton() {
        String enterCityText = binding.enterCity.getText().toString();
        if (checkEnterCity(enterCityText)) {
            binding.enterCity.setText("");
            dataViewModel.saveCitiesData(enterCityText, true);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                startShowWeatherFragment();
            } else {
                adapter.updateCitiesList(dataViewModel.getListCities());
                binding.recyclerViewCityList.setAdapter(adapter);
            }
        }
    }

    private void saveCheckBoxes() {
        dataViewModel.saveCheckBoxesData(
                binding.checkBoxHumidity.isChecked(), binding.checkBoxPressure.isChecked(),
                binding.checkBoxWindSpeed.isChecked()
        );
    }

    private boolean checkEnterCity(String text) {
        if (checkCity.matcher(text).matches()) {
            binding.enterCity.setError(null);
            return true;
        } else {
            binding.enterCity.setError("Wrong city name");
            return false;
        }
    }

    @Override
    public void onCityTextClick(int position) {
        dataViewModel.saveCitiesData(dataViewModel.getListCities().get(position), false);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            startShowWeatherFragment();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        saveCheckBoxes();
    }
}
