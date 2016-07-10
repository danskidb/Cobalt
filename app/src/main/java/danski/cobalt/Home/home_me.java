package danski.cobalt.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import danski.cobalt.R;
import danski.cobalt.sql.Player;
import danski.cobalt.sql.SQLManager;

public class home_me extends Fragment {

    ImageView profilePicture;
    TextView profileName;

    public home_me() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static home_me newInstance(String param1, String param2) {
        home_me fragment = new home_me();
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
        View v = inflater.inflate(R.layout.fragment_home_me, container, false);

        profilePicture = (ImageView) v.findViewById(R.id.fragment_me_profilepicture);
        profileName = (TextView) v.findViewById(R.id.fragment_me_profilename);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(SQLManager.instance == null) new SQLManager(getContext());
        Player p = SQLManager.instance.getPlayer(prefs.getLong("steamid64", 0));

        profileName.setText(p.Name);
        Picasso.with(getContext()).load(p.URL_avatarfull).into(profilePicture);

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
