package pt.manuelcovas.ebikemonitor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanResult;
import android.widget.Toast;

import java.util.UUID;

public class ESPeBike extends BluetoothGattCallback {

    public static final UUID EBIKE_SERVICE_UUID = UUID.fromString("00002926-0000-1000-8000-00805F9B34FB");
    public static final UUID EBIKE_TX_UUID      = UUID.fromString("0000AA01-0000-1000-8000-00805F9B34FB");
    public static final UUID EBIKE_RX_UUID      = UUID.fromString("0000AB01-0000-1000-8000-00805F9B34FB");
    private static final int EBIKE_BLE_MTU      = 517;

    private MainActivity mainActivity;
    private BluetoothGatt gattClient;

    private boolean connected;
    private boolean connecting;

    public ESPeBike(ScanResult scanResult) {
        mainActivity = MainActivity.getInstance();
        connected = false;
        connecting = true;
        scanResult.getDevice().connectGatt(mainActivity, false, this, BluetoothDevice.TRANSPORT_LE);
    }


    public boolean isConnected() {
        return connected;
    }

    public void disconnect(boolean showToast, final String reason) {
        if (gattClient != null) {
            gattClient.disconnect();
            gattClient.close();
        }

        if (connecting) {
            mainActivity.eBikeScanner.scanDialog.dismiss();
        }else{
            mainActivity.eBikeScanner.onConnectionSateChange();
        }

        connecting = false;
        connected = false;

        if (showToast)
            mainActivity.getMainExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity, reason, Toast.LENGTH_LONG).show();
                }
            });
    }


    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            gattClient = gatt;
            gattClient.discoverServices();
        }else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            disconnect(true, "Device disconnected. Status: "+status);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            gatt.requestMtu(EBIKE_BLE_MTU);
        }else{
            disconnect(true, "BLE Service discovery failed. Status: "+status);
        }
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            onConnect();
        }else{
            disconnect(true, "BLE failed to set connection MTU. Status: "+status);
        }
    }

    private void onConnect() {

        BluetoothGattService eBikeService = gattClient.getService(EBIKE_SERVICE_UUID);
        BluetoothGattCharacteristic eBikeTxCharacteristic = (eBikeService == null ? null : eBikeService.getCharacteristic(EBIKE_TX_UUID));
        BluetoothGattCharacteristic eBikeRxCharacteristic = (eBikeService == null ? null : eBikeService.getCharacteristic(EBIKE_RX_UUID));

        if (eBikeService == null || eBikeTxCharacteristic == null || eBikeRxCharacteristic == null) {

            String message = "This BLE device is incompatible.";

            if (eBikeService == null) {
                message = message.concat("\nMissing BLE service.");
            }else{
                if (eBikeTxCharacteristic == null) message = message.concat("\nMissing ESP-eBike's Tx BLE characteristic.");
                if (eBikeRxCharacteristic == null) message = message.concat("\nMissing ESP-eBike's Rx BLE characteristic.");
            }

            disconnect(true, message);
            return;
        }

        gattClient.setCharacteristicNotification(eBikeTxCharacteristic, true);
        connecting = false;
        connected = true;
        mainActivity.eBikeScanner.scanDialog.dismiss();
    }


    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (characteristic.getUuid().equals(EBIKE_RX_UUID)) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

            }else{
                disconnect(true, "BLE write failed. Status: "+status);
            }
        }
    }
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        if (characteristic.getUuid().equals(EBIKE_TX_UUID)) {
            // Process new data
        }
    }
}
