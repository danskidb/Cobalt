package danski.cobalt;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import danski.cobalt.sql.MatchListRetreiver;
import danski.cobalt.sql.SQLManager;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;

    Button b;
    ListView lv;
    SQLManager sm;
    ArrayAdapter<String> listada;

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
    }

    public void populateList(){

        final Cursor allmatches = sm.getAllMatches();
        ArrayList<String> cols = new ArrayList<>();
        for(int i = 0; i < allmatches.getCount(); i++){
            allmatches.moveToPosition(i);
            cols.add(allmatches.getString(allmatches.getColumnIndex("match_id")));
        }

        listada.clear();
        listada.addAll(cols);
        listada.notifyDataSetChanged();
        lv.deferNotifyDataSetChanged();
    }

    void findViews(){
        b = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.listView);

        ArrayList<String> temp = new ArrayList<>();
        temp.add("...");
        listada = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, temp);
        lv.setAdapter(listada);

    }
}
