package danski.cobalt.Adaptor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import danski.cobalt.Defines;
import danski.cobalt.R;
import danski.cobalt.StatsActivity.Stat;
import danski.cobalt.StatsActivity.StatTypes;
import danski.cobalt.StatsActivity.StatsGenerator;

/**
 * Created by danny on 24/09/2015.
 */
public class Binder_maingraphs extends DataBinder<Binder_maingraphs.ViewHolder> {

    SharedPreferences prefs;

    public Binder_maingraphs(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_main_card_graphs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        StatTypes[] st = StatTypes.values();
        Stat[] enabledstats = new Stat[st.length];

        prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());

        for(int i = 0; i < enabledstats.length; i++){
            enabledstats[i] = new Stat(st[i]);
            if(st[i] != StatTypes.kda) enabledstats[i].enabled = prefs.getBoolean(enabledstats[i].title, false);    //TODO: Fix winrate!
            else enabledstats[i].enabled = prefs.getBoolean(enabledstats[i].title, true);

            if(enabledstats[i].enabled && enabledstats[i].type != StatTypes.winrate) {
                enabledstats[i].generateDataPoints(new StatsGenerator(Defines.CurrentContext));

                LineGraphSeries<DataPoint> lgs = new LineGraphSeries<DataPoint>(enabledstats[i].datapoints);
                lgs.setColor(enabledstats[i].color);
                lgs.setTitle(enabledstats[i].title);

                holder.graph.getLegendRenderer().setVisible(true);
                holder.graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

                holder.graph.addSeries(lgs);
            }
        }

        holder.graph.getViewport().setScrollable(true);
        holder.graph.getViewport().setXAxisBoundsManual(true);
        holder.graph.getViewport().setMinX(0);
        holder.graph.getViewport().setMaxX(20);



    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        GraphView graph;

        public ViewHolder(View view) {
            super(view);
            graph = (GraphView) view.findViewById(R.id.maingraph);
        }
    }
}

