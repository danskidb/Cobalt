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
    }};

    public static String returnGameMode(int mode){
        if(game_mode.containsKey(mode)){
            return game_mode.get(mode);
        }
        return "Unknown";
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
