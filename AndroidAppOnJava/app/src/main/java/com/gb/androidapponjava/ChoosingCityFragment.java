package com.gb.androidapponjava;

import static com.gb.androidapponjava.ShowWeatherFragment.PARCEL;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ChoosingCityFragment extends Fragment {

    final String CURRENT_CITY = "CURRENT_CITY";
    final String LIST = "LIST";

    static ChooseCityPresenter presenter = ChooseCityPresenter.getInstance();

    boolean isLandscape;
    Parcel currentParcel;
    TextInputEditText enterCity;
    CheckBox checkBoxHumidity;
    CheckBox checkBoxPressure;
    CheckBox checkBoxWindSpeed;
    final CitiesAdapter adapter = new CitiesAdapter();
    List<String> cities;
    Pattern checkCity = Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}$");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_choose_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            cities = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.cities)));
        } else {
            cities = savedInstanceState.getStringArrayList(LIST);
        }
        initList(view);
        enterCity = view.findViewById(R.id.enterCity);
        checkBoxHumidity = view.findViewById(R.id.checkBoxHumidity);
        checkBoxPressure = view.findViewById(R.id.checkBoxPressure);
        checkBoxWindSpeed = view.findViewById(R.id.checkBoxWindSpeed);
        checkBoxHumidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showExtras(compoundButton, b);
            }
        });
        checkBoxPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showExtras(compoundButton, b);
            }
        });
        checkBoxWindSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showExtras(compoundButton, b);
            }
        });

        enterCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }
                TextView textView = (TextView) view;
                String text = textView.getText().toString();
                checkEnterCity(textView, text);
            }
        });

        restoreInstance(enterCity, checkBoxHumidity, checkBoxPressure, checkBoxWindSpeed);
        Button showWeatherButton = view.findViewById(R.id.buttonShowWeather);
        showWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = enterCity.getText().toString();
                saveInstance(enterCity, checkBoxHumidity, checkBoxPressure, checkBoxWindSpeed);
                if (checkEnterCity(enterCity, text)) {
                    if (!cities.contains(text)) {
                        cities.add(cities.size(), text);
                        adapter.setCities(cities);
                        currentParcel.setCities(cities);
                        adapter.notifyItemInserted(cities.size() - 1);

                    }
                    Parcel enterParcel = new Parcel(text, cities.size() - 1, cities);
                    showWeather(enterParcel);
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            Serializable savedParcel = savedInstanceState.getSerializable(CURRENT_CITY);
            if (savedParcel instanceof Parcel) {
                currentParcel = (Parcel) savedParcel;
            }
        } else {
            currentParcel = new Parcel(cities.get(0), 0, cities);
        }
        if (isLandscape) {
            showWeather(currentParcel);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(CURRENT_CITY, currentParcel);
        outState.putStringArrayList(LIST, (ArrayList<String>) cities);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        saveInstance(enterCity, checkBoxHumidity, checkBoxPressure, checkBoxWindSpeed);
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreInstance(enterCity, checkBoxHumidity, checkBoxPressure, checkBoxWindSpeed);
        changeTheme();
    }

    private void initList(View view) {
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setCities(cities);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CitiesAdapter.OnItemClickListener() {
            @Override
            public void onCityTextClick(View view, int position) {
                currentParcel = new Parcel(cities.get(position), position, cities);
                saveInstance(enterCity, checkBoxHumidity, checkBoxPressure, checkBoxWindSpeed);
                showWeather(currentParcel);
            }
        });
    }

    private void showWeather(Parcel currentParcel) {
        if (isLandscape) {
            ShowWeatherFragment showWeatherFragment = (ShowWeatherFragment) getFragmentManager().findFragmentById(R.id.weather);
            if (showWeatherFragment == null || showWeatherFragment.getParcel().getCityIndex() != currentParcel.getCityIndex()) {
                showWeatherFragment = ShowWeatherFragment.create(currentParcel);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.weather, showWeatherFragment);
                fragmentTransaction.commit();
            }
        } else {
            Intent intent = new Intent(getContext(), ShowWeatherActivity.class);
            intent.putExtra(PARCEL, currentParcel);
            startActivity(intent);
        }
    }

    private void showExtras(CompoundButton compoundButton, boolean b) {
        if (isLandscape) {
            saveInstance(enterCity, checkBoxHumidity, checkBoxPressure, checkBoxWindSpeed);
            ShowWeatherFragment.showExtras();
        }
        View view = getActivity().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view.getRootView(), compoundButton.getText() + " checked", Snackbar.LENGTH_LONG);
        snackbar.show();
        snackbar.setAction("Back", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compoundButton.setChecked(!b);
            }
        });
    }

    private void saveInstance(EditText enterCity, CheckBox checkBoxHumidity, CheckBox checkBoxPressure, CheckBox checkBoxWindSpeed) {
        presenter.setEnterCity(enterCity.getText().toString());
        presenter.setCheckBoxHumidity(checkBoxHumidity.isChecked());
        presenter.setCheckBoxPressure(checkBoxPressure.isChecked());
        presenter.setCheckBoxWindSpeed(checkBoxWindSpeed.isChecked());
    }

    private void restoreInstance(EditText enterCity, CheckBox checkBoxHumidity, CheckBox checkBoxPressure, CheckBox checkBoxWindSpeed) {
        enterCity.setText(presenter.getEnterCity());
        checkBoxHumidity.setChecked(presenter.getIsCheckBoxHumidity());
        checkBoxPressure.setChecked(presenter.getIsCheckBoxPressure());
        checkBoxWindSpeed.setChecked(presenter.getIsCheckBoxWindSpeed());
    }

    private boolean checkEnterCity(TextView textView, String text) {
        if (checkCity.matcher(text).matches()) {
            textView.setError(null);
            return true;
        } else {
            textView.setError("Wrong city name");
            return false;
        }
    }

    public void changeTheme(){
        if(presenter.getIsDarkTheme()){
            ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();
    }
}
