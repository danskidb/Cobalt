package danski.cobalt.sql;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import danski.cobalt.Defines;
import danski.cobalt.Home.home_matchhistory;

/**
 * Created by Danny on 02/02/2016.
 */
public class MatchListRetreiver {

    private String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=" + Defines.APIKEY + "&account_id=";
    public static MatchListRetreiver instance;
    ProgressDialog pDialog;
    public boolean alsoGetLatestMatch = false;
    boolean pullToRefresh;

    public MatchListRetreiver(){
        instance = this;
    }

    public void RetreiveAsync(boolean _pullToRefresh){
        pullToRefresh = _pullToRefresh;
        new RetreiveList().execute();
    }

    private class RetreiveList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(Defines.CurrentContext);
            pDialog.setMessage("Retreiving match history");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0){
            retreive();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            if(pullToRefresh){
                try{
                    home_matchhistory.instance.populateList();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }

    public void retreive(){

        ServiceHandler sh = new ServiceHandler();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext);
        String jsonStr = sh.getJSON(url + prefs.getLong("steamid64", 0), 0);

        if(jsonStr != null){
            Log.i("MLR", "Found JSON...");
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject result = jsonObj.getJSONObject("result");

                JSONArray matches = result.getJSONArray("matches");
                SQLManager sq = new SQLManager(Defines.CurrentContext);

                Log.i("MatchListRetreiver", "Adding matches to database if they don't exist yet");

                int i;
                for (i = 0 ; i < matches.length(); i++){

                    JSONObject m = matches.getJSONObject(i);
                    if(!sq.doesMatchExist(m.getLong("match_id"))) {
                        sq.addMatch(m);
                    }

                    JSONArray players = m.getJSONArray("players");
                    for(int j = 0; j < players.length(); j++){
                        JSONObject p = players.getJSONObject(j);
                        if(!sq.doesPlayerExist(p.getLong("account_id"))){
                            sq.addPlayer(p.getLong("account_id"));
                        }
                        if(!sq.isPlayerInMatch(p.getLong("account_id"), m.getLong("match_id"))){
                            sq.linkPlayerToMatch(p, m.getLong("match_id"));
                        }
                    }

                }
                Log.i("MatchListRetreiver", "Retreived: " + i + " entries");

                final MatchRetreiver mr = new MatchRetreiver();
                if(prefs.getBoolean("sett_downloadallonupdate", false)){
                    ArrayList<Long> allmatcheslist = sq.getAllMatchesList();
                    List<Long> allmatchesWithoutDetail = new ArrayList<>();

                    for(Long match : allmatcheslist){
                        if(!sq.doesMatchHaveDetails(match)){
                            allmatchesWithoutDetail.add(match);
                        }
                    }
                    allmatcheslist = null;

                    for(Long match: allmatchesWithoutDetail){
                        mr.retreive(match);
                    }

                    //todo: finish this threading, boi.
                /*    int threads = 4;
                    int chunks = allmatchesWithoutDetail.size() / threads;
                    final List<List<Long>> listoflist = Lists.partition(allmatchesWithoutDetail, chunks);

                    for(int j = 0; j < threads; j++){
                        final int chunk = j;

                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                List<Long> matchlist = listoflist.get(chunk);
                                for(Long match : matchlist){
                                    mr.retreive(match);
                                }

                            }
                        };
                        t.start();
                    }*/

                } else {
                    if(alsoGetLatestMatch){
                        mr.retreive(sq.getAllMatchesList().get(0));
                    }
                }

            } catch (JSONException e){
                e.printStackTrace();
                //todo: Handle that nothing came back.
            }

        } else {
            Log.e("MLR", "Couldn't download JSON file! Are you connected to internet?");
        }
    }
}
