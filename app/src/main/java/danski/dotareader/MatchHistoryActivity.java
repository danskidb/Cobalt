package danski.dotareader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import danski.dotareader.Data.Match;
import danski.dotareader.MatchActivity.TabbedMatchActivity;


public class MatchHistoryActivity extends ListActivity {

    private ProgressDialog pDialog;

    JSONArray matches = null;
    ArrayList<HashMap<String, String>> matchList;

    Match tempmatch;

    String MatchDB;
    Long steamid64;
    Long steamid32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_list);
        Defines.CurrentContext = MatchHistoryActivity.this;

        matchList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();


        //Read settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        if(checksteamid != null){
            Log.d("MHA: ", "Found steamid! Let's load matches...");
            MatchDB = prefs.getString("matchdb", null);
            steamid64 = Long.parseLong(prefs.getString("steamid", null));
            steamid32 = Defines.idTo32(steamid64);
            new GetMatches().execute();
        } else {
            Log.d("MHA: ", "Could not find matches!");

        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("POS", position + "");
        Defines.SelectedMatch = Defines.CurrentMatches[position];

        //Intent i = new Intent(MatchHistoryActivity.this, MatchActivity.class);
        Intent i = new Intent(MatchHistoryActivity.this, TabbedMatchActivity.class);
        startActivity(i);
    }

    /**
     * Async task class to get json by making HTTP call
     * Used for filling the ListView with Heroes
     * */
    private class GetMatches extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MatchHistoryActivity.this);
            pDialog.setMessage("Loading matches...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if (MatchDB != null) {
                try {
                    // Getting JSON Array node
                    matches = new JSONArray(MatchDB);

                    Defines.CurrentMatches = new Match[matches.length()];
                    Gson gson = new Gson();
                    Defines.CurrentMatches = gson.fromJson(MatchDB, Match[].class);

                    // looping through All matches
                    for (int i = 0; i < matches.length(); i++) {
                        JSONObject c = matches.getJSONObject(i);

                        int matchid = c.getInt("matchid");

                        // tmp hashmap for single match
                        HashMap<String, String> match = new HashMap<String, String>();

                        tempmatch = Defines.CurrentMatches[i];
                        for (int j = 0; j < tempmatch.Players.length; j++) {
                            if (tempmatch.Players[j].account_id == steamid32) {
                                //Log.e("HISTORY " + tempmatch.matchid, "WE FOUND YOU PLAYIN AS " + tempmatch.Players[j].hero_name);

                                match.put("heroimgurl", tempmatch.Players[j].hero_image_url);
                                match.put("heroname", tempmatch.Players[j].hero_name);
                                match.put("kda", tempmatch.Players[j].kills + "/" + tempmatch.Players[j].deaths + "/" + tempmatch.Players[j].assists + " :KDA");

                                String conditionstring;
                                switch(tempmatch.winningSide){
                                    case Radiant:
                                        if(tempmatch.Players[j].player_slot <= 4){
                                            conditionstring = "WON";
                                        } else {
                                            conditionstring = "LOST";
                                        }
                                        break;
                                    case Dire:
                                        if(tempmatch.Players[j].player_slot <= 4){
                                            conditionstring = "LOST";
                                        } else {
                                            conditionstring = "WON";
                                        }
                                        break;
                                    default:
                                        conditionstring = "";
                                        break;

                                }
                                match.put("condition", conditionstring);

                                Date origDate = new Date(tempmatch.start_time * 1000);
                                String date = new SimpleDateFormat("dd-MM / HH:mm").format(origDate) + " / " + tempmatch.duration;
                                match.put("time", date);

                            }
                        }


                        match.put("matchid", matchid + "");

                        // adding contact to match list
                        matchList.add(match);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            /**
             * Updating parsed JSON data into ListView
             * */
            SimpleAdapter sa = new SimpleAdapter(
                    MatchHistoryActivity.this,
                    matchList,
                    R.layout.item_matchlist,
                    new String[] {"heroimgurl", "heroname", "kda", "condition", "time"}, //, TAG_IMAGEURL},
                    new int[] { R.id.matchlist_heroimg, R.id.matchlist_heroname, R.id.matchlist_kda, R.id.matchlist_result, R.id.matchlist_time} //, R.id.hero_image}
            );
            sa.setViewBinder(new MatchHistoryListItemBinder());

            ListAdapter adapter = sa;
            setListAdapter(adapter);
        }

    }


    //Extension for custom hero view
    class MatchHistoryListItemBinder implements SimpleAdapter.ViewBinder{
        public boolean setViewValue(View view, Object inputData, String textRepresentation) {
            int id = view.getId();
            String data = (String) inputData;
            switch (id) {
                case R.id.matchlist_heroimg:
                    ImageView img = (ImageView) view.findViewById(R.id.matchlist_heroimg);
                    Picasso.with(MatchHistoryActivity.this).load(data).into(img);
                    break;

                case R.id.matchlist_heroname:
                    TextView txt = (TextView) view.findViewById(R.id.matchlist_heroname);
                    txt.setText(data);
                    break;

                case R.id.matchlist_kda:
                    TextView kda = (TextView) view.findViewById(R.id.matchlist_kda);
                    kda.setText(data);
                    break;

                case R.id.matchlist_result:
                    TextView res = (TextView) view.findViewById(R.id.matchlist_result);
                    res.setText(data);
                    if(data == "WON"){
                        res.setTextColor(Color.rgb(0,127,0));
                    } else {
                        res.setTextColor(Color.RED);
                    }
                    break;

                case R.id.matchlist_time:
                    TextView tme = (TextView) view.findViewById(R.id.matchlist_time);
                    tme.setText(data);
            }
            return true;

        }
    }



}
