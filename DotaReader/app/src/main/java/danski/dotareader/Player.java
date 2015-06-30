package danski.dotareader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danny on 29-6-2015.
 */
public class Player {
    public int account_id;
    public String player_name;
    public int player_slot;
    public int hero_id;
    public String hero_name;
    public int item_0;
    public int item_1;
    public int item_2;
    public int item_3;
    public int item_4;
    public int item_5;
    public int kills;
    public int deaths;
    public int assists;
    public int leaver_status;
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
            item_0 = input.getInt("item_0");
            item_1 = input.getInt("item_1");
            item_2 = input.getInt("item_2");
            item_3 = input.getInt("item_3");
            item_4 = input.getInt("item_4");
            item_5 = input.getInt("item_5");
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

            //Make data to human format
            if(account_id == -1){
                player_name = "Anonymous";
            } else {
                player_name = "Not so Anonymous";
            }

            //Hero Name from local JSON
            String herojson = Defines.RawToString(R.raw.heroes);
            if(herojson != null || herojson != "ERROR"){
                JSONObject heroobj = new JSONObject(herojson);
                JSONArray heroArray = heroobj.getJSONArray("heroes");

                for (int i = 0; i < heroArray.length(); i++){
                    JSONObject c = heroArray.getJSONObject(i);

                    int id = c.getInt("id");
                    String name = c.getString("localized_name");

                    if(hero_id == id){
                        hero_name = name;
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
