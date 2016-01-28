-- Creator:       MySQL Workbench 6.3.6/ExportSQLite Plugin 0.1.0
-- Author:        Danny
-- Caption:       New Model
-- Project:       Name of the project
-- Changed:       2016-01-28 16:45
-- Created:       2016-01-28 15:27
PRAGMA foreign_keys = OFF;

-- Schema: cobaltdb
ATTACH "cobaltdb.sdb" AS "cobaltdb";
BEGIN;
CREATE TABLE "cobaltdb"."Hero"(
  "hero_id" INTEGER PRIMARY KEY NOT NULL,
  "hero_name" VARCHAR(45),
  "biography" VARCHAR(45),
  "primary_attribute" VARCHAR(45),
  "strength_base" INTEGER,
  "strength_gain" FLOAT,
  "intelligence_base" INTEGER,
  "intelligence_gain" FLOAT,
  "agility_base" INTEGER,
  "agility_gain" FLOAT,
  "movespeed" INTEGER,
  "startingdamage_min" INTEGER,
  "startingdamage_max" INTEGER,
  "startingarmor" INTEGER,
  "attack_type" VARCHAR(45),
  "roles" VARCHAR(45)
);
CREATE TABLE "cobaltdb"."ability"(
  "ability_id" INTEGER PRIMARY KEY NOT NULL,
  "ability_name" VARCHAR(45),
  "description" VARCHAR(45),
  "affects" VARCHAR(45),
  "notes" VARCHAR(45),
  "dmg" VARCHAR(45),
  "attributes" VARCHAR(45),
  "cmd" VARCHAR(45),
  "lore" VARCHAR(45),
  "hurl" VARCHAR(45)
);
CREATE TABLE "cobaltdb"."Item"(
  "item_id" INTEGER PRIMARY KEY NOT NULL,
  "item_name" VARCHAR(45),
  "description" VARCHAR(45),
  "img" VARCHAR(45),
  "qual" VARCHAR(45),
  "cost" INTEGER,
  "notes" VARCHAR(45),
  "attributes" VARCHAR(45),
  "manacost" INTEGER,
  "cooldown" INTEGER,
  "lore" VARCHAR(45),
  "components" VARCHAR(45),
  "created" BOOL
);
CREATE TABLE "cobaltdb"."Hero_has_ability"(
  "Hero_hero_id" INTEGER NOT NULL,
  "ability_ability_id" INTEGER NOT NULL,
  PRIMARY KEY("Hero_hero_id","ability_ability_id"),
  CONSTRAINT "fk_Hero_has_ability_Hero1"
    FOREIGN KEY("Hero_hero_id")
    REFERENCES "Hero"("hero_id"),
  CONSTRAINT "fk_Hero_has_ability_ability1"
    FOREIGN KEY("ability_ability_id")
    REFERENCES "ability"("ability_id")
);
CREATE INDEX "cobaltdb"."Hero_has_ability.fk_Hero_has_ability_ability1_idx" ON "Hero_has_ability" ("ability_ability_id");
CREATE INDEX "cobaltdb"."Hero_has_ability.fk_Hero_has_ability_Hero1_idx" ON "Hero_has_ability" ("Hero_hero_id");
CREATE TABLE "cobaltdb"."Player"(
  "account_id" INTEGER PRIMARY KEY NOT NULL,
  "name" VARCHAR(45)
);
CREATE TABLE "cobaltdb"."Match"(
  "match_id" INTEGER PRIMARY KEY NOT NULL,
  "start_time" INTEGER,
  "lobby_type" INTEGER,
  "radiant_team_id" INTEGER,
  "dire_team_id" INTEGER,
  "radiant_win" BOOL,
  "duration" INTEGER,
  "match_seq_num" INTEGER,
  "tower_status_radiant" INTEGER,
  "tower_status_dire" INTEGER,
  "barracks_status_radiant" INTEGER,
  "barracks_status_dire" INTEGER,
  "cluster" INTEGER,
  "first_blood_time" INTEGER,
  "human_players" INTEGER,
  "leagueid" INTEGER,
  "positive_votes" INTEGER,
  "negative_votes" INTEGER,
  "game_mode" INTEGER,
  "flags" INTEGER,
  "engine" INTEGER
);
CREATE TABLE "cobaltdb"."Match_has_Player"(
  "Match_match_id" INTEGER NOT NULL,
  "Player_account_id" INTEGER NOT NULL,
  "Hero_hero_id" INTEGER NOT NULL,
  "Item_item_id" INTEGER NOT NULL,
  "Item_item_id1" INTEGER NOT NULL,
  "Item_item_id2" INTEGER NOT NULL,
  "Item_item_id3" INTEGER NOT NULL,
  "Item_item_id4" INTEGER NOT NULL,
  "Item_item_id5" INTEGER NOT NULL,
  "kills" INTEGER,
  "deaths" INTEGER,
  "assists" INTEGER,
  "leaver_status" INTEGER,
  "gold" INTEGER,
  "last_hits" INTEGER,
  "denies" INTEGER,
  "gold_per_minute" INTEGER,
  "xp_per_minute" INTEGER,
  "gold_spent" INTEGER,
  "hero_damage" INTEGER,
  "tower_damage" INTEGER,
  "hero_healing" INTEGER,
  "level" INTEGER,
  PRIMARY KEY("Match_match_id","Player_account_id"),
  CONSTRAINT "fk_Match_has_Player_Match"
    FOREIGN KEY("Match_match_id")
    REFERENCES "Match"("match_id"),
  CONSTRAINT "fk_Match_has_Player_Player1"
    FOREIGN KEY("Player_account_id")
    REFERENCES "Player"("account_id"),
  CONSTRAINT "fk_Match_has_Player_Hero1"
    FOREIGN KEY("Hero_hero_id")
    REFERENCES "Hero"("hero_id"),
  CONSTRAINT "fk_Match_has_Player_Item1"
    FOREIGN KEY("Item_item_id")
    REFERENCES "Item"("item_id"),
  CONSTRAINT "fk_Match_has_Player_Item2"
    FOREIGN KEY("Item_item_id1")
    REFERENCES "Item"("item_id"),
  CONSTRAINT "fk_Match_has_Player_Item3"
    FOREIGN KEY("Item_item_id2")
    REFERENCES "Item"("item_id"),
  CONSTRAINT "fk_Match_has_Player_Item4"
    FOREIGN KEY("Item_item_id3")
    REFERENCES "Item"("item_id"),
  CONSTRAINT "fk_Match_has_Player_Item5"
    FOREIGN KEY("Item_item_id4")
    REFERENCES "Item"("item_id"),
  CONSTRAINT "fk_Match_has_Player_Item6"
    FOREIGN KEY("Item_item_id5")
    REFERENCES "Item"("item_id")
);
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Player1_idx" ON "Match_has_Player" ("Player_account_id");
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Match_idx" ON "Match_has_Player" ("Match_match_id");
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Hero1_idx" ON "Match_has_Player" ("Hero_hero_id");
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Item1_idx" ON "Match_has_Player" ("Item_item_id");
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Item2_idx" ON "Match_has_Player" ("Item_item_id1");
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Item3_idx" ON "Match_has_Player" ("Item_item_id2");
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Item4_idx" ON "Match_has_Player" ("Item_item_id3");
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Item5_idx" ON "Match_has_Player" ("Item_item_id4");
CREATE INDEX "cobaltdb"."Match_has_Player.fk_Match_has_Player_Item6_idx" ON "Match_has_Player" ("Item_item_id5");
CREATE TABLE "cobaltdb"."Match_has_Player_has_ability"(
  "Match_has_Player_Match_match_id" INTEGER NOT NULL,
  "Match_has_Player_Player_account_id" INTEGER NOT NULL,
  "ability_ability_id" INTEGER NOT NULL,
  "time" INTEGER,
  "level" INTEGER,
  PRIMARY KEY("Match_has_Player_Match_match_id","Match_has_Player_Player_account_id","ability_ability_id"),
  CONSTRAINT "fk_Match_has_Player_has_ability_Match_has_Player1"
    FOREIGN KEY("Match_has_Player_Match_match_id","Match_has_Player_Player_account_id")
    REFERENCES "Match_has_Player"("Match_match_id","Player_account_id"),
  CONSTRAINT "fk_Match_has_Player_has_ability_ability1"
    FOREIGN KEY("ability_ability_id")
    REFERENCES "ability"("ability_id")
);
CREATE INDEX "cobaltdb"."Match_has_Player_has_ability.fk_Match_has_Player_has_ability_ability1_idx" ON "Match_has_Player_has_ability" ("ability_ability_id");
CREATE INDEX "cobaltdb"."Match_has_Player_has_ability.fk_Match_has_Player_has_ability_Match_has_Player1_idx" ON "Match_has_Player_has_ability" ("Match_has_Player_Match_match_id","Match_has_Player_Player_account_id");
COMMIT;
