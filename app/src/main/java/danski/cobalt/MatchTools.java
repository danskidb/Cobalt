package danski.cobalt;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

import danski.cobalt.sql.SQLManager;

/**
 * Created by Danny on 10/02/2016.
 */
public class MatchTools {

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
        SQLManager sm = new SQLManager(context);
        return sm.getPlayerInMatch(Defines.idTo32(Long.parseLong(Defines.mysteamid)), matchid);
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
}
