package danski.cobalt.sql;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import danski.cobalt.Defines;
import danski.cobalt.Home.home_matchhistory;

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

    public void retreive(long matchid){
        matchToRetreive = matchid;
        new RetreiveMatch().execute();
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

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.getJSON(url + matchToRetreive, 0);

            if(jsonStr != null){

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

            }

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
}