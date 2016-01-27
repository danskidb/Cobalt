package danski.cobalt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import danski.cobalt.PreferencesActivity.LoginActivity;


public class SetupWizard extends ActionBarActivity {

    ImageButton steamlogin;
    Boolean allowContinue = false;

    String steamid;
    MatchUpdater mu;
    SetupWizard wiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_wizard);
        allowContinue = false;
        Defines.CurrentContext = this;
        wiz = this;

        steamlogin = (ImageButton) findViewById(R.id.steamLogin);

        steamlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Defines.CurrentContext, LoginActivity.class));
                finish();
            }
        });


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("openid")){
                Toast.makeText(getApplicationContext(), "Successfully logged in!", Toast.LENGTH_LONG).show();

                Log.d("steamid", extras.getString("openid"));
                steamid = extras.getString("openid");

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("steamid", steamid);
                editor.apply();

                mu = new MatchUpdater();
                mu.FreshMatches(wiz);

                editor.putBoolean("didSetup", true);
                editor.apply();

            }

        }

    }
}
