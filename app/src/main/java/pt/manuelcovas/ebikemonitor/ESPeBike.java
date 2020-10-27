package pt.manuelcovas.ebikemonitor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
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
    private boolean connected = false;

    public ESPeBike(ScanResult scanResult) {
        mainActivity = MainActivity.getInstance();
        scanResult.getDevice().connectGatt(mainActivity, false, this, BluetoothDevice.TRANSPORT_LE);
    }


    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            gattClient = gatt;
            gattClient.discoverServices();
        }else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            gattClient.close();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            gatt.requestMtu(EBIKE_BLE_MTU);
        }else{
            // Failed
        }
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            gattClient.setCharacteristicNotification(gatt.getService(EBIKE_SERVICE_UUID).getCharacteristic(EBIKE_TX_UUID), true);
            connected = true;
            mainActivity.eBikeScanner.scanDialog.dismiss();
        }else{
            // Failed
        }
    }


    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (characteristic.getUuid().equals(EBIKE_RX_UUID)) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

            }else{
                // Failed
            }
        }
    }
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        if (characteristic.getUuid().equals(EBIKE_TX_UUID)) {
            // Process new data
        }
    }



    private void subscribeValues() {
      /*List<BluetoothGattCharacteristic> characteristics = gattConnection.getService(BLE_SERVICE_UUID).getCharacteristics();
        BluetoothGattCharacteristic currentChar;
        String currentCharUuid;

        for (int i = 0; i < characteristics.size(); i++) {
            currentChar = characteristics.get(i);
            currentCharUuid = currentChar.getUuid().toString();

            if (currentCharUuid.equalsIgnoreCase(BLE_AMPS_UUID_STR) ||
                    currentCharUuid.equalsIgnoreCase(BLE_CELLS_UUID_STR) ||
                    currentCharUuid.equalsIgnoreCase(BLE_SPEED_UUID_STR) ||
                    currentCharUuid.equalsIgnoreCase(BLE_SYSTEM_STATUS_UUID_STR) ||
                    currentCharUuid.equalsIgnoreCase(BLE_THROTTLE_UUID_STR))
            {
                gattConnection.setCharacteristicNotification(currentChar, true);
            }

            if (currentCharUuid.equalsIgnoreCase(BLE_SYSTEM_VALUES_UUID_STR)) {
                systemValuesChar = currentChar;
                gattConnection.readCharacteristic(currentChar);
            }
        }*/
    }


    public boolean isConnected() {
        return connected;
    }

    public void disconnect(int status, String reason) {
        gattClient.disconnect();
        gattClient.close();

        if (status != BluetoothGatt.GATT_SUCCESS)
            Toast.makeText(mainActivity, reason, Toast.LENGTH_LONG).show();
    }
}
