package danski.cobalt.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Danny on 28/01/2016.
 */
public class SQLManager extends SQLiteOpenHelper {

    static String databaseName = "cobaltdb";
    static int databaseVersion = 4;

    Context context;
    AssetManager am;

    SQLiteDatabase db;

    public SQLManager (Context _context){
        super(_context, databaseName, null, databaseVersion);
        context = _context;
        am = context.getAssets();
        Log.i("SQLM", "All ready!");
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

    public Cursor getAllMatches(){
        db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from Match", null);
        return res;
    }

    @Override
    public void onCreate(SQLiteDatabase db) throws SQLiteException{
        try{
            Log.i("SQLM", "Going to create tables");
            db.execSQL("CREATE TABLE \"Hero\"(\n" +
                    "  \"hero_id\" INTEGER PRIMARY KEY NOT NULL,\n" +
                    "  \"hero_name\" VARCHAR(45),\n" +
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
                    "CREATE INDEX \"Hero_has_ability.fk_Hero_has_ability_Hero1_idx\" ON \"Hero_has_ability\" (\"Hero_hero_id\");\n" +
                    "CREATE TABLE \"Player\"(\n" +
                    "  \"account_id\" INTEGER PRIMARY KEY NOT NULL,\n" +
                    "  \"name\" VARCHAR(45)\n" +
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
                    "  \"engine\" INTEGER\n" +
                    ");");

            db.execSQL("CREATE TABLE \"Match_has_Player\"(\n" +
                    "  \"Match_match_id\" INTEGER NOT NULL,\n" +
                    "  \"Player_account_id\" INTEGER NOT NULL,\n" +
                    "  \"Hero_hero_id\" INTEGER NOT NULL,\n" +
                    "  \"Item_item_id\" INTEGER NOT NULL,\n" +
                    "  \"Item_item_id1\" INTEGER NOT NULL,\n" +
                    "  \"Item_item_id2\" INTEGER NOT NULL,\n" +
                    "  \"Item_item_id3\" INTEGER NOT NULL,\n" +
                    "  \"Item_item_id4\" INTEGER NOT NULL,\n" +
                    "  \"Item_item_id5\" INTEGER NOT NULL,\n" +
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
                    "  \"level\" INTEGER,\n" +
                    "  PRIMARY KEY(\"Match_match_id\",\"Player_account_id\"),\n" +
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
                    "CREATE INDEX \"Match_has_Player.fk_Match_has_Player_Item6_idx\" ON \"Match_has_Player\" (\"Item_item_id5\");\n" +
                    "CREATE TABLE \"Match_has_Player_has_ability\"(\n" +
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
        db.execSQL("DROP TABLE IF EXISTS Hero_has_Ability");
        db.execSQL("DROP TABLE IF EXISTS Item");
        db.execSQL("DROP TABLE IF EXISTS Match");
        db.execSQL("DROP TABLE IF EXISTS Match_has_Player");
        db.execSQL("DROP TABLE IF EXISTS ability");

        //todo: CLEAR OUT ALL TABLEZ
        
        // Create tables again
        onCreate(db);
    }
}