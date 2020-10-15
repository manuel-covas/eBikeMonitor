package pt.manuelcovas.ebikemonitor.dialogs;

import android.widget.LinearLayout;

import pt.manuelcovas.ebikemonitor.MainActivity;
import pt.manuelcovas.ebikemonitor.R;


public class ScanResultEntry {

    MainActivity mainActivity;

    public ScanResultEntry(MainActivity m, LinearLayout scanResultsLayout) {
        mainActivity = m;
        mainActivity.getLayoutInflater().inflate(R.layout.scan_result_entry, scanResultsLayout);
    }
}
