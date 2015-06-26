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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match);

        thisMatch = Defines.SelectedMatch;


        tv_id = (TextView)findViewById(R.id.ma_matchid);
        tv_id.setText(thisMatch.matchid + "");

        tv_duration = (TextView) findViewById(R.id.ma_duration);
        tv_duration.setText(thisMatch.duration + "");

    }
}
