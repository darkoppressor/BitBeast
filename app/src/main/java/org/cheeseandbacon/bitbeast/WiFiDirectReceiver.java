package org.cheeseandbacon.bitbeast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class WiFiDirectReceiver extends BroadcastReceiver {
    private static final String TAG=WiFiDirectReceiver.class.getName();

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private Activity_Battle_Menu_Wifi activity;

    public WiFiDirectReceiver () {
    }

    public WiFiDirectReceiver (WifiP2pManager manager, WifiP2pManager.Channel channel, Activity_Battle_Menu_Wifi activity) {
        super();

        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive (Context context, Intent intent) {
        switch (intent.getAction()) {
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                Log.d(TAG, "Wi-Fi P2P state changed: " + intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1));

                if (activity != null) {
                    activity.stateToggled(intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1) == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
                }
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                if (manager != null) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                    // These provide some additional data, but none of that seems to be needed
                    //WifiP2pInfo wifiP2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                    //WifiP2pGroup group = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);

                    if (networkInfo.isConnected()) {
                        // We are connected with another device

                        manager.requestConnectionInfo(channel, activity);
                    } else {
                        // This is a disconnect

                        Log.d(TAG, "Wi-Fi P2P disconnected");

                        if (activity != null) {
                            activity.onP2pDisconnection();
                        }
                    }
                } else {
                    Log.d(TAG, "Wi-Fi P2P connection changed");
                }
                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                Log.d(TAG, "Wi-Fi P2P device changed");
                break;
        }
    }
}
