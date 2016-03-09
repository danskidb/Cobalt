package danski.cobalt.Match;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import danski.cobalt.R;

public class MatchActivity extends AppCompatActivity {

    Long matchid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Bundle b = getIntent().getExtras();
        matchid = b.getLong("matchid");

        this.getSupportActionBar().setTitle("Match " + matchid);
    }
}
