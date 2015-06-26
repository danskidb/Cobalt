package danski.dotareader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danny on 26/06/2015.
 */
public class HeroArrayFiller extends AsyncTask<Void, Void, Void> {

    private ProgressDialog pDialog;
    public Context context;
    final String TAG_NAME = "name";
    final String TAG_ID = "id";
    final String TAG_LOCNAM = "localized_name";
    private String url = "https://api.steampowered.com/IEconDOTA2_570/GetHeroes/v0001/?key=7B5DF1FD8BA33927FAC62EF3D1DB37FB&language=en_us";

    Hero[] hero_array;
    JSONArray heroes = null;


    HeroArrayFiller(Context _context){
        context = _context;
    }

    public Hero[] getHeroes(){
        this.execute();

        return hero_array;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject result = jsonObj.getJSONObject("result");

                // Getting JSON Array node
                heroes = result.getJSONArray("heroes");

                hero_array = new Hero[heroes.length()];

                // looping through All Contacts
                for (int i = 0; i < heroes.length(); i++) {
                    JSONObject c = heroes.getJSONObject(i);

                    String name = c.getString(TAG_NAME);
                    int id = c.getInt(TAG_ID);
                    String localized_name = c.getString(TAG_LOCNAM);
                    String baldHero = name.replace("npc_dota_hero_", "");
                    String HeroImageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/" + baldHero + "_full.png";    //Hero Image URL

                    hero_array[i] = new Hero(name, id, localized_name, HeroImageUrl);

                    Log.d("Hero array: ", hero_array[i].localized_name);
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

    }

}