package danski.cobalt.Match;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import danski.cobalt.R;
import danski.cobalt.sql.MatchSummaryFormatter;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by danny on 26-7-2016.
 */
public class match_summary extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static match_summary newInstance(int position, long matchid) {
        match_summary f = new match_summary();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putLong("matchid", matchid);
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
        View rootView = inflater.inflate(R.layout.fragment_match_summary,container,false);
        long matchid = getArguments().getLong("matchid");

        MatchSummaryFormatter msf = new MatchSummaryFormatter();
        msf.loadData(matchid);


        //Cards
        ArrayList<Card> cards = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            if(i == 0){
                SummaryCard card = new SummaryCard(getContext(), msf);
                card.setType(0);

                cards.add(card);
            } else {
                AchievementCard card;

                switch(i){
                    case 1:
                        card = new AchievementCard(getContext(), msf.kills, "Most Kills");
                        break;
                    case 2:
                        card = new AchievementCard(getContext(), msf.deaths, "Most Deaths");
                        break;
                    case 3:
                        card = new AchievementCard(getContext(), msf.assists, "Most Assist");
                        break;
                    case 4:
                        card = new AchievementCard(getContext(), msf.last_hits, "Most Last Hits");
                        break;
                    case 5:
                        card = new AchievementCard(getContext(), msf.denies, "Most Denies");
                        break;
                    default:
                        card = new AchievementCard(getContext());
                        break;
                }

                card.setType(1);
                cards.add(card);
            }
        }

        SummaryCardAdapter mCardArrayAdapter = new SummaryCardAdapter(getContext(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(2);

        CardListView listView = (CardListView) rootView.findViewById(R.id.match_summary_list);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }

        return rootView;
    }

    public class SummaryCardAdapter extends CardArrayAdapter {

        /**
         * Constructor
         *
         * @param context The current context.
         * @param cards   The cards to represent in the ListView.
         */
        public SummaryCardAdapter(Context context, List<Card> cards) {
            super(context, cards);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }

    public class SummaryCard extends Card{

        MatchSummaryFormatter msf;

        public SummaryCard(Context context, MatchSummaryFormatter _msf){
            super(context, R.layout.item_card_summary);
            msf = _msf;
            init();
        }

        void init(){
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            TextView starttime = (TextView) parent.findViewById(R.id.summarycard_starttime);
            starttime.setText(msf.starttime);
            TextView duration = (TextView) parent.findViewById(R.id.summarycard_duration);
            duration.setText(msf.duration);
            TextView fbt = (TextView) parent.findViewById(R.id.summarycard_firstbloodtime);
            fbt.setText(msf.firstbloodtime);
            TextView gamemode = (TextView) parent.findViewById(R.id.summarycard_gamemode);
            gamemode.setText(msf.gamemode);
        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }

    public class AchievementCard extends Card {

        MatchSummaryRecord record;
        String recordtext;

        public AchievementCard(Context context, MatchSummaryRecord _record, String _recordtext){
            super(context, R.layout.item_card_achievement);
            record = _record;
            recordtext = _recordtext;
            init();
        }

        public AchievementCard(Context context){
            super(context, R.layout.item_card_achievement);
            init();

        }

        void init(){
        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            if(record != null){
                TextView tv_achievement = (TextView) parent.findViewById(R.id.card_hero_name);
                tv_achievement.setText(recordtext);

                TextView tv_record = (TextView) parent.findViewById(R.id.card_achievement_record);
                tv_record.setText(record.amount + "");

                ImageView iv_gradient = (ImageView) parent.findViewById(R.id.card_achievement_overlay);
                if(record.radiant) iv_gradient.setImageResource(R.drawable.gradient_green);
                else iv_gradient.setImageResource(R.drawable.gradient_red);

                TextView tv_ashero = (TextView) parent.findViewById(R.id.card_achievement_ashero);
                tv_ashero.setText("as " + record.localized_hero);

                ImageView iv_hero = (ImageView) parent.findViewById(R.id.card_hero_heroimg);
                Picasso.with(getContext()).load(record.hero_image_url).placeholder(R.drawable.templar_assassin_full).fit().into(iv_hero);

                ImageView herobg = (ImageView) parent.findViewById(R.id.card_achievement_heroImg);
                Picasso.with(getContext()).load(record.hero_image_url).placeholder(R.drawable.templar_assassin_full).into(herobg);

                TextView tv_playername = (TextView) parent.findViewById(R.id.card_achievement_playername);
                if(record.steamid < 0) tv_playername.setText("Anonymous");
                else tv_playername.setText(record.steamname);
            }



        }
    }


}