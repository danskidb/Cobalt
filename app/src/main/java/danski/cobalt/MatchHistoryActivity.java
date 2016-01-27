package danski.cobalt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import danski.cobalt.Adaptor.MatchHistoryListAdapter;


/**
 * Created by danny on 27-9-15.
 */
public class MatchHistoryActivity extends AppCompatActivity {

    ListView matchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Defines.CurrentContext = this;
        setContentView(R.layout.activity_matchhistory);

        matchlist = (ListView) findViewById(R.id.matchistory_list);
        matchlist.setAdapter(new MatchHistoryListAdapter(this));
    }

    //TODO: Pull to refresh?
}
