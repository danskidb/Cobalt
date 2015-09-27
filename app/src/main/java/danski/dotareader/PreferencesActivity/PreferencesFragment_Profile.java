package danski.dotareader.PreferencesActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import danski.dotareader.Adaptor.PlayerDetailListAdapter;
import danski.dotareader.Data.Match;
import danski.dotareader.Defines;
import danski.dotareader.MatchUpdater;
import danski.dotareader.R;
import danski.dotareader.ServiceHandler;

/**
 * Created by danny on 27-9-15.
 */
public class PreferencesFragment_Profile extends Fragment {


    private static final String ARG_POSITION = "position";
    private int position;
    EditText usernamefield;
    TextView steamidtext;
    Button redownload;


    private ProgressDialog pDialog;

    String username;
    String steamid;

    String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&vanityurl=";

    public static PreferencesFragment_Profile newInstance(int position) {
        PreferencesFragment_Profile f = new PreferencesFragment_Profile();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.activity_preferences, container, false);

        usernamefield = (EditText) root.findViewById(R.id.field_username);
        steamidtext = (TextView) root.findViewById(R.id.tv_steamid);
        redownload = (Button) root.findViewById(R.id.pref_redownload);

        //Read settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
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

        //What to do when redownload is pressed
        redownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchUpdater mu = new MatchUpdater();
                mu.FreshMatches();
            }
        });


        return root;

    }

    private class GetSteamID extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Defines.CurrentContext);
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
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext()).edit();
                        editor.putString("steamid", steamid);
                        editor.putString("username", username);
                        editor.apply();
                    }
                    else if (success == 42)
                    {
                        //TODO: Throw error couldnt find user
                        steamid = "Unknown!";
                    } else {
                        //TODO: Throw error internet/blah blah

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
