package pt.manuelcovas.ebikemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import pt.manuelcovas.ebikemonitor.datatypes.ESPeBike.SystemStats;

public class RidingActivity extends AppCompatActivity {

    MainActivity mainActivity;

    private TextView packVoltage, packCurrent, packPercentage;
    private ImageView packVoltageIcon;
    private ProgressBar throttleProgress;
    private FloatingActionButton lockButton, unlockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_riding);
        initUI();

        mainActivity = MainActivity.getInstance();
        mainActivity.setRidingActivity(this);
    }

    private void initUI() {
        packVoltage = findViewById(R.id.pack_voltage_riding);
        packCurrent = findViewById(R.id.pack_current_riding);
        packPercentage = findViewById(R.id.pack_percentage_riding);

        packVoltageIcon = findViewById(R.id.pack_voltage_icon_riding);

        throttleProgress = findViewById(R.id.throttle_progress);
        throttleProgress.setMin(0);
        throttleProgress.setMax(100);

        lockButton = findViewById(R.id.lock_button);
        unlockButton = findViewById(R.id.unlock_button);

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.eBikeScanner.eBike.isConnected())
                    mainActivity.eBikeScanner.eBike.toggleUnlocked(false);
            }
        });
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.eBikeScanner.eBike.isConnected())
                    mainActivity.eBikeScanner.eBike.toggleUnlocked(true);
            }
        });
    }

    void updateUI(SystemStats systemStats) {
        packVoltage.setText(String.format(Locale.ENGLISH, "%.2f", systemStats.packVoltage));
        packCurrent.setText(String.format(Locale.ENGLISH, "%.2f", systemStats.packCurrent));

        throttleProgress.setProgress((int) Math.round(systemStats.throttlePercentage), true);
    }
}