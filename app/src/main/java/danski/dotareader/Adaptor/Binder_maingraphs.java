package danski.dotareader.Adaptor;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import danski.dotareader.Defines;
import danski.dotareader.R;
import danski.dotareader.StatsActivity.Stat;
import danski.dotareader.StatsActivity.StatTypes;
import danski.dotareader.StatsActivity.StatsGenerator;

/**
 * Created by danny on 24/09/2015.
 */
public class Binder_maingraphs extends DataBinder<Binder_maingraphs.ViewHolder> {

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
        Stat stat = new Stat(StatTypes.kda, Color.BLACK, "KDA Ratio", new StatsGenerator(Defines.CurrentContext));

        LineGraphSeries<DataPoint> kdalgs = new LineGraphSeries<DataPoint>(stat.datapoints);
        kdalgs.setColor(stat.color);
        kdalgs.setTitle(stat.title);

        holder.graph.getLegendRenderer().setVisible(true);
        holder.graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        holder.graph.addSeries(kdalgs);
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

