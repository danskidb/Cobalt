package danski.cobalt.sql.DataStructure;

/**
 * Created by danny on 23-11-2016.
 */

public class Item {
    public int item_id;
    public String item_name;
    public String desc;
    public String image_filename;
    public String qual;
    public int cost;
    public String notes;
    public String attributes;
    public int manacost;
    public int cooldown;
    public String lore;
    public String components;
    public int createdint;

    public Item(int _item_id, String _item_name){
        item_id = _item_id;
        item_name = _item_name;
    }

}
