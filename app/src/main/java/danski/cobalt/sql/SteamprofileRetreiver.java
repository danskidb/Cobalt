package danski.cobalt.sql;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import danski.cobalt.Defines;

/**
 * Created by Danny on 09/03/2016.
 */
public class SteamprofileRetreiver {

    public static SteamprofileRetreiver instance;
    public static String URL_profilerecv = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + Defines.APIKEY + "&steamids=";
    ProgressDialog pDialog;

    public SteamprofileRetreiver(){
        instance = this;
    }

    public void retreivePlayerDetails(Long steamid64){
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.getJSON(URL_profilerecv + steamid64, 0);

        if(jsonStr != null){

            try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject result = jsonObj.getJSONObject("response");

                JSONArray players = result.getJSONArray("players");
                SQLManager sq = new SQLManager(Defines.CurrentContext);

                Log.i("SteamProfileRetreiver", "Adding players to database if they don't exist yet");

                int i;
                for (i = 0 ; i < players.length(); i++){

                    JSONObject m = players.getJSONObject(i);
                    if(!sq.doesPlayerExist(steamid64)) {
                        sq.addPlayer(steamid64);
                        sq.updatePlayerDetail(m.getString("steamid"), m.getString("personaname"), m.getString("profileurl"), m.getString("avatarfull"), m.getString("avatarmedium"));
                        Log.i("Steamprofilereceiver", "Hello, " + m.getString("personaname") + "!");
                    } else {
                        sq.updatePlayerDetail(m.getString("steamid"), m.getString("personaname"), m.getString("profileurl"), m.getString("avatarfull"), m.getString("avatarmedium"));
                        Log.i("Steamprofilereceiver", m.getString("personaname") + " already exists, apparently...");
                    }

                }
                Log.i("SteamProfileRetreiver", "Retreived: " + i + " entries");


            } catch (JSONException e){
                e.printStackTrace();
                //todo: Handle that nothing came back.
            }

        }

    }

    public void retreivePlayerDetails(Long[] steamid64s){

    }



}
