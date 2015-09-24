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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        long steamid64 = Long.parseLong(checksteamid);
        long steamid32 = Defines.idTo32(steamid64);

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

        holder.kdagraph.getLegendRenderer().setVisible(true);
        holder.kdagraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        holder.kdagraph.addSeries(kdalgs);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        GraphView kdagraph;

        public ViewHolder(View view) {
            super(view);
            kdagraph = (GraphView) view.findViewById(R.id.maingraph);
        }
    }
}

