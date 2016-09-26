package danski.cobalt.Match;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

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
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager pager = (ViewPager) findViewById(R.id.Matchviewpager);
        pager.setAdapter(new MatchPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.Matchtabs);
        tabs.setViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dotabuff) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dotabuff.com/matches/" + matchid));
            startActivity(i);

            return true;
        } else if (id == R.id.action_yasp) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yasp.co/matches/" + matchid));
            startActivity(i);
            return true;
        } else if(id == android.R.id.home){
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public class MatchPagerAdapter extends FragmentPagerAdapter{
        private final String[] TITLES = {"Summary", "You", "Players", "Structures"};

        public MatchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch(position){
                case 0:
                    return match_summary.newInstance(position, matchid);
                case 1:
                    return match_you.newInstance(position, matchid);
                case 2:
                    return match_players.newInstance(position);
                case 3:
                    return match_structures.newInstance(position);
                default:
                    return match_summary.newInstance(position, matchid);
            }
        }
    }
}
