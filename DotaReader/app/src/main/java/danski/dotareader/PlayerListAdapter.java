package danski.dotareader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Danny on 30-6-2015.
 */
public class PlayerListAdapter extends BaseAdapter {
    Context context;
    String[] player;
    String[] hero;
    private static LayoutInflater inflater = null;

    public PlayerListAdapter(Context context, String[] player, String[] hero) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.player = player;
        this.hero = hero;
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
            vi = inflater.inflate(R.layout.item_playerlist, null);
        TextView tv_player = (TextView) vi.findViewById(R.id.playerlist_player);
        tv_player.setText(player[position]);

        TextView tv_hero  = (TextView) vi.findViewById(R.id.playerlist_hero);
        tv_hero.setText(hero[position]);
        return vi;
    }

}
