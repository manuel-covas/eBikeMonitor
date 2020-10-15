package pt.manuelcovas.ebikemonitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

public class ESPeBike {

    private boolean connected = false;


    public ESPeBike(MainActivity mainActivity, BluetoothDevice bluetoothDevice) {

    }


    public boolean isConnected() {
        return connected;
    }
}
