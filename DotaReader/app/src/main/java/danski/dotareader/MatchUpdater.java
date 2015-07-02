package danski.dotareader;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Danny on 29-6-2015.
 */
public class MatchUpdater{

    private String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&account_id=";
    private ProgressDialog pDialog;

    String steamid;
    String matchesRequested = "&matches_requested=";
    int matchesRequestedInt = 5;
    float progress;

    String jsonStr;
    String MatchDB;

    JSONArray matches = null;

    public void UpdateLocal() {
        //Read settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        if(checksteamid != null){
            Log.d("MHA: ", "Found steamid! Let's load matches...");
            steamid = prefs.getString("steamid", null);
            new GetMatches().execute();
        } else {
            Log.d("MHA: ", "Could not find steamid!");
        }
    }

    private class GetMatches extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Defines.CurrentContext);
            pDialog.setMessage("Downloading/Processing Matches");
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setProgress(0);
            pDialog.setMax(matchesRequestedInt);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            String FinalUrl = url + steamid + matchesRequested + matchesRequestedInt;
            Log.d("MatchUpdater", "Url: " + FinalUrl);
            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(FinalUrl, ServiceHandler.GET);

            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject result = jsonObj.getJSONObject("result");

                    // Getting JSON Array node
                    matches = result.getJSONArray("matches");

                    Defines.CurrentMatches = new Match[matches.length()];

                    // looping through All matches
                    for (int i = 0; i < matches.length(); i++) {
                        JSONObject c = matches.getJSONObject(i);

                        int matchid = c.getInt("match_id");

                        Defines.CurrentMatches[i] = new Match(matchid, i);
                        pDialog.incrementProgressBy(1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MatchUpdater", "ERROR WHILE PROCESSING!");
                }

                //Serializing to file
                Gson gson = new Gson();
                MatchDB = gson.toJson(Defines.CurrentMatches);
                Log.d("MatchUpdater", MatchDB);

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext()).edit();
            editor.putString("matchlist", jsonStr);
            editor.putString("matchdb", MatchDB);
            editor.apply();

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }

}
