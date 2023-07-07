package com.gb.androidapponjava;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.androidapponjava.modules.WeatherFromApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ShowWeatherFragment extends Fragment {
    public static final String PARCEL = "PARCEL";

    static ChooseCityPresenter presenter = ChooseCityPresenter.getInstance();

    static LinearLayout humidityLinear;
    static LinearLayout pressureLinear;
    static LinearLayout windSpeedLinear;


    public static ShowWeatherFragment create(Parcel parcel) {
        ShowWeatherFragment showWeatherFragment = new ShowWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARCEL, parcel);
        showWeatherFragment.setArguments(args);
        return showWeatherFragment;
    }

    public Parcel getParcel() {
        Serializable savedParcel = getArguments().getSerializable(PARCEL);
        if (savedParcel instanceof Parcel) {
            return (Parcel) savedParcel;
        } else {
            List<String> cities = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cities)));
            return new Parcel(cities.get(0), 0, cities);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView city = view.findViewById(R.id.city);
        TextView temperatureValue = view.findViewById(R.id.temperatureValue);
        TextView humidityValue = view.findViewById(R.id.humidityValue);
        TextView pressureValue = view.findViewById(R.id.pressureValue);
        TextView windSpeedValue = view.findViewById(R.id.windSpeedValue);

        humidityLinear = view.findViewById(R.id.humidityLinear);
        pressureLinear = view.findViewById(R.id.pressureLinear);
        windSpeedLinear = view.findViewById(R.id.windSpeedLinear);

        Parcel parcel = getParcel();
        int index;
        if (parcel.getCityIndex() > getResources().getStringArray(R.array.cities).length) {
            index = getResources().getStringArray(R.array.cities).length;
        } else {
            index = parcel.getCityIndex();
        }
        city.setText(parcel.getCityName());
        String cityName;
        if (Locale.getDefault().getLanguage().equals("ru") && parcel.getCityIndex() < getResources().getStringArray(R.array.cities).length) {
            setLocale("en");
            cityName = getResources().getStringArray(R.array.cities)[index];
            setLocale("ru");
        } else {
            cityName = parcel.getCityName();
        }
        WeatherFromApi weatherFromApi = new WeatherFromApi(cityName);

        weatherFromApi.getWeather(temperatureValue, humidityValue, pressureValue, windSpeedValue);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerViewForecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ForecastAdapter forecastAdapter = new ForecastAdapter(Arrays.asList(getResources().getStringArray(R.array.days)), index, getContext());
        recyclerView.setAdapter(forecastAdapter);

        showExtras();

        Button settingsButton = view.findViewById(R.id.buttonSettings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingsActivity.class));
            }
        });
        Button showWeatherOnInternet = view.findViewById(R.id.showWeatherOnInternet);
        showWeatherOnInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri address = Uri.parse("https://yandex.ru/pogoda/korolev");
                startActivity(new Intent(Intent.ACTION_VIEW, address));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        changeTheme();
    }

    public static void showExtras() {
        boolean isCheckedHumidity = presenter.getIsCheckBoxHumidity();
        boolean isCheckedPressure = presenter.getIsCheckBoxPressure();
        boolean isCheckedWindSpeed = presenter.getIsCheckBoxWindSpeed();
        if (isCheckedHumidity) {
            humidityLinear.setVisibility(View.VISIBLE);
        } else {
            humidityLinear.setVisibility(View.GONE);
        }
        if (isCheckedPressure) {
            pressureLinear.setVisibility(View.VISIBLE);
        } else {
            pressureLinear.setVisibility(View.GONE);
        }
        if (isCheckedWindSpeed) {
            windSpeedLinear.setVisibility(View.VISIBLE);
        } else {
            windSpeedLinear.setVisibility(View.GONE);
        }
    }

    private void setLocale(String region) {
        Locale locale = new Locale(region);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public void changeTheme() {
        if (presenter.getIsDarkTheme()) {
            ((AppCompatActivity) getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            ((AppCompatActivity) getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();
    }
}
