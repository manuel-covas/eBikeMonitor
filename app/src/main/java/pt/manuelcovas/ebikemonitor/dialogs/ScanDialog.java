package pt.manuelcovas.ebikemonitor.dialogs;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;

import pt.manuelcovas.ebikemonitor.MainActivity;
import pt.manuelcovas.ebikemonitor.R;

public class ScanDialog {

    private MainActivity mainActivity;
    private AlertDialog scanDialog;

    private ConstraintLayout scanningRoot, scanResultsRoot;
    private LinearLayout scanResultsLayout;
    private Button cancelButton, connectButton;

    public void hide() {
        scanDialog.hide();
    }

    public void show() {
        scanningRoot.setVisibility(View.VISIBLE);
        scanResultsRoot.setVisibility(View.GONE);
        scanResultsLayout.removeAllViews();
        scanDialog.show();
    }


    HashMap<String, ScanResultEntry> scanResults = new HashMap<>();

    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, final ScanResult result) {

            String deviceAddress = result.getDevice().getAddress();

            if (scanResults.isEmpty()) {
                scanningRoot.setVisibility(View.GONE);
                scanResultsRoot.setVisibility(View.VISIBLE);
            }
            if (!scanResults.containsKey(deviceAddress))
                scanResults.put(deviceAddress, new ScanResultEntry(mainActivity, scanResultsLayout));

            scanResults.get(deviceAddress).update(result);
        }
    };

    public ScanCallback getScanCallback() {
        return scanCallback;
    }



    public ScanDialog(MainActivity m, DialogInterface.OnDismissListener onDismissListener) {
        mainActivity = m;

        View scanDialogView = mainActivity.getLayoutInflater().inflate(R.layout.scan_dialog, null);
        scanDialog = new AlertDialog.Builder(mainActivity).setView(scanDialogView).setOnDismissListener(onDismissListener).setCancelable(true).create();
        scanningRoot = scanDialogView.findViewById(R.id.scanning_root);
        scanResultsRoot = scanDialogView.findViewById(R.id.scan_results_root);
        scanResultsLayout = scanDialogView.findViewById(R.id.scan_results_layout);
    }

}
