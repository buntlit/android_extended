package com.gb.androidapponjava.view;

import static com.gb.androidapponjava.databinding.FragmentChooseCityBinding.inflate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentChooseCityBinding;
import com.gb.androidapponjava.viewmodel.DataViewModel;

import java.util.regex.Pattern;

public class ChoosingCityFragment extends Fragment implements CitiesAdapter.OnItemClickListener, CheckBox.OnClickListener {

    private DataViewModel viewModel;
    private FragmentChooseCityBinding binding;
    private final CitiesAdapter adapter = new CitiesAdapter(this);
    private final Pattern checkCity = Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}$");

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
            setAdapter();
        });
        viewModel.getLiveDataCheckBoxes().observe(getViewLifecycleOwner(), model -> {
        });
        viewModel.getLiveDataSettings().observe(getViewLifecycleOwner(), model -> {
            initView();
        });
        handleViews();
    }

    private void initView() {
        requireActivity().setTitle(R.string.choosing_city_name);
        setCheckBoxes();
        setAdapter();
    }

    private void setCheckBoxes() {
        binding.checkBoxHumidity.setChecked(viewModel.isHumidityCheckBoxPressed());
        binding.checkBoxPressure.setChecked(viewModel.isPressureCheckBoxPressed());
        binding.checkBoxWindSpeed.setChecked(viewModel.isWindSpeedCheckBoxPressed());
    }

    private void setAdapter() {
        adapter.updateCitiesList(viewModel.getListCities());
        binding.recyclerViewCityList.setAdapter(adapter);
    }

    private void handleViews() {

        binding.checkBoxHumidity.setOnClickListener(this);
        binding.checkBoxPressure.setOnClickListener(this);
        binding.checkBoxWindSpeed.setOnClickListener(this);
        binding.buttonShowWeather.setOnClickListener(view ->
                processingShowWeatherButton()
        );
    }

    private void startShowWeatherFragment() {
        if (viewModel.getWeather() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.alert_dialog_title).setMessage(R.string.internet_error)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel_dialog_button, (dialogInterface, i) -> {
                            }
                    )
                    .setPositiveButton(R.string.go_to_internet_settings, (dialogInterface, i) ->
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .show();
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.showWeatherFragment);
        }
    }

    private void processingShowWeatherButton() {
        String enterCityText = binding.enterCity.getText().toString();
        if (checkEnterCity(enterCityText)) {
            binding.enterCity.setText("");
            viewModel.saveCitiesData(enterCityText, true);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                startShowWeatherFragment();
            } else {
                setAdapter();
            }
        }
    }

    private void saveCheckBoxes() {
        viewModel.saveCheckBoxesData(
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
        viewModel.saveCitiesData(viewModel.getListCities().get(position), false);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            startShowWeatherFragment();
        }
    }

    @Override
    public void onClick(View view) {
        saveCheckBoxes();
    }
}
