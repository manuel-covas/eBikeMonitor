package pt.manuelcovas.ebikemonitor;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanResult;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import pt.manuelcovas.ebikemonitor.datatypes.ESPeBike.ESPeBikeResponse;
import pt.manuelcovas.ebikemonitor.datatypes.ESPeBike.ESPeBikeResponseType;
import pt.manuelcovas.ebikemonitor.datatypes.ESPeBike.SystemStats;


public class ESPeBike extends BluetoothGattCallback {

    public static final UUID EBIKE_SERVICE_UUID = UUID.fromString("00002926-0000-1000-8000-00805F9B34FB");
    public static final UUID EBIKE_TX_UUID      = UUID.fromString("0000AA01-0000-1000-8000-00805F9B34FB");
    public static final UUID EBIKE_RX_UUID      = UUID.fromString("0000AB01-0000-1000-8000-00805F9B34FB");
    private static final int EBIKE_BLE_MTU      = 517;

    private MainActivity mainActivity;
    private AuthenticationKey authenticationKey;

    private BluetoothGatt gattClient;
    private BluetoothGattCharacteristic eBikeTxCharacteristic;
    private BluetoothGattCharacteristic eBikeRxCharacteristic;
    private ArrayList<ESPeBikeBLECallback> waitingCallbacks;

    private boolean connected;
    private boolean connecting;

    private double speed = 0;
    private int throttle = 0;
    private double battery_current = 0;
    private double battery_charge_left = 0;
    private double battery_capacity = 0;
    private double battery_voltage = 0;

    private BatteryCell[] battery_cells;
    private SystemStats systemStats;

    public ESPeBike(ScanResult scanResult) {
        mainActivity = MainActivity.getInstance();
        waitingCallbacks = new ArrayList<>();

        connected = false;
        connecting = true;
        gattClient = scanResult.getDevice().connectGatt(mainActivity, false, this, BluetoothDevice.TRANSPORT_LE);
        authenticationKey = mainActivity.authenticationKey;
    }


    public boolean isConnected() {
        return connected;
    }

    public void disconnect(boolean showToast, final String reason) {
        if (!connected && !connecting) return;

        if (gattClient != null) {
            gattClient.disconnect();
            gattClient.close();
        }

        connected = false;

        if (connecting) {
            mainActivity.eBikeScanner.scanDialog.dismiss();
        }else{
            mainActivity.eBikeScanner.onConnectionSateChange();
        }

        connecting = false;

        if (showToast)
            mainActivity.getMainExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity, reason, Toast.LENGTH_LONG).show();
                }
            });
    }


    public void toggleSystemStatsStream(boolean enabled) {
        eBikeRxCharacteristic.setValue(new byte[] {
                (byte) ESPeBikeResponseType.EBIKE_COMMAND_SYSTEM_STATS_STREAM.ordinal(),
                (byte) (enabled ? 0x01 : 0x00)
        });
        gattClient.writeCharacteristic(eBikeRxCharacteristic);
    }

    public void toggleUnlocked(boolean unlocked) {
        waitingCallbacks.add(new ESPeBikeBLECallback(ESPeBikeResponseType.EBIKE_COMMAND_AUTH_GET_CHALLENGE) {
            @Override
            public void run() {
                byte[] command = {
                        (byte) ESPeBikeResponseType.EBIKE_COMMAND_AUTHED_COMMAND_TOGGLE_UNLOCK.ordinal(),
                        (byte) (unlocked ? 0x01 : 0x00)
                };
                byte[] challenge = super.receivedResponse.extraData;
                byte[] signature = authenticationKey.signAuthedCommand(command, challenge);

                eBikeRxCharacteristic.setValue(ByteBuffer.allocate(command.length + signature.length).put(command).put(signature).array());
                gattClient.writeCharacteristic(eBikeRxCharacteristic);
            }
        });
    }


    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
        if (newState == BluetoothGatt.STATE_CONNECTED && connecting) {
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

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (characteristic.getUuid().equals(EBIKE_RX_UUID)) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                disconnect(true, "BLE write failed. Status: "+status);
            }
            mainActivity.getMainExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity,"Successful write.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        if (characteristic.getUuid().equals(EBIKE_TX_UUID)) {
            // Process new data
            processBLEMessage(characteristic.getValue());
        }
    }

    private void onConnect() {

        BluetoothGattService eBikeService = gattClient.getService(EBIKE_SERVICE_UUID);
        eBikeTxCharacteristic = (eBikeService == null ? null : eBikeService.getCharacteristic(EBIKE_TX_UUID));
        eBikeRxCharacteristic = (eBikeService == null ? null : eBikeService.getCharacteristic(EBIKE_RX_UUID));

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

        gattClient.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
        gattClient.setCharacteristicNotification(eBikeTxCharacteristic, true);
        connecting = false;
        connected = true;

        mainActivity.eBikeScanner.scanDialog.onConnect();
        toggleSystemStatsStream(true);
    }



    private void processBLEMessage(byte[] payload) {

        if (payload.length < 9)
            return;  // Too short, ignore. Must contain response byte and two 4 byte error integers.

        ESPeBikeResponse response = new ESPeBikeResponse(payload);

        if (response.hasError) {
            String message = response.eBikeResponseType + " (0x"+Integer.toHexString(response.eBikeResponseType.ordinal())+") failed!\n\n" +
                             response.eBikeErrorType + " (0x"+Integer.toHexString(response.eBikeErrorType.ordinal())+")\n\n" +
                             "ESP-IDF error: (0x"+Integer.toHexString(response.espError)+")";
            mainActivity.getMainExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(mainActivity).setMessage(message).setTitle("ESP-eBike Error").create().show();
                }
            });
            return;
        }

        switch (response.eBikeResponseType) {

            case EBIKE_RESPONSE_SYSTEM_STATS_UPDATE:
                systemStats = new SystemStats(Arrays.copyOfRange(payload, 9, payload.length));
                mainActivity.updateUI(systemStats);
            break;

            default:
                feedCallbacks(response);
            return;
        }
    }


    private void feedCallbacks(ESPeBikeResponse response) {

        for (int i = 0; i < waitingCallbacks.size(); i++) {
            if (waitingCallbacks.get(i).wantsResponse(response)) {
                waitingCallbacks.remove(i);
                return;
            }
        }

        mainActivity.getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, "Dropped response of type " + response.eBikeResponseType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private abstract class ESPeBikeBLECallback implements Runnable {

        protected ESPeBikeResponseType responseOfInterest;
        protected ESPeBikeResponse receivedResponse;

        ESPeBikeBLECallback(ESPeBikeResponseType responseOfInterest) {
            this.responseOfInterest = responseOfInterest;
        }

        boolean wantsResponse(ESPeBikeResponse receivedResponse) {
            if (receivedResponse.eBikeResponseType != responseOfInterest)
                return false;
            this.receivedResponse = receivedResponse;
            this.run();
            return true;
        }
    }
}
