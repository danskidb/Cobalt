package danski.cobalt.Home;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import danski.cobalt.Defines;
import danski.cobalt.MatchTools;
import danski.cobalt.R;
import danski.cobalt.sql.SQLManager;

/**
 * Created by Danny on 08/02/2016.
 */
public class MatchListAdaptor extends CursorAdapter {

    Context context;
    Cursor matchlist;

    int placeholderImage = R.drawable.templar_assassin_full;

    public MatchListAdaptor(Context _context, Cursor _cursor, int flags) {
        super(_context, _cursor, flags);
        context = _context;
        matchlist = _cursor;

    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View v = null;

        if(cursor.getInt(cursor.getColumnIndex("hasdetail")) > 0){
            v = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false);

        } else {
            v = LayoutInflater.from(context).inflate(R.layout.item_match_small, parent, false);        }

        v.setTag(holder);
        return v;

    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor matchlist) {
        ViewHolder holder = new ViewHolder();

        if(matchlist.getInt(matchlist.getColumnIndex("hasdetail")) > 0){
            holder.status = (TextView) view.findViewById(R.id.item_match_status);
            holder.id = (TextView) view.findViewById(R.id.item_match_id);
            holder.timestarted = (TextView) view.findViewById(R.id.item_match_time);
            holder.duration = (TextView) view.findViewById(R.id.item_match_duration);
            holder.KDA = (TextView) view.findViewById(R.id.item_match_kda);
            holder.matchtype = (TextView) view.findViewById(R.id.item_match_type);
            holder.overlay = (ImageView) view.findViewById(R.id.match_overlay);
            holder.heroImage = (ImageView) view.findViewById(R.id.match_heroImg);

            SQLManager sm = new SQLManager(context, false);
            Cursor match = sm.getMatch(matchlist.getLong(matchlist.getColumnIndex("match_id")));
            Cursor playerdata = MatchTools.getMyPlayerDetails(matchlist.getLong(matchlist.getColumnIndex("match_id")), context);
            Cursor hero = sm.getHero(playerdata.getInt(playerdata.getColumnIndex("Hero_hero_id")));

            Picasso.with(context).load(Defines.heroimgurl + hero.getString(hero.getColumnIndex("hero_title")) + "_full.png").placeholder(placeholderImage).into(holder.heroImage);


            if(playerdata.getInt(playerdata.getColumnIndex("leaver_status")) > 0){
                holder.overlay.setImageDrawable(context.getResources().getDrawable(R.drawable.gradient_orange));
                holder.status.setTextColor(context.getResources().getColor(R.color.text_abandon));

                holder.status.setText(Defines.getLeaverStatus(playerdata.getInt(playerdata.getColumnIndex("leaver_status"))));


            } else {
                if(playerdata.getInt(playerdata.getColumnIndex("win")) > 0){
                    holder.overlay.setImageDrawable(context.getResources().getDrawable(R.drawable.gradient_green));
                    holder.status.setText("WON");
                    holder.status.setTextColor(context.getResources().getColor(R.color.text_win));
                } else {
                    holder.overlay.setImageDrawable(context.getResources().getDrawable(R.drawable.gradient_red));
                    holder.status.setText("LOST");
                    holder.status.setTextColor(context.getResources().getColor(R.color.text_loss));
                }
            }

            String kdastr = "KDA: " + playerdata.getInt(playerdata.getColumnIndex("kills")) + " / " + playerdata.getInt(playerdata.getColumnIndex("deaths")) + " / " + playerdata.getInt(playerdata.getColumnIndex("assists"));
            holder.KDA.setText(kdastr);

            int[] duration = Defines.splitToComponentTimes(matchlist.getInt(matchlist.getColumnIndex("duration")));
            holder.duration.setText(duration[0] + "h " + duration[1] + "m");
            holder.matchtype.setText(MatchTools.getGameMode(matchlist.getInt(matchlist.getColumnIndex("game_mode"))));

            sm.close();
        } else {
            holder.status = (TextView) view.findViewById(R.id.item_match_status);
            holder.id = (TextView) view.findViewById(R.id.item_match_small_id);
            holder.timestarted = (TextView) view.findViewById(R.id.item_match_small_time);
            holder.heroImage = (ImageView) view.findViewById(R.id.heroImg_small);

            SQLManager sm = new SQLManager(context);
            Cursor playerdata = MatchTools.getMyPlayerDetails(matchlist.getLong(matchlist.getColumnIndex("match_id")), context);
            Cursor hero = sm.getHero(playerdata.getInt(playerdata.getColumnIndex("Hero_hero_id")));
            Log.v("MLA", hero.getString(hero.getColumnIndex("hero_title")) + "_full.png");
            Picasso.with(context).load(Defines.heroimgurl + hero.getString(hero.getColumnIndex("hero_title")) + "_full.png").placeholder(placeholderImage).fit().into(holder.heroImage);

        }

        // Extract properties from cursor
        holder.id.setText(matchlist.getString(matchlist.getColumnIndex("match_id")));

        Date origDate = new Date(matchlist.getLong(matchlist.getColumnIndex("start_time")) * 1000);
        holder.timestarted.setText(new SimpleDateFormat("dd-MM / HH:mm").format(origDate));



    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    private int getItemViewType(Cursor matchlist) {
        return matchlist.getInt(matchlist.getColumnIndex("hasdetail"));
    }

    @Override
    public int getItemViewType(int position) {
        Cursor matchlist = (Cursor) getItem(position);
        return getItemViewType(matchlist);
    }

    public static class ViewHolder {
        public TextView status;         //win / loss / abandon / download
        public TextView id;             //matchid
        public TextView timestarted;    //time started
        public TextView duration;       //duration of match
        public TextView KDA;            //KDA hero made
        public TextView matchtype;      //match type
        public ImageView heroImage;     //Hero image
        public ImageView overlay;       //Gradient image
    }

}
