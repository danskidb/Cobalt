package danski.cobalt.PreferencesActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import danski.cobalt.Adaptor.Adapter_Preferences_Graphs;
import danski.cobalt.Defines;
import danski.cobalt.R;
import danski.cobalt.StatsActivity.Stat;
import danski.cobalt.StatsActivity.StatTypes;

/**
 * Created by danny on 27-9-15.
 */
public class PreferencesFragment_Home extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    Stat[] enabledstats;
    ListView graphlist;
    SharedPreferences prefs;


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
        enabledstats = new Stat[st.length];

        prefs = PreferenceManager.getDefaultSharedPreferences(Defines.CurrentContext.getApplicationContext());

        for(int i = 0; i < enabledstats.length; i++){
            enabledstats[i] = new Stat(st[i]);
            if(st[i] != StatTypes.kda) enabledstats[i].enabled = prefs.getBoolean(enabledstats[i].title, false);
            else enabledstats[i].enabled = prefs.getBoolean(enabledstats[i].title, true);
        }

        graphlist.setAdapter(new Adapter_Preferences_Graphs(Defines.CurrentContext, enabledstats));

        return root;
    }

}
