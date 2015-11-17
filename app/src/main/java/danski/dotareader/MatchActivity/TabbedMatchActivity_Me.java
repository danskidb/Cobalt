package danski.dotareader.MatchActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import danski.dotareader.Adaptor.Adapter_match_me;
import danski.dotareader.Data.Match;
import danski.dotareader.Data.Player;
import danski.dotareader.Defines;
import danski.dotareader.R;
import me.grantland.widget.AutofitHelper;

/**
 * Created by danny on 17-11-15.
 */
public class TabbedMatchActivity_Me extends Fragment {

    Match thisMatch;
    Player me;
    ListView lv;
    TextView hero;
    TextView kda;
    ImageView heroimg;

    ArrayList<String> listitem;
    ArrayList<String> listimg;

    private static final String ARG_POSITION = "position";
    private int position;

    public static TabbedMatchActivity_Me newInstance(int position) {
        TabbedMatchActivity_Me f = new TabbedMatchActivity_Me();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_matchactivity_me, container, false);
        thisMatch = Defines.SelectedMatch;
        hero = (TextView) root.findViewById(R.id.ma_me_heroandlv);
        AutofitHelper.create(hero);
        kda = (TextView) root.findViewById(R.id.ma_me_kda);
        AutofitHelper.create(kda);
        heroimg = (ImageView) root.findViewById(R.id.ma_me_heroimg);
        lv = (ListView) root.findViewById(R.id.ma_me_list);

        //FILL HERE

        //get player
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        String steamid = prefs.getString("steamid", "0");

        for (Player p : thisMatch.Players){
            if(p.steamid64.toString().equals(steamid)){
                me = p;
                Picasso.with(getActivity()).load(p.hero_image_url).into(heroimg);
                hero.setText(p.hero_name + ", level " + p.level);
                kda.setText("KDA: " + p.kills + "/" + p.deaths + "/" + p.assists);
            }
        }

        listitem = new ArrayList<>();
        listimg = new ArrayList<>();
        listitem.add("Items");
        listimg.add("0");

        for(String item : me.item_name){
            listitem.add(item);
            Log.i("TMMM", "Added item " + item);

        }

        for(String itemurl : me.item_image_url)
            listimg.add(itemurl);

        listitem.add("Stats");
        listimg.add("0");

        addStat("Kills", me.kills);
        addStat("Death", me.deaths);
        addStat("Assist", me.assists);
        addStat("Last Hits", me.last_hits);
        addStat("Denies", me.denies);
        addStat("XPM", me.xp_per_min);
        addStat("GPM", me.gold_per_min);
        /*addStat("Final gold", me.gold);
        addStat("Gold spent", me.gold_spent);
        addStat("Tower damage", me.tower_damage);
        addStat("Hero damage", me.hero_damage);
        addStat("Hero healing", me.hero_healing);*/


        Adapter_match_me adapter = new Adapter_match_me(getActivity(), listitem, listimg);
        lv.setAdapter(adapter);


        return root;

    }

    public void addStat(String which, int stat){
        listitem.add(which + ": " + stat);
        listimg.add("0");
    }
}
