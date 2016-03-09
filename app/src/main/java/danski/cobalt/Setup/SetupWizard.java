package danski.cobalt.Setup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import danski.cobalt.Defines;
import danski.cobalt.Home.HomeActivity;
import danski.cobalt.R;
import danski.cobalt.sql.HeroRetreiver;
import danski.cobalt.sql.ItemRetreiver;
import danski.cobalt.sql.MatchListRetreiver;
import danski.cobalt.sql.SteamprofileRetreiver;

public class SetupWizard extends AppCompatActivity {

    Context context;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_wizard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_setup_wizard));
        Defines.CurrentContext = this;
        context = this;

        ImageButton login = (ImageButton) findViewById(R.id.loginSteam);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("openid")){
            Toast.makeText(context, getString(R.string.setup_sucesslogin), Toast.LENGTH_LONG).show();
            pullInitialData();
        }

    }

    public void pullInitialData(){
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Getting initial data...");
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMax(5);
        pDialog.setProgress(0);
        pDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.setMessage(getString(R.string.setup_loading_heroes));
                        pDialog.setProgress(0);
                    }
                });
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                new SteamprofileRetreiver().retreivePlayerDetails(prefs.getLong("steamid64", 0));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.setMessage(getString(R.string.setup_loading_heroes));
                        pDialog.setProgress(1);
                    }
                });
                HeroRetreiver hr = new HeroRetreiver();
                hr.retreive();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.setMessage(getString(R.string.setup_loading_items));
                        pDialog.setProgress(2);
                    }
                });
                ItemRetreiver ir = new ItemRetreiver();
                ir.retreive();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.setMessage(getString(R.string.setup_loading_match_history));
                        pDialog.setProgress(3);
                    }
                });
                MatchListRetreiver mlr = new MatchListRetreiver();
                mlr.retreive();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.setMessage(getString(R.string.setup_loading_last_matches));
                        pDialog.setProgress(4);
                    }
                });


                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putBoolean("setupcomplete", true);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.setProgress(5);
                        startActivity(new Intent(context, HomeActivity.class));
                        if (pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                        finish();
                    }
                });
            }
        }).start();
    }

}
