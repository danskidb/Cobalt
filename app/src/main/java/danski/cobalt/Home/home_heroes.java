package danski.cobalt.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import danski.cobalt.R;


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

