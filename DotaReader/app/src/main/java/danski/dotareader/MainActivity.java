package danski.dotareader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends ActionBarActivity {

    Button herolistbtn;
    Button matchesbtn;
    Button prefsbtn;
    Button tstmatch;
    Button upd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Defines.CurrentContext = MainActivity.this;


        herolistbtn = (Button) findViewById(R.id.btn_herolist);
        herolistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HeroList.class));
            }
        });

        matchesbtn = (Button) findViewById(R.id.btn_matches);
        matchesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MatchHistoryActivity.class));
            }
        });


        //Download matches
        tstmatch = (Button) findViewById(R.id.testmatch);
        tstmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchUpdater mu = new MatchUpdater();
                mu.FreshMatches();
            }
        });

        Defines.CurrentContext = MainActivity.this;

        //
        //LAST MATCH
        //
        //Read settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        if(checksteamid != null){
            Log.d("MHA: ", "Found steamid! Let's load matches...");
            String MatchDB = prefs.getString("matchdb", null);

            try {
                // Getting JSON Array node
                JSONArray matches = new JSONArray(MatchDB);
                Defines.CurrentMatches = new Match[matches.length()];
                Gson gson = new Gson();
                Defines.CurrentMatches = gson.fromJson(MatchDB, Match[].class);
            } catch(JSONException e){
                e.printStackTrace();
            }

            //If matches found... Display the button
            Log.d("MainActivity: ", "Now we can also display last match :)");


        } else {
            Log.d("MainActivity: ", "Could not find steamid!");
            //If no matches are found... start the setup wizard.

        }

        Defines.CurrentContext = MainActivity.this;


    }

    @Override
    protected void onResume(){
        super.onResume();

        Defines.CurrentContext = MainActivity.this;

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
            ma.UpdateLocal();
            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, Preferences.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
