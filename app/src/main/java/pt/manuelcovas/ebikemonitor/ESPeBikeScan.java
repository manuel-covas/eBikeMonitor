package pt.manuelcovas.ebikemonitor;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import pt.manuelcovas.ebikemonitor.dialogs.ScanDialog;

public class ESPeBikeScan implements DialogInterface.OnDismissListener {

    public static final int REQUEST_ENABLE_BT = 11;
    public static final int REQUEST_GRANT_LOC = 12;

    final MainActivity mainActivity;
    ESPeBikeScan self;
    Executor executor;
    ESPeBike eBike = null;

    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;

    FloatingActionButton rideButton;
    ScanDialog scanDialog;


    View.OnClickListener onRideButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (eBike == null || !eBike.isConnected()) {
                rideButton.setEnabled(false);
                scan();
            }else{
                // Switch activity
            }
        }
    };

    private void scan() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (!enableBluetooth())
                    return;
                if (!requestLocationPermission())
                    return;

                scanDialog = new ScanDialog(mainActivity, self);
                scanDialog.show();

                ArrayList<ScanFilter> scanFilter = new ArrayList<>();
                scanFilter.add(new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString("00002926-0000-1000-8000-00805f9b34fb")).build());

                if (bluetoothLeScanner == null)
                    bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
                bluetoothLeScanner.startScan(/*scanFilter, new ScanSettings.Builder().setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT).setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH).setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE).setReportDelay(0).setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build(),*/ scanDialog.getScanCallback());
            }
        });
    }

    @Override    // Handle dialog dismiss
    public void onDismiss(DialogInterface dialog) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, "Scanning canceled.", Toast.LENGTH_SHORT).show();
                rideButton.setEnabled(true);
                bluetoothLeScanner.stopScan(scanDialog.getScanCallback());
            }
        });
    }


    private boolean enableBluetooth() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mainActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }
        return true;
    }
    private boolean requestLocationPermission() {
        if (mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION };
            ActivityCompat.requestPermissions(mainActivity, permissions, REQUEST_GRANT_LOC);
            return false;
        }
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                if (bluetoothAdapter == null)
                    bluetoothAdapter = ((BluetoothManager) mainActivity.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
                scan();
            }else{
                Toast.makeText(mainActivity, R.string.bluetooth_denied, Toast.LENGTH_SHORT).show();
                rideButton.setEnabled(true);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_GRANT_LOC) {
            if (permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scan();
            }else{
                Toast.makeText(mainActivity, R.string.location_denied, Toast.LENGTH_SHORT).show();
                rideButton.setEnabled(true);
            }
        }
    }


    public ESPeBikeScan(MainActivity m) {
        self = this;
        this.mainActivity = m;
        executor = mainActivity.getMainExecutor();

        rideButton = mainActivity.findViewById(R.id.ride_button);
        rideButton.setOnClickListener(onRideButtonClick);

        //Check for BLE
        if (!mainActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            new AlertDialog.Builder(mainActivity)
                    .setTitle("Missing Feature")
                    .setMessage("Android reports that this device does not support Bluetooth Low Energy (BLE).\n" +
                            "This functionality is required in order to communicate with ESP-eBike.")
                    .setPositiveButton("OK", null)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mainActivity.finish();
                        }
                    }).create().show();
            return;
        }

        bluetoothAdapter = ((BluetoothManager) mainActivity.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }
}
