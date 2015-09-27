package danski.dotareader.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import danski.dotareader.Data.Match;
import danski.dotareader.Defines;
import danski.dotareader.R;

/**
 * Created by danny on 27-9-15.
 */
public class MatchHistoryListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Long steamid32;
    Context context;

    public MatchHistoryListAdapter(Context _context) {
        context = _context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        steamid32 = Defines.idTo32(Long.parseLong(prefs.getString("steamid", null)));

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Defines.CurrentMatches.length;
    }

    @Override
    public Object getItem(int position) {
        return Defines.CurrentMatches[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.item_matchlist, null);

        ImageView heroimg = (ImageView) vi.findViewById(R.id.matchlist_heroimg);
        TextView heroname = (TextView) vi.findViewById(R.id.matchlist_heroname);
        TextView kda = (TextView) vi.findViewById(R.id.matchlist_kda);
        TextView result = (TextView) vi.findViewById(R.id.matchlist_result);
        TextView time = (TextView) vi.findViewById(R.id.matchlist_time);

        Match tempmatch = Defines.CurrentMatches[position];
        thingtobreak:
        for (int i = 0; i < tempmatch.Players.length; i++){
            if(tempmatch.Players[i].account_id == steamid32){
                Picasso.with(context).load(tempmatch.Players[i].hero_image_url).into(heroimg);
                heroname.setText(tempmatch.Players[i].hero_name);
                kda.setText(tempmatch.Players[i].kills + "/" + tempmatch.Players[i].deaths + "/" + tempmatch.Players[i].assists + " :KDA");

                String conditionstring;
                switch(tempmatch.winningSide){
                    case Radiant:
                        if(tempmatch.Players[i].player_slot <= 4){
                            conditionstring = "WON";
                            result.setTextColor(Color.rgb(0, 127, 0));
                        } else {
                            conditionstring = "LOST";
                            result.setTextColor(Color.RED);
                        }
                        break;
                    case Dire:
                        if(tempmatch.Players[i].player_slot <= 4){
                            conditionstring = "LOST";
                            result.setTextColor(Color.RED);
                        } else {
                            conditionstring = "WON";
                            result.setTextColor(Color.rgb(0,127,0));
                        }
                        break;
                    default:
                        conditionstring = "";
                        break;

                }
                result.setText(conditionstring);

                Date origDate = new Date(tempmatch.start_time * 1000);
                time.setText(new SimpleDateFormat("dd-MM / HH:mm").format(origDate) + " / " + tempmatch.duration);

                break thingtobreak;
            }
        }

        final int pos = position;
        final View vivi = vi;
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Defines.SelectedMatch = Defines.CurrentMatches[pos];
                Intent i = new Intent(vivi.getContext(), danski.dotareader.MatchActivity.TabbedMatchActivity.class);
                vivi.getContext().startActivity(i);
            }
        });

        return vi;

    }


}