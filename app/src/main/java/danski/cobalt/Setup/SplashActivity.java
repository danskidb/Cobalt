package danski.cobalt.Setup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import danski.cobalt.Defines;
import danski.cobalt.Home.HomeActivity;
import danski.cobalt.R;
import danski.cobalt.sql.SQLManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        Defines.CurrentContext = this;

        SQLManager sqlm = new SQLManager(this);
        sqlm.getReadableDatabase();
        sqlm.close();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("setupcomplete", false)){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, SetupWizard.class);
            startActivity(intent);
            finish();
        }
    }
}
