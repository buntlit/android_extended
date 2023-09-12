package com.gb.androidapponjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class LowBatteryAndChangeConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String stringToToast;
        if (intent.getAction().equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager manager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting())
                stringToToast = context.getString(R.string.connected_to_internet_alert);
            else stringToToast = context.getString(R.string.internet_error);
        } else stringToToast = context.getString(R.string.low_battery_alert);
        Toast.makeText(context, stringToToast, Toast.LENGTH_LONG).show();
    }
}
