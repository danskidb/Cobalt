package danski.cobalt.sql;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import danski.cobalt.Defines;

/**
 * Created by Danny on 08/02/2016.
 */
public class HeroRetreiver {

    private String heroidsurl = "https://api.steampowered.com/IEconDOTA2_570/GetHeroes/v0001/?key=" + Defines.APIKEY + "&language=en_us";
    private String heropickerdataurl = "http://www.dota2.com/jsfeed/heropickerdata?v=0";
    public static HeroRetreiver instance;
    ProgressDialog pDialog;

    public HeroRetreiver(){
        instance = this;
    }
    public void retreiveAsync(){
        new RetreiveList().execute();
    }

    private class RetreiveList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(Defines.CurrentContext);
            pDialog.setMessage("Downloading hero data");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0){
            retreive();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }

    public void retreive(){
        ServiceHandler sh = new ServiceHandler();
        Log.i("HeroRetreiver", "Downloading 2 JSON files");
        String heroidsJSON = sh.getJSON(heroidsurl, 0);
        String heropickerdataJSON = sh.getJSON(heropickerdataurl, 0);
        SQLManager sq = new SQLManager(Defines.CurrentContext, false);

        if(heroidsJSON != null && heropickerdataJSON != null){
            Log.i("HeroRetreiver", "Got data. Parsing items...");

            try{
                JSONObject heroidjsonobj = new JSONObject(heroidsJSON);
                JSONObject heropickerdatajsonobj = new JSONObject(heropickerdataJSON);

                JSONObject result = heroidjsonobj.getJSONObject("result");
                JSONArray heroes = result.getJSONArray("heroes");
                for(int i = 0; i < heroes.length(); i++){
                    JSONObject hero = heroes.getJSONObject(i);

                    if(!sq.doesHeroExist(hero.getInt("id"))){
                        Log.i("HeroRetreiver", hero.getString("localized_name") + " - Does not exist, creating.");

                        String title = hero.getString("name");
                        title = title.replace("npc_dota_hero_", "");

                        JSONObject heropickerdata = heropickerdatajsonobj.getJSONObject(title);
                        Log.i("HeroRetreiver", heropickerdata.getString("bio"));


                        sq.addHero(hero, heropickerdata);
                    }
                }



            } catch (JSONException e){
                e.printStackTrace();
                //todo: Handle that nothing came back.
            }
        }
        sq.close();
    }
}
