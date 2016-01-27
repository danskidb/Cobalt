package danski.cobalt.MatchActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import danski.cobalt.Data.Match;
import danski.cobalt.Defines;
import danski.cobalt.Adaptor.PlayerListAdapter;
import danski.cobalt.R;


public class TabbedMatchActivity_Overview extends Fragment {

    Match thisMatch;
    TextView tv_id;
    TextView tv_duration;
    TextView tv_firstblood;
    TextView tv_winners;
    TextView tv_lobbytype;
    TextView tv_cluster;
    TextView tv_gamemode;
    ListView lv_playerlist;

    private static final String ARG_POSITION = "position";

    private int position;

    public static TabbedMatchActivity_Overview newInstance(int position) {
        TabbedMatchActivity_Overview f = new TabbedMatchActivity_Overview();
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
        View root = inflater.inflate(R.layout.activity_match, container, false);


        thisMatch = Defines.SelectedMatch;


        tv_id = (TextView)root.findViewById(R.id.ma_matchid);
        tv_id.setText(thisMatch.matchid + "");

        tv_duration = (TextView) root.findViewById(R.id.ma_duration);
        tv_duration.setText(thisMatch.duration + "");

        tv_firstblood = (TextView) root.findViewById(R.id.ma_firstblood);
        tv_firstblood.setText(thisMatch.firstBloodTime + "");


        tv_winners = (TextView) root.findViewById(R.id.ma_winside);
        tv_winners.setText(thisMatch.winningSide + "");

        tv_lobbytype = (TextView) root.findViewById(R.id.ma_lobbytype);
        tv_lobbytype.setText(thisMatch.lobbyType);

        tv_cluster = (TextView) root.findViewById(R.id.ma_cluster);
        tv_cluster.setText(thisMatch.ServerRegion);

        tv_gamemode = (TextView) root.findViewById(R.id.ma_gamemode);
        tv_gamemode.setText(thisMatch.GameMode);

        lv_playerlist = (ListView) root.findViewById(R.id.ma_playerlist);
        String[] player = new String[thisMatch.Players.length];
        String[] hero = new String[thisMatch.Players.length];
        String[] kda = new String[thisMatch.Players.length];

        for(int i = 0; i < thisMatch.Players.length; i++){
            player[i] = thisMatch.Players[i].player_name;
            hero[i] = thisMatch.Players[i].hero_image_url + "";

            if(thisMatch.Players[i].leaver_status == 0) {
                kda[i] = "KDA: " + thisMatch.Players[i].kills + "/" + thisMatch.Players[i].deaths + "/" + thisMatch.Players[i].assists;
            } else {
                kda[i] = thisMatch.Players[i].leaver_text;
            }
        }

        lv_playerlist.setAdapter(new PlayerListAdapter(Defines.CurrentContext, player, hero, kda));



        return root;

    }
}
