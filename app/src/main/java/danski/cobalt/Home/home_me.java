package danski.cobalt.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import danski.cobalt.Defines;
import danski.cobalt.Match.MatchActivity;
import danski.cobalt.MatchTools;
import danski.cobalt.R;
import danski.cobalt.sql.DataStructure.Player;
import danski.cobalt.sql.SQLManager;

public class home_me extends Fragment {

    ImageView profilePicture;
    TextView profileName;
    TextView lastGame;

    public home_me() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static home_me newInstance(String param1, String param2) {
        home_me fragment = new home_me();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_me, container, false);

        profilePicture = (ImageView) v.findViewById(R.id.fragment_me_profilepicture);
        profileName = (TextView) v.findViewById(R.id.fragment_me_profilename);
        lastGame = (TextView) v.findViewById(R.id.fragment_me_lastseen);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(SQLManager.instance == null) new SQLManager(getContext());
        Player p = SQLManager.instance.getPlayer(prefs.getLong("steamid64", 0));

        profileName.setText(p.Name);
        Picasso.with(getContext()).load(p.URL_avatarfull).into(profilePicture);

        setBannerData(v);
        setLastMatchData(v);


        return v;
    }

    void setBannerData(View v){
        LinearLayout winratelayout = (LinearLayout) v.findViewById(R.id.fragment_me_winratelayout);
        LinearLayout wlalayout = (LinearLayout) v.findViewById(R.id.fragment_me_wlalayout);
        TextView wla = (TextView) v.findViewById(R.id.fragment_me_wla);
        TextView winrate = (TextView) v.findViewById(R.id.fragment_me_winrate);

        winratelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Your winrate over downloaded matches. This does not take Abandons in account.", Snackbar.LENGTH_LONG).show();
            }
        });

        wlalayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Your total Wins/Losses/Abandons of downloaded matches.", Snackbar.LENGTH_LONG).show();
            }
        });

        if(MatchTools.instance == null) new MatchTools();
        int[] winlossassist = MatchTools.instance.getWLA();

        wla.setText(winlossassist[0] + " / " + winlossassist[1] + " / " + winlossassist[2]);
        winrate.setText(MatchTools.instance.calculateWinRate() + "%");
    }

    void setLastMatchData(View v){
        if(SQLManager.instance == null) new SQLManager(getContext());
        Cursor lastmatch = SQLManager.instance.getMatch(SQLManager.instance.getAllMatchesList().get(0));
        final long matchid = lastmatch.getLong(lastmatch.getColumnIndex("match_id"));
        Cursor playerdata = MatchTools.getMyPlayerDetails(matchid, getContext());
        Cursor hero = SQLManager.instance.getHero(playerdata.getInt(playerdata.getColumnIndex("Hero_hero_id")));

        //Hero image
        ImageView lm_heroImage = (ImageView) v.findViewById(R.id.fragment_me_lm_heroimg);
        Picasso.with(getContext()).load(Defines.heroimgurl + hero.getString(hero.getColumnIndex("hero_title")) + "_full.png").placeholder(R.drawable.templar_assassin_full).into(lm_heroImage);

        //Win or loss?
        TextView lm_result = (TextView) v.findViewById(R.id.fragment_me_lm_matchresult);
        ImageView lm_gradient = (ImageView) v.findViewById(R.id.fragment_me_lm_overdraw);
        setWinLossAbandon(playerdata, lm_gradient, lm_result);

        //Contents
        TextView lm_kda = (TextView) v.findViewById(R.id.fragment_me_lm_kda);
        lm_kda.setText( "KDA: " + playerdata.getInt(playerdata.getColumnIndex("kills")) + " / " + playerdata.getInt(playerdata.getColumnIndex("deaths")) + " / " + playerdata.getInt(playerdata.getColumnIndex("assists")));

        TextView lm_duration = (TextView) v.findViewById(R.id.fragment_me_lm_duration);
        int[] duration = Defines.splitToComponentTimes(lastmatch.getInt(lastmatch.getColumnIndex("duration")));
        lm_duration.setText(duration[0] + "h " + duration[1] + "m");

        TextView lm_matchtype = (TextView) v.findViewById(R.id.fragment_me_lm_matchtype);
        lm_matchtype.setText(MatchTools.returnGameMode(lastmatch.getInt(lastmatch.getColumnIndex("game_mode"))));

        TextView lm_time = (TextView) v.findViewById(R.id.fragment_me_lm_time);
        Date origDate = new Date(lastmatch.getLong(lastmatch.getColumnIndex("start_time")) * 1000);
        lm_time.setText(new SimpleDateFormat("dd-MM / HH:mm").format(origDate));
        lastGame.setText(getString(R.string.frament_me_lastseen) + "  "+ new SimpleDateFormat("dd MMM yyyy").format(origDate));

        FrameLayout fl = (FrameLayout) v.findViewById(R.id.fragment_me_lm_layout);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MatchActivity.class);
                Bundle b = new Bundle();
                b.putLong("matchid", matchid);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    void setWinLossAbandon(Cursor playerdata, ImageView overlay, TextView result){
        if(playerdata.getInt(playerdata.getColumnIndex("leaver_status")) > 0){
            overlay.setImageDrawable(getContext().getResources().getDrawable(R.drawable.gradient_orange));
            result.setTextColor(getContext().getResources().getColor(R.color.text_abandon));
            
            result.setText(Defines.getLeaverStatus(playerdata.getInt(playerdata.getColumnIndex("leaver_status"))));


        } else {
            if(playerdata.getInt(playerdata.getColumnIndex("win")) > 0){
                overlay.setImageDrawable(getContext().getResources().getDrawable(R.drawable.gradient_green));
                result.setText("WON");
                result.setTextColor(getContext().getResources().getColor(R.color.text_win));
            } else {
                overlay.setImageDrawable(getContext().getResources().getDrawable(R.drawable.gradient_red));
                result.setText("LOST");
                result.setTextColor(getContext().getResources().getColor(R.color.text_loss));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
