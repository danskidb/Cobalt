package danski.dotareader.PreferencesActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import danski.dotareader.R;
import danski.dotareader.StatsActivity.StatTypes;

/**
 * Created by danny on 27-9-15.
 */
public class PreferencesFragment_Home extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    stat_enabled[] enabledstats;
    String[] graphtypes;
    ListView graphlist;

    public static PreferencesFragment_Home newInstance(int position) {
        PreferencesFragment_Home f = new PreferencesFragment_Home();
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
        View root = inflater.inflate(R.layout.fragment_preferences_home, container, false);
        graphlist = (ListView) root.findViewById(R.id.pref_home_graphlist);

        StatTypes[] st = StatTypes.values();
        enabledstats = new stat_enabled[st.length];
        graphtypes = new String[st.length];

        for(int i = 0; i < enabledstats.length; i++){
            enabledstats[i] = new stat_enabled(st[i], false);
            graphtypes[i] = st[i].toString();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(root.getContext(),
                android.R.layout.simple_list_item_1, graphtypes);

        graphlist.setAdapter(adapter);

        return root;
    }

    class stat_enabled{
        StatTypes stat;
        Boolean enabled;

        stat_enabled(StatTypes _stat, Boolean _enabled){
            stat = _stat;
            enabled = _enabled;
        }
    }
}
