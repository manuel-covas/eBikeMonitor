package pt.manuelcovas.ebikemonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    ConstraintLayout speedTab, powerTab, settingsTab;
    BottomNavigationView bottomBar;

    AlertDialog authenticationKeyInfoDialog;
    FloatingActionButton authenticationKeyInfo;
    EditText authenticationKeyEditText;
    AuthenticationKey authenticationKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
    }


    protected void initUi() {
        speedTab = findViewById(R.id.speed_tab);
        powerTab = findViewById(R.id.power_tab);
        settingsTab = findViewById(R.id.settings_tab);

        bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setOnNavigationItemSelectedListener(onBottomBarNavigate);
        bottomBar.setSelectedItemId(R.id.navigation_power);


        authenticationKeyEditText = findViewById(R.id.authentication_key_edittext);
        authenticationKey = new AuthenticationKey(this);
        authenticationKeyEditText.setHorizontallyScrolling(true);
        authenticationKeyEditText.setMovementMethod(new ScrollingMovementMethod());
        authenticationKeyEditText.addTextChangedListener(authenticationKey);
        authenticationKeyInfoDialog = new AlertDialog.Builder(this).setMessage(R.string.authorization_key_info_message).setTitle("Authentication Key").setPositiveButton("OK", null).create();
        authenticationKeyInfo = findViewById(R.id.authentication_key_info);
        authenticationKeyInfo.setOnClickListener(onAuthenticationKeyInfoClick);

    }


    BottomNavigationView.OnNavigationItemSelectedListener onBottomBarNavigate = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            speedTab.setVisibility(View.GONE);
            powerTab.setVisibility(View.GONE);
            settingsTab.setVisibility(View.GONE);
            bottomBar.getMenu().getItem(0).setIcon(R.drawable.ic_twotone_timer_24px);
            bottomBar.getMenu().getItem(1).setIcon(R.drawable.ic_twotone_bolt_24px);
            bottomBar.getMenu().getItem(2).setIcon(R.drawable.ic_twotone_settings_24dp);

            switch (menuItem.getItemId()) {

                case R.id.navigation_speed:
                    bottomBar.getMenu().getItem(0).setIcon(R.drawable.ic_full_timer_24px);
                    speedTab.setVisibility(View.VISIBLE);
                    break;

                case R.id.navigation_power:
                    bottomBar.getMenu().getItem(1).setIcon(R.drawable.ic_full_bolt_24px);
                    powerTab.setVisibility(View.VISIBLE);
                    break;

                case R.id.navigation_settings:
                    bottomBar.getMenu().getItem(2).setIcon(R.drawable.ic_full_settings_24px);
                    settingsTab.setVisibility(View.VISIBLE);
                    break;
            }

            return true;
        }
    };


    View.OnClickListener onAuthenticationKeyInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            authenticationKeyInfoDialog.show();
        }
    };


}