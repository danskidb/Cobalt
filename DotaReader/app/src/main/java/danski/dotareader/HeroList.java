package danski.dotareader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HeroList extends ListActivity {

    private String url = "https://api.steampowered.com/IEconDOTA2_570/GetHeroes/v0001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&language=en_us";
    private String tempurl = "https://api.myjson.com/bins/4zb6s";
    private ProgressDialog pDialog;

    final String TAG_NAME = "name";
    final String TAG_ID = "id";
    final String TAG_LOCNAM = "localized_name";

    JSONArray heroes = null;
    ArrayList<HashMap<String, String>> heroList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_list);

        heroList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();

        new GetHeroes().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetHeroes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HeroList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject result = jsonObj.getJSONObject("result");

                    // Getting JSON Array node
                    heroes = result.getJSONArray("heroes");

                    // looping through All Contacts
                    for (int i = 0; i < heroes.length(); i++) {
                        JSONObject c = heroes.getJSONObject(i);

                        String name = c.getString(TAG_NAME);
                        int id = c.getInt(TAG_ID);
                        String localized_name = c.getString(TAG_LOCNAM);

                        // tmp hashmap for single contact
                        HashMap<String, String> hero = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        hero.put(TAG_NAME, name);
                        hero.put(TAG_ID, id + "");
                        hero.put(TAG_LOCNAM, localized_name);

                        // adding contact to contact list
                        heroList.add(hero);
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
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    HeroList.this, heroList,
                    R.layout.item_herolist, new String[] { TAG_LOCNAM },
                        new int[] { R.id.localized_name});

            setListAdapter(adapter);
        }

    }
}
