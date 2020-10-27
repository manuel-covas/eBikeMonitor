package pt.manuelcovas.ebikemonitor.dialogs;

import android.app.AlertDialog;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;
import java.util.function.BiConsumer;

import pt.manuelcovas.ebikemonitor.ESPeBikeScan;
import pt.manuelcovas.ebikemonitor.MainActivity;
import pt.manuelcovas.ebikemonitor.R;

public class ScanDialog implements DialogInterface.OnDismissListener {

    private MainActivity mainActivity;
    private ESPeBikeScan eBikeScan;
    private ScanDialog self;
    private AlertDialog scanDialog;

    private ConstraintLayout scanningRoot, scanResultsRoot;
    private TextView scanningText1;
    private Switch scanFilterSwitch;
    private LinearLayout scanResultsLayout; public LinearLayout getScanResultsLayout() { return scanResultsLayout; }
    private Button cancelButton, connectButton;

    private HashMap<String, ScanResultEntry> scanResults;
    private String selectedDeviceAddress;

    public ScanDialog(ESPeBikeScan s) {
        self = this;
        eBikeScan = s;
        mainActivity = MainActivity.getInstance();
        scanResults = new HashMap<>();

        View scanDialogView = mainActivity.getLayoutInflater().inflate(R.layout.scan_dialog, null);
        scanDialog = new AlertDialog.Builder(mainActivity).setView(scanDialogView).setOnDismissListener(this).setCancelable(true).create();
        scanningRoot = scanDialogView.findViewById(R.id.scanning_root);
        scanResultsRoot = scanDialogView.findViewById(R.id.scan_results_root);
        scanningText1 = scanDialogView.findViewById(R.id.scanning_text1);
        scanFilterSwitch = scanDialogView.findViewById(R.id.scan_filter_switch);
        scanResultsLayout = scanDialogView.findViewById(R.id.scan_results_layout);
        cancelButton = scanDialogView.findViewById(R.id.scan_dialog_cancel_button);
        connectButton = scanDialogView.findViewById(R.id.scan_dialog_connect_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.connectToSelectedEntry();
            }
        });
        scanFilterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanResults.forEach(new BiConsumer<String, ScanResultEntry>() {
                    @Override
                    public void accept(String s, ScanResultEntry scanResultEntry) {
                        scanResultEntry.filterUUID(scanFilterSwitch.isChecked());
                    }
                });
            }
        });
    }


    public void show() {
        scanningRoot.setVisibility(View.VISIBLE);
        scanResultsRoot.setVisibility(View.GONE);
        scanResultsLayout.removeAllViews();
        scanDialog.show();
    }

    public void dismiss() {
        scanDialog.dismiss();
    }
    @Override    // Handle dialog dismiss
    public void onDismiss(DialogInterface dialog) {
        eBikeScan.stop();
    }


    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, final ScanResult result) {

            String deviceAddress = result.getDevice().getAddress();

            if (scanResults.isEmpty()) {
                scanningRoot.setVisibility(View.GONE);
                scanResultsRoot.setVisibility(View.VISIBLE);
            }
            if (!scanResults.containsKey(deviceAddress)) {
                ScanResultEntry scanResultEntry = new ScanResultEntry(mainActivity, self);
                scanResultEntry.updateScanResult(result);
                scanResultEntry.filterUUID(scanFilterSwitch.isChecked());
                scanResults.put(deviceAddress, scanResultEntry);
            }else{
                scanResults.get(deviceAddress).updateScanResult(result);
            }
        }
    };
    public ScanCallback getScanCallback() { return scanCallback; }


    public void resultEntryClick(final String deviceMacAddress) {
        if (selectedDeviceAddress != null) {
            scanResults.get(selectedDeviceAddress).setSelected(false);
        }else{
            connectButton.setEnabled(true);
        }
        selectedDeviceAddress = deviceMacAddress;
        scanResults.get(selectedDeviceAddress).setSelected(true);
    }


    private void connectToSelectedEntry() {
        eBikeScan.stop();
        scanResultsRoot.setVisibility(View.GONE);
        scanningRoot.setVisibility(View.VISIBLE);
        scanningText1.setText(R.string.scan_dialog_connecting);
        eBikeScan.connectToScanResult(scanResults.get(selectedDeviceAddress).getScanResult());
        Toast.makeText(mainActivity, "Connecting to "+ selectedDeviceAddress, Toast.LENGTH_SHORT).show();
    }
}
