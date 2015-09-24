package danski.dotareader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Danny on 30-6-2015.
 */
public class PlayerListAdapter extends BaseAdapter {
    Context context;
    String[] player;
    String[] url;
    String[] kda;
    private static LayoutInflater inflater = null;

    public PlayerListAdapter(Context context, String[] player, String[] url, String [] kda) {
        this.context = context;
        this.player = player;
        this.url = url;
        this.kda = kda;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return player.length;
    }

    @Override
    public Object getItem(int position) {
        return player[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.item_playerlist, null);

        //Player name
        TextView tv_player = (TextView) vi.findViewById(R.id.playerlist_player);
        tv_player.setText(player[position]);

        //Hero image
        ImageView tv_hero  = (ImageView) vi.findViewById(R.id.playerlist_heroimg);
        Picasso.with(Defines.CurrentContext).load(url[position]).into(tv_hero);

        //Background color
        //TODO: Use more pretty colors.
        LinearLayout layout = (LinearLayout) vi.findViewById(R.id.playerlist_layout);
        if(position <= 4){
            //Radiant = goodguys
            layout.setBackgroundColor(Color.GREEN);
        } else {
            //Dire = badguys
            layout.setBackgroundColor(Color.RED);
        }

        //KDA
        TextView tv_kda = (TextView) vi.findViewById(R.id.playerlist_kda);
        tv_kda.setText(kda[position]);


        return vi;
    }

}
