package danski.dotareader;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

public class TabbedStatsActivity_WinRate extends Fragment {


    private static final String ARG_POSITION = "position";

    private int position;

    public static TabbedStatsActivity_WinRate newInstance(int position) {
        TabbedStatsActivity_WinRate f = new TabbedStatsActivity_WinRate();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.stats_winrate, container, false);


        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String MatchDB = prefs.getString("matchdb", null);
        String checksteamid = prefs.getString("steamid", null);
        long steamid64 = Long.parseLong(checksteamid);
        long steamid32 = Defines.idTo32(steamid64);
        GraphView winrategraph = (GraphView) getView().findViewById(R.id.winrategraph);

        DataPoint[] points = new DataPoint[Defines.CurrentMatches.length];

        for (int i = 0; i < Defines.CurrentMatches.length; i++){
            for (int j = 0; j < Defines.CurrentMatches[i].Players.length; j++){
                if(Defines.CurrentMatches[i].Players[j].account_id == steamid32){
                    //Calculate winrate here
                }
            }
        }
*/


        return root;

    }

}
