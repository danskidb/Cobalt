package danski.cobalt;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import danski.cobalt.Data.Match;

/**
 * Created by Danny on 26/06/2015.
 */

public class Defines {
    public static Match[] CurrentMatches;
    public static Match SelectedMatch;
    public static Context CurrentContext;
    static long convertor = 76561197960265728l;
    public static String key = "7B5DF1FD8BA33927FAC62EF3D1DB37FB";
    public static long openid_steamid64;

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

    public static long idTo32(long id64){

        return id64 - convertor;
    }

    public static long idTo64(long id32){

        return id32 + convertor;
    }

    public static int[] splitToComponentTimes(long seconds){
        long longVal = seconds;
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }
}