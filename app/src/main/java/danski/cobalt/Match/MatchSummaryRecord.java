package danski.cobalt.Match;

import android.database.Cursor;

import danski.cobalt.Defines;
import danski.cobalt.MatchTools;
import danski.cobalt.sql.DataStructure.Player;
import danski.cobalt.sql.SQLManager;
import danski.cobalt.sql.SteamprofileRetreiver;

/**
 * Created by Danny on 21/08/2016.
 */
public class MatchSummaryRecord{
    String steamname;
    long steamid;
    int amount;
    boolean radiant = false;
    String localized_hero;
    String hero_image_url;


    public MatchSummaryRecord(int _amount, Cursor Match_has_Player){
        amount = _amount;

        if(Match_has_Player.getInt(Match_has_Player.getColumnIndex("player_slot")) <= 4){
            radiant = true;
        }

        int heroint = Match_has_Player.getInt(Match_has_Player.getColumnIndex("Hero_hero_id"));
        localized_hero = MatchTools.getHeroName(heroint);
        hero_image_url = MatchTools.getHeroImageUrl(heroint);

        steamid = Match_has_Player.getInt(Match_has_Player.getColumnIndex("Player_account_id"));

        Player p = SQLManager.instance.getPlayer(Defines.idTo64(steamid));
        if(p!= null) steamname = p.Name;
        else steamname = "Anonymous";
    }
}