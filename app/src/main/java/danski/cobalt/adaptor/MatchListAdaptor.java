package danski.cobalt.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import danski.cobalt.R;

/**
 * Created by Danny on 08/02/2016.
 */
public class MatchListAdaptor extends CursorAdapter {

    Context context;
    Cursor cursor;

    public MatchListAdaptor(Context _context, Cursor _cursor, int flags) {
        super(_context, _cursor, flags);
        context = _context;
        cursor = _cursor;

    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_match_small, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView status = (TextView) view.findViewById(R.id.item_match_small_status);
        TextView id = (TextView) view.findViewById(R.id.item_match_small_id);
        TextView time = (TextView) view.findViewById(R.id.item_match_small_time);

        // Extract properties from cursor
        id.setText("ID: " + cursor.getString(cursor.getColumnIndex("match_id")));

        Date origDate = new Date(cursor.getLong(cursor.getColumnIndex("start_time")) * 1000);
        time.setText(new SimpleDateFormat("dd-MM / HH:mm").format(origDate));

    }


}
