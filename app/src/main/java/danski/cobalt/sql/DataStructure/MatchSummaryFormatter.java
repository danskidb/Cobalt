package danski.cobalt.sql.DataStructure;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import danski.cobalt.Defines;
import danski.cobalt.Match.MatchSummaryRecord;
import danski.cobalt.MatchTools;
import danski.cobalt.sql.SQLManager;
import danski.cobalt.sql.SteamprofileRetreiver;

/**
 * Created by Danny on 21/08/2016.
 */
public class MatchSummaryFormatter {

    public String starttime;
    public String duration;
    public String firstbloodtime;
    public String gamemode;
    public MatchSummaryRecord kills;
    public MatchSummaryRecord deaths;
    public MatchSummaryRecord assists;
    public MatchSummaryRecord last_hits;
    public MatchSummaryRecord denies;
    public MatchSummaryRecord herodamage;
    public MatchSummaryRecord herohealing;
    public MatchSummaryRecord towerdamage;

    public MatchSummaryFormatter(){
        if(SQLManager.instance == null) new SQLManager(Defines.CurrentContext);
        if(MatchTools.instance == null) new MatchTools();
        if(SteamprofileRetreiver.instance == null) new SteamprofileRetreiver();

    }

    public void loadData(long matchid){
        Log.w("MSF", "Attempting to get match " + matchid);
        Cursor match = SQLManager.instance.getMatch(matchid);


        int mode = match.getInt(match.getColumnIndex("game_mode"));
        gamemode = MatchTools.instance.getGameMode(mode);

        int[] d = Defines.splitToComponentTimes(match.getInt(match.getColumnIndex("duration")));
        duration = d[0] + ":" + d[1] + " min";

        Date origDate = new Date(match.getLong(match.getColumnIndex("start_time")) * 1000);
        starttime = new SimpleDateFormat("MMM dd, HH:mm").format(origDate);

        int[] fbt = Defines.splitToComponentTimes(match.getLong(match.getColumnIndex("first_blood_time")));
        firstbloodtime = fbt[1] + ":" + fbt[2] + " min";

        match.close();

        //calc most kills
        Cursor record = SQLManager.instance.getRecordPlayer(matchid, "kills");
        kills = new MatchSummaryRecord(record.getInt(record.getColumnIndex("kills")), record);
        record.close();

        record = SQLManager.instance.getRecordPlayer(matchid, "deaths");
        deaths = new MatchSummaryRecord(record.getInt(record.getColumnIndex("deaths")), record);
        record.close();

        record = SQLManager.instance.getRecordPlayer(matchid, "assists");
        assists = new MatchSummaryRecord(record.getInt(record.getColumnIndex("assists")), record);
        record.close();

        record = SQLManager.instance.getRecordPlayer(matchid, "last_hits");
        last_hits = new MatchSummaryRecord(record.getInt(record.getColumnIndex("last_hits")), record);
        record.close();

        record = SQLManager.instance.getRecordPlayer(matchid, "denies");
        denies = new MatchSummaryRecord(record.getInt(record.getColumnIndex("denies")), record);
        record.close();

        record = SQLManager.instance.getRecordPlayer(matchid, "hero_damage");
        herodamage = new MatchSummaryRecord(record.getInt(record.getColumnIndex("hero_damage")), record);
        record.close();

        record = SQLManager.instance.getRecordPlayer(matchid, "tower_damage");
        towerdamage = new MatchSummaryRecord(record.getInt(record.getColumnIndex("tower_damage")), record);
        record.close();

        record = SQLManager.instance.getRecordPlayer(matchid, "hero_healing");
        herohealing = new MatchSummaryRecord(record.getInt(record.getColumnIndex("hero_healing")), record);
        record.close();

    }







}
