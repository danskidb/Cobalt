package danski.dotareader.PreferencesActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import danski.dotareader.Adaptor.PlayerDetailListAdapter;
import danski.dotareader.Data.Match;
import danski.dotareader.Defines;
import danski.dotareader.MatchUpdater;
import danski.dotareader.R;
import danski.dotareader.ServiceHandler;

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
