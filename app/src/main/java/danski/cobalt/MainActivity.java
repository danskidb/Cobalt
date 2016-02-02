package danski.cobalt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import danski.cobalt.sql.MatchListRetreiver;
import danski.cobalt.sql.SQLManager;

public class MainActivity extends AppCompatActivity {

    Button b;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Defines.CurrentContext = this;

        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MatchListRetreiver mlr = new MatchListRetreiver();
                mlr.retreive();

            }
        });
    }
}
