package danski.cobalt.StatsActivity;

import android.graphics.Color;

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
    public Boolean enabled;



    public Stat(StatTypes _type, StatsGenerator sg){
        type = _type;
        datapoints = sg.generate(_type);
        generateColorandTitle();
    }

    public Stat(StatTypes _type){
        type = _type;
        generateColorandTitle();
    }

    void generateColorandTitle(){
        switch(type){
            case kda:
                color = Color.BLACK;
                title = "KDA Ratio";
                break;
            case gpm:
                color = Color.MAGENTA;
                title = "GPM";
                break;
            case xpm:
                color = Color.GRAY;
                title = "XPM";
                break;
            case lasthits:
                color = Color.BLUE;
                title = "Last Hits";
                break;
            case denies:
                color = Color.YELLOW;
                title = "Denies";
                break;
            case winrate:
                color = Color.BLACK;
                title = "Win rate";
                break;
            case kill:
                color = Color.GREEN;
                title = "Kills";
                break;
            case death:
                color = Color.RED;
                title = "Deaths";
                break;
            case assist:
                color = Color.CYAN;
                title = "Assist";
                break;
            case goldspent:
                color = Color.DKGRAY;
                title = "Gold Spent";
                break;
            default:
                break;
        }
    }

    public void generateDataPoints(StatsGenerator sg){
        datapoints = sg.generate(type);
    }
}
