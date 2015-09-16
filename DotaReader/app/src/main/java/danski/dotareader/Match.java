package danski.dotareader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danny on 26/06/2015.
 */
public class Match {

    //Usable data
    public int arraypos;
    public int matchid;
    public String duration;
    public String firstBloodTime;
    public long start_time;
    public Sides winningSide;
    public String lobbyType;
    public String ServerRegion;
    public String GameMode;
    public Player[] Players;

    //Async
    private String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/?key=" + Defines.key + "&match_id=";

    public Match(){

    }


    public Match(int _matchid, int _arraypos){
        //Get input data
        matchid = _matchid;
        arraypos = _arraypos;

        Log.d("Match " + matchid, "STARTED PROCESSING");

        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(url + matchid, ServiceHandler.GET);

        if(jsonStr != null){
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject result = jsonObj.getJSONObject("result");

                //General data
                int[] time = Defines.splitToComponentTimes(result.getInt("duration"));
                duration = time[1] + ":" + time[2];

                time = Defines.splitToComponentTimes(result.getInt("first_blood_time"));
                firstBloodTime = time[1] + ":" + time[2];

                start_time = result.getLong("start_time");
                if(result.getBoolean("radiant_win")){
                    winningSide = Sides.Radiant;
                } else {
                    winningSide = Sides.Dire;
                }

                //Lobby type from local json
                String LobbyJSON = Defines.RawToString(R.raw.lobbies);
                if(LobbyJSON != null || LobbyJSON != "ERROR"){
                    JSONObject lobbyJSONObj = new JSONObject(LobbyJSON);
                    JSONArray lobbiesArray = lobbyJSONObj.getJSONArray("lobbies");

                    for (int i = 0; i < lobbiesArray.length(); i++){
                        JSONObject c = lobbiesArray.getJSONObject(i);

                        int id = c.getInt("id");
                        String name = c.getString("name");

                        if(result.getInt("lobby_type") == id){
                            lobbyType = name;
                        }
                    }
                }

                //Cluster from local json
                String ClusterJSON = Defines.RawToString(R.raw.regions);
                if(ClusterJSON != null || ClusterJSON != "ERROR"){
                    JSONObject clusterJSONObj = new JSONObject(ClusterJSON);
                    JSONArray clusterArray = clusterJSONObj.getJSONArray("regions");

                    for (int i = 0; i < clusterArray.length(); i++){
                        JSONObject c = clusterArray.getJSONObject(i);

                        int id = c.getInt("id");
                        String name = c.getString("name");

                        if(result.getInt("cluster") == id){
                            ServerRegion = name;
                        }
                    }
                }

                //Game Mode from local json
                String modjson = Defines.RawToString(R.raw.mods);
                if(modjson != null || modjson != "ERROR"){
                    JSONObject modJSONObj = new JSONObject(modjson);
                    JSONArray modArray = modJSONObj.getJSONArray("mods");

                    for (int i = 0; i < modArray.length(); i++){
                        JSONObject c = modArray.getJSONObject(i);

                        int id = c.getInt("id");
                        String name = c.getString("name");

                        if(result.getInt("game_mode") == id){
                            GameMode = name;
                        }
                    }
                }


                //Player data
                JSONArray playerJSONArr;
                playerJSONArr = result.getJSONArray("players");
                Players = new Player[playerJSONArr.length()];

                for(int i = 0; i < playerJSONArr.length(); i++)
                {
                    JSONObject c = playerJSONArr.getJSONObject(i);
                    Players[i] = new Player(c);
                }



            } catch(JSONException e){
                e.printStackTrace();
            }
        }

        /*

            Acquire steam usernames!

         */

        StringBuilder userurl = new StringBuilder();
        userurl.append("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=");
        userurl.append(Defines.key);
        userurl.append("&steamids=");
        for (int i = 0; i < Players.length; i++){
            userurl.append(Players[i].steamid64);
            userurl.append(",");
        }
        String usernameurl = userurl.toString();

        jsonStr = sh.makeServiceCall(usernameurl, ServiceHandler.GET);

        if(jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject response = jsonObj.getJSONObject("response");

                JSONArray players = response.getJSONArray("players");

                for (int i = 0; i < players.length(); i++){
                    JSONObject c = players.getJSONObject(i);
                    String steamid = c.getString("steamid");
                    String personaname = c.getString("personaname");


                    for(int j = 0; j < Players.length; j++){

                        if(steamid.equals(Players[j].steamid64 + "")){
                            Players[j].player_name = personaname;
                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        Log.d("Match " + matchid, "IS PROCESSED");

    }

}
