package danski.dotareader.StatsActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;

import com.astuetz.PagerSlidingTabStrip;

import danski.dotareader.Defines;
import danski.dotareader.R;

public class TabbedStatsActivity extends ActionBarActivity {

    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_match);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

        Defines.CurrentContext = this;

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Winrate", "KDA", "Per Minute", "Per Match", "Hall of Fame"};

        public MyPagerAdapter(FragmentManager fm) {
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
            //return SuperAwesomeCardFragment.newInstance(position);

            StatsGenerator sg = new StatsGenerator(Defines.CurrentContext);
            Stat[] stat;

            switch(position){
                case 0:
                    return TabbedStatsActivity_WinRate.newInstance(position);
                case 1:
                    //kda
                    stat = new Stat[1];
                    stat[0] = new Stat(StatTypes.kda, sg);
                    return StatFragment.newInstance(position, stat);
                case 2:
                    //Per minute
                    stat = new Stat[2];
                    stat[0] = new Stat(StatTypes.xpm, sg);
                    stat[1] = new Stat(StatTypes.gpm, sg);
                    return StatFragment.newInstance(position, stat);
                case 3:
                    //Per match
                    stat = new Stat[2];
                    stat[0] = new Stat(StatTypes.lasthits, sg);
                    stat[1] = new Stat(StatTypes.denies, sg);
                    return StatFragment.newInstance(position, stat);
                case 4:
                    return TabbedStatsActivity_Scoreboard.newInstance(position);
                default:
                    return TabbedStatsActivity_WinRate.newInstance(position);
            }
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };
}
