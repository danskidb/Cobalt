package danski.dotareader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MatchHistoryActivity extends ListActivity {

    private ProgressDialog pDialog;

    final String TAG_MATCHID = "match_id";              //numeric match id

    JSONArray matches = null;
    ArrayList<HashMap<String, String>> matchList;

    String jsonStr;
    String MatchDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_list);
        Defines.CurrentContext = MatchHistoryActivity.this;

        matchList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();


        //Read settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        if(checksteamid != null){
            Log.d("MHA: ", "Found steamid! Let's load matches...");
            jsonStr = prefs.getString("matchlist", null);
            MatchDB = prefs.getString("matchdb", null);
            new GetMatches().execute();
        } else {
            Log.d("MHA: ", "Could not find matches!");

        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("POS", position + "");
        Defines.SelectedMatch = Defines.CurrentMatches[position];

        //Intent i = new Intent(MatchHistoryActivity.this, MatchActivity.class);
        Intent i = new Intent(MatchHistoryActivity.this, TabbedMatchActivity.class);
        startActivity(i);
    }

    /**
     * Async task class to get json by making HTTP call
     * Used for filling the ListView with Heroes
     * */
    private class GetMatches extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MatchHistoryActivity.this);
            pDialog.setMessage("Loading matches...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

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

                        int matchid = c.getInt(TAG_MATCHID);

                        // tmp hashmap for single match
                        HashMap<String, String> match = new HashMap<String, String>();

                        match.put(TAG_MATCHID, matchid + "");

                        // adding contact to match list
                        matchList.add(match);
                    }

                    Gson gson = new Gson();
                    Defines.CurrentMatches = gson.fromJson(MatchDB, Match[].class);

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

            /**
             * Updating parsed JSON data into ListView
             * */
            SimpleAdapter sa = new SimpleAdapter(
                    MatchHistoryActivity.this,
                    matchList,
                    R.layout.item_matchlist,
                    new String[] { TAG_MATCHID}, //, TAG_IMAGEURL},
                    new int[] { R.id.matchlist_id} //, R.id.hero_image}
            );
            sa.setViewBinder(new MatchHistoryListItemBinder());

            ListAdapter adapter = sa;
            setListAdapter(adapter);
        }

    }


    //Extension for custom hero view
    class MatchHistoryListItemBinder implements SimpleAdapter.ViewBinder{
        public boolean setViewValue(View view, Object inputData, String textRepresentation) {
            int id = view.getId();
            String data = (String) inputData;
            switch (id) {
                /*case R.id.hero_image:
                    ImageView img = (ImageView) view.findViewById(R.id.hero_image);
                    Picasso.with(MatchHistoryActivity.this).load(data).into(img);
                    break;
                */
                case R.id.matchlist_id:
                    TextView txt = (TextView) view.findViewById(R.id.matchlist_id);
                    txt.setText(data);
                    break;
            }
            return true;

        }
    }



}
