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

public class TabbedStatsActivity_PerMatch extends Fragment {


    private static final String ARG_POSITION = "position";

    private int position;

    public static TabbedStatsActivity_PerMatch newInstance(int position) {
        TabbedStatsActivity_PerMatch f = new TabbedStatsActivity_PerMatch ();
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

        DataPoint[] lhp = new DataPoint[Defines.CurrentMatches.length];
        DataPoint[] dnp = new DataPoint[Defines.CurrentMatches.length];

        for (int i = 0; i < Defines.CurrentMatches.length; i++){
            for (int j = 0; j < Defines.CurrentMatches[i].Players.length; j++){
                int invert = Defines.CurrentMatches.length - i - 1;
                if(Defines.CurrentMatches[i].Players[j].account_id == steamid32){
                    lhp[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].last_hits);
                    dnp[i] = new DataPoint(i, Defines.CurrentMatches[invert].Players[j].denies);
                    //Log.d("datapoint", i + ", " + Defines.CurrentMatches[invert].Players[j].xp_per_min);
                }
            }
        }

        LineGraphSeries<DataPoint> lh = new LineGraphSeries<DataPoint>(lhp);
        lh.setColor(Color.BLUE);
        lh.setTitle("Last Hits");
        LineGraphSeries<DataPoint> dn = new LineGraphSeries<DataPoint>(dnp);
        dn.setColor(Color.RED);
        dn.setTitle("Denies");

        perminutegraph.getLegendRenderer().setVisible(true);
        perminutegraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        perminutegraph.addSeries(lh);
        perminutegraph.addSeries(dn);
        return root;

    }

}
