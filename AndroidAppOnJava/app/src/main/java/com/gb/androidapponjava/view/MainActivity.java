package com.gb.androidapponjava.view;

import static com.gb.androidapponjava.modules.Constants.PREFERENCES_DEFAULT;
import static com.gb.androidapponjava.modules.Constants.PREFERENCES_EMAIL;
import static com.gb.androidapponjava.modules.Constants.PREFERENCES_NAME;
import static com.gb.androidapponjava.modules.Constants.PREFERENCES_URI;
import static com.gb.androidapponjava.modules.Constants.PREFERENCES_USERNAME;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.gb.androidapponjava.LowBatteryAndChangeConnectionReceiver;
import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.ActivityChooseCityBinding;
import com.gb.androidapponjava.databinding.HeaderMainBinding;
import com.gb.androidapponjava.viewmodel.DataViewModel;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityChooseCityBinding binding;
    private HeaderMainBinding headerBinding;
    private DataViewModel viewModel;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;
    private final BroadcastReceiver receiver = new LowBatteryAndChangeConnectionReceiver();
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseCityBinding.inflate(getLayoutInflater());
        headerBinding = HeaderMainBinding.bind(binding.navView.getHeaderView(0));
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);
        preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        init();
    }

    private void init() {
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
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        initTokenForRegister();
        handleHeader();
    }

    private void handleHeader() {

        if (preferences.contains(PREFERENCES_USERNAME)) {
            setHeaderInfo(preferences.getString(PREFERENCES_USERNAME, PREFERENCES_DEFAULT),
                    preferences.getString(PREFERENCES_EMAIL, PREFERENCES_DEFAULT),
                    preferences.getString(PREFERENCES_URI, PREFERENCES_DEFAULT));
        }

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();

        ActivityResultLauncher<IntentSenderRequest> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                try {
                                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                                    if (credential.getGoogleIdToken() != null) {

                                        SharedPreferences.Editor editor = preferences.edit();
                                        String username = credential.getDisplayName();
                                        String email = credential.getId();
                                        String uriPicture = credential.getProfilePictureUri().toString();
                                        editor.putString(PREFERENCES_USERNAME, username);
                                        editor.putString(PREFERENCES_EMAIL, email);
                                        editor.putString(PREFERENCES_URI, uriPicture);
                                        editor.apply();

                                        setHeaderInfo(username, email, uriPicture);

                                    }
                                } catch (ApiException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

        headerBinding.buttonSignIn.setOnClickListener(view ->
                oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(this, result -> {
                            IntentSenderRequest request = new IntentSenderRequest
                                    .Builder(result.getPendingIntent().getIntentSender())
                                    .build();
                            activityResultLauncher.launch(request);
                        })
                        .addOnFailureListener(this, e ->
                                Log.d("TAG", "init: " + e.getLocalizedMessage())
                        )
        );

        headerBinding.buttonSignOut.setOnClickListener(view -> {
            oneTapClient.signOut();
            headerBinding.headerTitle.setVisibility(View.GONE);
            headerBinding.buttonSignOut.setVisibility(View.GONE);
            headerBinding.buttonSignIn.setVisibility(View.VISIBLE);
            preferences.edit().clear().apply();
        });
    }

    private void setHeaderInfo(String username, String email, String picture) {
        headerBinding.buttonSignIn.setVisibility(View.GONE);
        headerBinding.buttonSignOut.setVisibility(View.VISIBLE);
        headerBinding.headerTitle.setVisibility(View.VISIBLE);
        headerBinding.usernameFromAccount.setText(username);
        headerBinding.emailFromAccount.setText(email);
        Picasso.get()
                .load(picture)
                .resize(200, 200)
                .into(headerBinding.imageFromAccount);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        binding = null;
    }

    private void initTokenForRegister() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task ->
                        Log.d("TAG", "initToken: " + task.getResult())
                );
    }
}
