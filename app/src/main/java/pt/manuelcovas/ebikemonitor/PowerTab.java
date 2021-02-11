package pt.manuelcovas.ebikemonitor;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import pt.manuelcovas.ebikemonitor.datatypes.ESPeBike.CellVoltages;
import pt.manuelcovas.ebikemonitor.datatypes.ESPeBike.SystemStats;

public class PowerTab {

    private MainActivity mainActivity;

    private TextView    cell1Voltage,  cell2Voltage,  cell3Voltage,  cell4Voltage,  cell5Voltage,  cell6Voltage,  packVoltage,     packCurrent;
    private ImageView   cell1Icon,     cell2Icon,     cell3Icon,     cell4Icon,     cell5Icon,     cell6Icon,     packVoltageIcon;
    private ProgressBar cell1Progress, cell2Progress, cell3Progress, cell4Progress, cell5Progress, cell6Progress;

    public PowerTab(MainActivity m) {
        mainActivity = m;
        initUI();
    }

    private void initUI() {
        cell1Voltage = mainActivity.findViewById(R.id.cell1_voltage);
        cell2Voltage = mainActivity.findViewById(R.id.cell2_voltage);
        cell3Voltage = mainActivity.findViewById(R.id.cell3_voltage);
        cell4Voltage = mainActivity.findViewById(R.id.cell4_voltage);
        cell5Voltage = mainActivity.findViewById(R.id.cell5_voltage);
        cell6Voltage = mainActivity.findViewById(R.id.cell6_voltage);

        cell1Icon = mainActivity.findViewById(R.id.cell1_icon);
        cell2Icon = mainActivity.findViewById(R.id.cell2_icon);
        cell3Icon = mainActivity.findViewById(R.id.cell3_icon);
        cell4Icon = mainActivity.findViewById(R.id.cell4_icon);
        cell5Icon = mainActivity.findViewById(R.id.cell5_icon);
        cell6Icon = mainActivity.findViewById(R.id.cell6_icon);

        cell1Progress = mainActivity.findViewById(R.id.cell1_progress);
        cell2Progress = mainActivity.findViewById(R.id.cell2_progress);
        cell3Progress = mainActivity.findViewById(R.id.cell3_progress);
        cell4Progress = mainActivity.findViewById(R.id.cell4_progress);
        cell5Progress = mainActivity.findViewById(R.id.cell5_progress);
        cell6Progress = mainActivity.findViewById(R.id.cell6_progress);

        packCurrent = mainActivity.findViewById(R.id.pack_current_main);
        packVoltage = mainActivity.findViewById(R.id.pack_voltage_main);
        packVoltageIcon = mainActivity.findViewById(R.id.pack_bolt);
    }

    public void updateUI(SystemStats systemStats) {

        CellVoltages cellVoltages = systemStats.cellVoltages;

        mainActivity.getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {
                cell1Voltage.setText(String.format(Locale.ENGLISH, "%.3f", cellVoltages.cellVoltages[0]));
                cell2Voltage.setText(String.format(Locale.ENGLISH, "%.3f", cellVoltages.cellVoltages[1]));
                cell3Voltage.setText(String.format(Locale.ENGLISH, "%.3f", cellVoltages.cellVoltages[4]));
                cell4Voltage.setText(String.format(Locale.ENGLISH, "%.3f", cellVoltages.cellVoltages[5]));
                cell5Voltage.setText(String.format(Locale.ENGLISH, "%.3f", cellVoltages.cellVoltages[6]));
                cell6Voltage.setText(String.format(Locale.ENGLISH, "%.3f", cellVoltages.cellVoltages[9]));

                packVoltage.setText(String.format(Locale.ENGLISH, "%.2f", systemStats.packVoltage));
                packCurrent.setText(String.format(Locale.ENGLISH, "%.2f", systemStats.packCurrent));
            }
        });
    }
}
