package dungeonmania;

import java.io.Serializable;

import com.google.gson.JsonObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectableEntities.Arrows;
import dungeonmania.entities.collectableEntities.InvincibilityPotion;
import dungeonmania.entities.collectableEntities.InvisibilityPotion;
import dungeonmania.entities.collectableEntities.Key;
import dungeonmania.entities.collectableEntities.SunStone;
import dungeonmania.entities.collectableEntities.Sword;
import dungeonmania.entities.collectableEntities.TimeTurner;
import dungeonmania.entities.collectableEntities.Treasure;
import dungeonmania.entities.collectableEntities.Wood;
import dungeonmania.entities.collectableEntities.Bomb.Bomb;
import dungeonmania.entities.movingEntities.Assassin;
import dungeonmania.entities.movingEntities.Hydra;
import dungeonmania.entities.movingEntities.Mercenary;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.entities.movingEntities.Spider;
import dungeonmania.entities.movingEntities.ZombieToast;
import dungeonmania.entities.staticEntities.Boulder;
import dungeonmania.entities.staticEntities.Door;
import dungeonmania.entities.staticEntities.Exit;
import dungeonmania.entities.staticEntities.FloorSwitch;
import dungeonmania.entities.staticEntities.LightBulb;
import dungeonmania.entities.staticEntities.Portal;
import dungeonmania.entities.staticEntities.SwampTile;
import dungeonmania.entities.staticEntities.SwitchDoor;
import dungeonmania.entities.staticEntities.TimeTravellingPortal;
import dungeonmania.entities.staticEntities.Wall;
import dungeonmania.entities.staticEntities.Wire;
import dungeonmania.entities.staticEntities.ZombieToastSpawner;
import dungeonmania.util.Position;

public class EntityFactory implements Serializable {

    public static Entity getInstance(String type, Position p, Config configVariables, JsonObject entityObj) {
        if (type.equals("player")) {
            return new Player(type, p, Double.valueOf(configVariables.getPlayerHealth()), Double.valueOf(configVariables.getPlayerAttack()));
        } else if (type.equals("wall")) {
            return new Wall(type, p);
        } else if (type.equals("exit")) {
            return new Exit(type, p);
        } else if (type.equals("boulder")) {
            return new Boulder(type, p);
        } else if (type.equals("switch")) {
            String logic = "NONE";
            if (entityObj.has("logic")) {
                logic = entityObj.get("logic").getAsString().toUpperCase();
            }
            return new FloorSwitch(type, p, logic);
        } else if (type.equals("door")) {
            int key = entityObj.get("key").getAsInt();
            return new Door(type, p, key);
        } else if (type.equals("portal")) {
            String colour = entityObj.get("colour").getAsString();
            return new Portal(type, p, colour);
        } else if (type.equals("zombie_toast_spawner")) {
            return new ZombieToastSpawner(type, p);
        } else if (type.equals("spider")) {
            return new Spider(type, p, Double.valueOf(configVariables.getSpiderHealth()), Double.valueOf(configVariables.getSpiderAttack()));
        } else if (type.equals("zombie_toast")) {
            return new ZombieToast(type, p, Double.valueOf(configVariables.getZombieHealth()), Double.valueOf(configVariables.getZombieAttack()));
        } else if (type.equals("mercenary")) {
            return new Mercenary(type, p, Double.valueOf(configVariables.getMercenaryHealth()), Double.valueOf(configVariables.getMercenaryAttack()), configVariables.getBribeAmount(), Double.valueOf(configVariables.getBribeRadius()));
        } else if (type.equals("treasure")) {
            return new Treasure(type, p);
        } else if (type.equals("key")) {
            int key = entityObj.get("key").getAsInt();
            return new Key(type, p, key);
        } else if (type.equals("invincibility_potion")) {
            return new InvincibilityPotion(type, p);
        } else if (type.equals("invisibility_potion")) {
            return new InvisibilityPotion(type, p);
        } else if (type.equals("wood")) {
            return new Wood(type, p);
        } else if (type.equals("arrow")) {
            return new Arrows(type, p);
        } else if (type.equals("bomb")) {
            String logic = "NONE";
            if (entityObj.has("logic")) {
                logic = entityObj.get("logic").getAsString().toUpperCase();
            }
            return new Bomb(type, p, Double.valueOf(configVariables.getBombRadius()), logic);
        } else if (type.equals("sword")) {
            return new Sword(type, p, Double.valueOf(configVariables.getSwordDurability()), Double.valueOf(configVariables.getSwordAttack()));
        } else if (type.equals("assassin")) {
            return new Assassin(type, p, Double.valueOf(configVariables.getAssassinHealth()), Double.valueOf(configVariables.getAssassinAttack()), configVariables.getAssassinBribeAmount(), Double.valueOf(configVariables.getBribeRadius()), Double.valueOf(configVariables.getAssassinBribeFailRate()), configVariables.getAssassinReconRadius());
        } else if (type.equals("hydra")) {
            return new Hydra(type, p, Double.valueOf(configVariables.getHydraHealth()), Double.valueOf(configVariables.getHydraAttack()), Double.valueOf(configVariables.getHydraHealthIncreaseRate()), configVariables.getHydraHealthIncreaseAmount());
        } else if (type.equals("swamp_tile")) {
            p = new Position(p.getX(), p.getY(), -1);
            int movement_factor = entityObj.get("movement_factor").getAsInt();
            return new SwampTile(type, p, movement_factor);
        } else if (type.equals("sun_stone")) {
            return new SunStone(type, p);
        } else if (type.equals("time_turner")) {
            return new TimeTurner(type, p);
        } else if (type.equals("time_travelling_portal")) {
            String colour = entityObj.get("colour").getAsString();
            return new TimeTravellingPortal(type, p, colour);
        } else if (type.equals("light_bulb_off")) {
            String logic = entityObj.get("logic").getAsString().toUpperCase();
            return new LightBulb(type, p, logic);
        } else if (type.equals("wire")) {
            return new Wire(type, p);
        } else if (type.equals("switch_door")) {
            String logic = entityObj.get("logic").getAsString().toUpperCase();
            int key = entityObj.get("key").getAsInt();
            return new SwitchDoor(type, p, key, logic);
        } else {
            return null;
        }
    }
}
