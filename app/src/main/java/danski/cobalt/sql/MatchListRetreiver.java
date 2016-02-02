package danski.cobalt.sql;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import danski.cobalt.Defines;
import danski.cobalt.MainActivity;
import danski.cobalt.data.Match;

/**
 * Created by Danny on 02/02/2016.
 */
public class MatchListRetreiver {

    private String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=" + Defines.APIKEY + "&account_id=";
    private String id64 = "76561198076104596";
    public static MatchListRetreiver instance;
    ProgressDialog pDialog;

    public MatchListRetreiver(){
        instance = this;
    }

    public void retreive(){

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

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.getJSON(url + id64, 0);

            if(jsonStr != null){

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

            MainActivity.instance.populateList();
        }
    }
}
