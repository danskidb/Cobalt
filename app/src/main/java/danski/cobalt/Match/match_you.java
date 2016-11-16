package danski.cobalt.Match;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import danski.cobalt.Defines;
import danski.cobalt.MatchTools;
import danski.cobalt.R;
import danski.cobalt.sql.MatchYouFormatter;
import danski.cobalt.sql.SQLManager;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

import static android.view.View.GONE;

/**
 * Created by danny on 26-7-2016.
 */
public class match_you extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static match_you newInstance(int position, long matchid) {
        match_you f = new match_you();
        Bundle b = new Bundle();
        b.putLong("matchid", matchid);
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
        View rootView = inflater.inflate(R.layout.fragment_match_you
                ,container,false);
        long matchid = getArguments().getLong("matchid");

        MatchYouFormatter myf = new MatchYouFormatter();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        myf.LoadData(matchid, prefs.getLong("steamid64", 0));


        ArrayList<Card> cards = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            if(i == 0) {
                YouCard card = new YouCard(getContext(), myf);
                card.setType(0);

                cards.add(card);
            } else if (i == 4){
                ItemCard card = new ItemCard(getContext(), myf);
                card.setType(2);
                CardHeader header = new CardHeader(getContext());
                header.setTitle("Items");
                card.addCardHeader(header);
                cards.add(card);

            } else {
                MultiStatCard card = new MultiStatCard(getContext(), myf, i);
                card.setType(1);
                CardHeader header = new CardHeader(getContext());

                switch(i){
                    case 1:
                        header.setTitle("KDA");
                        break;
                    case 2:
                        header.setTitle("Farm");
                        break;
                    case 3:
                        header.setTitle("Damage");
                        break;
                    default:
                        header.setTitle("ERROR");
                        break;
                }

                card.addCardHeader(header);
                cards.add(card);
            }
        }

        YouCardAdapter mCardArrayAdapter = new YouCardAdapter(getContext(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        CardListView listView = (CardListView) rootView.findViewById(R.id.match_you_list);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }

        return rootView;
    }

    public class YouCardAdapter extends CardArrayAdapter {

        /**
         * Constructor
         *
         * @param context The current context.
         * @param cards   The cards to represent in the ListView.
         */
        public YouCardAdapter(Context context, List<Card> cards) {
            super(context, cards);
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }

    public class YouCard extends Card{

        MatchYouFormatter myf;

        public YouCard(Context context, MatchYouFormatter _myf){
            super(context, R.layout.item_card_you);
            myf = _myf;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view){
            ImageView heroimage = (ImageView) parent.findViewById(R.id.youcard_heroimg);
            Picasso.with(getContext()).load(myf.heroImageUrl).placeholder(R.drawable.templar_assassin_full).fit().into(heroimage);
            TextView heroname = (TextView) parent.findViewById(R.id.youcard_heroname);
            heroname.setText(myf.localizedHeroName);


            if(SQLManager.instance == null) new SQLManager(getContext());
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            Cursor cur = SQLManager.instance.getHeroMatchesOfPlayer(Defines.idTo32(prefs.getLong("steamid64", 0)), myf.heroTitle);
            Log.d("Match_you", cur.getCount() + " AMMOUNT OF BLYATCYKA");
           /* for(int i = 0; i < cur.getCount(); i++){
                Log.d("Match_you", "Match "+i+":  " + cur.getLong(cur.getColumnIndex("Match_match_id")));
                cur.moveToNext();
            }*/

            TextView playtime = (TextView) parent.findViewById(R.id.youcard_playtime);
            playtime.setText("You've played " + myf.localizedHeroName +" "+ cur.getCount() + " times.");
        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }

    public class MultiStatCard extends Card{

        MatchYouFormatter myf;
        int cardnumber;

        public MultiStatCard(Context context, MatchYouFormatter _myf, int _cardnumber){
            super(context, R.layout.item_card_multistat);
            cardnumber = _cardnumber;
            myf = _myf;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view){
            TextView title0 = (TextView) parent.findViewById(R.id.multistat_title_0);
            TextView stat0 = (TextView) parent.findViewById(R.id.multistat_0);

            TextView title1 = (TextView) parent.findViewById(R.id.multistat_title_1);
            TextView stat1 = (TextView) parent.findViewById(R.id.multistat_1);

            TextView title2 = (TextView) parent.findViewById(R.id.multistat_title_2);
            TextView stat2 = (TextView) parent.findViewById(R.id.multistat_2);

            TableRow row3 = (TableRow) parent.findViewById(R.id.multistat_row_3);
            TextView title3 = (TextView) parent.findViewById(R.id.multistat_title_3);
            TextView stat3 = (TextView) parent.findViewById(R.id.multistat_3);

            TableRow row4 = (TableRow) parent.findViewById(R.id.multistat_row_4);
            TextView title4 = (TextView) parent.findViewById(R.id.multistat_title_4);
            TextView stat4 = (TextView) parent.findViewById(R.id.multistat_4);

            switch(cardnumber){
                case 1:
                    title0.setText("Level");
                    stat0.setText(myf.level + "");
                    title1.setText("Kills");
                    stat1.setText(myf.kills + "");
                    title2.setText("Deaths");
                    stat2.setText(myf.deaths + "");
                    title3.setText("Assist");
                    stat3.setText(myf.assist + "");
                    title4.setText("KDA Ratio");
                    stat4.setText(myf.kda + "");
                    break;
                case 2:
                    title0.setText("Gold");
                    stat0.setText(myf.gold + "");
                    title1.setText("Last Hits");
                    stat1.setText(myf.lasthits + "");
                    title2.setText("Denies");
                    stat2.setText(myf.denies + "");
                    title3.setText("Avg. Gold/Minute");
                    stat3.setText(myf.gpm + "");
                    title4.setText("Avg. XP/Minute");
                    stat4.setText(myf.xpm + "");
                    break;
                case 3:
                    title0.setText("Hero Damage");
                    stat0.setText(myf.herodmg + "");
                    title1.setText("Hero Healing");
                    stat1.setText(myf.heroheal + "");
                    title2.setText("Tower Damage");
                    stat2.setText(myf.towerdmg + "");
                    row3.setVisibility(View.INVISIBLE);
                    row4.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }

        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }

    public class ItemCard extends Card{

        MatchYouFormatter myf;

        public ItemCard(Context context, MatchYouFormatter _myf){
            super(context, R.layout.item_card_items);
            myf = _myf;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view){
            ImageView item0 = (ImageView) parent.findViewById(R.id.card_item_0);
            ImageView item1 = (ImageView) parent.findViewById(R.id.card_item_1);
            ImageView item2 = (ImageView) parent.findViewById(R.id.card_item_2);
            ImageView item3 = (ImageView) parent.findViewById(R.id.card_item_3);
            ImageView item4 = (ImageView) parent.findViewById(R.id.card_item_4);
            ImageView item5 = (ImageView) parent.findViewById(R.id.card_item_5);

            if(myf.item[0] == 0) {
                item0.setVisibility(GONE);
            } else {
                Picasso.with(getContext()).load(MatchTools.getItemUrl(myf.item[0])).placeholder(R.drawable.vanguard_lg).fit().centerInside().into(item0);
            }
            if(myf.item[1] == 0) {
                item1.setVisibility(GONE);
            } else {
                Picasso.with(getContext()).load(MatchTools.getItemUrl(myf.item[1])).placeholder(R.drawable.vanguard_lg).fit().centerInside().into(item1);
            }
            if(myf.item[2] == 0) {
                item2.setVisibility(GONE);
            } else {
                Picasso.with(getContext()).load(MatchTools.getItemUrl(myf.item[2])).placeholder(R.drawable.vanguard_lg).fit().centerInside().into(item2);
            }
            if(myf.item[3] == 0) {
                item3.setVisibility(GONE);
            } else {
                Picasso.with(getContext()).load(MatchTools.getItemUrl(myf.item[3])).placeholder(R.drawable.vanguard_lg).fit().centerInside().into(item3);
            }
            if(myf.item[4] == 0) {
                item4.setVisibility(GONE);
            } else {
                Picasso.with(getContext()).load(MatchTools.getItemUrl(myf.item[4])).placeholder(R.drawable.vanguard_lg).fit().centerInside().into(item4);
            }
            if(myf.item[5] == 0) {
                item5.setVisibility(GONE);
            } else {
                Picasso.with(getContext()).load(MatchTools.getItemUrl(myf.item[5])).placeholder(R.drawable.vanguard_lg).fit().centerInside().into(item5);
            }
        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }
}
