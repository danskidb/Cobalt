package danski.cobalt.sql;

import android.database.Cursor;
import android.util.Log;

import danski.cobalt.Defines;
import danski.cobalt.MatchTools;

/**
 * Created by danny on 26-9-2016.
 */

public class MatchYouFormatter {
    public String heroTitle;
    public String localizedHeroName;
    public String heroImageUrl;
    public int playedtimesbefore;       //how much x you've played this hero before.
    public String PlayTime;

    public int level;
    public int kills;
    public int deaths;
    public int assist;
    public float kda;

    public int gold;
    public int lasthits;
    public int denies;
    public int gpm;
    public int xpm;

    public int herodmg;
    public int towerdmg;
    public int heroheal;

    public int[] item = new int[6];

    public MatchYouFormatter(){
        if(SQLManager.instance == null) new SQLManager(Defines.CurrentContext);
        if(MatchTools.instance == null) new MatchTools();
    }

    public void LoadData(long matchid, long steamid64){
        Cursor player = SQLManager.instance.getPlayerInMatch(Defines.idTo32(steamid64), matchid);
        player.moveToFirst();


        //The you card
        localizedHeroName = MatchTools.getHeroName(player.getInt(player.getColumnIndex("Hero_hero_id")));
        heroImageUrl = MatchTools.getHeroImageUrl(player.getInt(player.getColumnIndex("Hero_hero_id")));
        heroTitle = MatchTools.getHeroTitle(player.getInt(player.getColumnIndex("Hero_hero_id")));

        //kda stats
        level = player.getInt(player.getColumnIndex("level"));
        kills = player.getInt(player.getColumnIndex("kills"));
        deaths = player.getInt(player.getColumnIndex("deaths"));
        assist = player.getInt(player.getColumnIndex("assists"));
        kda = (kills + assist) / deaths;

        //farm stats
        gold = player.getInt(player.getColumnIndex("gold"));
        lasthits = player.getInt(player.getColumnIndex("last_hits"));
        denies = player.getInt(player.getColumnIndex("denies"));
        gpm = player.getInt(player.getColumnIndex("gold_per_minute"));
        xpm = player.getInt(player.getColumnIndex("xp_per_minute"));

        //damage stats
        herodmg = player.getInt(player.getColumnIndex("hero_damage"));
        towerdmg = player.getInt(player.getColumnIndex("tower_damage"));
        heroheal = player.getInt(player.getColumnIndex("hero_healing"));

        //items
        item[0] = player.getInt(player.getColumnIndex("Item_item_id"));
        item[1] = player.getInt(player.getColumnIndex("Item_item_id1"));
        item[2] = player.getInt(player.getColumnIndex("Item_item_id2"));
        item[3] = player.getInt(player.getColumnIndex("Item_item_id3"));
        item[4] = player.getInt(player.getColumnIndex("Item_item_id4"));
        item[5] = player.getInt(player.getColumnIndex("Item_item_id5"));


        player.close();
    }

}
