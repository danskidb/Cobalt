package danski.dotareader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SetupWizard extends ActionBarActivity {


    Button confirmbtn;
    Button continuebtn;
    EditText usernamefield;
    TextView displaynameresult;
    TextView steamidresult;

    Boolean allowContinue = false;

    private ProgressDialog pDialog;
    String username;
    String persona;
    String steamid;

    String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&vanityurl=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_wizard);

        allowContinue = false;

        confirmbtn = (Button) findViewById(R.id.setup_confirm);
        continuebtn = (Button) findViewById(R.id.setup_continue);
        usernamefield = (EditText) findViewById(R.id.setup_uname);

        displaynameresult = (TextView) findViewById(R.id.setup_result_uname);
        steamidresult = (TextView) findViewById(R.id.setup_result_id);


        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernamefield.getText().toString();
                username = username.replaceAll("\\s", "");
                usernamefield.setText(username);
                GetSteamID id = new GetSteamID();
                id.execute();
            }
        });

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allowContinue){
                    //TODO: Catch when an error happens
                    MatchUpdater mu = new MatchUpdater();
                    mu.FreshMatches();

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    editor.putBoolean("didSetup", true);
                    editor.apply();

                    finish();
                } else{
                    //TODO: Popup for user to enter correct steam creds
                }
            }
        });

    }

    private class GetSteamID extends AsyncTask<Void, Void, Void> {
        int err = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SetupWizard.this);
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
                        allowContinue = true;
                        err = 0;
                    }
                    else if (success == 42)
                    {
                        //TODO: Throw error couldnt find user
                        steamid = "Unknown!";
                        allowContinue = false;
                        err = 1;
                    } else {
                        //TODO: Throw error internet/blah blah
                        steamid = "ERROR!";
                        allowContinue = false;
                        err = 2;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            //TODO: Get details of user (current username)
            if(allowContinue) {
                jsonStr = "";
                jsonStr = sh.makeServiceCall("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&steamids=" + steamid, ServiceHandler.GET);
                if (jsonStr != null || jsonStr != "") {
                    try {
                        JSONObject obj = new JSONObject(jsonStr);
                        JSONObject response = obj.getJSONObject("response");
                        JSONArray players = response.getJSONArray("players");
                        JSONObject player = players.getJSONObject(0);
                        String result_persona = player.getString("personaname");
                        persona = result_persona;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            steamidresult.setText("SteamID: " + steamid);
            displaynameresult.setText("Display name: " + persona);

            AlertDialog.Builder alert;

            switch(err){
                case 0:
                    //Found user
                    alert = new AlertDialog.Builder(SetupWizard.this);
                    alert.setTitle("Found you!");
                    alert.setMessage("SteamID: " + steamid + "\nDisplay name: " + persona);
                    alert.setPositiveButton("OK", null);
                    alert.setCancelable(true);
                    alert.create().show();
                    break;
                case 1:
                    //no user found
                    alert = new AlertDialog.Builder(SetupWizard.this);
                    alert.setTitle("No result!");
                    alert.setMessage("Could not find you!\nCheck your profile name and internet connection.");
                    alert.setPositiveButton("OK", null);
                    alert.setCancelable(true);
                    alert.create().show();
                    break;
                case 2:
                    //Erorr
                    alert = new AlertDialog.Builder(SetupWizard.this);
                    alert.setTitle("Error");
                    alert.setMessage("An error occured! Try again...\nHave you got a stable internet connection?");
                    alert.setPositiveButton("OK", null);
                    alert.setCancelable(true);
                    alert.create().show();
                    break;
            }



        }

    }
}
