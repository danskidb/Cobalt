package danski.cobalt.sql;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import danski.cobalt.Defines;
import danski.cobalt.Home.home_matchhistory;
import danski.cobalt.MatchTools;

/**
 * Created by Danny on 02/02/2016.
 */
public class MatchRetreiver {

    private String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/?key=" + Defines.APIKEY + "&match_id=";
    public static MatchRetreiver instance;
    ProgressDialog pDialog;

    public long matchToRetreive;

    public MatchRetreiver(){
        instance = this;
    }

    //retreives one match.
    public void retreiveAsync(long matchid){
        matchToRetreive = matchid;
        new RetreiveMatch().execute();
    }

    //retreives the last 5 matches, regardless whether they are in the database or not.
    public void retreiveLastX(int ammount){
        SQLManager sq = new SQLManager(Defines.CurrentContext, false);
        final ArrayList<Long> last5matches = sq.getLastXMatches(ammount);

        for(Long l : last5matches){
            retreive(l);
        }

    }

    //retreives all the matches you haven't downloaded yet, up until it finds the latest downloaded.
    public void retreiveLastUntilDownloaded(){

    }

    //retreives all matches in the matchlist. WARNING: THIS CAN TAKE LONG ON A SLOW CONNECTION.
    public void retreiveAllMatches(){
        SQLManager sq = new SQLManager(Defines.CurrentContext, false);
        final ArrayList<Long> allmatches = sq.getAllMatchesList();

        for(Long l : allmatches){
            retreive(l);
        }
    }

    private class RetreiveMatch extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(Defines.CurrentContext);
            pDialog.setMessage("Grabbing match " + matchToRetreive);
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


            if (pDialog.isShowing())
                pDialog.dismiss();

            try{
                home_matchhistory.instance.populateList();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void retreive(){
        retreive(matchToRetreive);
    }

    public void retreive(long match){
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.getJSON(url + match, 0);

        if(jsonStr != null){
            Log.i("MR", "Found JSON...");
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject result = jsonObj.getJSONObject("result");

                JSONArray players = result.getJSONArray("players");
                SQLManager sq = new SQLManager(Defines.CurrentContext, false);

                sq.setMatchDetails(result);

                int i;
                for (i = 0 ; i < players.length(); i++){

                    JSONObject p = players.getJSONObject(i);
                    //if(sq.isPlayerInMatch(p.getLong("account_id"), result.getLong("match_id"))) {
                    sq.addPlayerDataToMatch(p, result.getLong("match_id"));
                }
                sq.close();

            } catch (JSONException e){
                e.printStackTrace();
                //todo: Handle that nothing came back.
            }
            if(MatchTools.instance != null) MatchTools.instance.wlaCalculated = false;
        } else {
            Log.e("MR", "Couldn't get a JSON! Are you connected to internet?");
        }
    }
}