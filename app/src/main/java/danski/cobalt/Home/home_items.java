package danski.cobalt.Home;

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

import danski.cobalt.Match.match_you;
import danski.cobalt.MatchTools;
import danski.cobalt.R;
import danski.cobalt.sql.DataStructure.Hero;
import danski.cobalt.sql.DataStructure.Item;
import danski.cobalt.sql.SQLManager;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by danny on 17-7-2016.
 */
public class home_items extends Fragment {


    public home_items() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static home_items newInstance(String param1, String param2) {
        home_items fragment = new home_items();
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
        View v = inflater.inflate(R.layout.fragment_home_items, container, false);

        if(SQLManager.instance == null) new SQLManager(getContext());
        ArrayList<Item> items = SQLManager.instance.getAllItems();

        ArrayList<Card> cards = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            ItemCard card = new ItemCard(getContext(), items.get(i));
            card.setType(0);
            cards.add(card);
        }

        ItemCardAdapter mCardArrayAdapter = new ItemCardAdapter(getContext(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(1);

        CardListView listView = (CardListView) v.findViewById(R.id.frag_item_list);
        if(listView != null) listView.setAdapter(mCardArrayAdapter);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //SUBCLASSES

    public class ItemCardAdapter extends CardArrayAdapter {
        public ItemCardAdapter(Context context, List<Card> cards) {
            super(context, cards);
        }

        @Override
        public int getViewTypeCount() {return 1;}
    }

    public class ItemCard extends Card{

        Item i;

        public ItemCard(Context context, Item _i){
            super(context, R.layout.item_card_item);
            i = _i;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view){
            TextView heroname = (TextView) view.findViewById(R.id.card_hero_name);
            ImageView heroimg = (ImageView) view.findViewById(R.id.card_item_itemImg);
            ImageView herobgimg = (ImageView) view.findViewById(R.id.card_hero_heroimg);

            heroname.setText(i.item_name);
            Picasso.with(getContext()).load(MatchTools.getItemUrl(i.item_id)).placeholder(R.drawable.templar_assassin_full).fit().into(heroimg);
            Picasso.with(getContext()).load(MatchTools.getItemUrl(i.item_id)).placeholder(R.drawable.templar_assassin_full).into(herobgimg);
        }

        @Override
        public int getType(){
            return type;
        }
    }
}
