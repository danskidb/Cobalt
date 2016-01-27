package danski.cobalt.Adaptor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

import danski.cobalt.Data.Player;
import danski.cobalt.Defines;
import danski.cobalt.MatchActivity.TabbedMatchActivity;
import danski.cobalt.R;

/**
 * Created by danny on 24/09/2015.
 */
public class Binder_lastmatch extends DataBinder<Binder_lastmatch.ViewHolder> {

    Player lastplayer;
    Long steamid32;

    public Binder_lastmatch(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        steamid32 = Defines.idTo32(Long.parseLong(checksteamid));
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_main_card_lastmatch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder lmvh, int position) {
        for (int j = 0; j < Defines.CurrentMatches[0].Players.length; j++) {
            if (Defines.CurrentMatches[0].Players[j].account_id == steamid32) {
                lastplayer = Defines.CurrentMatches[0].Players[j];

                Picasso.with(Defines.CurrentContext).load(lastplayer.hero_image_url).into(lmvh.hero);

                String conditionstring;
                switch (Defines.CurrentMatches[0].winningSide) {
                    case Radiant:
                        if (Defines.CurrentMatches[0].Players[j].player_slot <= 4) {
                            conditionstring = "WON";
                        } else {
                            conditionstring = "LOST";
                        }
                        break;
                    case Dire:
                        if (Defines.CurrentMatches[0].Players[j].player_slot <= 4) {
                            conditionstring = "LOST";
                        } else {
                            conditionstring = "WON";
                        }
                        break;
                    default:
                        conditionstring = "";
                        break;

                }
                lmvh.condition.setText(conditionstring);

                Date origDate = new Date(Defines.CurrentMatches[0].start_time * 1000);
                String date = new SimpleDateFormat("dd-MM / HH:mm").format(origDate);
                lmvh.started.setText("Started " + date);
                lmvh.duration.setText("Lasted " + Defines.CurrentMatches[0].duration);
                lmvh.matchid.setText("ID: " + Defines.CurrentMatches[0].matchid);

                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[0]).into(lmvh.item0);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[1]).into(lmvh.item1);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[2]).into(lmvh.item2);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[3]).into(lmvh.item3);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[4]).into(lmvh.item4);
                Picasso.with(Defines.CurrentContext).load(lastplayer.item_image_url[5]).into(lmvh.item5);

                lmvh.kill.setText(lastplayer.kills + "");
                lmvh.death.setText(lastplayer.deaths + "");
                lmvh.assist.setText(lastplayer.assists + "");
                lmvh.lasthit.setText(lastplayer.last_hits + "");
                lmvh.deny.setText(lastplayer.denies + "");
                lmvh.xpm.setText(lastplayer.xp_per_min + "");
                lmvh.gpm.setText(lastplayer.gold_per_min + "");
            }
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hero;
        TextView started;
        TextView duration;
        TextView condition;
        TextView matchid;

        ImageView item0;
        ImageView item1;
        ImageView item2;
        ImageView item3;
        ImageView item4;
        ImageView item5;

        TextView kill;
        TextView death;
        TextView assist;
        TextView lasthit;
        TextView deny;
        TextView xpm;
        TextView gpm;

        View v;

        public ViewHolder(View _v) {
            super(_v);
            v = _v;

            hero = (ImageView) v.findViewById(R.id.lastmatch_heroimg);
            started = (TextView) v.findViewById(R.id.lm_started);
            duration = (TextView) v.findViewById(R.id.lm_lasted);
            condition = (TextView) v.findViewById(R.id.lm_condition);
            matchid = (TextView) v.findViewById(R.id.lm_id);

            item0 = (ImageView) v.findViewById(R.id.lm_item0);
            item1 = (ImageView) v.findViewById(R.id.lm_item1);
            item2 = (ImageView) v.findViewById(R.id.lm_item2);
            item3 = (ImageView) v.findViewById(R.id.lm_item3);
            item4 = (ImageView) v.findViewById(R.id.lm_item4);
            item5 = (ImageView) v.findViewById(R.id.lm_item5);

            kill = (TextView) v.findViewById(R.id.lm_kill);
            death = (TextView) v.findViewById(R.id.lm_death);
            assist = (TextView) v.findViewById(R.id.lm_assist);
            lasthit = (TextView) v.findViewById(R.id.lm_lasthit);
            deny = (TextView) v.findViewById(R.id.lm_deny);
            xpm = (TextView) v.findViewById(R.id.lm_xpm);
            gpm = (TextView) v.findViewById(R.id.lm_gpm);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Defines.SelectedMatch = Defines.CurrentMatches[0];
                    Intent i = new Intent(v.getContext(), TabbedMatchActivity.class);
                    v.getContext().startActivity(i);
                }
            });

        }
    }

}
