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
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by danny on 26-7-2016.
 */
public class match_you extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static match_you newInstance(int position) {
        match_you f = new match_you();
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
        View rootView = inflater.inflate(R.layout.fragment_match_you
                ,container,false);

        ArrayList<Card> cards = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            if(i == 0){
                YouCard card = new YouCard(getContext());
                card.setType(0);

                cards.add(card);
            } else {
                MultiStatCard card = new MultiStatCard(getContext());
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
                    case 4:
                        header.setTitle("Items");
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
        mCardArrayAdapter.setInnerViewTypeCount(2);

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
            return 2;
        }
    }

    public class YouCard extends Card{

        public YouCard(Context context){
            super(context, R.layout.item_card_you);
            init();
        }

        void init(){
        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }

    public class MultiStatCard extends Card{

        public MultiStatCard(Context context){
            super(context, R.layout.item_card_multistat);
            init();
        }

        void init(){
        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }
}
