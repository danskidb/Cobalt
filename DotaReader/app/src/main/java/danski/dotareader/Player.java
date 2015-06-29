package danski.dotareader;

/**
 * Created by Danny on 29-6-2015.
 */
public class Player {
    public int account_id;
    public int player_slot;
    public int hero_id;
    public int item_0;
    public int item_1;
    public int item_2;
    public int item_3;
    public int item_4;
    public int item_5;
    public int kills;
    public int deaths;
    public int assists;
    public int leaver_status;
    public int gold;
    public int last_hits;
    public int denies;
    public int gold_per_min;
    public int xp_per_min;
    public int gold_spent;
    public int hero_damage;
    public int tower_damage;
    public int hero_healing;
    public int level;
    public ability_upgrades[] ability_upgrades;

    public Player(){

    }

    class ability_upgrades{
        int ability;
        int time;
        int level;
    }
}
