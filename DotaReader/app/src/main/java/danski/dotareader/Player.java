package danski.dotareader;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danny on 29-6-2015.
 */
public class Player {
    public int account_id;
    public Long steamid64;
    public String player_name;
    public int player_slot;
    public int hero_id;
    public String hero_name;
    public String hero_image_url;
    public int item[];
    public String item_name[];
    public String item_image_url[];
    public int kills;
    public int deaths;
    public int assists;
    public int leaver_status;
    public String leaver_text;
    public int gold;
    public int last_hits;
    public int denies;
    public int gold_per_min;
    public int xp_per_min;
    public int gold_spent;
    public int hero_damage;
    public int tower_damage;
    public int hero_healing;
    public int level;
    public ability_upgrade[] ability_upgrades;

    public Player(JSONObject input){
        try{
            //Player values
            account_id = input.getInt("account_id");
            player_slot = input.getInt("player_slot");
            hero_id = input.getInt("hero_id");
            kills = input.getInt("kills");
            deaths = input.getInt("deaths");
            assists = input.getInt("assists");
            leaver_status = input.getInt("leaver_status");
            gold = input.getInt("gold");
            last_hits = input.getInt("last_hits");
            denies = input.getInt("denies");
            gold_per_min = input.getInt("gold_per_min");
            xp_per_min = input.getInt("xp_per_min");
            gold_spent = input.getInt("gold_spent");
            hero_damage = input.getInt("hero_damage");
            tower_damage = input.getInt("tower_damage");
            hero_healing = input.getInt("hero_healing");
            level = input.getInt("level");

            //Parse array of ability upgrades
            JSONArray JAA; //JSON Array abilities
            JAA = input.getJSONArray("ability_upgrades");
            ability_upgrades = new ability_upgrade[JAA.length()];

            for(int i = 0; i < JAA.length(); i++){
                JSONObject c = JAA.getJSONObject(i);

                ability_upgrades[i] = new ability_upgrade(
                        c.getInt("ability"),
                        c.getInt("time"),
                        c.getInt("level")
                );
            }

            steamid64 = Defines.idTo64(account_id);

            //Make data to human format
            if(account_id == -1){
                player_name = "Anonymous";
            } else {
                player_name = "Not so Anonymous";
            }

            //Parse items
            item = new int[6];
            item_name = new String[6];
            item_image_url = new String[6];
            for (int i = 0; i < 6; i++){
                item[i] = input.getInt("item_" + i);
            }

            //Item name from local JSON
            String itemjson = Defines.RawToString(R.raw.items);
            if(itemjson != null || itemjson != "ERROR"){
                JSONObject itemobj = new JSONObject(itemjson);
                JSONObject result = itemobj.getJSONObject("result");
                JSONArray itemArray = result.getJSONArray("items");

                for (int i = 0; i < itemArray.length(); i++){
                    JSONObject c = itemArray.getJSONObject(i);

                    int id = c.getInt("id");
                    String name = c.getString("name");
                    int cost = c.getInt("cost");
                    String localized_name = c.getString("localized_name");

                    String balditem = name.replace("item_", "");

                    for (int a = 0; a < 6; a++){
                        if(id == item[a]){
                            item_name[a] = localized_name;
                            item_image_url[a] = "http://cdn.dota2.com/apps/dota2/images/items/" + balditem + "_lg.png";
                        }
                    }




                }

            }

            //Hero Name from local JSON
            String herojson = Defines.RawToString(R.raw.heroes);
            if(herojson != null || herojson != "ERROR"){
                JSONObject heroobj = new JSONObject(herojson);
                JSONArray heroArray = heroobj.getJSONArray("heroes");

                for (int i = 0; i < heroArray.length(); i++){
                    JSONObject c = heroArray.getJSONObject(i);

                    int id = c.getInt("id");
                    String locname = c.getString("localized_name");
                    String name = c.getString("name");

                    String baldhero = name.replace("npc_dota_hero_", "");

                    if(hero_id == id){
                        hero_name = locname;
                        hero_image_url = "http://cdn.dota2.com/apps/dota2/images/heroes/" + baldhero + "_full.png";    //Hero Image URL
                    }
                }
            }

            String leaverjson = Defines.RawToString(R.raw.leaverstatus);
            if(leaverjson != null || leaverjson != "ERROR"){
                JSONArray leaverarray = new JSONArray(leaverjson);

                for (int i = 0; i < leaverarray.length(); i++){
                    JSONObject c = leaverarray.getJSONObject(i);

                    if(c.getInt("id") == leaver_status){
                        leaver_text = c.getString("description");
                    }
                }
            }


            Log.d("Player " + account_id, "Is processed");
        } catch(JSONException e){
            e.printStackTrace();
        }


    }

    class ability_upgrade{
        public int ability;
        public int time;
        public int level;

        public ability_upgrade(int _ability, int _time, int _level){
            ability = _ability;
            time = _time;
            level = _level;
        }
    }
}
