package danski.cobalt.PreferencesActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import danski.cobalt.MatchUpdater;
import danski.cobalt.R;

/**
 * Created by danny on 27-9-15.
 */
public class PreferencesFragment_Profile extends Fragment {


    private static final String ARG_POSITION = "position";
    private int position;
    Button redownload;
    Button logout;

    public static PreferencesFragment_Profile newInstance(int position) {
        PreferencesFragment_Profile f = new PreferencesFragment_Profile();
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
        View root = inflater.inflate(R.layout.activity_preferences, container, false);
        final View froot = root;

        redownload = (Button) root.findViewById(R.id.pref_redownload);
        logout = (Button) root.findViewById(R.id.pref_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchUpdater mu = new MatchUpdater();
                mu.RemoveFile();
                mu = null;

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(froot.getContext()).edit();
                editor.putBoolean("didSetup", false);
                editor.apply();

                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //What to do when redownload is pressed
        redownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchUpdater mu = new MatchUpdater();
                mu.FreshMatches();
                mu = null;
            }
        });


        return root;

    }

}
