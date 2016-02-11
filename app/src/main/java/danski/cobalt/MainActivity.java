package danski.cobalt;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import danski.cobalt.adaptor.MatchListAdaptor;
import danski.cobalt.sql.HeroRetreiver;
import danski.cobalt.sql.ItemRetreiver;
import danski.cobalt.sql.MatchListRetreiver;
import danski.cobalt.sql.MatchRetreiver;
import danski.cobalt.sql.SQLManager;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;

    Button b;
    Button grabitem;
    Button grabheroes;
    ListView lv;
    SQLManager sm;

    MatchListAdaptor mla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Defines.CurrentContext = this;
        instance = this;
        sm = new SQLManager(this);

        findViews();

        Cursor matches = sm.getAllMatches();
        if(matches.getCount() > 0){
            populateList();
        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchListRetreiver mlr = new MatchListRetreiver();
                mlr.retreive();
            }
        });

        grabitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemRetreiver ir = new ItemRetreiver();
                ir.retreive();
            }
        });

        grabheroes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeroRetreiver hr = new HeroRetreiver();
                hr.retreive();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    TextView matchid = (TextView) view.findViewById(R.id.item_match_small_id);
                    MatchRetreiver mr = new MatchRetreiver();
                    mr.retreive(Long.parseLong(matchid.getText().toString()));
                } catch (Exception e){

                }
            }
        });
    }

    public void populateList(){


        final Cursor allmatches = sm.getAllMatches();
        mla = new MatchListAdaptor(this, allmatches, 0);
        lv.setAdapter(mla);

    }

    void findViews(){
        b = (Button) findViewById(R.id.button);
        grabitem = (Button) findViewById(R.id.button3) ;
        grabheroes = (Button) findViewById(R.id.button2);
        lv = (ListView) findViewById(R.id.listView);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        sm.getDatabase().close();
    }
}
