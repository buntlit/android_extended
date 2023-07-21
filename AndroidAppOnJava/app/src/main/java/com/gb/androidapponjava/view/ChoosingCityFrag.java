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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentChooseCityBinding;
import com.gb.androidapponjava.model.Model;
import com.gb.androidapponjava.viewmodel.DataViewModel;

import java.util.List;
import java.util.regex.Pattern;

public class ChoosingCityFrag extends Fragment {

    private DataViewModel dataViewModel;
    private FragmentChooseCityBinding fragmentChooseCityBinding;
    final CitiesAdapter adapter = new CitiesAdapter();
    private RecyclerView recyclerView = null;
    private List<String> cities;
    private MutableLiveData<Model> liveData;

    Pattern checkCity = Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}$");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentChooseCityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_city, container, false);
        dataViewModel = new ViewModelProvider(getActivity()).get(DataViewModel.class);
        liveData = dataViewModel.getLiveData();
        fragmentChooseCityBinding.setChoosingCityViewmodel(dataViewModel);
        fragmentChooseCityBinding.executePendingBindings();
        initView();
        return fragmentChooseCityBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        liveData.observe(getActivity(), new Observer<Model>() {
            @Override
            public void onChanged(Model model) {
                changeData();
            }
        });

    }

    private void initView() {
        getActivity().setTitle(R.string.choosing_city_name);
        if (liveData.getValue().isDarkTheme() == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        fragmentChooseCityBinding.checkBoxHumidity.setChecked(liveData.getValue().isCheckBoxHumidity());
        fragmentChooseCityBinding.checkBoxPressure.setChecked(liveData.getValue().isCheckBoxPressure());
        fragmentChooseCityBinding.checkBoxWindSpeed.setChecked(liveData.getValue().isCheckBoxWindSpeed());
        recyclerView = fragmentChooseCityBinding.recyclerViewCityList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cities = liveData.getValue().getCities();
        adapter.setCities(cities);
        recyclerView.setAdapter(adapter);
    }

    private void changeData() {
        fragmentChooseCityBinding.checkBoxHumidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                liveData.getValue().setCheckBoxHumidity(b);
                liveData.setValue(liveData.getValue());
            }
        });

        fragmentChooseCityBinding.checkBoxPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                liveData.getValue().setCheckBoxPressure(b);
                liveData.setValue(liveData.getValue());
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

        adapter.setOnItemClickListener(new CitiesAdapter.OnItemClickListener() {
            @Override
            public void onCityTextClick(View view, int position) {
                saveLiveData(cities.get(position), position);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    startShowWeatherFragment();
                }
            }

        });
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

    private void saveLiveData(String cityName, int position) {
        liveData.getValue().setCities(cities);
        liveData.getValue().setCityName(cityName);
        liveData.getValue().setCityIndex(position);
        liveData.setValue(liveData.getValue());
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
}
