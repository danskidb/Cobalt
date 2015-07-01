package danski.dotareader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Danny on 1-7-2015.
 */
public class PlayerDetailListAdapter extends BaseAdapter {

    Context context;
    String[] player;
    String[] url;
    String[] kda;
    private static LayoutInflater inflater = null;

    public PlayerDetailListAdapter(Context context, String[] player, String[] url, String [] kda) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.player = player;
        this.url = url;
        this.kda = kda;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return player.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return player[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.item_playerdetaillist, null);

        //Player name
        TextView tv_player = (TextView) vi.findViewById(R.id.playerdetaillist_player);
        tv_player.setText(player[position]);

        //Hero image
        ImageView tv_hero  = (ImageView) vi.findViewById(R.id.playerdetaillist_heroimg);
        Picasso.with(Defines.CurrentContext).load(url[position]).into(tv_hero);

        //Background color
        //TODO: Use more pretty colors.
        RelativeLayout layout = (RelativeLayout) vi.findViewById(R.id.playerdetaillist_layout);
        if(position <= 4){
            //Radiant = goodguys
            layout.setBackgroundColor(Color.GREEN);
        } else {
            //Dire = badguys
            layout.setBackgroundColor(Color.RED);
        }

        //KDA
        //TextView tv_kda = (TextView) vi.findViewById(R.id.playerdetaillist_kda);
        //tv_kda.setText(kda[position]);


        return vi;
    }
}
