package danski.cobalt.Match;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import danski.cobalt.R;
import danski.cobalt.sql.DataStructure.MatchPlayersFormatter;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
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
        long matchid = getArguments().getLong("matchid");

        MatchPlayersFormatter mpf = new MatchPlayersFormatter();
        mpf.LoadData(matchid);

        ArrayList<Card> cards = new ArrayList<>();

        PlayerCard card = new PlayerCard(getContext());
        card.setType(0);
        cards.add(card);

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


        public PlayerCard(Context context){
            super(context, R.layout.item_card_you);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view){

        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }

}