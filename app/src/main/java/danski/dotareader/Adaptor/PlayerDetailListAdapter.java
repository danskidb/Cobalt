package danski.dotareader.Adaptor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import danski.dotareader.Data.Match;
import danski.dotareader.Data.Player;
import danski.dotareader.Defines;
import danski.dotareader.R;

/**
 * Created by Danny on 1-7-2015.
 */
public class PlayerDetailListAdapter extends BaseAdapter {

    Context context;
    Match match;
    Player[] player;
    private static LayoutInflater inflater = null;

    public PlayerDetailListAdapter(Context context, Match match) {
        this.context = context;
        this.match = match;

        player = match.Players;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return match.Players.length;
    }

    @Override
    public Object getItem(int position) {
        return match.Players[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.item_playerdetaillist, null);

        //Player name
        TextView tv_player = (TextView) vi.findViewById(R.id.playerdetaillist_player);
        tv_player.setText(player[position].player_name);

        //Hero image
        ImageView tv_hero  = (ImageView) vi.findViewById(R.id.playerdetaillist_heroimg);
        Picasso.with(Defines.CurrentContext).load(player[position].hero_image_url).into(tv_hero);

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

        //Item images
        ImageView[] iv_items;
        iv_items = new ImageView[6];
        iv_items[0] = (ImageView) vi.findViewById(R.id.playerdetaillist_item0);
        iv_items[1] = (ImageView) vi.findViewById(R.id.playerdetaillist_item1);
        iv_items[2] = (ImageView) vi.findViewById(R.id.playerdetaillist_item2);
        iv_items[3] = (ImageView) vi.findViewById(R.id.playerdetaillist_item3);
        iv_items[4] = (ImageView) vi.findViewById(R.id.playerdetaillist_item4);
        iv_items[5] = (ImageView) vi.findViewById(R.id.playerdetaillist_item5);

        for (int a = 0; a < 6; a++){
            String curr;
            curr = player[position].item_image_url[a];

            Picasso.with(Defines.CurrentContext).load(curr).into(iv_items[a]);
        }

        //Stats
        TextView kills = (TextView) vi.findViewById(R.id.pd_kills);
        kills.setText(player[position].kills + "");

        TextView death = (TextView) vi.findViewById(R.id.pd_death);
        death.setText(player[position].deaths + "");

        TextView assist = (TextView) vi.findViewById(R.id.pd_assist);
        assist.setText(player[position].assists + "");

        TextView gold = (TextView) vi.findViewById(R.id.pd_gold);
        gold.setText(player[position].gold_spent + "");

        TextView lh = (TextView) vi.findViewById(R.id.pd_lasthit);
        lh.setText(player[position].last_hits + "");

        TextView dn = (TextView) vi.findViewById(R.id.pd_deny);
        dn.setText(player[position].denies + "");

        TextView xpm = (TextView) vi.findViewById(R.id.pd_xpm);
        xpm.setText(player[position].xp_per_min + "");

        TextView gpm = (TextView) vi.findViewById(R.id.pd_gpm);
        gpm.setText(player[position].gold_per_min + "");

        TextView apm = (TextView) vi.findViewById(R.id.pd_apm);

        TextView hdmg = (TextView) vi.findViewById(R.id.pd_herodmg);
        hdmg.setText(player[position].hero_damage + "");

        TextView hheal = (TextView) vi.findViewById(R.id.pd_heroheal);
        hheal.setText(player[position].hero_healing + "");

        TextView tdmg = (TextView) vi.findViewById(R.id.pd_towerdmg);
        tdmg.setText(player[position].tower_damage + "");



        return vi;
    }
}
