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

import danski.cobalt.MatchTools;
import danski.cobalt.R;
import danski.cobalt.sql.DataStructure.Hero;
import danski.cobalt.sql.SQLManager;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * Created by danny on 17-7-2016.
 */
public class home_heroes extends Fragment {


    public home_heroes() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static home_heroes newInstance(String param1, String param2) {
        home_heroes fragment = new home_heroes();
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
        View v = inflater.inflate(R.layout.fragment_home_heroes, container, false);

        if(SQLManager.instance == null) new SQLManager(getContext());
        ArrayList<Hero> heroes = SQLManager.instance.getAllHeroes();

        ArrayList<Card> cards = new ArrayList<>();
        for(int i = 0; i < heroes.size(); i++){
            HeroCard card = new HeroCard(getContext(), heroes.get(i));
            card.setType(0);
            cards.add(card);
        }

        HeroCardAdapter mCardArrayAdapter = new HeroCardAdapter(getContext(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(1);

        CardListView listView = (CardListView) v.findViewById(R.id.frag_hero_list);
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

    public class HeroCardAdapter extends CardArrayAdapter {
        public HeroCardAdapter(Context context, List<Card> cards) {
            super(context, cards);
        }

        @Override
        public int getViewTypeCount() {return 1;}
    }

    public class HeroCard extends Card{

        Hero h;

        public HeroCard(Context context, Hero _h){
            super(context, R.layout.item_card_hero);
            h = _h;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view){
            TextView heroname = (TextView) view.findViewById(R.id.card_hero_name);
            ImageView heroimg = (ImageView) view.findViewById(R.id.card_hero_heroImg);
            ImageView herobgimg = (ImageView) view.findViewById(R.id.card_hero_heroimg);

            heroname.setText(h.hero_name);
            Picasso.with(getContext()).load(MatchTools.getHeroImageUrl(h.hero_id)).placeholder(R.drawable.templar_assassin_full).fit().into(heroimg);
            Picasso.with(getContext()).load(MatchTools.getHeroImageUrl(h.hero_id)).placeholder(R.drawable.templar_assassin_full).into(herobgimg);
        }

        @Override
        public int getType(){
            return type;
        }
    }

}

