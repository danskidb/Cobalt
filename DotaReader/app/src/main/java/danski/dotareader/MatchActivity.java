package danski.dotareader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MatchActivity extends ActionBarActivity {

    int matchid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent intent = getIntent();
        matchid = intent.getIntExtra("matchid", 1);

        if(matchid != 1){

        }
    }
}
