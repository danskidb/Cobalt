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
public class ItemRetreiver {
    private String url = "http://www.dota2.com/jsfeed/itemdata";
    public static ItemRetreiver instance;
    ProgressDialog pDialog;

    public ItemRetreiver(){
        instance = this;
    }

    public void retreive(){
        new RetreiveList().execute();
    }

    private class RetreiveList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(Defines.CurrentContext);
            pDialog.setMessage("Downloading item list");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0){

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.getJSON(url, 0);
            SQLManager sq = new SQLManager(Defines.CurrentContext, false);

            if(jsonStr != null){
                Log.i("ItemRetreiver", "Got data. Parsing items from JSON...");

                try{
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject itemdata = jsonObj.getJSONObject("itemdata");

                    for(int i = 0; i < itemdata.names().length(); i++){
                        JSONObject item = itemdata.getJSONObject(itemdata.names().getString(i));

                        if(!sq.doesItemExist(item.getInt("id"))){
                            Log.i("ItemRetreiver", item.getString("dname") + " - Does not exist, creating.");

                            sq.addItem(item);
                        } else {
                            Log.i("ItemRetreiver", item.getString("dname") + " - Exists, updating.");

                            sq.updateItem(item);
                        }
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    //todo: Handle that nothing came back.
                }

            }

            sq.close();
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
