package danski.cobalt.sql.DataStructure;

import android.util.Log;

/**
 * Created by Danny on 09/03/2016.
 */
public class Player {
    public String steamid64;
    public String Name;
    public String URL_profile;
    public String URL_avatarfull;
    public String URL_avatarmed;

    public Player(){}

    public Player(String _steamid64, String _name, String _URL_profile, String _URL_avatarfull, String _URL_avatarmed){
        steamid64 = _steamid64;
        Name = _name;
        URL_profile = _URL_profile;
        URL_avatarfull = _URL_avatarfull;
        URL_avatarmed = _URL_avatarmed;
    }
}
