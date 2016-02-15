package danski.cobalt.sql;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import danski.cobalt.Defines;

/**
 * Created by Danny on 08/02/2016.
 */
public class HeroRetreiver {

    private String heropickerdataurl = "http://www.dota2.com/jsfeed/heropickerdata";
    private String heropediadataurl = "http://www.dota2.com/jsfeed/heropediadata";
    public static HeroRetreiver instance;
    ProgressDialog pDialog;

    public HeroRetreiver(){
        instance = this;
    }
    public void retreive(){
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

            ServiceHandler sh = new ServiceHandler();
            String heropickerJSON = sh.getJSON(heropickerdataurl, 0);
            SQLManager sq = new SQLManager(Defines.CurrentContext);

            if(heropickerJSON != null){
                Log.i("HeroRetreiver", "Got data. Parsing items from JSON...");

                try{
                    JSONObject jsonObj = new JSONObject(heropickerJSON);

                    for(int i = 0; i < jsonObj.names().length(); i++){
                        JSONObject hero = jsonObj.getJSONObject(jsonObj.names().getString(i));

                        Log.i("HeroRetreiver", hero.getString("name"));


                        /*
                        todo: Find a JSON File which has the ID's, title name (eg "antimage") and image url.
                         */

                        /*if(!sq.doesItemExist(item.getInt("id"))){
                            Log.i("HeroRetreiver", item.getString("dname") + " - Does not exist, creating.");

                            sq.addItem(item);
                        } else {
                            Log.i("HeroRetreiver", item.getString("dname") + " - Exists, updating.");

                            sq.updateItem(item);
                        }*/
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    //todo: Handle that nothing came back.
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);


            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }
}
