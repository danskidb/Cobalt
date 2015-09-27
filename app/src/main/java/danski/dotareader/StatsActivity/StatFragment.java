package danski.dotareader.StatsActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import danski.dotareader.Defines;
import danski.dotareader.R;

/**
 * Created by danny on 27-9-15.
 */
public class StatFragment extends Fragment{


    private static final String ARG_POSITION = "position";

    private int position;
    Stat[] stats;

    public static StatFragment newInstance(int position, Stat[] _stat) {
        StatFragment f = new StatFragment ();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putSerializable("stat", _stat);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
        stats = (Stat[]) getArguments().getSerializable("stat");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.stats_winrate, container, false);

        GraphView graph = (GraphView) root.findViewById(R.id.winrategraph);
        StatsGenerator sg = new StatsGenerator(root.getContext());

        for(Stat st: stats){
            LineGraphSeries<DataPoint> lgs = new LineGraphSeries<DataPoint>(st.datapoints);
            lgs.setColor(st.color);
            lgs.setTitle(st.title);

            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            graph.addSeries(lgs);
        }
        return root;

    }

}
