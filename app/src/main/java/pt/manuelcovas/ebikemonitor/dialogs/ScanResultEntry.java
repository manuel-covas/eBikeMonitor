package pt.manuelcovas.ebikemonitor.dialogs;

import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.graphics.Color;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import pt.manuelcovas.ebikemonitor.ESPeBike;
import pt.manuelcovas.ebikemonitor.ESPeBikeScan;
import pt.manuelcovas.ebikemonitor.MainActivity;
import pt.manuelcovas.ebikemonitor.R;


public class ScanResultEntry {

    private ScanDialog scanDialog;
    private MainActivity mainActivity;
    private ConstraintLayout entry_root;

    private RadioButton radioButton;
    private TextView deviceNameTextView, rssiTextView;
    private ImageView rssiImage;

    private ScanResult scanResult;
    private boolean matchedServiceUUID;

    public ScanResultEntry(MainActivity m, ScanDialog s) {
        scanDialog = s;
        mainActivity = m;

        entry_root = (ConstraintLayout) mainActivity.getLayoutInflater().inflate(R.layout.scan_result_entry, null);
        entry_root.setOnClickListener(onEntryClick);
        radioButton = entry_root.findViewById(R.id.radio_button);
        radioButton.setOnClickListener(onEntryClick);
        deviceNameTextView = entry_root.findViewById(R.id.device_name);
        rssiTextView = entry_root.findViewById(R.id.rssi_text);
        rssiImage = entry_root.findViewById(R.id.rssi_image);

        scanDialog.getScanResultsLayout().addView(entry_root);
    }


    public ScanResult getScanResult() { return scanResult; }
    public void updateScanResult(ScanResult s) {
        scanResult = s;
        matchedServiceUUID = false;

        int rssi = scanResult.getRssi();
        List<ParcelUuid> advertisedUUIDs = scanResult.getScanRecord().getServiceUuids();

        if (advertisedUUIDs != null) {
            for (int i = 0; i < advertisedUUIDs.size(); i++) {
                if (advertisedUUIDs.get(i).getUuid().compareTo(ESPeBike.EBIKE_SERVICE_UUID) == 0)
                    matchedServiceUUID = true;
            }
        }

        deviceNameTextView.setText(scanResult.getDevice().getName());
        rssiTextView.setText("RSSI: "+rssi);

        if (rssi < -100) {
            rssiImage.setImageResource(R.drawable.ic_round_reception_0_bar_24px);
        }else if (rssi < -90) {
            rssiImage.setImageResource(R.drawable.ic_round_reception_1_bar_24px);
        }else if (rssi < -80) {
            rssiImage.setImageResource(R.drawable.ic_round_reception_2_bar_24px);
        }else if (rssi < -65) {
            rssiImage.setImageResource(R.drawable.ic_round_reception_3_bar_24px);
        }else{
            rssiImage.setImageResource(R.drawable.ic_round_reception_4_bar_24px);
        }
    }

    public void filterUUID(boolean filterEnabled) {
        if (!matchedServiceUUID)
            entry_root.setVisibility(filterEnabled ? View.GONE : View.VISIBLE);
    }


    public void setSelected(boolean selected) {
        radioButton.setChecked(selected);
        entry_root.setBackgroundColor(selected ? ContextCompat.getColor(mainActivity, R.color.colorAccentFaded) : Color.TRANSPARENT);
    }

    private View.OnClickListener onEntryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            scanDialog.resultEntryClick(scanResult.getDevice().getAddress());
            setSelected(true);
        }
    };
}
