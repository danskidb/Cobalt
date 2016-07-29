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
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by danny on 26-7-2016.
 */
public class match_summary extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static match_summary newInstance(int position) {
        match_summary f = new match_summary();
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
        View rootView = inflater.inflate(R.layout.fragment_match_summary,container,false);

        ArrayList<Card> cards = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            if(i == 0){
                SummaryCard card = new SummaryCard(getContext());
                card.setType(0);

                cards.add(card);
            } else {
                AchievementCard card = new AchievementCard(getContext());
                card.setType(1);
                CardHeader header = new CardHeader(getContext());

                switch(i){
                    case 1:
                        header.setTitle("Most Kills");
                        break;
                    case 2:
                        header.setTitle("Most Deaths");
                        break;
                    case 3:
                        header.setTitle("Most Assist");
                        break;
                    case 4:
                        header.setTitle("Most Last Hits");
                        break;
                    case 5:
                        header.setTitle("Most Denies");
                        break;
                    default:
                        header.setTitle("ERROR");
                        break;
                }

                card.addCardHeader(header);
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

        public SummaryCard(Context context){
            super(context, R.layout.item_card_summary);
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

    public class AchievementCard extends Card{

        public AchievementCard(Context context){
            super(context, R.layout.item_card_achievement);
            init();
        }

        void init(){
            setBackgroundResourceId(R.drawable.gradient_green);
        }

        @Override
        public int getType() {
            //Very important with different inner layouts
            return type;
        }
    }

}