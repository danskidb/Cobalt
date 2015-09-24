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

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class TabbedStatsActivity_KDA extends Fragment {


    private static final String ARG_POSITION = "position";

    private int position;

    public static TabbedStatsActivity_KDA newInstance(int position) {
        TabbedStatsActivity_KDA f = new TabbedStatsActivity_KDA ();
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
        GraphView kdagraph = (GraphView) root.findViewById(R.id.winrategraph);
        float ysize = 0;

        DataPoint[] kdap = new DataPoint[Defines.CurrentMatches.length];
        for (int i = 0; i < Defines.CurrentMatches.length; i++) {
            for (int j = 0; j < Defines.CurrentMatches[i].Players.length; j++) {
                int invert = Defines.CurrentMatches.length - i - 1;
                if(Defines.CurrentMatches[i].Players[j].account_id == steamid32) {
                    int kill = Defines.CurrentMatches[invert].Players[j].kills;
                    int death = Defines.CurrentMatches[invert].Players[j].deaths;
                    int assist = Defines.CurrentMatches[invert].Players[j].assists;
                    float kda = (kill + assist)/(death +1);

                    kdap[i] = new DataPoint(i, kda);
                }
            }
        }

        LineGraphSeries<DataPoint> kdalgs = new LineGraphSeries<DataPoint>(kdap);
        kdalgs.setColor(Color.BLACK);
        kdalgs.setTitle("KDA Ratio");

        kdagraph.getLegendRenderer().setVisible(true);
        kdagraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        kdagraph.addSeries(kdalgs);

        return root;

    }

/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_stats_williamchart, container, false);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        long steamid64 = Long.parseLong(checksteamid);
        long steamid32 = Defines.idTo32(steamid64);

        LineChartView lcv = (LineChartView) root.findViewById(R.id.linechart);
        Point[] lcvdata = new Point[Defines.CurrentMatches.length];
        LineSet ls = new LineSet();

        for (int i = 0; i < Defines.CurrentMatches.length; i++) {
            for (int j = 0; j < Defines.CurrentMatches[i].Players.length; j++) {
                int invert = Defines.CurrentMatches.length - i - 1;
                if(Defines.CurrentMatches[i].Players[j].account_id == steamid32) {
                    int kill = Defines.CurrentMatches[invert].Players[j].kills;
                    int death = Defines.CurrentMatches[invert].Players[j].deaths;
                    int assist = Defines.CurrentMatches[invert].Players[j].assists;
                    float kda = (kill + assist)/(death +1);

                    lcvdata[i] = new Point(i + "", kda);
                    ls.addPoint(lcvdata[i]);

                    //kdap[i] = new DataPoint(i, kda);
                }
            }
        }

        lcv.addData(ls);

        return root;
    }*/

}
