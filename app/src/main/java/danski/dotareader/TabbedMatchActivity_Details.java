package danski.dotareader;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class TabbedMatchActivity_Details extends Fragment {

    Match thisMatch;
    ListView lv_playerlist;

    private static final String ARG_POSITION = "position";

    private int position;

    public static TabbedMatchActivity_Details newInstance(int position) {
        TabbedMatchActivity_Details f = new TabbedMatchActivity_Details();
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
        View root = inflater.inflate(R.layout.fragment_matchactivity_details, container, false);
        thisMatch = Defines.SelectedMatch;


        lv_playerlist = (ListView) root.findViewById(R.id.det_playerlist);

        lv_playerlist.setAdapter(new PlayerDetailListAdapter(Defines.CurrentContext, thisMatch));



        return root;

    }
}
