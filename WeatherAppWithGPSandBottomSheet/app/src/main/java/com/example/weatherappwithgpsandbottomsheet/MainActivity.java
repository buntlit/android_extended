package com.example.weatherappwithgpsandbottomsheet;

import static com.example.weatherappwithgpsandbottomsheet.databinding.ActivityMainBinding.inflate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.weatherappwithgpsandbottomsheet.databinding.ActivityMainBinding;
import com.example.weatherappwithgpsandbottomsheet.ui.main.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPermissions();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getLiveData();
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityResultLauncher<String[]> locationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts
                                    .RequestMultiplePermissions(), result -> {
                            }
                    );
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }


}