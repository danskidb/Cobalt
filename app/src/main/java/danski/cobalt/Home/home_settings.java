package danski.cobalt.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import danski.cobalt.R;

/**
 * Created by danny on 17-7-2016.
 */
public class home_settings extends Fragment {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    public home_settings() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static home_settings newInstance(String param1, String param2) {
        home_settings fragment = new home_settings();
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
        View v = inflater.inflate(R.layout.fragment_home_settings, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();


        Switch downloadAllOnUpdate = (Switch) v.findViewById(R.id.setting_downloadallonupdate);
        downloadAllOnUpdate.setChecked(prefs.getBoolean("sett_downloadallonupdate", false));
        downloadAllOnUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("sett_downloadallonupdate", true);
                    editor.apply();

                    //throw popup for downloading all data

                } else {
                    editor.putBoolean("sett_downloadallonupdate", false);
                    editor.apply();
                }
            }
        });

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

}