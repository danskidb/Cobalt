package danski.dotareader;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class Preferences extends ActionBarActivity {

    EditText usernamefield;
    TextView steamidtext;

    private ProgressDialog pDialog;

    String username;
    String steamid;

    String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&vanityurl=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        usernamefield = (EditText) findViewById(R.id.field_username);
        steamidtext = (TextView) findViewById(R.id.tv_steamid);

        //Read settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String checkuname = prefs.getString("username", null);
        if(checkuname != null) {
            username = prefs.getString("username", null);
            usernamefield.setText(username);
        }
        String checksteamid = prefs.getString("steamid", null);
        if(checksteamid != null){
            steamid = prefs.getString("steamid", null);
            steamidtext.setText("Steam ID: " + steamid);
        }

        //Listen when we are done editing. then call the Async task.
        usernamefield.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    username = usernamefield.getText().toString();
                    username = username.replaceAll("\\s", "");
                    usernamefield.setText(username);
                    new GetSteamID().execute();
                }
            }
        });
    }

    private class GetSteamID extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Preferences.this);
            pDialog.setMessage("Please wait... Finding SteamID");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + username, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject response = jsonObj.getJSONObject("response");

                    int success = response.getInt("success");
                    if(success == 1)
                    {
                        Log.d("steamid", response.getString("steamid"));
                        steamid = response.getString("steamid");
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        editor.putString("steamid", steamid);
                        editor.putString("username", username);
                        editor.apply();
                    }
                    else if (success == 42)
                    {
                        //Throw error couldnt find user
                        steamid = "Unknown!";
                    } else {
                        steamid = "ERROR!";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            steamidtext.setText("Steam ID: " + steamid);

        }

    }
}

