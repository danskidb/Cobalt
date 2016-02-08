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


}
