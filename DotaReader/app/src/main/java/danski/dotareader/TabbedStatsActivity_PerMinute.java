package danski.dotareader;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class TabbedStatsActivity_PerMinute extends Fragment {


    private static final String ARG_POSITION = "position";

    private int position;

    public static TabbedStatsActivity_PerMinute newInstance(int position) {
        TabbedStatsActivity_PerMinute f = new TabbedStatsActivity_PerMinute();
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


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        long steamid64 = Long.parseLong(checksteamid);
        long steamid32 = Defines.idTo32(steamid64);
        GraphView perminutegraph = (GraphView) root.findViewById(R.id.winrategraph);

        DataPoint[] xpmp = new DataPoint[Defines.CurrentMatches.length];
        DataPoint[] gpmp = new DataPoint[Defines.CurrentMatches.length];

        for (int i = 0; i < Defines.CurrentMatches.length; i++){
            //Log.d("match", i + "");
            for (int j = 0; j < Defines.CurrentMatches[i].Players.length; j++){
                if(Defines.CurrentMatches[i].Players[j].account_id == steamid32){
                    xpmp[i] = new DataPoint(i, Defines.CurrentMatches[i].Players[j].xp_per_min);
                    gpmp[i] = new DataPoint(i, Defines.CurrentMatches[i].Players[j].gold_per_min);
                    //Log.d("datapoint", i + ", " + Defines.CurrentMatches[i].Players[j].xp_per_min);

                    //TODO: REVERSE THESE SUCKERS
                }
            }
        }

        LineGraphSeries<DataPoint> xpm = new LineGraphSeries<DataPoint>(xpmp);
        xpm.setColor(Color.BLUE);
        xpm.setTitle("XPM");
        LineGraphSeries<DataPoint> gpm = new LineGraphSeries<DataPoint>(gpmp);
        gpm.setColor(Color.RED);
        gpm.setTitle("GPM");

        perminutegraph.getLegendRenderer().setVisible(true);
        perminutegraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        perminutegraph.addSeries(xpm);
        perminutegraph.addSeries(gpm);
        return root;

    }

}
