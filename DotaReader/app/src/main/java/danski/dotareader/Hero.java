package danski.dotareader;

/**
 * Created by Danny on 26/06/2015.
 */
public class Hero {
    public String name;
    public int id;
    public String localized_name;
    public String image_url;

    public Hero(){

    }

    public Hero(String _name, int _id, String _localized_name){
        name = _name;
        id = _id;
        localized_name = _localized_name;
    }

    public Hero(String _name, int _id, String _localized_name, String _image_url){
        name = _name;
        id = _id;
        localized_name = _localized_name;
        image_url = _image_url;
    }
}
