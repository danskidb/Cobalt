package danski.dotareader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TabbedMatchActivity_Structures extends Fragment {

    Match thisMatch;
    ListView lv_playerlist;

    private static final String ARG_POSITION = "position";

    private int position;

    public static TabbedMatchActivity_Structures newInstance(int position) {
        TabbedMatchActivity_Structures f = new TabbedMatchActivity_Structures();
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
        View root = inflater.inflate(R.layout.fragment_matchactivity_structures, container, false);
        thisMatch = Defines.SelectedMatch;


        //FILL HERE



        return root;

    }
}
