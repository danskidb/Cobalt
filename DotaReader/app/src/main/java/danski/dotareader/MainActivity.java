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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    //Buttons
    Button herolistbtn;
    Button itemlistbtn;
    Button matchesbtn;
    Button tstmatch;

    //Last match view
    RelativeLayout layout;

    ImageView hero;
    TextView started;
    TextView duration;
    TextView condition;
    TextView matchid;

    ImageView item0;
    ImageView item1;
    ImageView item2;
    ImageView item3;
    ImageView item4;
    ImageView item5;

    TextView kill;
    TextView death;
    TextView assist;
    TextView lasthit;
    TextView deny;
    TextView xpm;
    TextView gpm;


    //Other data
    boolean steamid = false;
    Match lastmatch;
    Player lastplayer;
    Long steamid64;
    Long steamid32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Defines.CurrentContext = MainActivity.this;

        find();
        clickListeners();

        Defines.CurrentContext = MainActivity.this;

        reloadMatchHistory();
        if(!steamid){
            //TODO: load setup wizard.
        }

        Defines.CurrentContext = MainActivity.this;


    }

    @Override
    protected void onResume(){
        super.onResume();

        Defines.CurrentContext = MainActivity.this;
        reloadMatchHistory();
        Defines.CurrentContext = MainActivity.this;

    }

    void reloadMatchHistory() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);

        if (checksteamid != null) {
            Log.d("MHA: ", "Found steamid! Let's load matches...");
            steamid = true;
            String MatchDB = prefs.getString("matchdb", null);
            steamid64 = Long.parseLong(checksteamid);
            steamid32 = Defines.idTo32(steamid64);

            if(MatchDB != null) {
                try {
                    // Getting JSON Array node
                    JSONArray matches = new JSONArray(MatchDB);
                    Defines.CurrentMatches = new Match[matches.length()];
                    Gson gson = new Gson();
                    Defines.CurrentMatches = gson.fromJson(MatchDB, Match[].class);
                    lastmatch = Defines.CurrentMatches[0];


                    setLastMatchData();
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

    void setLastMatchData(){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: load match view
                Defines.SelectedMatch = Defines.CurrentMatches[0];
                Intent i = new Intent(Defines.CurrentContext, TabbedMatchActivity.class);
                startActivity(i);
            }
        });

        for (int j = 0; j < lastmatch.Players.length; j++) {
            if (lastmatch.Players[j].account_id == steamid32) {
                lastplayer = lastmatch.Players[j];

                Picasso.with(Defines.CurrentContext).load(lastplayer.hero_image_url).into(hero);

                String conditionstring;
                switch (lastmatch.winningSide) {
                    case Radiant:
                        if (lastmatch.Players[j].player_slot <= 4) {
                            conditionstring = "WON";
                        } else {
                            conditionstring = "LOST";
                        }
                        break;
                    case Dire:
                        if (lastmatch.Players[j].player_slot <= 4) {
                            conditionstring = "LOST";
                        } else {
                            conditionstring = "WON";
                        }
                        break;
                    default:
                        conditionstring = "";
                        break;

                }
                condition.setText(conditionstring);

                Date origDate = new Date(lastmatch.start_time * 1000);
                String date = new SimpleDateFormat("dd-MM / HH:mm").format(origDate);
                started.setText("Started " + date);
                duration.setText("Lasted " + lastmatch.duration);
                matchid.setText("ID: " + lastmatch.matchid);

                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[0]).into(item0);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[1]).into(item1);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[2]).into(item2);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[3]).into(item3);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[4]).into(item4);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[5]).into(item5);

                kill.setText(lastplayer.kills + "");
                death.setText(lastplayer.deaths + "");
                assist.setText(lastplayer.assists + "");
                lasthit.setText(lastplayer.last_hits + "");
                deny.setText(lastplayer.denies + "");
                xpm.setText(lastplayer.xp_per_min + "");
                gpm.setText(lastplayer.gold_per_min + "");
            }
        }
    }





    void find(){
        //herolistbtn = (Button) findViewById(R.id.btn_herolist);
        matchesbtn = (Button) findViewById(R.id.btn_matches);
        tstmatch = (Button) findViewById(R.id.testmatch);

        layout = (RelativeLayout) findViewById(R.id.lastmatch_layout);

        hero = (ImageView) findViewById(R.id.lastmatch_heroimg);
        started = (TextView) findViewById(R.id.lm_started);
        duration = (TextView) findViewById(R.id.lm_lasted);
        condition = (TextView) findViewById(R.id.lm_condition);
        matchid = (TextView) findViewById(R.id.lm_id);

        item0 = (ImageView) findViewById(R.id.lm_item0);
        item1 = (ImageView) findViewById(R.id.lm_item1);
        item2 = (ImageView) findViewById(R.id.lm_item2);
        item3 = (ImageView) findViewById(R.id.lm_item3);
        item4 = (ImageView) findViewById(R.id.lm_item4);
        item5 = (ImageView) findViewById(R.id.lm_item5);

        kill = (TextView) findViewById(R.id.lm_kill);
        death = (TextView) findViewById(R.id.lm_death);
        assist = (TextView) findViewById(R.id.lm_assist);
        lasthit = (TextView) findViewById(R.id.lm_lasthit);
        deny = (TextView) findViewById(R.id.lm_deny);
        xpm = (TextView) findViewById(R.id.lm_xpm);
        gpm = (TextView) findViewById(R.id.lm_gpm);
    }

    void clickListeners(){
        /*herolistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HeroList.class));
            }
        });*/

        matchesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MatchHistoryActivity.class));
            }
        });


        //Download matches
        tstmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchUpdater mu = new MatchUpdater();
                mu.FreshMatches();
                reloadMatchHistory();
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
            ma.UpdateLocal();
            reloadMatchHistory();
            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, Preferences.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
