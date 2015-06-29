package danski.dotareader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

import com.squareup.picasso.Picasso;

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
    final String TAG_IMAGEURL = "hero_image_url";

    JSONArray heroes = null;
    ArrayList<HashMap<String, String>> heroList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        Defines.CurrentContext = HeroList.this;

        heroList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();

        new GetHeroes().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     * Used for filling the ListView with Heroes
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

            //Log.d("Response: ", "> " + jsonStr);

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
                        String baldHero = name.replace("npc_dota_hero_", "");
                        String HeroImageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/" + baldHero + "_full.png";    //Hero Image URL
                        //Log.d("Hero Image Url", HeroImageUrl);

                        // tmp hashmap for single contact
                        HashMap<String, String> hero = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        hero.put(TAG_NAME, name);
                        hero.put(TAG_ID, id + "");
                        hero.put(TAG_LOCNAM, localized_name);
                        hero.put(TAG_IMAGEURL, HeroImageUrl);

                        // adding contact to hero list
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
            SimpleAdapter sa = new SimpleAdapter(
                    HeroList.this,
                    heroList,
                    R.layout.item_herolist,
                    new String[] { TAG_LOCNAM , TAG_IMAGEURL},
                    new int[] { R.id.localized_name, R.id.hero_image}
            );
            sa.setViewBinder(new HeroViewBinder());

            ListAdapter adapter = sa;
            setListAdapter(adapter);

        }

    }

    //Extension for custom hero view
    class HeroViewBinder implements SimpleAdapter.ViewBinder{
        public boolean setViewValue(View view, Object inputData, String textRepresentation) {
            int id = view.getId();
            String data = (String) inputData;
            switch (id) {
                case R.id.hero_image:
                    ImageView img = (ImageView) view.findViewById(R.id.hero_image);
                    Picasso.with(HeroList.this).load(data).into(img);
                    break;

                case R.id.localized_name:
                    TextView txt = (TextView) view.findViewById(R.id.localized_name);
                    txt.setText(data);
                    break;

            }
            return true;

        }
    }



}
