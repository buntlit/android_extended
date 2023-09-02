package com.gb.androidapponjava.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.ActivityChooseCityBinding;
import com.gb.androidapponjava.viewmodel.DataViewModel;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityChooseCityBinding binding;
    private DataViewModel viewModel;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseCityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            drawer = (DrawerLayout) binding.getRoot();
            NavHostFragment navHostFragment =
                    (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            navController = navHostFragment.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(
                    navController.getGraph())
                    .setOpenableLayout(drawer)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            binding.navView.setNavigationItemSelectedListener(this);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id;
        switch (item.getItemId()) {
            case (R.id.menu_settings):
                id = R.id.settingsFragment;
                break;
            case (R.id.menu_history):
                id = R.id.weatherHistoryFragment;
                break;
            case (R.id.menu_weather):
            default:
                id = R.id.choosingCityFragment;
        }
        Navigation.findNavController(binding.container).navigate(id);
        item.setChecked(true);
        drawer.closeDrawers();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_clear_cities_data):
                viewModel.clearCitiesData();
                break;
            case (R.id.menu_clear_weather_history_data):
                viewModel.clearHistoryWeatherData();
                break;
            case (R.id.menu_clear_all_data):
                viewModel.clearAllData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
