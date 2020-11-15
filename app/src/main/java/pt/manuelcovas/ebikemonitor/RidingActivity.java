package pt.manuelcovas.ebikemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RidingActivity extends AppCompatActivity {

    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);

        mainActivity = MainActivity.getInstance();
        mainActivity.setRidingActivity(this);
    }
}