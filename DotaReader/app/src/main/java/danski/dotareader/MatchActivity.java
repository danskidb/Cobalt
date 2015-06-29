package danski.dotareader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MatchActivity extends ActionBarActivity {

    Match thisMatch;
    TextView tv_id;
    TextView tv_duration;
    TextView tv_firstblood;
    TextView tv_winners;
    TextView tv_lobbytype;
    TextView tv_cluster;
    TextView tv_gamemode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Defines.CurrentContext = MatchActivity.this;
        setContentView(R.layout.activity_match);

        thisMatch = Defines.SelectedMatch;


        tv_id = (TextView)findViewById(R.id.ma_matchid);
        tv_id.setText(thisMatch.matchid + "");

        //TODO: Decimal in time
        tv_duration = (TextView) findViewById(R.id.ma_duration);
        tv_duration.setText(thisMatch.duration + "");

        //TODO: Decimal in time
        tv_firstblood = (TextView) findViewById(R.id.ma_firstblood);
        float rounded = Math.round(thisMatch.firstBloodTime * 100.0f)/100.0f;
        tv_firstblood.setText(rounded + "");


        tv_winners = (TextView) findViewById(R.id.ma_winside);
        tv_winners.setText(thisMatch.winningSide + "");

        tv_lobbytype = (TextView) findViewById(R.id.ma_lobbytype);
        tv_lobbytype.setText(thisMatch.lobbyType);

        tv_cluster = (TextView) findViewById(R.id.ma_cluster);
        tv_cluster.setText(thisMatch.ServerRegion);

        tv_gamemode = (TextView) findViewById(R.id.ma_gamemode);
        tv_gamemode.setText(thisMatch.GameMode);
    }
}
