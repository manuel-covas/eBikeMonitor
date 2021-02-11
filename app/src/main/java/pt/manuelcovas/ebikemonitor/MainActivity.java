package pt.manuelcovas.ebikemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import pt.manuelcovas.ebikemonitor.datatypes.ESPeBike.CellVoltages;
import pt.manuelcovas.ebikemonitor.datatypes.ESPeBike.SystemStats;


public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_NAME = "eBikeMonitor_shared_preferences";

    private static MainActivity single_instance = null;
    public static MainActivity getInstance() { return single_instance; }
    public static void setInstance(MainActivity instance) { single_instance = instance; }

    PowerTab powerTab;

    ConstraintLayout speedTabContainer, powerTabContainer, settingsTabContainer;
    BottomNavigationView bottomBar;

    EditText authenticationKeyEditText;
    AuthenticationKey authenticationKey;

    ESPeBikeScan eBikeScanner;
    RidingActivity ridingActivity;

    boolean riding = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setInstance(this);

        setContentView(R.layout.activity_main);
        initUI();

        eBikeScanner = new ESPeBikeScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        riding = false;
    }

    protected void initUI() {

        powerTab = new PowerTab(this);

        speedTabContainer = findViewById(R.id.speed_tab);
        powerTabContainer = findViewById(R.id.power_tab);
        settingsTabContainer = findViewById(R.id.settings_tab);

        bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setOnNavigationItemSelectedListener(onBottomBarNavigate);
        bottomBar.setSelectedItemId(R.id.navigation_power);

        authenticationKeyEditText = findViewById(R.id.authentication_key_edittext);
        authenticationKey = new AuthenticationKey(this);
        authenticationKeyEditText.setHorizontallyScrolling(true);
        authenticationKeyEditText.setMovementMethod(new ScrollingMovementMethod());
        authenticationKeyEditText.addTextChangedListener(authenticationKey);
    }

    BottomNavigationView.OnNavigationItemSelectedListener onBottomBarNavigate = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            speedTabContainer.setVisibility(View.GONE);
            powerTabContainer.setVisibility(View.GONE);
            settingsTabContainer.setVisibility(View.GONE);
            bottomBar.getMenu().getItem(0).setIcon(R.drawable.ic_twotone_timer_24px);
            bottomBar.getMenu().getItem(1).setIcon(R.drawable.ic_twotone_bolt_24px);
            bottomBar.getMenu().getItem(2).setIcon(R.drawable.ic_twotone_settings_24dp);

            switch (menuItem.getItemId()) {

                case R.id.navigation_speed:
                    bottomBar.getMenu().getItem(0).setIcon(R.drawable.ic_full_timer_24px);
                    speedTabContainer.setVisibility(View.VISIBLE);
                    break;

                case R.id.navigation_power:
                    bottomBar.getMenu().getItem(1).setIcon(R.drawable.ic_full_bolt_24px);
                    powerTabContainer.setVisibility(View.VISIBLE);
                    break;

                case R.id.navigation_settings:
                    bottomBar.getMenu().getItem(2).setIcon(R.drawable.ic_full_settings_24px);
                    settingsTabContainer.setVisibility(View.VISIBLE);
                    break;
            }

            return true;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eBikeScanner.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        eBikeScanner.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void setRidingActivity(RidingActivity r) {
        ridingActivity = r;
        riding = true;
    }


    public void updateUI(SystemStats systemStats) {
        getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (riding) {
                    ridingActivity.updateUI(systemStats);
                }else{
                    powerTab.updateUI(systemStats);
                }
            }
        });
    }
}