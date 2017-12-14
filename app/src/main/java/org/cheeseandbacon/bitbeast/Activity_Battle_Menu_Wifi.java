/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Activity_Battle_Menu_Wifi extends AppCompatActivity implements WifiP2pManager.ChannelListener, WifiP2pManager.ConnectionInfoListener, Handler.Callback {
    private static final String TAG = Activity_Battle_Menu_Wifi.class.getName();

    private static final String INSTANCE_NAME = "bitbeast";
    private static final String SERVICE_TYPE = "_bitbeast._tcp";
    public static final int SERVER_PORT = 50000;
    private static final String RECORD_LISTEN_PORT = "listenPort";
    private static final String RECORD_VERSION = "version";
    private static final String RECORD_NAME = "name";

    private static final int INVALID_VERSION_CODE = -1;

    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_RUNNABLE_READY = 1;
    public static final int MESSAGE_SOCKET_ERROR = 2;
    public static final int MESSAGE_RUNNABLE_ERROR = 3;

    private BattleIo battleIo;

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private boolean state;
    private HashMap<String, BitBeastWifiData> nearbyDevices;
    private ArrayList<Pair<WifiP2pDevice, BitBeastWifiData>> devices;

    private Pet_Status pet_status;

    private Handler handler = new Handler(this);
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private Thread connectionThread;

    private LinearLayout progressLayout;

    private class WifiDirectArrayAdapter extends ArrayAdapter<Pair<WifiP2pDevice, BitBeastWifiData>> {
        public WifiDirectArrayAdapter (Context context, int textViewResourceId, List<Pair<WifiP2pDevice, BitBeastWifiData>> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent){
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_battle_menu_wifi, null);
            }

            WifiP2pDevice device = devices.get(position).first;

            TextView textView = convertView.findViewById(R.id.list_item);

            if (textView != null) {
                textView.setTypeface(Font.font1);
                textView.setText(device.deviceName);
            }

            return convertView;
        }
    }

    private class BitBeastWifiData {
        private String name;
        private int port;

        public BitBeastWifiData (String name, int port) {
            this.name = name;
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_menu_wifi);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        progressLayout = findViewById(R.id.battleMenuWifiProgress);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        resetBattleIo();

        initialize();

        setupAdapter();

        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.button_battle_menu_wifi_refresh));
        Font.set_typeface(getAssets(), (TextView)findViewById(R.id.text_battle_menu_available_devices));

        reset();
    }

    @Override
    protected void onResume () {
        super.onResume();

        Options.set_keep_screen_on(getWindow());

        overridePendingTransition(R.anim.transition_in,R.anim.transition_out);

        pet_status=new Pet_Status();
        StorageManager.load_pet_status(this,null,pet_status);

        registerService();

        registerReceiver(broadcastReceiver, intentFilter);

        if(pet_status.get_energy()<Pet_Status.ENERGY_LOSS_BATTLE){
            int energy_short=Pet_Status.ENERGY_LOSS_BATTLE-pet_status.get_energy();

            fail(pet_status.name+" needs "+energy_short+" more energy to battle!");
        }
    }

    @Override
    protected void onPause () {
        super.onPause();

        overridePendingTransition(R.anim.transition_in,R.anim.transition_out);

        cancelConnections();

        resetBattleIo();

        unregisterService();

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Drawable_Manager.unbind_drawables(findViewById(R.id.root_battle_menu));
        System.gc();
    }

    @Override
    public void onChannelDisconnected() {
        stopThread();

        unregisterService();

        unregisterReceiver(broadcastReceiver);

        resetBattleIo();

        initialize();

        registerService();

        updateAdapter();

        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onConnectionInfoAvailable (WifiP2pInfo wifiP2pInfo) {
        boolean weAreServer = false;

        if (wifiP2pInfo.isGroupOwner) {
            // We are connected as the group owner

            Log.d(TAG, "Wi-Fi P2P connected as group owner");

            weAreServer = true;

            connectionThread = new BattleServerThread(handler, queue);
            connectionThread.start();
        } else {
            // We are connected as a peer

            Log.d(TAG, "Wi-Fi P2P connected as peer");

            connectionThread = new BattleClientThread(handler, queue, wifiP2pInfo.groupOwnerAddress, SERVER_PORT);
            connectionThread.start();
        }

        int our_seed = RNG.random_range(0, Integer.MAX_VALUE);

        battleIo = new BattleIo(weAreServer, pet_status, our_seed);

        progressLayout.setVisibility(View.VISIBLE);
    }

    public void stateToggled (boolean state) {
        this.state = state;
    }

    public void onP2pDisconnection () {
        resetBattleIo();
    }

    private void fail (String toastText) {
        Toast.makeText(this, toastText,Toast.LENGTH_SHORT).show();

        Log.e(TAG, toastText);

        finish();
    }

    public void reset () {
        pet_status = null;
    }

    private int getOurVersionCode () {
        int ourVersionCode = INVALID_VERSION_CODE;

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            ourVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Do nothing
        }

        return ourVersionCode;
    }

    private void initialize () {
        channel = manager.initialize(this, getMainLooper(), this);
        broadcastReceiver = new WiFiDirectReceiver(manager, channel, this);

        state = false;
        nearbyDevices = new HashMap<>();
        devices = new ArrayList<>();
    }

    public void refresh (View view) {
        unregisterService();
        registerService();
    }

    private void registerService () {
        HashMap<String, String> record = new HashMap<>();
        record.put(RECORD_LISTEN_PORT, String.valueOf(SERVER_PORT));
        record.put(RECORD_VERSION, String.valueOf(getOurVersionCode()));
        record.put(RECORD_NAME, pet_status.name);

        WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo.newInstance(INSTANCE_NAME, SERVICE_TYPE, record);

        manager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Local service added

                discoverServices();
            }

            @Override
            public void onFailure(int i) {
                // Failed to add local service

                switch (i) {
                    case WifiP2pManager.P2P_UNSUPPORTED:
                        fail(getString(R.string.battle_menu_wifi_p2p_unsupported));
                        break;
                    case WifiP2pManager.BUSY:
                        fail(getString(R.string.battle_menu_wifi_p2p_busy));
                        break;
                    case WifiP2pManager.ERROR:
                        fail(getString(R.string.battle_menu_wifi_p2p_error));
                        break;
                }
            }
        });
    }

    private void unregisterService () {
        manager.clearServiceRequests(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Successfully cleared any registered service discovery requests
            }

            @Override
            public void onFailure(int i) {
                // Failed to clear any registered service discovery requests
            }
        });

        manager.clearLocalServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Successfully cleared any registered local services
            }

            @Override
            public void onFailure(int i) {
                // Failed to clear any registered local services
            }
        });

        ///QQQ Is this the correct way to "clear" the listeners?
        manager.setDnsSdResponseListeners(channel, null, null);
    }

    private void discoverServices () {
        WifiP2pManager.DnsSdTxtRecordListener txtRecordListener = (fullDomain, record, device) -> {
            if (fullDomain.equalsIgnoreCase(INSTANCE_NAME + "." + SERVICE_TYPE + ".local.") && record.containsKey(RECORD_VERSION)) {
                if (getOurVersionCode() != INVALID_VERSION_CODE) {
                    // If the service's game version matches our own
                    if (record.get(RECORD_VERSION).equals(String.valueOf(getOurVersionCode()))) {
                        nearbyDevices.put(device.deviceAddress, new BitBeastWifiData(record.get(RECORD_NAME), Integer.parseInt(record.get(RECORD_LISTEN_PORT))));

                        Log.d(TAG, "Supported Bonjour TXT record arrived: " + fullDomain + " (" +
                                record.get(RECORD_NAME) + ", MAC address: " + device.deviceAddress + ", Port: " + record.get(RECORD_LISTEN_PORT) + ")");
                    } else {
                        Log.d(TAG, "Supported Bonjour TXT record arrived: " + fullDomain + " (" +
                                record.get(RECORD_NAME) + ", MAC address: " + device.deviceAddress + ", Port: " +
                                record.get(RECORD_LISTEN_PORT) + ")" + ", but versions don't match: we have " +
                                getOurVersionCode() + " and they have " + record.get(RECORD_VERSION));
                    }
                } else {
                    Log.d(TAG, "Supported Bonjour TXT record arrived: " + fullDomain + " (" +
                            record.get(RECORD_NAME) + ", MAC address: " + device.deviceAddress + ", Port: " +
                            record.get(RECORD_LISTEN_PORT) + ")" + ", but our version is invalid: we have " +
                            getOurVersionCode());
                }
            } else {
                Log.d(TAG, "Unsupported Bonjour TXT record arrived: " + fullDomain + " (" + device.deviceName +
                        ", MAC address: " + device.deviceAddress + ")");
            }
        };

        WifiP2pManager.DnsSdServiceResponseListener responseListener = (instanceName, registrationType, device) -> {
            ///QQQ Is it possible to get here without the device having arrived via the txtRecordListener first?
            if (nearbyDevices.containsKey(device.deviceAddress)) {
                BitBeastWifiData wifiData = nearbyDevices.get(device.deviceAddress);
                device.deviceName = wifiData.getName();

                devices.add(new Pair<>(device, wifiData));

                updateAdapter();

                Log.d(TAG, "Bonjour service responded with corresponding entry in nearbyDevices: " + instanceName +
                        "." + registrationType + " (" + device.deviceName + ", MAC address: " + device.deviceAddress + ", Port: " +
                        wifiData.getPort() + ")");
            } else {
                Log.d(TAG, "Bonjour service responded, but has no entry in nearbyDevices: " + instanceName + "." +
                        registrationType + " (" + device.deviceName + ", MAC address: " + device.deviceAddress + ")");
            }
        };

        manager.setDnsSdResponseListeners(channel, responseListener, txtRecordListener);

        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();

        manager.addServiceRequest(channel, serviceRequest, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Service discovery request added successfully

                manager.discoverServices(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        // Successfully started service discovery
                    }

                    @Override
                    public void onFailure(int i) {
                        // Failed to start service discovery

                        switch (i) {
                            case WifiP2pManager.P2P_UNSUPPORTED:
                                fail(getString(R.string.battle_menu_wifi_p2p_unsupported));
                                break;
                            case WifiP2pManager.BUSY:
                                fail(getString(R.string.battle_menu_wifi_p2p_busy));
                                break;
                            case WifiP2pManager.ERROR:
                                fail(getString(R.string.battle_menu_wifi_p2p_error));
                                break;
                        }
                    }
                });
            }

            @Override
            public void onFailure(int i) {
                // Failed to add service discovery request

                switch (i) {
                    case WifiP2pManager.P2P_UNSUPPORTED:
                        fail(getString(R.string.battle_menu_wifi_p2p_unsupported));
                        break;
                    case WifiP2pManager.BUSY:
                        fail(getString(R.string.battle_menu_wifi_p2p_busy));
                        break;
                    case WifiP2pManager.ERROR:
                        fail(getString(R.string.battle_menu_wifi_p2p_error));
                        break;
                }
            }
        });
    }

    private void setupAdapter () {
        ListView listView= findViewById(R.id.list_view_battle_menu);
        listView.setDividerHeight(0);
        listView.setAdapter(new WifiDirectArrayAdapter(this, R.layout.list_item_battle_menu_wifi, devices));

        listView.setOnItemClickListener((parent, view, position, id) -> connect(devices.get(position).first));
    }

    private void updateAdapter () {
        ListView listView= findViewById(R.id.list_view_battle_menu);
        ((WifiDirectArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void resetBattleIo () {
        progressLayout.setVisibility(View.GONE);

        battleIo = null;
    }

    private void connect (WifiP2pDevice server) {
        final String deviceName = server.deviceName;
        final String deviceMacAddress = server.deviceAddress;

        Log.d(TAG, "Connecting to " + deviceName + " (" + deviceMacAddress + ")");

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = deviceMacAddress;
        ///QQQ Am I sure about this line?
        config.wps.setup = WpsInfo.PBC;

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Connection succeeded

                Log.d(TAG, "Connected to " + deviceName + " (" + deviceMacAddress + ")");
            }

            @Override
            public void onFailure(int i) {
                // Connection failed

                Toast.makeText(Activity_Battle_Menu_Wifi.this, "Failed to connect to " + deviceName, Toast.LENGTH_SHORT).show();

                Log.d(TAG, "Failed to connect to " + deviceName + " (" + deviceMacAddress + ")");
            }
        });
    }

    private void stopThread() {
        if (connectionThread != null && !connectionThread.isInterrupted()) {
            connectionThread.interrupt();
        }
    }

    private void cancelConnections () {
        stopThread();

        manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Successfully cancelled any ongoing p2p group negotiations
            }

            @Override
            public void onFailure(int i) {
                // Failed to cancel any ongoing p2p group negotiations
            }
        });

        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Successfully removed the current p2p group
            }

            @Override
            public void onFailure(int i) {
                // Failed to remove the current p2p group
            }
        });

        resetBattleIo();
    }

    @Override
    public boolean handleMessage (Message message) {
        switch (message.what) {
            case MESSAGE_READ:
                byte[] readBuffer = (byte[]) message.obj;

                String rawData = new String(readBuffer, 0, message.arg1);

                Log.d(TAG, "Raw data received");

                if (battleIo != null) {
                    BattleIoDataResult result = battleIo.receiveMessage(this, rawData);

                    if (result.shouldEndConnection) {
                        resetBattleIo();

                        cancelConnections();
                    }

                    if (result.intent != null) {
                        // Start the actual battle activity, passing it the two pets' data
                        startActivity(result.intent);
                    }
                }
                break;

            case MESSAGE_RUNNABLE_READY:
                Log.d(TAG, "Runnable ready");

                if (battleIo != null) {
                    battleIo.setQueue(queue);

                    battleIo.beginBattle();
                }
                break;

            case MESSAGE_SOCKET_ERROR: case MESSAGE_RUNNABLE_ERROR:
                // An error occurred while forming a socket connection or running a runnable

                Log.d(TAG, "A socket/runnable error occurred");

                cancelConnections();

                resetBattleIo();
                break;
        }

        return true;
    }
}
