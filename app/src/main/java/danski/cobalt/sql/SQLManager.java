package danski.cobalt.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import danski.cobalt.sql.DataStructure.Player;


/**
 * Created by Danny on 28/01/2016.
 */
public class SQLManager extends SQLiteOpenHelper {

    public static SQLManager instance;

    static String databaseName = "cobaltdb";
    static int databaseVersion = 28;

    Context context;
    AssetManager am;

    SQLiteDatabase db;

    public SQLManager (Context _context){
        super(_context, databaseName, null, databaseVersion);
        context = _context;
        am = context.getAssets();
        instance = this;
    }

    public SQLManager (Context _context, boolean overrideinstance){
        super(_context, databaseName, null, databaseVersion);
        context = _context;
        am = context.getAssets();
        if(overrideinstance) instance = this;
    }

    public boolean doesItemExist(int itemID){
        db = this.getReadableDatabase();

        String query = "Select * from Item where item_id = " + itemID;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public boolean doesMatchExist(long matchid){
       db = this.getReadableDatabase();

        String query = "Select * from Match where match_id = " + matchid;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public boolean doesPlayerExist(long playerid){
        db = this.getReadableDatabase();

        String query = "Select * from Player where account_id = " + playerid;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public boolean doesHeroExist(int heroid){
        db = this.getReadableDatabase();

        String query = "Select * from Hero where hero_id = " + heroid;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public boolean isPlayerInMatch(long playerid, long matchid){
        db = this.getReadableDatabase();

        //Anon.
        if(playerid == Long.parseLong("4294967295")){
            return false;
        }

        String query = "Select * from Match_has_Player where Match_match_id = " + matchid + " and Player_account_id = " + playerid;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public boolean addItem(JSONObject item){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try{
            cv.put("manacost", item.getInt("mc"));
        } catch (JSONException e){
            cv.put("manacost", 0);
        }

        try{
            cv.put("cooldown", item.getInt("cd"));
        } catch (JSONException e){
            cv.put("cooldown", 0);
        }


        try{
            cv.put("item_id", item.getInt("id"));
            cv.put("item_name", item.getString("dname"));
            cv.put("description", item.getString("desc"));
            cv.put("img", item.getString("img"));
            cv.put("qual", item.getString("qual"));
            cv.put("cost", item.getInt("cost"));
            cv.put("notes", item.getString("notes"));
            cv.put("attributes", item.getString("attrib"));
            cv.put("lore", item.getString("lore"));
            cv.put("components", item.getString("components"));
            cv.put("created", item.getBoolean("created"));
            db.insert("Item", null, cv);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean addMatch(JSONObject match){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try{
            cv.put("match_id", match.getLong("match_id"));
            cv.put("match_seq_num", match.getLong("match_seq_num"));
            cv.put("start_time", match.getLong("start_time"));
            cv.put("lobby_type", match.getInt("lobby_type"));
            cv.put("radiant_team_id", match.getInt("radiant_team_id"));
            cv.put("dire_team_id", match.getInt("dire_team_id"));
            db.insert("Match", null, cv);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean addPlayer(long playerid){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try{
            cv.put("account_id", playerid);
            db.insert("Player", null, cv);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePlayerDetail(String playerid, String name, String profileurl, String avatarfullurl, String avatarmedurl){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try{
            cv.put("name", name);
            cv.put("profile_url", profileurl);
            cv.put("avatarfull_url", avatarfullurl);
            cv.put("avatarmed_url", avatarmedurl);

            db.update("Player", cv, "account_id=?", new String[]{playerid});
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Player getPlayer(Long playerid){
        db = this.getReadableDatabase();
        String query = "Select * from Player where account_id = " + playerid;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            Player p = new Player(
                    cursor.getString(cursor.getColumnIndex("account_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("profile_url")),
                    cursor.getString(cursor.getColumnIndex("avatarfull_url")),
                    cursor.getString(cursor.getColumnIndex("avatarmed_url")));
            cursor.close();
            return p;
        } else {
            cursor.close();
            return null;
        }
    }

    public boolean addHero(JSONObject heroid, JSONObject heropickerdata){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try{
            //Hero ID json data
            cv.put("hero_id", heroid.getInt("id"));                      //22
            String title = heroid.getString("name");
            title = title.replace("npc_dota_hero_", "");
            cv.put("hero_title", title);                                //furion
            cv.put("hero_name", heroid.getString("localized_name"));      //Nature's prophet

            //Heropickerdata JSON
            cv.put("biography", heropickerdata.getString("bio"));
            cv.put("attack_type", heropickerdata.getString("atk_l"));

            JSONArray roles = heropickerdata.getJSONArray("roles_l");
            String rolestring = "";
            for(int i = 0; i < roles.length(); i++){
                rolestring += roles.getString(i);
                if(i != roles.length() -1) rolestring += ", ";
            }
            cv.put("roles", rolestring);


            db.insert("Hero", null, cv);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateItem(JSONObject item){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //todo: update cooldown and manacost.

        try{
            cv.put("item_id", item.getInt("id"));
            cv.put("item_name", item.getString("dname"));
            cv.put("description", item.getString("desc"));
            cv.put("img", item.getString("img"));
            cv.put("qual", item.getString("qual"));
            cv.put("cost", item.getInt("cost"));
            cv.put("notes", item.getString("notes"));
            cv.put("attributes", item.getString("attrib"));
            cv.put("lore", item.getString("lore"));
            cv.put("components", item.getString("components"));
            cv.put("created", item.getBoolean("created"));

            db.update("Item",
                    cv,
                    "item_id = ?",
                    new String[]{ String.valueOf(item.getInt("id"))}
            );

            return true;
        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean linkPlayerToMatch(JSONObject player, long matchid){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        try{
            cv.put("Player_account_id", player.getLong("account_id"));
            cv.put("Match_match_id", matchid);
            cv.put("player_slot", player.getInt("player_slot"));
            cv.put("Hero_hero_id", player.getInt("hero_id"));
            db.insert("Match_has_Player", null, cv);
            return true;
        }catch (JSONException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPlayerDataToMatch(JSONObject player, long matchid){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try{
            cv.put("Item_item_id", player.getInt("item_0"));
            cv.put("Item_item_id1", player.getInt("item_1"));
            cv.put("Item_item_id2", player.getInt("item_2"));
            cv.put("Item_item_id3", player.getInt("item_3"));
            cv.put("Item_item_id4", player.getInt("item_4"));
            cv.put("Item_item_id5", player.getInt("item_5"));
            cv.put("kills", player.getInt("kills"));
            cv.put("deaths", player.getInt("deaths"));
            cv.put("assists", player.getInt("assists"));
            cv.put("leaver_status", player.getInt("leaver_status"));
            cv.put("gold", player.getInt("gold"));
            cv.put("last_hits", player.getInt("last_hits"));
            cv.put("denies", player.getInt("denies"));
            cv.put("gold_per_minute", player.getInt("gold_per_min"));
            cv.put("xp_per_minute", player.getInt("xp_per_min"));
            cv.put("gold_spent", player.getLong("gold_spent"));
            cv.put("hero_damage", player.getLong("hero_damage"));
            cv.put("tower_damage", player.getLong("tower_damage"));
            cv.put("hero_healing", player.getLong("hero_healing"));
            cv.put("level", player.getInt("level"));
            //cv.put("hasdetail", true);

            Cursor match = getMatch(matchid);
            int radiantwin = match.getInt(match.getColumnIndex("radiant_win"));

            if(radiantwin > 0){
                if(player.getInt("player_slot") <= 4){
                    cv.put("win", true);
                } else {
                    cv.put("win", false);
                }
            } else {
                if(player.getInt("player_slot") <= 4){
                    cv.put("win", false);
                } else {
                    cv.put("win", true);
                }
            }

            match.close();
            db.update("Match_has_Player",
                    cv,
                    "Match_match_id = ? AND player_slot = ?",
                    new String[] { String.valueOf(matchid), String.valueOf(player.getLong("player_slot"))}
            );



            return true;
        }catch (JSONException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean doesMatchHaveDetails(long matchid){
        db = this.getReadableDatabase();
        String query = "Select hasdetail from Match where match_id=" + matchid;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount() > 0) {
            if (c.getInt(c.getColumnIndex("hasdetail")) > 0) {
                c.close();
                return true;
            }
        }

        c.close();
        return false;
    }

    public boolean setMatchDetails(JSONObject match){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try{
            cv.put("hasdetail", true);
            cv.put("radiant_win", match.getBoolean("radiant_win"));
            cv.put("duration", match.getInt("duration"));
            cv.put("tower_status_radiant", match.getInt("tower_status_radiant"));
            cv.put("tower_status_dire", match.getInt("tower_status_dire"));
            cv.put("barracks_status_radiant", match.getInt("barracks_status_radiant"));
            cv.put("barracks_status_dire", match.getInt("barracks_status_dire"));
            cv.put("cluster", match.getInt("cluster"));
            cv.put("first_blood_time", match.getInt("first_blood_time"));
            cv.put("human_players", match.getInt("human_players"));
            cv.put("leagueid", match.getInt("leagueid"));
            cv.put("positive_votes", match.getInt("positive_votes"));
            cv.put("negative_votes", match.getInt("negative_votes"));
            cv.put("game_mode", match.getInt("game_mode"));
            cv.put("flags", match.getInt("flags"));
            cv.put("engine", match.getInt("engine"));


            db.update("Match",
                    cv,
                    "match_id = ?",
                    new String[] { String.valueOf(match.getLong("match_id"))}
            );
            return true;
        }catch (JSONException e){
            e.printStackTrace();
            return false;
        }
    }

    public Cursor getAllMatches(){
        db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select rowid _id,* from Match ORDER BY match_id DESC", null);
        return res;
    }

    public ArrayList<Long> getAllMatchesList(){
        ArrayList<Long> allmatches = new ArrayList<>();

        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select rowid _id,* from Match ORDER BY match_id DESC", null);

        if(cur.getCount() == 0) Log.w("SQLM", "Allmatches cursor is empty");
        else {
            cur.moveToFirst();

            for (int i = 0; i < cur.getCount(); i++){
                cur.moveToPosition(i);
                allmatches.add(Long.parseLong(cur.getString(cur.getColumnIndex("match_id"))));
            }
        }

        cur.close();
        return allmatches;
    }

    public Cursor getMatch(long matchid){
        db = this.getReadableDatabase();
        String query = "Select rowid _id,* from Match WHERE match_id = " + matchid;
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return res;
    }

    public Cursor getPlayerInMatch(long playerid32, long matchid){
        db = this.getReadableDatabase();
        String query = "Select rowid _id,* from Match_has_Player WHERE Player_account_id = " + playerid32 + " AND Match_match_id = " + matchid;
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return res;
    }

    public Cursor getHero(int heroid){
        db = this.getReadableDatabase();
        String query = "Select rowid _id,* from Hero WHERE hero_id = " + heroid;
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return res;
    }

    public Cursor getItem(int itemid){
        db = this.getReadableDatabase();
        String query = "Select rowid _id,* from Item WHERE item_id = " + itemid;
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return res;
    }

    public ArrayList<Long> getLastXMatches(int ammount){
        ArrayList<Long> lastxmatches = new ArrayList<>();

        db = this.getReadableDatabase();
        String query = "Select rowid _id,match_id from Match ORDER BY match_id DESC LIMIT "+ammount;
        Cursor cur = db.rawQuery(query, null);

        if(cur.getCount() == 0) Log.w("SQLM", "Last 5 matches cursor is empty");
        else {
            cur.moveToFirst();

            for (int i = 0; i < cur.getCount(); i++){
                cur.moveToPosition(i);
                lastxmatches.add(Long.parseLong(cur.getString(cur.getColumnIndex("match_id"))));
            }
        }

        cur.close();
        return lastxmatches;
    }

    public Cursor getRecordPlayer(long matchid, String table) {
        db = this.getReadableDatabase();
        String query = "Select rowid _id,max("+table+"),* from Match_has_Player WHERE Match_match_id = " + matchid;
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return res;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) throws SQLiteException {
        try {
            Log.i("SQLM", "Going to create tables");

            db.execSQL("CREATE TABLE \"Hero\"(\n" +                     //EG
                    "  \"hero_id\" INTEGER PRIMARY KEY NOT NULL,\n" +   //22
                    "  \"hero_title\" VARCHAR(45),\n" +                 //furion
                    "  \"hero_name\" VARCHAR(45),\n" +                  //Nature's Prophet
                    "  \"biography\" VARCHAR(45),\n" +
                    "  \"primary_attribute\" VARCHAR(45),\n" +
                    "  \"strength_base\" INTEGER,\n" +
                    "  \"strength_gain\" FLOAT,\n" +
                    "  \"intelligence_base\" INTEGER,\n" +
                    "  \"intelligence_gain\" FLOAT,\n" +
                    "  \"agility_base\" INTEGER,\n" +
                    "  \"agility_gain\" FLOAT,\n" +
                    "  \"movespeed\" INTEGER,\n" +
                    "  \"startingdamage_min\" INTEGER,\n" +
                    "  \"startingdamage_max\" INTEGER,\n" +
                    "  \"startingarmor\" INTEGER,\n" +
                    "  \"attack_type\" VARCHAR(45),\n" +
                    "  \"roles\" VARCHAR(45)\n" +
                    ");");

            db.execSQL("CREATE TABLE \"ability\"(\n" +
                    "  \"ability_id\" INTEGER PRIMARY KEY NOT NULL,\n" +
                    "  \"ability_name\" VARCHAR(45),\n" +
                    "  \"description\" VARCHAR(45),\n" +
                    "  \"affects\" VARCHAR(45),\n" +
                    "  \"notes\" VARCHAR(45),\n" +
                    "  \"dmg\" VARCHAR(45),\n" +
                    "  \"attributes\" VARCHAR(45),\n" +
                    "  \"cmd\" VARCHAR(45),\n" +
                    "  \"lore\" VARCHAR(45),\n" +
                    "  \"hurl\" VARCHAR(45)\n" +
                    ");");

            db.execSQL("CREATE TABLE \"Item\"(\n" +
                    "  \"item_id\" INTEGER PRIMARY KEY NOT NULL,\n" +
                    "  \"item_name\" VARCHAR(45),\n" +
                    "  \"description\" VARCHAR(45),\n" +
                    "  \"img\" VARCHAR(45),\n" +
                    "  \"qual\" VARCHAR(45),\n" +
                    "  \"cost\" INTEGER,\n" +
                    "  \"notes\" VARCHAR(45),\n" +
                    "  \"attributes\" VARCHAR(45),\n" +
                    "  \"manacost\" INTEGER,\n" +
                    "  \"cooldown\" INTEGER,\n" +
                    "  \"lore\" VARCHAR(45),\n" +
                    "  \"components\" VARCHAR(45),\n" +
                    "  \"created\" BOOL\n" +
                    ");");

            db.execSQL("CREATE TABLE \"Hero_has_ability\"(\n" +
                    "  \"Hero_hero_id\" INTEGER NOT NULL,\n" +
                    "  \"ability_ability_id\" INTEGER NOT NULL,\n" +
                    "  PRIMARY KEY(\"Hero_hero_id\",\"ability_ability_id\"),\n" +
                    "  CONSTRAINT \"fk_Hero_has_ability_Hero1\"\n" +
                    "    FOREIGN KEY(\"Hero_hero_id\")\n" +
                    "    REFERENCES \"Hero\"(\"hero_id\"),\n" +
                    "  CONSTRAINT \"fk_Hero_has_ability_ability1\"\n" +
                    "    FOREIGN KEY(\"ability_ability_id\")\n" +
                    "    REFERENCES \"ability\"(\"ability_id\")\n" +
                    ");");

            db.execSQL("CREATE INDEX \"Hero_has_ability.fk_Hero_has_ability_ability1_idx\" ON \"Hero_has_ability\" (\"ability_ability_id\");\n" +
                    "CREATE INDEX \"Hero_has_ability.fk_Hero_has_ability_Hero1_idx\" ON \"Hero_has_ability\" (\"Hero_hero_id\");");

            db.execSQL("CREATE TABLE \"Player\"(\n" +
                    "  \"account_id\" INTEGER PRIMARY KEY NOT NULL,\n" +
                    "  \"name\" VARCHAR(45),\n" +
                    "  \"profile_url\" VARCHAR(45),\n" +
                    "  \"avatarfull_url\" VARCHAR(45),\n" +
                    "  \"avatarmed_url\" VARCHAR(45)\n" +
                    ");");

            db.execSQL("CREATE TABLE \"Match\"(\n" +
                    "  \"match_id\" INTEGER PRIMARY KEY NOT NULL,\n" +
                    "  \"start_time\" INTEGER,\n" +
                    "  \"lobby_type\" INTEGER,\n" +
                    "  \"radiant_team_id\" INTEGER,\n" +
                    "  \"dire_team_id\" INTEGER,\n" +
                    "  \"radiant_win\" BOOL,\n" +
                    "  \"duration\" INTEGER,\n" +
                    "  \"match_seq_num\" INTEGER,\n" +
                    "  \"tower_status_radiant\" INTEGER,\n" +
                    "  \"tower_status_dire\" INTEGER,\n" +
                    "  \"barracks_status_radiant\" INTEGER,\n" +
                    "  \"barracks_status_dire\" INTEGER,\n" +
                    "  \"cluster\" INTEGER,\n" +
                    "  \"first_blood_time\" INTEGER,\n" +
                    "  \"human_players\" INTEGER,\n" +
                    "  \"leagueid\" INTEGER,\n" +
                    "  \"positive_votes\" INTEGER,\n" +
                    "  \"negative_votes\" INTEGER,\n" +
                    "  \"game_mode\" INTEGER,\n" +
                    "  \"flags\" INTEGER,\n" +
                    "  \"hasdetail\" BOOL,\n" +
                    "  \"engine\" INTEGER\n" +
                    ");");

            db.execSQL("CREATE TABLE \"Match_has_Player\"(\n" +
                    "  \"Match_match_id\" INTEGER NOT NULL,\n" +
                    "  \"Player_account_id\" INTEGER NOT NULL,\n" +
                    "  \"Hero_hero_id\" INTEGER NOT NULL,\n" +
                    "  \"Item_item_id\" INTEGER,\n" +
                    "  \"Item_item_id1\" INTEGER,\n" +
                    "  \"Item_item_id2\" INTEGER,\n" +
                    "  \"Item_item_id3\" INTEGER,\n" +
                    "  \"Item_item_id4\" INTEGER,\n" +
                    "  \"Item_item_id5\" INTEGER,\n" +
                    "  \"player_slot\" INTEGER,\n" +
                    "  \"kills\" INTEGER,\n" +
                    "  \"deaths\" INTEGER,\n" +
                    "  \"assists\" INTEGER,\n" +
                    "  \"leaver_status\" INTEGER,\n" +
                    "  \"gold\" INTEGER,\n" +
                    "  \"last_hits\" INTEGER,\n" +
                    "  \"denies\" INTEGER,\n" +
                    "  \"gold_per_minute\" INTEGER,\n" +
                    "  \"xp_per_minute\" INTEGER,\n" +
                    "  \"gold_spent\" INTEGER,\n" +
                    "  \"hero_damage\" INTEGER,\n" +
                    "  \"tower_damage\" INTEGER,\n" +
                    "  \"hero_healing\" INTEGER,\n" +
                    "  \"win\" BOOL,\n" +
                    "  \"level\" INTEGER,\n" +
                    "  PRIMARY KEY(\"Match_match_id\",\"player_slot\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Match\"\n" +
                    "    FOREIGN KEY(\"Match_match_id\")\n" +
                    "    REFERENCES \"Match\"(\"match_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Player1\"\n" +
                    "    FOREIGN KEY(\"Player_account_id\")\n" +
                    "    REFERENCES \"Player\"(\"account_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Hero1\"\n" +
                    "    FOREIGN KEY(\"Hero_hero_id\")\n" +
                    "    REFERENCES \"Hero\"(\"hero_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Item1\"\n" +
                    "    FOREIGN KEY(\"Item_item_id\")\n" +
                    "    REFERENCES \"Item\"(\"item_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Item2\"\n" +
                    "    FOREIGN KEY(\"Item_item_id1\")\n" +
                    "    REFERENCES \"Item\"(\"item_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Item3\"\n" +
                    "    FOREIGN KEY(\"Item_item_id2\")\n" +
                    "    REFERENCES \"Item\"(\"item_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Item4\"\n" +
                    "    FOREIGN KEY(\"Item_item_id3\")\n" +
                    "    REFERENCES \"Item\"(\"item_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Item5\"\n" +
                    "    FOREIGN KEY(\"Item_item_id4\")\n" +
                    "    REFERENCES \"Item\"(\"item_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_Item6\"\n" +
                    "    FOREIGN KEY(\"Item_item_id5\")\n" +
                    "    REFERENCES \"Item\"(\"item_id\")\n" +
                    ");");

            db.execSQL("CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Player1_idx\" ON \"Match_has_Player\" (\"Player_account_id\");\n" +
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Match_idx\" ON \"Match_has_Player\" (\"Match_match_id\");\n" +
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Hero1_idx\" ON \"Match_has_Player\" (\"Hero_hero_id\");\n" +
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Item1_idx\" ON \"Match_has_Player\" (\"Item_item_id\");\n" +
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Item2_idx\" ON \"Match_has_Player\" (\"Item_item_id1\");\n" +
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Item3_idx\" ON \"Match_has_Player\" (\"Item_item_id2\");\n" +
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Item4_idx\" ON \"Match_has_Player\" (\"Item_item_id3\");\n" +
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Item5_idx\" ON \"Match_has_Player\" (\"Item_item_id4\");\n" +
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Item6_idx\" ON \"Match_has_Player\" (\"Item_item_id5\");\n");

            db.execSQL("CREATE TABLE \"Match_has_Player_has_ability\"(\n" +
                    "  \"Match_has_Player_Match_match_id\" INTEGER NOT NULL,\n" +
                    "  \"Match_has_Player_Player_account_id\" INTEGER NOT NULL,\n" +
                    "  \"ability_ability_id\" INTEGER NOT NULL,\n" +
                    "  \"time\" INTEGER,\n" +
                    "  \"level\" INTEGER,\n" +
                    "  PRIMARY KEY(\"Match_has_Player_Match_match_id\",\"Match_has_Player_Player_account_id\",\"ability_ability_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_has_ability_Match_has_Player1\"\n" +
                    "    FOREIGN KEY(\"Match_has_Player_Match_match_id\",\"Match_has_Player_Player_account_id\")\n" +
                    "    REFERENCES \"Match_has_Player\"(\"Match_match_id\",\"Player_account_id\"),\n" +
                    "  CONSTRAINT \"fk_Match_has_Player_has_ability_ability1\"\n" +
                    "    FOREIGN KEY(\"ability_ability_id\")\n" +
                    "    REFERENCES \"ability\"(\"ability_id\")\n" +
                    ");\n" +
                    "CREATE INDEX \"Match_has_Player_has_ability.fk_Match_has_Player_has_ability_ability1_idx\" ON \"Match_has_Player_has_ability\" (\"ability_ability_id\");\n" +
                    "CREATE INDEX \"Match_has_Player_has_ability.fk_Match_has_Player_has_ability_Match_has_Player1_idx\" ON \"Match_has_Player_has_ability\" (\"Match_has_Player_Match_match_id\",\"Match_has_Player_Player_account_id\");\n");
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS Hero");
        db.execSQL("DROP TABLE IF EXISTS Player");
        db.execSQL("DROP TABLE IF EXISTS Hero_has_Ability");
        db.execSQL("DROP TABLE IF EXISTS Item");
        db.execSQL("DROP TABLE IF EXISTS Match");
        db.execSQL("DROP TABLE IF EXISTS Match_has_Player");
        db.execSQL("DROP TABLE IF EXISTS Match_has_Player_has_ability");
        db.execSQL("DROP TABLE IF EXISTS ability");

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("setupcomplete", false);
        editor.apply();
        
        // Create tables again
        onCreate(db);
    }

    public SQLiteDatabase getDatabase(){
        return db;
    }
}
