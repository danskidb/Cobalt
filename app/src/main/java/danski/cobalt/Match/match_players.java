package danski.cobalt.Match;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import danski.cobalt.MatchTools;
import danski.cobalt.R;
import danski.cobalt.sql.DataStructure.MatchPlayersFormatter;
import danski.cobalt.sql.DataStructure.Player;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by danny on 26-7-2016.
 */
public class match_players extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static match_players newInstance(int position, long matchid) {
        match_players f = new match_players();
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
        View rootView = inflater.inflate(R.layout.fragment_match_players,container,false);

        //Load Data
        long matchid = getArguments().getLong("matchid");
        MatchPlayersFormatter mpf = new MatchPlayersFormatter();
        mpf.LoadData(matchid);

        //Create & fill cards
        ArrayList<Card> cards = new ArrayList<>();

        for(int i = 0; i < mpf.players.size(); i++){
            PlayerCard card = new PlayerCard(getContext(), mpf.players.get(i));
            card.setType(0);

            PlayerCardExpanded exp = new PlayerCardExpanded(getContext());
            card.addCardExpand(exp);
            cards.add(card);
        }


        //Put cards in adapter & view.
        PlayerCardAdapter mCardArrayAdapter = new PlayerCardAdapter(getContext(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(1);

        CardListView listView = (CardListView) rootView.findViewById(R.id.match_players_list);
        if(listView != null){
            listView.setAdapter(mCardArrayAdapter);
        }

        return rootView;
    }

    public class PlayerCardAdapter extends CardArrayAdapter {

        /**
         * Constructor
         *
         * @param context The current context.
         * @param cards   The cards to represent in the ListView.
         */
        public PlayerCardAdapter(Context context, List<Card> cards) {
            super(context, cards);
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }

    public class PlayerCard extends Card{

        Player p;

        public PlayerCard(Context context, Player _p){
            super(context, R.layout.item_card_you);
            p = _p;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view){
            RelativeLayout youcardlayout = (RelativeLayout) view.findViewById(R.id.youcard_layout);

            ViewToClickToExpand viewToClickToExpand =
                    ViewToClickToExpand.builder()
                            .setupView(youcardlayout);
            setViewToClickToExpand(viewToClickToExpand);

            //ImageView heroimage = (ImageView) parent.findViewById(R.id.youcard_heroimg);
            //Picasso.with(getContext()).load(p.heroImageUrl).placeholder(R.drawable.templar_assassin_full).fit().into(heroimage);
            TextView heroname = (TextView) parent.findViewById(R.id.youcard_heroname);

            String steamname;
            if(p.Name!= null) steamname = p.Name;
            else steamname = "Anonymous";
            heroname.setText(steamname);


        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }

    public class PlayerCardExpanded extends CardExpand {

        public PlayerCardExpanded(Context context) {
            super(context, R.layout.item_cardexpand_players);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view){
            parent.setBackgroundColor(mContext.getResources().getColor(R.color.card_background));

        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }

}