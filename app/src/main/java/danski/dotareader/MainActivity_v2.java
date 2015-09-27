package danski.dotareader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import danski.dotareader.Adaptor.Adapter_MainActivity;
import danski.dotareader.Data.Match;
import danski.dotareader.StatsActivity.TabbedStatsActivity;

/**
 * Created by danny on 24/09/2015.
 */
public class MainActivity_v2 extends ActionBarActivity{

    Button matchesbtn;
    Button statsbtn;
    Button herobtn;
    Button itembtn;
    RecyclerView rv;
    LinearLayoutManager llm;
    Boolean steamid;
    Long steamid32;
    Boolean didsetup;
    SharedPreferences prefs;

    public static MainActivity_v2 thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Defines.CurrentContext = this;
        thisActivity = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        didsetup = prefs.getBoolean("didSetup", false);
        if(!didsetup){
            //TODO: load setup wizard.
            Intent i = new Intent(Defines.CurrentContext, SetupWizard.class);
            startActivity(i);
            finish();
        } else {
            setContentView(R.layout.activity_main_v2);
            reloadMatchHistory();
        }




    }

    public void reloadMatchHistory() {
        String checksteamid = prefs.getString("steamid", null);


        if (checksteamid != null) {
            Log.d("MHA: ", "Found steamid! Let's load matches...");
            steamid = true;
            String MatchDB = prefs.getString("matchdb", null);
            Long steamid64 = Long.parseLong(checksteamid);
            steamid32 = Defines.idTo32(steamid64);

            if(MatchDB != null) {
                try {
                    // Getting JSON Array node
                    JSONArray matches = new JSONArray(MatchDB);
                    Defines.CurrentMatches = new Match[matches.length()];
                    Gson gson = new Gson();
                    Defines.CurrentMatches = gson.fromJson(MatchDB, Match[].class);

                    setupUI();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //If matches found... Display the button
            Log.d("MainActivity: ", "Now we can also display last match :)");


        } else {
            steamid = false;
            Log.d("MainActivity: ", "Could not find steamid!");
        }
    }

    void setupUI(){
        matchesbtn = (Button) findViewById(R.id.btn_matches);
        statsbtn = (Button) findViewById(R.id.btn_stats);
        herobtn = (Button) findViewById(R.id.btn_heroes);
        itembtn = (Button) findViewById(R.id.btn_items);
        rv = (RecyclerView) findViewById(R.id.mainact_recyclerview);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(Defines.CurrentContext);
        rv.setLayoutManager(llm);

        Adapter_MainActivity ad = new Adapter_MainActivity();

        rv.setAdapter(ad);

        matchesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Defines.CurrentContext, MatchHistoryActivity.class));
            }
        });

        statsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Defines.CurrentContext, TabbedStatsActivity.class));

            }
        });

        herobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            MatchUpdater ma = new MatchUpdater();
            ma.UpdateLocal(thisActivity);
            //reloadMatchHistory();
            //recreate();

            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(Defines.CurrentContext, Preferences.class));
            return true;
        }

       /* if (id == R.id.action_import) {
            MatchUpdater ma = new MatchUpdater();
            ma.LoadFromFile(true, this);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
