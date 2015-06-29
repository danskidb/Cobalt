package danski.dotareader;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Danny on 26/06/2015.
 */

public class Defines {
    public static Hero[] heroes;
    public static Match[] CurrentMatches;
    public static Match SelectedMatch;
    public static Context CurrentContext;


    public static String RawToString(int Resource){
        InputStream stream = CurrentContext.getResources().openRawResource(Resource);
        String output;

        try{
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            output = new String(buffer);
        } catch(IOException e){
            e.printStackTrace();
            output = "ERROR";
        }


        return new String(output);
    }

}