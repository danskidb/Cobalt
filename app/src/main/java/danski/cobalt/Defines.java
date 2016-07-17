package danski.cobalt;

import android.content.Context;

/**
 * Created by Danny on 28/01/2016.
 */
public class Defines {
    static long SteamIDconvertor = 76561197960265728l;
    public static Context CurrentContext;
    public static String APIKEY = "7B5DF1FD8BA33927FAC62EF3D1DB37FB";
    public static String itemimgurl = "http://media.steampowered.com/apps/dota2/images/items/";
    public static String heroimgurl = "http://media.steampowered.com/apps/dota2/images/heroes/"; //furion_full.png

    public static long idTo32(long id64){
        return id64 - SteamIDconvertor;
    }

    public static long idTo64(long id32){
        return id32 + SteamIDconvertor;
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

    public static String getLeaverStatus(int status){
        switch(status){
            case 1:
                return "ABANDON";
            case 2:
                return "+5 MIN. DISCONNECT";
            case 3:
                return "DISCONNECT & LEAVE";
            case 4:
                return "AFK ABANDON";
            case 5:
                return "NEVER CONNECTED";
            case 6:
                return "NEVER CONNECTED";
            default:
                return "ABANDON";
        }
    }

}
