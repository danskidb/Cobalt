package danski.dotareader;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by danny on 18/09/2015.
 */
class ScoreCard{
    String statdefinition;
    String actualstat;
    String statdetail;
    String heroimgurl;
    int arraypos;


    ScoreCard(String _statdefinition, String _actualstat, String _statdetail, String _heroimgurl, int _arraypos){
        statdefinition = _statdefinition;
        actualstat = _actualstat;
        statdetail = _statdetail;
        heroimgurl = _heroimgurl;
        arraypos = _arraypos;
    }
}


public class TabbedStatsActivity_Scoreboard_rvadapter extends RecyclerView.Adapter<TabbedStatsActivity_Scoreboard_rvadapter.CardViewHolder>{

    List<ScoreCard> cardlist;

    public TabbedStatsActivity_Scoreboard_rvadapter(List<ScoreCard> _cardlist){
        cardlist = _cardlist;
    }

    @Override
    public int getItemCount(){
        return cardlist.size();
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i){
        ScoreCard sc = cardlist.get(i);
        Picasso.with(cardViewHolder.v.getContext()).load(sc.heroimgurl).into(cardViewHolder.vheroimg);

        cardViewHolder.vstatdefinition.setText(sc.statdefinition);
        cardViewHolder.vactualstat.setText(sc.actualstat);
        cardViewHolder.vstatdetail.setText(sc.statdetail);
        cardViewHolder.varraypos = sc.arraypos;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stats_scoreboard_card, viewGroup, false);
        return new CardViewHolder(itemView);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        protected TextView vstatdefinition;
        protected TextView vactualstat;
        protected TextView vstatdetail;
        protected int varraypos;
        protected ImageView vheroimg;

        View v;

        public CardViewHolder(View _v){
            super(_v);
            v = _v;

            vheroimg = (ImageView) v.findViewById(R.id.stat_heroimg);
            vstatdefinition = (TextView) v.findViewById(R.id.statdefinition);
            vactualstat = (TextView) v.findViewById(R.id.actualstat);
            vstatdetail = (TextView) v.findViewById(R.id.statdetail);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Defines.SelectedMatch = Defines.CurrentMatches[varraypos];
                    Intent i = new Intent(v.getContext(), TabbedMatchActivity.class);
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
