package danski.cobalt.Match;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import danski.cobalt.R;

/**
 * Created by danny on 26-7-2016.
 */
public class match_players extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static match_players newInstance(int position) {
        match_players f = new match_players();
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
        View rootView = inflater.inflate(R.layout.fragment_match_players,container,false);

        return rootView;
    }

}