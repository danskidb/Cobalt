package danski.cobalt;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

import danski.cobalt.sql.SQLManager;

/**
 * Created by Danny on 10/02/2016.
 */
public class MatchTools {

    public static MatchTools instance;
    private int[] WLA;
    public boolean wlaCalculated;

    public MatchTools(){
        instance = this;
        wlaCalculated = false;
    }

    public static HashMap<Integer, String> game_mode = new HashMap<Integer, String>() {{
        put(0, "Unknown");
        put(1, "All Pick");
        put(2, "Captains Mode");
        put(3, "Random Draft");
        put(4, "Single Draft");
        put(5, "All Random");
        put(6, "Intro");
        put(7, "Diretide");
        put(8, "Reverse Captains Mode");
        put(9, "Greeviling");
        put(10, "Tutorial");
        put(11, "Mid Only");
        put(12, "Least Played");
        put(13, "Limited Heroes");
        put(14, "Compendium Matchmaking");
        put(15, "Custom");
        put(16, "Captains Draft");
        put(17, "New Bloom");
        put(18, "Ability Draft");
        put(19, "Event");
        put(20, "All Random Death Match");
        put(21, "1v1 Solo Mid");
        put(22, "Ranked All Pick");
    }};

    public static String getGameMode(int mode){
        if(game_mode.containsKey(mode)){
            return game_mode.get(mode);
        }
        return "Unknown ("+mode+")";
    }

    public static String getHeroName(int hero){
        Cursor heroentry = SQLManager.instance.getHero(hero);
        return heroentry.getString(heroentry.getColumnIndex("hero_name"));
    }

    public static String getHeroImageUrl(int hero){
        Cursor heroentry = SQLManager.instance.getHero(hero);
        return Defines.heroimgurl + heroentry.getString(heroentry.getColumnIndex("hero_title")) + "_full.png";
    }

    public static Cursor getMyPlayerDetails(long matchid, Context context){
        SQLManager sm = new SQLManager(context, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Cursor cur = sm.getPlayerInMatch(Defines.idTo32(prefs.getLong("steamid64", 0)), matchid);
        return cur;
    }

    public static boolean didWin(Cursor Match_has_Player, Cursor Match){
        Match_has_Player.moveToFirst();

        if(Match.getInt(Match.getColumnIndex("radiant_win")) > 0){
            if(Match_has_Player.getInt(Match_has_Player.getColumnIndex("player_slot")) <= 4){
                return true;
            } else {
                return false;
            }
        } else {
            if(Match_has_Player.getInt(Match_has_Player.getColumnIndex("player_slot")) <= 4){
                return false;
            } else {
                return true;
            }
        }
    }

    public float calculateWinRate(){
        if(!wlaCalculated) calculateWLA();
        float winrate = (float)WLA[0] / (float)WLA[3] * 100;
        winrate = Defines.round(winrate, 2);

        return winrate;
    }

    public void calculateWLA(){
        WLA = new int[4];

        if(SQLManager.instance == null) new SQLManager(Defines.CurrentContext);
        ArrayList<Long> allmatches = SQLManager.instance.getAllMatchesList();
        for (Long matchid : allmatches){
            if(SQLManager.instance.doesMatchHaveDetails(matchid)){
                Cursor playerdata = getMyPlayerDetails(matchid, Defines.CurrentContext);
                playerdata.moveToFirst();


                if(playerdata.getInt(playerdata.getColumnIndex("win")) > 0){
                    WLA[0]++;
                } else {
                    WLA[1]++;
                }
                if(playerdata.getInt(playerdata.getColumnIndex("leaver_status")) > 0){
                    WLA[2]++;
                }

            }
        }
        WLA[3] = WLA[0] + WLA[1];

        wlaCalculated = true;
    }

    public int[] getWLA(){
        if(!wlaCalculated) calculateWLA();
        return WLA;
    }

}
