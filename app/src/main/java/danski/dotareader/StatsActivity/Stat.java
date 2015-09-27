package danski.dotareader.StatsActivity;

import com.jjoe64.graphview.series.DataPoint;

import java.io.Serializable;

/**
 * Created by danny on 27-9-15.
 */
public class Stat implements Serializable{
    public StatTypes type;
    public int color;
    public String title;
    public DataPoint[] datapoints;

    public Stat(StatTypes _type, int _col, String _title, StatsGenerator sg){
        type = _type;
        color = _col;
        title = _title;

        datapoints = sg.generate(_type);
    }
}
