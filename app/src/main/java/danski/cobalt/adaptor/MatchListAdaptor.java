package danski.cobalt.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import danski.cobalt.R;

/**
 * Created by Danny on 08/02/2016.
 */
public class MatchListAdaptor extends CursorAdapter {

    Context context;
    Cursor matchlist;

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

            holder.duration.setText("" + matchlist.getInt(matchlist.getColumnIndex("duration")));
            holder.matchtype.setText("" + matchlist.getInt(matchlist.getColumnIndex("game_mode")));
        } else {
            holder.status = (TextView) view.findViewById(R.id.item_match_status);
            holder.id = (TextView) view.findViewById(R.id.item_match_small_id);
            holder.timestarted = (TextView) view.findViewById(R.id.item_match_small_time);
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
