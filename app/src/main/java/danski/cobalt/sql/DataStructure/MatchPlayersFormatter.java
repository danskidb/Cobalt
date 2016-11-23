package danski.cobalt.sql.DataStructure;

import java.util.ArrayList;

import danski.cobalt.Defines;
import danski.cobalt.MatchTools;
import danski.cobalt.sql.SQLManager;
import danski.cobalt.sql.SteamprofileRetreiver;

/**
 * Created by danny on 7-11-2016.
 */

public class MatchPlayersFormatter {

    public ArrayList<Player> players;

    public MatchPlayersFormatter(){
        if(SQLManager.instance == null) new SQLManager(Defines.CurrentContext);
        if(MatchTools.instance == null) new MatchTools();
        if(SteamprofileRetreiver.instance == null) new SteamprofileRetreiver();
    }

    public void LoadData(long matchid){
        players = SQLManager.instance.getPlayersInMatch(matchid);
    }
}
