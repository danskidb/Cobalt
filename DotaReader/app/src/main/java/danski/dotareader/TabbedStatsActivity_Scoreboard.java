package danski.dotareader;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TabbedStatsActivity_Scoreboard extends Fragment {


    private static final String ARG_POSITION = "position";
    private int position;

    RecyclerView rv;
    LinearLayoutManager llm;
    List<ScoreCard> cards;

    public static TabbedStatsActivity_Scoreboard newInstance(int position) {
        TabbedStatsActivity_Scoreboard f = new TabbedStatsActivity_Scoreboard();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.stats_scoreboard, container, false);
        rv = (RecyclerView) root.findViewById(R.id.scoreboardrecyclerview);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(llm);

        findGreatest();
        fillCardArray();

        TabbedStatsActivity_Scoreboard_rvadapter adapter = new TabbedStatsActivity_Scoreboard_rvadapter(cards);
        rv.setAdapter(adapter);

        return root;

    }

    //Stats to fill cards with.
    int killmatch;
    int assistmatch;
    int lasthitmatch;
    int denymatch;
    int highestkdamatch;
    int goldmatch;

    SharedPreferences prefs;
    long steamid32;

    void findGreatest(){
        prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());
        steamid32 = Defines.idTo32(Long.parseLong(prefs.getString("steamid", null)));

        int kill = 0;
        int assist = 0;
        int lasthit = 0;
        int denies = 0;
        float highestkda = 0;
        int gold = 0;

        for (int i = 0; i < Defines.CurrentMatches.length; i++) {                   //Go trough matches
            for (int j = 0; j < Defines.CurrentMatches[i].Players.length; j++) {    //Go trough players in match

                if (Defines.CurrentMatches[i].Players[j].account_id == steamid32) { //Only get my stats pls
                    if(kill < Defines.CurrentMatches[i].Players[j].kills) {
                        kill = Defines.CurrentMatches[i].Players[j].kills;
                        killmatch = Defines.CurrentMatches[i].matchid;              //and store the matchid.
                    }
                    if(assist < Defines.CurrentMatches[i].Players[j].assists){
                        assist = Defines.CurrentMatches[i].Players[j].assists;
                        assistmatch = Defines.CurrentMatches[i].matchid;
                    }
                    if(lasthit < Defines.CurrentMatches[i].Players[j].last_hits) {
                        lasthit = Defines.CurrentMatches[i].Players[j].last_hits;
                        lasthitmatch = Defines.CurrentMatches[i].matchid;
                    }
                    if(denies < Defines.CurrentMatches[i].Players[j].denies) {
                        denies = Defines.CurrentMatches[i].Players[j].denies;
                        denymatch = Defines.CurrentMatches[i].matchid;
                    }

                    float kda = (Defines.CurrentMatches[i].Players[j].kills + Defines.CurrentMatches[i].Players[j].assists)/(Defines.CurrentMatches[i].Players[j].deaths + 1);
                    if(highestkda < kda) {
                        highestkda = kda;
                        highestkdamatch = Defines.CurrentMatches[i].matchid;
                    }

                    if(gold < Defines.CurrentMatches[i].Players[j].gold) {
                        gold = Defines.CurrentMatches[i].Players[j].gold;
                        goldmatch = Defines.CurrentMatches[i].matchid;
                    }

                }
            }
        }
    }

    void fillCardArray(){
        cards = new ArrayList<>();
        int currentmatch;

        currentmatch = killmatch;
        cards.add(new ScoreCard("Most kills",
                Match.getPlayerKills(currentmatch, steamid32) + "",
                Match.getPlayerHeroName(currentmatch, steamid32) /*+ ", " + Match.getMatchDate(currentmatch, steamid32)*/,
                Match.getPlayerHeroImageUrl(currentmatch, steamid32),
                Match.getMatchArrayPos(currentmatch)
        ));

        currentmatch = assistmatch;
        cards.add(new ScoreCard("Most assists",
                Match.getPlayerAssist(currentmatch, steamid32) + "",
                Match.getPlayerHeroName(currentmatch, steamid32) /*+ ", " + Match.getMatchDate(currentmatch, steamid32)*/,
                Match.getPlayerHeroImageUrl(currentmatch, steamid32),
                Match.getMatchArrayPos(currentmatch)
        ));

        currentmatch = lasthitmatch;
        cards.add(new ScoreCard("Most last hits",
                Match.getPlayerLastHits(currentmatch, steamid32) + "",
                Match.getPlayerHeroName(currentmatch, steamid32) /*+ ", " + Match.getMatchDate(currentmatch, steamid32)*/,
                Match.getPlayerHeroImageUrl(currentmatch, steamid32),
                Match.getMatchArrayPos(currentmatch)
        ));

        currentmatch = denymatch;
        cards.add(new ScoreCard("Most denies",
                Match.getPlayerDenies(currentmatch, steamid32) + "",
                Match.getPlayerHeroName(currentmatch, steamid32) /*+ ", " + Match.getMatchDate(currentmatch, steamid32)*/,
                Match.getPlayerHeroImageUrl(currentmatch, steamid32),
                Match.getMatchArrayPos(currentmatch)
        ));

        currentmatch = highestkdamatch;
        cards.add(new ScoreCard("Highest KDA",
                Match.getPlayerKDA(currentmatch, steamid32) + "",
                Match.getPlayerHeroName(currentmatch, steamid32) /*+ ", " + Match.getMatchDate(currentmatch, steamid32)*/,
                Match.getPlayerHeroImageUrl(currentmatch, steamid32),
                Match.getMatchArrayPos(currentmatch)
        ));

        currentmatch = goldmatch;
        cards.add(new ScoreCard("Most Gold",
                Match.getPlayerGold(currentmatch, steamid32) + "",
                Match.getPlayerHeroName(currentmatch, steamid32) /*+ ", " + Match.getMatchDate(currentmatch, steamid32)*/,
                Match.getPlayerHeroImageUrl(currentmatch, steamid32),
                Match.getMatchArrayPos(currentmatch)
        ));
    }



}

