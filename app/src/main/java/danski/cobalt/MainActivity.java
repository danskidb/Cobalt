package danski.cobalt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import danski.cobalt.sql.SQLManager;

public class MainActivity extends AppCompatActivity {

    Button b;
    Context context;
    SQLManager sm;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Log.i("MainActivity", "Going to create sql");
        sm = new SQLManager(context);
        db = sm.getReadableDatabase();

        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = sm.getReadableDatabase();

            }
        });
    }
}
