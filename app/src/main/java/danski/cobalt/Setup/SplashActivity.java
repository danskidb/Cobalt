package danski.cobalt.Setup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import danski.cobalt.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, SetupWizard.class);
        startActivity(intent);
        finish();
    }
}
