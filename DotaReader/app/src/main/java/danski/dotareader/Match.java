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
    public float duration;
    public float firstBloodTime;
    public Sides winningSide;
    public String lobbyType;
    public String ServerRegion;
    public String GameMode;

    //Async
    private String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&match_id=";

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
                duration = (float)result.getInt("duration") / 60;
                firstBloodTime = (float)result.getInt("first_blood_time") / 60;
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
                if(ClusterJSON != null || ClusterJSON != "ERROR"){
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


                Log.d("Match " + matchid, "IS PROCESSED");


                //Player data


            } catch(JSONException e){
                e.printStackTrace();
            }
        }
    }


    class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url + matchid, ServiceHandler.GET);

            if(jsonStr != null){
                try{
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject result = jsonObj.getJSONObject("result");

                    //General data
                    duration = (float)result.getInt("duration") / 60;
                    firstBloodTime = (float)result.getInt("first_blood_time") / 60;
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
                    if(ClusterJSON != null || ClusterJSON != "ERROR"){
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


                } catch(JSONException e){
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }


}
