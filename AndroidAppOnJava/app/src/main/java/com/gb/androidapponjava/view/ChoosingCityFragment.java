package com.gb.androidapponjava.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentChooseCityBinding;
import com.gb.androidapponjava.model.Model;
import com.gb.androidapponjava.viewmodel.DataViewModel;

import java.util.regex.Pattern;

public class ChoosingCityFragment extends Fragment implements CitiesAdapter.OnItemClickListener {

    private DataViewModel dataViewModel;
    private FragmentChooseCityBinding fragmentChooseCityBinding;
    private final CitiesAdapter adapter = new CitiesAdapter(this);
    private final Pattern checkCity = Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}$");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentChooseCityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_city, container, false);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        fragmentChooseCityBinding.setChoosingCityViewmodel(dataViewModel);
        fragmentChooseCityBinding.executePendingBindings();
        return fragmentChooseCityBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        dataViewModel.getLiveData().observe(getViewLifecycleOwner(), this::handleChangeData);
    }

    private void initView() {
        requireActivity().setTitle(R.string.choosing_city_name);

        fragmentChooseCityBinding.checkBoxHumidity.setChecked(liveData.getValue().isCheckBoxHumidity());
        fragmentChooseCityBinding.checkBoxPressure.setChecked(liveData.getValue().isCheckBoxPressure());
        fragmentChooseCityBinding.checkBoxWindSpeed.setChecked(liveData.getValue().isCheckBoxWindSpeed());
        fragmentChooseCityBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentChooseCityBinding.recyclerView.setAdapter(adapter);

        fragmentChooseCityBinding.checkBoxHumidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                model.setCheckBoxHumidity(b);
            }
        });

        fragmentChooseCityBinding.checkBoxPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                liveData.getValue().setCheckBoxPressure(b);
            }
        });

        fragmentChooseCityBinding.checkBoxWindSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                liveData.getValue().setCheckBoxWindSpeed(b);
                liveData.setValue(liveData.getValue());
            }
        });

        fragmentChooseCityBinding.buttonShowWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterCityText = fragmentChooseCityBinding.enterCity.getText().toString();
                if (checkEnterCity(enterCityText)) {
                    int position = cities.size();
                    cities.add(position, enterCityText);
                    adapter.setCities(cities);
                    recyclerView.setAdapter(adapter);
                    fragmentChooseCityBinding.enterCity.setText("");
                    saveLiveData(enterCityText, position);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        startShowWeatherFragment();
                    }
                }
            }
        });
    }

    private void handleChangeData(Model model) {
        changeDarkTheme(model.isDarkTheme());
        adapter.setCities(model.getCities());
    }

    private void startShowWeatherFragment() {
        Fragment showWeatherFrag = null;
        try {
            showWeatherFrag = ShowWeatherFrag.class.newInstance();
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(this.getId(), showWeatherFrag).addToBackStack("frag").commit();
    }

    private boolean checkEnterCity(String text) {
        if (checkCity.matcher(text).matches()) {
            fragmentChooseCityBinding.enterCity.setError(null);
            return true;
        } else {
            fragmentChooseCityBinding.enterCity.setError("Wrong city name");
            return false;
        }
    }

    @Override
    public void onCityTextClick(int position) {
        dataViewModel.onCityClick(position);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            startShowWeatherFragment();
        }
    }

    private void changeDarkTheme(Boolean isDarkMode){
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
