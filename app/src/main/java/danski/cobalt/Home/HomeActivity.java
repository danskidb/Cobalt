package danski.cobalt.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import danski.cobalt.Defines;
import danski.cobalt.R;
import danski.cobalt.sql.DataStructure.Player;
import danski.cobalt.sql.MatchListRetreiver;
import danski.cobalt.sql.SQLManager;
import danski.cobalt.sql.SteamprofileRetreiver;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    Player loggedinplayer;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        loggedinplayer = SQLManager.instance.getPlayer(prefs.getLong("steamid64", 0));

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get header and set the name and user image
        View headerlayout = navigationView.getHeaderView(0);
        ImageView img = (ImageView) headerlayout.findViewById(R.id.userimg);
        TextView tv = (TextView) headerlayout.findViewById(R.id.username);

        tv.setText(loggedinplayer.Name);
        Picasso.with(this).load(loggedinplayer.URL_avatarmed).into(img);


        Defines.CurrentContext = this;

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new home_me());
        this.getSupportActionBar().setTitle(loggedinplayer.Name);
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            MatchListRetreiver mlr = new MatchListRetreiver();
            mlr.alsoGetLatestMatch = true;
            mlr.RetreiveAsync(false);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        Class fragmentClass;
        switch(item.getItemId()){
            case R.id.nav_me:
                fragmentClass = home_me.class;
                this.getSupportActionBar().setTitle(getString(R.string.app_name));
                break;
            case R.id.nav_matchhistory:
                fragmentClass = home_matchhistory.class;
                this.getSupportActionBar().setTitle(getString(R.string.title_matchhistory));
                break;
            case R.id.nav_stats:
                fragmentClass = home_statistics.class;
                this.getSupportActionBar().setTitle(getString(R.string.title_stats));
                break;
            case R.id.nav_heroes:
                fragmentClass = home_heroes.class;
                this.getSupportActionBar().setTitle(getString(R.string.title_heroes));
                break;
            case R.id.nav_items:
                fragmentClass = home_items.class;
                this.getSupportActionBar().setTitle(getString(R.string.title_items));
                break;
            case R.id.nav_settings:
                fragmentClass = home_settings.class;
                this.getSupportActionBar().setTitle(getString(R.string.title_settings));
                break;
            default:
                fragmentClass = home_me.class;
                this.getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        try{
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        SQLManager.instance.getDatabase().close();
    }
}
