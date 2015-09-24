package danski.dotareader.Adaptor;

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

import java.text.SimpleDateFormat;
import java.util.Date;

import danski.dotareader.Data.Match;
import danski.dotareader.Data.Player;
import danski.dotareader.Defines;
import danski.dotareader.MatchActivity.TabbedMatchActivity;
import danski.dotareader.R;

/**
 * Created by danny on 24/09/2015.
 */


public class Adapter_MainActivity extends RecyclerView.Adapter<Adapter_MainActivity.LastMatchViewHolder>{

    Match lastmatch;

    Player lastplayer;
    Long steamid64;
    Long steamid32;

    public Adapter_MainActivity(Match _lastmatch){
        lastmatch = _lastmatch;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String checksteamid = prefs.getString("steamid", null);
        steamid64 = Long.parseLong(checksteamid);
        steamid32 = Defines.idTo32(steamid64);
    }

    @Override
    public int getItemCount(){
        return 1;
    }

    @Override
    public void onBindViewHolder(LastMatchViewHolder lmvh, int i){
        for (int j = 0; j < lastmatch.Players.length; j++) {
            if (lastmatch.Players[j].account_id == steamid32) {
                lastplayer = lastmatch.Players[j];

                Picasso.with(Defines.CurrentContext).load(lastplayer.hero_image_url).into(lmvh.hero);

                String conditionstring;
                switch (lastmatch.winningSide) {
                    case Radiant:
                        if (lastmatch.Players[j].player_slot <= 4) {
                            conditionstring = "WON";
                        } else {
                            conditionstring = "LOST";
                        }
                        break;
                    case Dire:
                        if (lastmatch.Players[j].player_slot <= 4) {
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

                Date origDate = new Date(lastmatch.start_time * 1000);
                String date = new SimpleDateFormat("dd-MM / HH:mm").format(origDate);
                lmvh.started.setText("Started " + date);
                lmvh.duration.setText("Lasted " + lastmatch.duration);
                lmvh.matchid.setText("ID: " + lastmatch.matchid);

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
    public LastMatchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_card_lastmatch, viewGroup, false);
        return new LastMatchViewHolder(itemView);
    }

    public static class LastMatchViewHolder extends RecyclerView.ViewHolder{
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

        public LastMatchViewHolder(View _v){
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
