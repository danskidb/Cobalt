package danski.dotareader;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

    //Async
    String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&match_id=";


    public Match(int _matchid, int _arraypos){
        //Get input data
        matchid = _matchid;
        arraypos = _arraypos;

        //Get data from JSON
        new FetchData().execute();
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

                    Log.d("Match " + matchid, "Duration = " + duration);
                    Log.d("Match " + matchid, "First blood time = " + firstBloodTime);
                    Log.d("Match " + matchid, "Winning Side = " + winningSide);


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
