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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }
}
