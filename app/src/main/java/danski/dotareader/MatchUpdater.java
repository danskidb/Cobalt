package danski.dotareader;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import danski.dotareader.Data.Match;

/**
 * Created by Danny on 29-6-2015.
 */
public class MatchUpdater{

    private String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&account_id=";
    private ProgressDialog pDialog;

    String steamid;
    String matchesRequested = "&matches_requested=";

    String jsonStr;
    String MatchDB;

    JSONArray matches = null;
    Match[] tempMatchArray;
    Match[] mergedTempMatchArray;

    MainActivity_v2 act;

    //TODO: Internet connection checks
    //TODO: Failsafe things

    //UPDATES THE CURRENT DATABASE.
    public void UpdateLocal(MainActivity_v2 _act) {
        act = _act;

        //Read settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        if(checksteamid != null){
            Log.d("MHA: ", "Found steamid! Let's load matches...");
            steamid = prefs.getString("steamid", null);
            MatchDB = prefs.getString("matchdb", null);

            try {
                // Getting JSON Array node
                matches = new JSONArray(MatchDB);
                Defines.CurrentMatches = new Match[matches.length()];
                Gson gson = new Gson();
                Defines.CurrentMatches = gson.fromJson(MatchDB, Match[].class);
            } catch(JSONException e){
                e.printStackTrace();
            }

            new UpdateMatches().execute();
        } else {
            Log.d("MHA: ", "Could not find steamid!");
        }
    }

    private class UpdateMatches extends AsyncTask<Void, Void, Void> {
        int newMatchAmmount = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Defines.CurrentContext);
            pDialog.setMessage("Downloading/Processing Matches");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String FinalUrl = url + steamid;
            Log.d("MatchUpdater", "Url: " + FinalUrl);
            jsonStr = sh.makeServiceCall(FinalUrl, ServiceHandler.GET);

            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject result = jsonObj.getJSONObject("result");

                    // Getting the freshest list of matches
                    matches = result.getJSONArray("matches");

                    //Define how many new matches are in the JSON
                    long latestindb = Defines.CurrentMatches[0].start_time;

                    for (int i = 0; i < matches.length(); i++) {
                        JSONObject c = matches.getJSONObject(i);
                        if (c.getLong("start_time") > latestindb) {
                            Log.e ("MatchUpdater", "FOUND A NEW MATCH");

                            newMatchAmmount++;
                        }
                    }

                    Log.e ("MatchUpdater", "New Matches found: " + newMatchAmmount);

                    //Create temporary array fetching the new individual matches
                    tempMatchArray = new Match[newMatchAmmount];

                    if(newMatchAmmount != 0){
                        for (int i = 0; i < matches.length(); i++) {
                            JSONObject c = matches.getJSONObject(i);
                            if(c.getLong("start_time") > latestindb){
                                tempMatchArray[i] = new Match(c.getInt("match_id"), i);

                            }
                        }
                    }

                    //Change old numbers:
                    for(int i = 0; i < Defines.CurrentMatches.length ;i++){
                        Defines.CurrentMatches[i].arraypos += newMatchAmmount;
                        Log.e ("MatchUpdater", "Old matches new arraypos: " + Defines.CurrentMatches[i].arraypos);
                    }

                    //Merge 'new' array with the old array
                    mergedTempMatchArray = new Match[Defines.CurrentMatches.length + tempMatchArray.length];
                    Log.e ("MatchUpdater", "New length: " + mergedTempMatchArray.length);
                    for(int i = 0; i < mergedTempMatchArray.length; i++) {
                        if(i < newMatchAmmount){
                            mergedTempMatchArray[i] = tempMatchArray[i];
                            Log.e ("MatchUpdater", "Added " + i + " To temp array NEW");

                        } else {
                            //TODO: This may become slow when you have a fuckton of matches.
                            for (int j = 0; j < Defines.CurrentMatches.length; j++){
                                if(Defines.CurrentMatches[j].arraypos == i){
                                    mergedTempMatchArray[i] = Defines.CurrentMatches[j];
                                }
                            }
                            Log.e ("MatchUpdater", "Added " + i + " To temp array OLD");

                        }
                    }
                    Log.e ("MatchUpdater", "mergedtempmatcharray length = " + mergedTempMatchArray.length);


                    //Save
                    Defines.CurrentMatches = mergedTempMatchArray;
                    tempMatchArray = null;
                    mergedTempMatchArray = null;

                    Log.e ("MatchUpdater", "currentmatches length = " + Defines.CurrentMatches.length);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MatchUpdater", "ERROR WHILE PROCESSING!");
                }

                //Serializing to file
                Gson gson = new Gson();
                MatchDB = gson.toJson(Defines.CurrentMatches);

                SaveToFile();


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            SaveToSharedPreferences();

            act.reloadMatchHistory();
            //act.recreate();

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }

    //CALLING THIS WILL REMOVE ALL THE OTHER MATCHES AND GET THE 10 LATEST.
    public void FreshMatches(){
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

        int matchesRequestedInt = 20;

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

                        //TODO: There will probably be a lot of matches here, so we may want to restrict/check this extra.
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

                SaveToFile();


            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            SaveToSharedPreferences();

            act = MainActivity_v2.thisActivity;
            act.reloadMatchHistory();


            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }

    public void SaveToFile(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String pretty = gson.toJson(Defines.CurrentMatches);

        File storage = Environment.getExternalStorageDirectory();
        File dir = new File(storage.getAbsolutePath() + "/Cobalt");
        dir.mkdirs();
        File file = new File(dir, "matchdb.json");
        FileOutputStream ops;

        try{
            ops = new FileOutputStream(file);
            ops.write(pretty.getBytes());
            ops.close();
            Log.e("SAVER" , "Saved to: " + file.getAbsolutePath());
        }catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    //RETURNS THE DATABASE AS A STRING, SAVES TO SHARED PREFS AND UPDATES THE MAIN VIEW.
    public String LoadFromFile(Boolean saveToSharedPrefs, MainActivity_v2 act){
        File storage = Environment.getExternalStorageDirectory();
        File file = new File(storage.getAbsolutePath() + "/Cobalt", "matchdb.json");

        FileInputStream ips;
        String content = "";

        try{
            ips = new FileInputStream(file);
            byte[] input = new byte[ips.available()];
            while(ips.read(input) != -1){}
            content += new String(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(saveToSharedPrefs){
            SaveToSharedPreferences(content);
        }

        UpdateLocal(act);

        return content;
    }

    //RETURNS THE DATABASE AS A STRING.
    public String LoadFromFile(){
        File storage = Environment.getExternalStorageDirectory();
        File file = new File(storage.getAbsolutePath() + "/Cobalt", "matchdb.json");

        FileInputStream ips;
        String content = "";

        try{
            ips = new FileInputStream(file);
            byte[] input = new byte[ips.available()];
            while(ips.read(input) != -1){}
            content += new String(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }


    public void SaveToSharedPreferences(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext()).edit();
        editor.remove("matchdb");
        editor.putString("matchdb", MatchDB);
        editor.apply();
    }

    public void SaveToSharedPreferences(String jsonString){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext()).edit();
        editor.remove("matchdb");
        editor.putString("matchdb", jsonString);
        editor.apply();
    }


}
