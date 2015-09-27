package danski.dotareader.StatsActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jjoe64.graphview.series.DataPoint;

import danski.dotareader.Defines;

/**
 * Created by danny on 27-9-15.
 */
public class StatsGenerator {

    long steamid32;
    SharedPreferences prefs;
    Context context;

    public StatsGenerator(Context _context){
        context = _context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        steamid32 = Defines.idTo32(Long.parseLong(prefs.getString("steamid", null)));
    }

    public DataPoint[] generate(StatTypes type){
        DataPoint[] point = new DataPoint[Defines.CurrentMatches.length];

        for (int i = 0; i < Defines.CurrentMatches.length; i++) {
            for (int j = 0; j < Defines.CurrentMatches[i].Players.length; j++) {
                int invert = Defines.CurrentMatches.length - i - 1;
                if (Defines.CurrentMatches[i].Players[j].account_id == steamid32) {

                    switch (type) {
                        case kill:
                            point[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].kills);
                            break;
                        case death:
                            point[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].deaths);
                            break;
                        case assist:
                            point[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].assists);
                            break;
                        case kda:
                            int kill = Defines.CurrentMatches[invert].Players[j].kills;
                            int death = Defines.CurrentMatches[invert].Players[j].deaths;
                            int assist = Defines.CurrentMatches[invert].Players[j].assists;

                            point[i] = new DataPoint(i, (kill + assist) / (death + 1));
                            break;
                        case xpm:
                            point[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].xp_per_min);
                            break;
                        case gpm:
                            point[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].gold_per_min);
                            break;
                        case lasthits:
                            point[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].last_hits);
                            break;
                        case denies:
                            point[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].denies);
                            break;
                        case goldspent:
                            point[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].gold_spent);
                            break;
                        case winrate:
                            break;
                    }
                }
            }
        }


        return point;
    }

}
