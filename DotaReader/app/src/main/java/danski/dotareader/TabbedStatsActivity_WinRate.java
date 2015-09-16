package danski.dotareader;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class TabbedStatsActivity_WinRate extends Fragment {


    private static final String ARG_POSITION = "position";

    private int position;

    public static TabbedStatsActivity_WinRate newInstance(int position) {
        TabbedStatsActivity_WinRate f = new TabbedStatsActivity_WinRate();
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
        View root = inflater.inflate(R.layout.stats_winrate, container, false);


        //FILL HERE



        return root;

    }

}
