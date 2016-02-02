package danski.cobalt.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danny on 28/01/2016.
 */
public class Match {
    public long match_id;
    long match_seq_num;
    long start_time;
    int lobby_type;
    int radiant_team_id;
    int dire_team_id;

    Player[] players;

    public Match(){
    }

    public Match(JSONObject match){
        try{
            match_id = match.getLong("match_id");
            Log.i("Match", "NEW MATCH: " + match_id);
            match_seq_num = match.getLong("match_seq_num");
            start_time = match.getLong("start_time");
            lobby_type = match.getInt("lobby_type");
            radiant_team_id = match.getInt("radiant_team_id");
            dire_team_id = match.getInt("dire_team_id");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
