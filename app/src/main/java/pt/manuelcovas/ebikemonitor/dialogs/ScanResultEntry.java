package pt.manuelcovas.ebikemonitor.dialogs;

import android.bluetooth.le.ScanResult;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import pt.manuelcovas.ebikemonitor.MainActivity;
import pt.manuelcovas.ebikemonitor.R;


public class ScanResultEntry implements View.OnClickListener {

    private MainActivity mainActivity;
    private ConstraintLayout entry_root;

    private RadioButton radioButton;
    private TextView deviceNameTextView, rssiTextView;
    private ImageView rssiImage;

    public ScanResultEntry(MainActivity m, LinearLayout scanResultsLayout) {
        mainActivity = m;

        entry_root = (ConstraintLayout) mainActivity.getLayoutInflater().inflate(R.layout.scan_result_entry, null);
        radioButton = entry_root.findViewById(R.id.radio_button);
        deviceNameTextView = entry_root.findViewById(R.id.device_name);
        rssiTextView = entry_root.findViewById(R.id.rssi_text);
        rssiImage = entry_root.findViewById(R.id.rssi_image);

        scanResultsLayout.addView(entry_root);

        entry_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton.performClick();
            }
        });
    }


    public void update(ScanResult scanResult) {

        int rssi = scanResult.getRssi();

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

    @Override
    public void onClick(View v) {

    }
}
