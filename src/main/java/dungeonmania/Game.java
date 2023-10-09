package dungeonmania;

import dungeonmania.Status.Status;
import dungeonmania.entities.Entity;
import dungeonmania.entities.buildableEntities.Bow;
import dungeonmania.entities.buildableEntities.Shield;
import dungeonmania.entities.collectableEntities.Arrows;
import dungeonmania.entities.buildableEntities.MidnightArmour;
import dungeonmania.entities.buildableEntities.Sceptre;
import dungeonmania.entities.collectableEntities.InvincibilityPotion;
import dungeonmania.entities.collectableEntities.InvisibilityPotion;
import dungeonmania.entities.collectableEntities.Key;
import dungeonmania.entities.collectableEntities.SunStone;
import dungeonmania.entities.collectableEntities.Sword;
import dungeonmania.entities.collectableEntities.Treasure;
import dungeonmania.entities.collectableEntities.Wood;
import dungeonmania.entities.collectableEntities.TimeTurner;
import dungeonmania.entities.collectableEntities.Bomb.Bomb;
import dungeonmania.entities.collectableEntities.Bomb.BombHelper;
import dungeonmania.entities.movingEntities.Assassin;
import dungeonmania.entities.movingEntities.Hydra;
import dungeonmania.entities.movingEntities.Mercenary;
import dungeonmania.entities.movingEntities.MovingEntity;
import dungeonmania.entities.movingEntities.OlderPlayer;
import dungeonmania.entities.collectableEntities.CollectableEntity;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.entities.movingEntities.Spider;
import dungeonmania.entities.movingEntities.ZombieToast;
import dungeonmania.entities.staticEntities.Boulder;
import dungeonmania.entities.staticEntities.Door;
import dungeonmania.entities.staticEntities.Exit;
import dungeonmania.entities.staticEntities.FloorSwitch;
import dungeonmania.entities.staticEntities.LightBulb;
import dungeonmania.entities.staticEntities.Portal;
import dungeonmania.entities.staticEntities.TimeTravellingPortal;
import dungeonmania.entities.staticEntities.SwampTile;
import dungeonmania.entities.staticEntities.SwitchDoor;
import dungeonmania.entities.staticEntities.Wall;
import dungeonmania.entities.staticEntities.Wire;
import dungeonmania.entities.staticEntities.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.AND;
import dungeonmania.goals.BouldersGoal;
import dungeonmania.goals.EnemiesGoal;
import dungeonmania.goals.ExitGoal;
import dungeonmania.goals.Goal;
import dungeonmania.goals.OR;
import dungeonmania.goals.TreasureGoal;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import spark.utils.IOUtils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;  
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Game implements Serializable {
    private String name;
    private String id;
    private String dungeonName;
    private String configName;
    private Map<String, List<Entity>> entities = new HashMap<String, List<Entity>>();
    private Status status;
    private Inventory inventory = new Inventory();
    private Position prevPos;
    private List<BattleResponse> battles = new ArrayList<BattleResponse>();
    private int treasureCollected = 0;
    private int enemiesDestroyed = 0;
    private Goal goals = null;

    private Config configVariables;

    public Game(String dungeonName, String configName, String dungeonStream, String configStream) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.dungeonName = dungeonName;
        this.configName = configName;
        this.configSetup(configStream);
        this.dungeonSetup(dungeonStream);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void configSetup(String configStream) {
        String data;
        data = configStream;
        this.configVariables = new Gson().fromJson(data, Config.class);    
    }

    public void setStatus() {
        status = new Status(Double.valueOf(configVariables.getInvincibilityPotionDuration()), 
        Double.valueOf(configVariables.getInvisibilityPotionDurability()));
    }

    public void dungeonSetup(String dungeonStream) {
        setStatus();
        
        try {
            JsonObject gsonObject = JsonParser.parseString(dungeonStream).getAsJsonObject();
            JsonArray entitiesList = gsonObject.getAsJsonArray("entities");

            Iterator<JsonElement> iterator = entitiesList.iterator();

			while (iterator.hasNext()) {
                JsonElement entity = iterator.next();
                JsonObject entityObj = entity.getAsJsonObject();

                String type = entityObj.get("type").getAsString();
                int x = entityObj.get("x").getAsInt();
                int y = entityObj.get("y").getAsInt();
                Position p = new Position(x, y);
                
                Entity o = EntityFactory.getInstance(type, p, configVariables, entityObj);

                if (o != null) {
                    if (entities.containsKey(type)) {
                        entities.get(type).add(o);
                    } else {
                        List<Entity> l = new ArrayList<Entity>();
                        l.add(o);
                        entities.put(type, l);
                    }
                }
			}

            JsonObject goalObj = gsonObject.getAsJsonObject("goal-condition");
            goals = generateGoals(goalObj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Goal generateGoals(JsonObject goalObj) {
        if (!goalObj.has("goal")) {
            return null;
        }

        Goal g = null;
        
        String condition = goalObj.get("goal").getAsString();
        if (Objects.equals(condition, "AND")) {
            if (goalObj.has("subgoals")) {
                JsonArray goalList = goalObj.getAsJsonArray("subgoals");
                Iterator<JsonElement> iterator = goalList.iterator();

                AND and = new AND();
                while (iterator.hasNext()) {
                    JsonElement goal = iterator.next();
                    JsonObject objGoal = goal.getAsJsonObject();
                    and.addChild(generateGoals(objGoal));
                }

                g = and;
            }
        } else if (Objects.equals(condition, "OR")) {
            if (goalObj.has("subgoals")) {
                JsonArray goalList = goalObj.getAsJsonArray("subgoals");
                Iterator<JsonElement> iterator = goalList.iterator();

                OR or = new OR();
                while (iterator.hasNext()) {
                    JsonElement goal = iterator.next();
                    JsonObject objGoal = goal.getAsJsonObject();
                    or.addChild(generateGoals(objGoal));
                }

                g = or;
            }
        }
        
        if (Objects.equals(condition, "boulders")) {
            g = new BouldersGoal();
        } else if (Objects.equals(condition, "enemies")) {
            g = new EnemiesGoal();
        } else if (Objects.equals(condition, "exit")) {
            g = new ExitGoal();
        } else if (Objects.equals(condition, "treasure")) {
            g = new TreasureGoal();
        }

        return g;
    }
    
    // For walls or entites that the player cannot pass through

    public void movePlayer(Direction movementDirection) {
        Player p = (Player) this.getPlayer();
        Position playerPos = p.getPosition();
        Position offset = movementDirection.getOffset();
        Position newPlayerPos = new Position(playerPos.getX() + offset.getX(), playerPos.getY() + offset.getY());
        prevPos = playerPos;
        updateEntities(p, newPlayerPos);
        updateLogicEntities();
    }


    public boolean hasWall(Position newPlayerPos) {
        List<Entity> entitiesInCellList = getEntitiesInCell(newPlayerPos.getX(), newPlayerPos.getY());

        for (Entity o: entitiesInCellList) {
            if (Objects.equals(o.getType(), "boulder")) {
                return true;
            }
        }

        return false;

    }

    public void updateEntities(Player p, Position newPlayerPos) {
        
        // If the player is blocked, do nothing except move spiders and zombies and Older Player
        if (!canPassThrough(getEntitiesInCell(newPlayerPos.getX(), newPlayerPos.getY()))) {
            for (String s: entities.keySet()) {
                for (Entity en: entities.get(s)) {
                    if (en.getType().equals("spider")) {
                        Spider sp = (Spider) en;
                        updateSpider(p, this, sp);
    
                    } else if (en.getType().equals("zombie_toast")) {
                        ZombieToast zom = (ZombieToast) en;
                        updateZombie(p, this, zom);
                    }  else if (en.getType().equals("older_player")) {
                        OlderPlayer op = (OlderPlayer) en;
                        updateOlderPlayer(p, this, op);
                    } else if(en.getType().equals("hydra")) {
                        Hydra hy = (Hydra) en;
                        updateHydra(p, this, hy);
                    }
                }
            }
            return;
        }

        // Look for boulders, switches, and bombs in the new position player wants to move into
       for (Entity o : getEntitiesInCell(newPlayerPos.getX(), newPlayerPos.getY())) {
            if (Objects.equals(o.getType(), "boulder")) {
                // Can get rid of casting if we add collideWithPlayer into Entity
                Boulder b = (Boulder) o;
                b.collideWithPlayer(p, this);
            } else if (Objects.equals(o.getType(), "bomb")) {
                Bomb b = (Bomb) o;
                b.collideWithPlayer(p, this);
            } else if (Objects.equals(o.getType(), "treasure")) {
                Treasure t = (Treasure) o;
                t.collideWithPlayer(p, this);
                treasureCollected += 1;
            } else if (Objects.equals(o.getType(), "sun_stone")) {
                SunStone s = (SunStone) o;
                s.collideWithPlayer(p, this);
                treasureCollected += 1;
            } else if (Objects.equals(o.getType(), "portal")) {
                Portal portal = (Portal) o;
                portal.collideWithPlayer(p, this);
            } else if (Objects.equals(o.getType(), "time_travelling_portal")) {
                TimeTravellingPortal timeportal = (TimeTravellingPortal) o;
                timeportal.collideWithPlayer(p, this);
            } else if (Objects.equals(o.getType(), "key")) {
                Key k = (Key) o;
                k.collideWithPlayer(p, this);
            } else if (o instanceof CollectableEntity) {
                CollectableEntity c = (CollectableEntity) o;
                c.collideWithPlayer(p, this);
            } 

        }

        if (!hasWall(newPlayerPos)) {
            if (!p.hasGoneThroughPortal()) {
                p.setPosition(newPlayerPos);
            } else if (p.isCanTeleport()) {
                p.sethasGoneThroughPortal(false);
                updateEntities(p, p.getPosition());
            } else {
                p.setPosition(prevPos);
                p.setCanTeleport(true);
            }
            
            updateMovingEntites(p, this);
        }

        // after movement, check if theres a boulder on top of switches
        updateSwitches();
        updateBombs();

    }

    /**
     * Updates the positions of moving entities, allows them to fight, and
     * spawns ZombieToasts from the ZombieToastSpawner/s, if it/they exist/s
     * @param p
     * @param g
     */
    public void updateMovingEntites(Player p, Game g) {
        for (String s: entities.keySet()) {
            for (Entity en: entities.get(s)) {

                if (Objects.equals(en.getType(), "mercenary")) {
                    Mercenary m = (Mercenary) en;
                    updateMercenary(p, g, m);

                } else if (en.getType().equals("spider")) {
                    Spider sp = (Spider) en;
                    updateSpider(p, g, sp);

                } else if (en.getType().equals("zombie_toast")) {
                    ZombieToast zom = (ZombieToast) en;
                    updateZombie(p, g, zom);

                } else if (en.getType().equals("assassin")) {
                    Assassin a = (Assassin) en;
                    updateAssassin(p, g, a);

                } else if (en.getType().equals("hydra")) {
                    Hydra h = (Hydra) en;
                    updateHydra(p, g, h);
                } else if (en.getType().equals("older_player")) {
                    OlderPlayer op = (OlderPlayer) en;
                    updateOlderPlayer(p, g, op);
                }

            }
        }
    }

    public void updateHydra(Player p, Game g, Hydra hy) {
        hy.updatePos(p.pos, g);
        battleHydra(p, g, hy);
    }

    public void updateOlderPlayer(Player p, Game g, OlderPlayer op) {
        op.updatePos(g);
        battleOlderPlayer(p, g, op);
    }

    public void updateSpider(Player p, Game g, Spider sp) {
        sp.updatePos(g);
        battleSpider(p, g, sp);
    }

    public void updateZombie(Player p, Game g, ZombieToast zom) {
        zom.updatePos(p.getPosition(), g);
        battleZombie(p, g, zom);
    }

    public void updateMercenary(Player p, Game g, Mercenary m) {
        if (m.getIsAlly() || (g.getInventory().getItems().containsKey("sceptre") && g.getInventory().getItems().get("sceptre").size() >= 1)) {
            m.updatePos(prevPos, g);
        } else {
            Double initialPlayerHealth = p.getHealth();
            Double initialEnemyHealth = m.getHealth();

            m.updatePos(p.getPosition(), g);

            if (p.getPosition().equals(m.getPosition())) {
                if (!g.getStatus().isInvisible() && !g.getStatus().isInvincible()) {
                    Double deltaEnemy = m.getDeltaEnemyHealth(p, g);
                    Double deltaPlayer = m.getDeltaPlayerHealth(p, g);
                    m.battle(p, deltaEnemy, deltaPlayer);

                    if (m.getBattleResponseIndex() == -1) {
                        m.setBattleResponseIndex(battles.size());
                        List<RoundResponse> l = new ArrayList<RoundResponse>();
                        l.add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                        BattleResponse r = new BattleResponse(m.getType(), l, initialPlayerHealth, initialEnemyHealth);
                        battles.add(r);
                    } else {
                        battles.get(m.getBattleResponseIndex()).getRounds().add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                    }
                }  
            } else if (g.getStatus().isInvincible()) {
                // Player takes no damage and enemies gets one shot
                m.battle(p, -m.getHealth(), (double) 0);

                if (m.getBattleResponseIndex() == -1) {
                    m.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse((double) 0, -m.getHealth(), getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(m.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(m.getBattleResponseIndex()).getRounds().add(new RoundResponse((double) 0, -m.getHealth(), getWeaponaryUsed()));
                }
            }
        }
    }

    public void updateAssassin(Player p, Game g, Assassin a) {
        if (a.getIsAlly() || (g.getInventory().getItems().containsKey("sceptre") && g.getInventory().getItems().get("sceptre").size() >= 1)) {
            a.updatePos(prevPos, g);
        } else {
            Double initialPlayerHealth = p.getHealth();
            Double initialEnemyHealth = a.getHealth();

            a.updatePos(p.getPosition(), g);

            if (p.getPosition().equals(a.getPosition())) {
                if (!g.getStatus().isInvincible() || (g.getStatus().isInvisible() && Position.calculateDistance(p.getPosition(), a.getPosition()) <= g.getConfigVariables().getAssassinReconRadius())) {
                    Double deltaEnemy = a.getDeltaEnemyHealth(p, g);
                    Double deltaPlayer = a.getDeltaPlayerHealth(p, g);
                    a.battle(p, deltaEnemy, deltaPlayer);

                    if (a.getBattleResponseIndex() == -1) {
                        a.setBattleResponseIndex(battles.size());
                        List<RoundResponse> l = new ArrayList<RoundResponse>();
                        l.add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                        BattleResponse r = new BattleResponse(a.getType(), l, initialPlayerHealth, initialEnemyHealth);
                        battles.add(r);
                    } else {
                        battles.get(a.getBattleResponseIndex()).getRounds().add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                    }
                }  
            } else if (g.getStatus().isInvincible()) {
                // Player takes no damage and enemies gets one shot
                a.battle(p, -a.getHealth(), (double) 0);

                if (a.getBattleResponseIndex() == -1) {
                    a.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse((double) 0, -a.getHealth(), getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(a.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(a.getBattleResponseIndex()).getRounds().add(new RoundResponse((double) 0, -a.getHealth(), getWeaponaryUsed()));
                }
            }
        }
    }

    public void battleSpider(Player p, Game g, Spider sp) {
        Double initialPlayerHealth = p.getHealth();
        Double initialEnemyHealth = sp.getHealth();

        if (p.getPosition().equals(sp.getPosition())) {
            if (!g.getStatus().isInvisible() && !g.getStatus().isInvincible()) {
                Double deltaEnemy = sp.getDeltaEnemyHealth(p, g);
                Double deltaPlayer = sp.getDeltaPlayerHealth(p, g);
                sp.battle(p, deltaEnemy, deltaPlayer);

                if (sp.getBattleResponseIndex() == -1) {
                    sp.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(sp.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(sp.getBattleResponseIndex()).getRounds().add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                }
            }
            else if (g.getStatus().isInvincible()) {
                // Player takes no damage and enemies gets one shot
                sp.battle(p, -sp.getHealth(), (double) 0);

                if (sp.getBattleResponseIndex() == -1) {
                    sp.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse((double) 0, -sp.getHealth(), getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(sp.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(sp.getBattleResponseIndex()).getRounds().add(new RoundResponse((double) 0, -sp.getHealth(), getWeaponaryUsed()));
                }
            }
        }
    }

    public void battleZombie(Player p, Game g, ZombieToast zom) {
        Double initialPlayerHealth = p.getHealth();
        Double initialEnemyHealth = zom.getHealth();
        if (p.getPosition().equals(zom.getPosition())) {
            if (!g.getStatus().isInvisible() && !g.getStatus().isInvincible()) {
                Double deltaEnemy = zom.getDeltaEnemyHealth(p, g);
                Double deltaPlayer = zom.getDeltaPlayerHealth(p, g);
                zom.battle(p, deltaEnemy, deltaPlayer);

                if (zom.getBattleResponseIndex() == -1) {
                    zom.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(zom.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(zom.getBattleResponseIndex()).getRounds().add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                }
            } else if (g.getStatus().isInvincible()) {
                // Player takes no damage and enemies gets one shot
                zom.battle(p, -zom.getHealth(), (double) 0);

                if (zom.getBattleResponseIndex() == -1) {
                    zom.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse(-zom.getHealth(), (double) 0, getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(zom.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(zom.getBattleResponseIndex()).getRounds().add(new RoundResponse(-zom.getHealth(),(double) 0, getWeaponaryUsed()));
                }
            }
            
        }
    }

    public void battleHydra(Player p, Game g, Hydra hy) {
        Double initialPlayerHealth = p.getHealth();
        Double initialEnemyHealth = hy.getHealth();
        if (p.getPosition().equals(hy.getPosition())) {
            if (!g.getStatus().isInvisible() && !g.getStatus().isInvincible()) {
                Double deltaEnemy = hy.getDeltaEnemyHealth(p, g);
                Double deltaPlayer = hy.getDeltaPlayerHealth(p, g);
                hy.battle(p, deltaEnemy, deltaPlayer);

                if (hy.getBattleResponseIndex() == -1) {
                    hy.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(hy.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(hy.getBattleResponseIndex()).getRounds().add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                }
            } else if (g.getStatus().isInvincible()) {
                // Player takes no damage and enemies gets one shot
                hy.battle(p, -hy.getHealth(), (double) 0);

                if (hy.getBattleResponseIndex() == -1) {
                    hy.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse((double) 0, -hy.getHealth(), getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(hy.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(hy.getBattleResponseIndex()).getRounds().add(new RoundResponse((double) 0, -hy.getHealth(), getWeaponaryUsed()));
                }
            }
            
        }
    }

    public void battleOlderPlayer(Player p, Game g, OlderPlayer op) {
        Double initialPlayerHealth = p.getHealth();
        Double initialEnemyHealth = op.getHealth();

        if (p.getPosition().equals(op.getPosition())) {
            // If the player has no potion effect
            if (!g.getStatus().isInvisible() && !g.getStatus().isInvincible()) {
                // If the player does not have these items
                if (!g.getInventory().getItems().containsKey("sun_stone") && !g.getInventory().getItems().containsKey("midnight_armour")) {
                    // Then you can fight
                    Double deltaEnemy = op.getDeltaEnemyHealth(p, g);
                    Double deltaPlayer = op.getDeltaPlayerHealth(p, g);
                    op.battle(p, deltaEnemy, deltaPlayer);

                    if (op.getBattleResponseIndex() == -1) {
                        op.setBattleResponseIndex(battles.size());
                        List<RoundResponse> l = new ArrayList<RoundResponse>();
                        l.add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                        BattleResponse r = new BattleResponse(op.getType(), l, initialPlayerHealth, initialEnemyHealth);
                        battles.add(r);
                    } else {
                        battles.get(op.getBattleResponseIndex()).getRounds().add(new RoundResponse(deltaPlayer, deltaEnemy, getWeaponaryUsed()));
                    }
                }  
            }
            else if (g.getStatus().isInvincible()) {
                // Player takes no damage and enemies gets one shot
                op.battle(p, -op.getHealth(), (double) 0);

                if (op.getBattleResponseIndex() == -1) {
                    op.setBattleResponseIndex(battles.size());
                    List<RoundResponse> l = new ArrayList<RoundResponse>();
                    l.add(new RoundResponse((double) 0, -op.getHealth(),  getWeaponaryUsed()));
                    BattleResponse r = new BattleResponse(op.getType(), l, initialPlayerHealth, initialEnemyHealth);
                    battles.add(r);
                } else {
                    battles.get(op.getBattleResponseIndex()).getRounds().add(new RoundResponse((double) 0, -op.getHealth(), getWeaponaryUsed()));
                }
            }
        }
    }

    public boolean canPassThrough(List<Entity> entitiesInCell) {
        boolean canPass = true;

        for (Entity o: entitiesInCell) {
            if (Objects.equals(o.getType(), "wall")) {
                canPass = false;
            } else if (Objects.equals(o.getType(), "bomb")) {
                Bomb b = (Bomb) o;
                if (!b.isCanPass()) canPass = false;
            } else if (Objects.equals(o.getType(), "door")) {
                Door d = (Door) o;
                Player p = (Player) this.getPlayer();
                d.collideWithPlayer(p, this);
                if (d.isLocked()) canPass = false;
            } else if (Objects.equals(o.getType(), "switch_door")) {
                SwitchDoor d = (SwitchDoor) o;
                Player p = (Player) this.getPlayer();
                d.collideWithPlayer(p, this);
                if (d.isLocked()) canPass = false;
            }
        }

        return canPass;
    }

    public void updateSwitches() {
        List<Entity> switchesList = getSwitchEntities();
        if (switchesList == null) return;
    
        // Loop through all the switches to check if theres a boulder in the same cell
        for (Entity s : switchesList) {
            List<Entity> entitiesOnTop = getEntitiesInCell(s.getPosition().getX(), s.getPosition().getY());
            FloorSwitch fs = (FloorSwitch) s;
            fs.turnSwitch(entitiesOnTop);
        }
    }

    public void removeDeadEntities() {
        Map<String, List<Entity>> aliveEntities = new HashMap<String, List<Entity>>();

        for (String s: entities.keySet()) {
            List<Entity> l = new ArrayList<Entity>();
            
            for (Entity o: entities.get(s)) {
                if (o.isAlive()) {
                    l.add(o);
                } else {
                    enemiesDestroyed += 1;
                }
            }

            if (l.size() > 0) {
                aliveEntities.put(s, l);
            }
        }

        entities = aliveEntities;
    }

    public void updateBombs() {
        List<Entity> bombsList = getBombsEntities();
        if (bombsList == null) return;
        
        for (Entity s : bombsList) {
            Bomb b = (Bomb) s;
            b.checkAdjacent(this);
            if (b.isHasDetonate()) return;
        }
    }

    public List<Entity> getEntitiesInCell(int X, int Y) {
        List<Entity> l = new ArrayList<Entity>();
        // Loop through all the entities and get 
        for (String s: entities.keySet()) {
            if (entities.containsKey(s)) {
                for (Entity o: entities.get(s)) {
                    if (o.getPosition().getX() == X && o.getPosition().getY() == Y) {
                        l.add(o);
                    }
                }
            }
        }

        return l;
    }

    /**
     * Find out if a position/cell on the map is empty
     * @returns true if empty, false if it's not empty
     */
    public boolean isEmpty(int X, int Y) {
        if(getEntitiesInCell(X, Y).size() != 0) {
            return false;
        }
        return true;
    }

    public void removeEntity(Entity e) {
        Map<String, List<Entity>> entitiesL = getEntities();

        for (String s: entitiesL.keySet()) {
            if (entitiesL.get(s).contains(e)) {
                entities.get(s).remove(e);
            }
        }
    }

    public void addEntity(Entity e) {
        Map<String, List<Entity>> entitiesL = getEntities();
        if (!entitiesL.containsKey(e.getType())) {
            List<Entity> l = new ArrayList<Entity>();
            l.add(e);
            entities.put(e.getType(), l);
        } else {
            entitiesL.get(e.getType()).add(e);
        }
    }

    public void spawnSpiderAndZoms(int tickcount) {
        if(getConfigVariables().getSpiderSpawnRate() != 0) {
            if((modulo(tickcount, getConfigVariables().getSpiderSpawnRate())) == 0) {
                spawnSpider();
            }
        }
        if(getConfigVariables().getZombieSpawnRate() != 0) {
            if((modulo(tickcount, getConfigVariables().getZombieSpawnRate())) == 0) {
                spawnZom();
            }
        }
    }

    public void spawnSpider() {
        Position pos = findSpiderSpawnPos();
        Spider sp = new Spider("spider", pos, Double.valueOf(configVariables.getSpiderHealth()), Double.valueOf(configVariables.getSpiderAttack()));
        addEntity(sp);
    }

    private Position findSpiderSpawnPos() {
        Random coordinateVal = new Random();
        int x = coordinateVal.nextInt(40); 
        int y = coordinateVal.nextInt(40);

        while(true) {
            if(hasWall(new Position(x, y))) {
                x = coordinateVal.nextInt(40);
                y = coordinateVal.nextInt(40);
            } else {
                break;
            }
        }
        
        return new Position(x, y);
    }

    /**
     * Spawns Zombies at ZombieToastSpawners, given that the right number of
     * ticks have passed
     */
    public void spawnZom() {
        Map<String, List<Entity>> enList = getEntities();
        List<ZombieToast> spawnList = new ArrayList<>();

        for(String str : enList.keySet()) {
            for(Entity en : entities.get(str)) {
                if(en.getType().equals("zombie_toast_spawner")) {
                    ZombieToastSpawner zSpawner = (ZombieToastSpawner) en;
                    ZombieToast zomb = zSpawner.spawnZombie(this, getConfigVariables().getZombieHealth(), getConfigVariables().getZombieAttack());
                    // If there's no space for the zombie to spawn, then do not spawn it
                    if(!zomb.getPosition().equals(new Position(-99, -99))) {
                        spawnList.add(zomb);
                    }
                }
            }
        }
        for(ZombieToast zom : spawnList) {
            addEntity(zom);
        }
    }

    public int modulo(int a, int b) {
        int modulo = a % b;
        if (modulo<0) modulo += b;
        return modulo;
    }

    public List<EntityResponse> getEntityResponse() {
        List<EntityResponse> entityResponses = new ArrayList<EntityResponse>();

        for (String s: entities.keySet()) {
            for (Entity o: entities.get(s)) {
                EntityResponse r = new EntityResponse(o.getId(), o.getType(), o.getPosition(), o.getIsInteractable());
                entityResponses.add(r);
            }
        }

        return entityResponses;
    }

    public double getTotalAllyDefence() {
        double totalDefence = 0;

        if (entities.containsKey("mercenary")) {
            for (Entity o: entities.get("mercenary")) {
                Mercenary m = (Mercenary) o;
                if (m.getIsAlly() || (this.getInventory().getItems().containsKey("sceptre") && this.getInventory().getItems().get("sceptre").size() >= 1)) {
                    totalDefence += Double.valueOf(configVariables.getAllyDefence());
                }
            }
        }

        if (entities.containsKey("assassin")) {
            for (Entity o: entities.get("assassin")) {
                Assassin a = (Assassin) o;
                if (a.getIsAlly() || (this.getInventory().getItems().containsKey("sceptre") && this.getInventory().getItems().get("sceptre").size() >= 1)) {
                    totalDefence += Double.valueOf(configVariables.getAllyDefence());
                }
            }
        }


        return totalDefence;
    }

    public double getTotalAllyAttack() {
        double totalAttack = 0;

        if (entities.containsKey("mercenary")) {
            for (Entity o: entities.get("mercenary")) {
                Mercenary m = (Mercenary) o;
                if (m.getIsAlly() || (this.getInventory().getItems().containsKey("sceptre") && this.getInventory().getItems().get("sceptre").size() >= 1)) {
                    totalAttack += Double.valueOf(configVariables.getAllyAttack());
                }
            }
        }

        if (entities.containsKey("assassin")) {
            for (Entity o: entities.get("assassin")) {
                Assassin a = (Assassin) o;
                if (a.getIsAlly() || (this.getInventory().getItems().containsKey("sceptre") && this.getInventory().getItems().get("sceptre").size() >= 1)) {
                    totalAttack += Double.valueOf(configVariables.getAllyAttack());
                }
            }
        }

        return totalAttack;
    }


    public double getTotalBowAttack() {
        double totalAttack = 0;

        if (inventory.getItems().containsKey("bow")) {
            for (Entity o: inventory.getItems().get("bow")) {
                Bow b = (Bow) o;
                if (!b.isBroken()) {
                    b.useItem();
                    totalAttack += 2;
                }
            }
        }

        if (totalAttack == 0) {
            totalAttack = 1;
        }

        return totalAttack;
    }

    public double getTotalSwordAttack() {
        double totalAttack = 0;

        if (inventory.getItems().containsKey("sword")) {
            for (Entity o: inventory.getItems().get("sword")) {
                Sword s = (Sword) o;
                if (!s.isBroken()) {
                    s.useSword();
                    totalAttack += s.getAttack();
                }
            }

        }

        return totalAttack;
    }

    public double getTotalShieldDefence() {
        double totalDefence = 0;

        if (inventory.getItems().containsKey("shield")) {
            for (Entity o: inventory.getItems().get("shield")) {
                Shield s = (Shield) o;
                if (!s.isBroken()) {
                    s.useItem();
                    totalDefence += s.getDefence();
                }
            }

        }

        return totalDefence;
    }

    public double getMidnightArmourAttack() {
        double totalAttack = 0;
        if (inventory.getItems().containsKey("midnight_armour")) {
            for (Entity o: inventory.getItems().get("midnight_armour")) {
                MidnightArmour s = (MidnightArmour) o;
                totalAttack += s.getAttack();
            }
        }
        return totalAttack;
    }

    public double getMidnightArmourDefence() {
        double totalDefence = 0;
        if (inventory.getItems().containsKey("midnight_armour")) {
            for (Entity o: inventory.getItems().get("midnight_armour")) {
                MidnightArmour s = (MidnightArmour) o;
                totalDefence += s.getDefence();
            }
        }
        return totalDefence;
    }

    public void useSceptre() {
        if (inventory.getItems().containsKey("sceptre")) {
            for (Entity o: inventory.getItems().get("sceptre")) {
                Sceptre s = (Sceptre) o;
                s.useItem();
            }
        }
    }

    public List<ItemResponse> getWeaponaryUsed() {
        List<ItemResponse> l = new ArrayList<ItemResponse>();

        if (inventory.getItems().containsKey("sword")) {
            for (Entity o: inventory.getItems().get("sword")) {
                Sword s = (Sword) o;
                if (!s.isBroken()) {
                    l.add(new ItemResponse(s.getId(), s.getType()));
                }
            }
        }

        if (inventory.getItems().containsKey("bow")) {
            for (Entity o: inventory.getItems().get("bow")) {
                Bow b = (Bow) o;
                if (!b.isBroken()) {
                    l.add(new ItemResponse(b.getId(), b.getType()));
                }
            }
        }

        if (inventory.getItems().containsKey("shield")) {
            for (Entity o: inventory.getItems().get("shield")) {
                Shield s = (Shield) o;
                if (!s.isBroken()) {
                    l.add(new ItemResponse(s.getId(), s.getType()));
                }
            }
        }

        if (inventory.getItems().containsKey("sceptre")) {
            for (Entity o: inventory.getItems().get("sceptre")) {
                Sceptre s = (Sceptre) o;
                if (!s.isBroken()) {
                    l.add(new ItemResponse(s.getId(), s.getType()));
                }
            }
        }

        if (inventory.getItems().containsKey("midnight_armour")) {
            for (Entity o: inventory.getItems().get("midnight_armour")) {
                MidnightArmour m = (MidnightArmour) o;
                if (!m.isBroken()) {
                    l.add(new ItemResponse(m.getId(), m.getType()));
                }
            }
        }

        return l;
    }

    public SwampTile getSwampTileAtPos(Position pos) {
        if (getEntitiesInCell(pos.getX(), pos.getY()) != null) {
            for (Entity o: getEntitiesInCell(pos.getX(), pos.getY())) {
                if (Objects.equals(o.getType(), "swamp_tile")) {
                    return (SwampTile) o;
                }
            }
        }

        return null;
    }

    // Recreates moving entities that can be removed from the game
    public void createEntity(String type, Position p, Config configVariables) {
       
        if (Objects.equals(type, "spider")) {
            new Spider(type, p, Double.valueOf(configVariables.getSpiderHealth()), Double.valueOf(configVariables.getSpiderAttack()));
        } else if (Objects.equals(type, "zombie_toast")) {
            new ZombieToast(type, p, Double.valueOf(configVariables.getZombieHealth()), Double.valueOf(configVariables.getZombieAttack()));
        } else if (Objects.equals(type, "mercenary")) {
            new Mercenary(type, p, Double.valueOf(configVariables.getMercenaryHealth()),
                Double.valueOf(configVariables.getMercenaryAttack()), configVariables.getBribeAmount(),
                Double.valueOf(configVariables.getBribeRadius()));
        } else if (Objects.equals(type, "assassin")) {
            new Assassin(type, p, Double.valueOf(configVariables.getAssassinHealth()), 
                Double.valueOf(configVariables.getAssassinAttack()), configVariables.getAssassinBribeAmount(), 
                Double.valueOf(configVariables.getBribeRadius()), Double.valueOf(configVariables.getAssassinBribeFailRate()), configVariables.getAssassinReconRadius());
        } else if (Objects.equals(type, "hydra")) {
            new Hydra(type, p, Double.valueOf(configVariables.getHydraHealth()), 
                Double.valueOf(configVariables.getHydraAttack()), Double.valueOf(configVariables.getHydraHealthIncreaseRate()), configVariables.getHydraHealthIncreaseAmount());
        } else if (type.equals("zombie_toast_spawner")) {
            new ZombieToastSpawner(type, p);
        } else if (type.equals("treasure")) {
            new Treasure(type, p);
        } else if (type.equals("invincibility_potion")) {
            new InvincibilityPotion(type, p);
        } else if (type.equals("invisibility_potion")) {
            new InvisibilityPotion(type, p);
        } else if (type.equals("wood")) {
            new Wood(type, p);
        } else if (type.equals("arrow")) {
            new Arrows(type, p);

        } else if (type.equals("sword")) {
            new Sword(type, p, Double.valueOf(configVariables.getSwordDurability()), Double.valueOf(configVariables.getSwordAttack()));
        } else if (type.equals("sun_stone")) {
            new SunStone(type, p);
        } else if (type.equals("time_turner")) {
            new TimeTurner(type, p);
        }
        
    }

    public void updateEntitiesPosition(String id, Position p) {
        for (String s: entities.keySet()) {
            for (Entity en: entities.get(s)) {
                if (Objects.equals(en.getId(), id)) {
                    if (en.getType().equals("mercenary")) {
                        Mercenary m = (Mercenary) en;
                        m.setPosition(p);
                    } else if (en.getType().equals("spider")) {
                        Spider sp = (Spider) en;
                        sp.setPosition(p);      

                    } else if (en.getType().equals("zombie_toast")) {
                        ZombieToast zom = (ZombieToast) en;
                        zom.setPosition(p);       

                    } else if (en.getType().equals("assassin")) {
                        Assassin a = (Assassin) en;
                        a.setPosition(p); 

                    } else if (en.getType().equals("hydra")) {
                        Hydra h = (Hydra) en;
                        h.setPosition(p);    
                    }
                }
            }
        }
        
    }
    private Map<String, List<Position>> trackposition = new HashMap<String, List<Position>>();
    private Map<Integer, GameState> State = new HashMap<Integer, GameState>();
    private GameState changedState;
    private int tickDiff;
    private int tickincrement;
    private List<Entity> RemovedEntityL = new ArrayList<Entity>();
   
    private List<Position> path = new ArrayList<Position>();
    private int tickcount;

    public void setTickcount(int tickcount) {
        this.tickcount = tickcount;
    }

    private boolean isMovingEntity(Entity e) {
        if (e.getType().equals("mercenary") ||
            e.getType().equals("spider") ||
            e.getType().equals("zombie_toast") ||
            e.getType().equals("assassin") ||
            e.getType().equals("hydra")
        ) return true;

        return false;

    }
    public void restoreDeadEntity(MovingEntity m) {
        String type = m.getType();
        if (m.getHealth() <= 0) {
            if (Objects.equals(type, "spider")) {
                m.setHealth(Double.valueOf(configVariables.getSpiderHealth()));
            } else if (Objects.equals(type, "zombie_toast")) {
                m.setHealth(Double.valueOf(configVariables.getZombieHealth()));
            } else if (Objects.equals(type, "mercenary")) {
                m.setHealth(Double.valueOf(configVariables.getMercenaryHealth()));
            } else if (Objects.equals(type, "assassin")) {
                m.setHealth(Double.valueOf(configVariables.getAssassinHealth()));  
            } else if (Objects.equals(type, "hydra")) {
                m.setHealth( Double.valueOf(configVariables.getHydraAttack()));
            }
    
        }
        
    }
    private void removeEntities(List<Entity> eList) {
        for(Entity e : eList) {
            this.removeEntity(e);
        }
    }

    public void storePosition(Entity e) {
        if (!trackposition.containsKey(e.getId())) {
            List<Position> l = new ArrayList<Position>();
            l.add(e.getPosition());
            this.trackposition.put(e.getId(), l);
        } else {
            this.trackposition.get(e.getId()).add(e.getPosition());
        }
    }


    public void OlderPlayerStatus() {
        if (entities.containsKey("older_player")) {
            OlderPlayer op = (OlderPlayer) getOlderPlayer();
            if (op.hasReachedDestination()) {
                this.removeEntity(op);
            }
        }

    }

    public void updateLogicEntities() {
        List<Entity> entitiesToRemove = new ArrayList<Entity>();

        for (String s: entities.keySet()) {
            for (Entity o: entities.get(s)) {
                if (Objects.equals(s, "bomb")) {
                    Bomb b = (Bomb) o;
                    if (b.update(this, (Player) getPlayer())) {
                        entitiesToRemove.add(o);

                        List<Entity> temp = BombHelper.getSquareRadiusEntities(this, b.getPosition(), configVariables.getBombRadius());

                        for (Entity e: temp) {
                            if (!entitiesToRemove.contains(e) && !Objects.equals(e.getType(), "player")) {
                                entitiesToRemove.add(e);
                            }
                        }  
                    } 
                } else if (Objects.equals(s, "light_bulb_off") || Objects.equals(s, "light_bulb_on")) {
                    LightBulb l = (LightBulb) o;
                    l.update(this);
                } else if (Objects.equals(s, "switch_door")) {
                    SwitchDoor sw = (SwitchDoor) o;
                    sw.update(this);
                }
            }
        }

        Map<String, List<Entity>> entitiesCopy = new HashMap<String, List<Entity>>();

        for (String s: entities.keySet()) {
            List<Entity> l = new ArrayList<Entity>();

            for (Entity o: entities.get(s)) {
                if (!entitiesToRemove.contains(o)) {
                    l.add(o);
                }
            }

            entitiesCopy.put(s, l);
        }

        entities = entitiesCopy;
    }
    
    public Map<String, List<Position>> getEntitiesPos() {
        return this.trackposition;
    }
    public void cleartrack() {
        this.trackposition.clear();
    }

    public void rewind(int ticks) {
        
        tickDiff = tickcount - ticks;
    
        if (tickDiff <= 0) {
            tickcount = 0;
            changedState = State.get(tickcount);
        } else {
            tickcount = tickDiff;
            changedState = State.get(tickcount);
        }
        if (tickcount - 1 < 0) {
            tickincrement = 0;
        } else {
            tickincrement = tickcount;
        }
            
       
        Map<String, List<Entity>> entitiesInChangedState = changedState.getEntities();
        
        Map<String, List<Position>> entitymovementHistory = this.getEntitiesPos();
        Map<String, List<Entity>> entitiesL = this.getEntities();

        //System.out.println(entitiesInChangedState);
        
        for(String str : entitiesInChangedState.keySet()) {
            for(Entity en : entitiesInChangedState.get(str)) {

                // If entity type no longer exist in the current Game State
                if (entitiesL.get(str) == null) {
                    if (isMovingEntity(en)) {
                        this.createEntity(en.getType(),en.getPosition(), configVariables);
                        this.addEntity(en);
                        this.restoreDeadEntity((MovingEntity)en);
                        this.updateEntitiesPosition(en.getId(), entitymovementHistory.get(en.getId()).get(tickincrement));
                    } else {
                        this.createEntity(en.getType(),en.getPosition(), configVariables);
                        this.addEntity(en);
                    }  
                 // If entity type does exist, but the specific entity does not
                } else if (!entitiesL.get(str).contains(en)) {
                    this.createEntity(en.getType(),en.getPosition(), configVariables);
                    this.addEntity(en);
                // If it does exist update their positions - moving entities
                } else {
                    if (isMovingEntity(en)) {
                        this.updateEntitiesPosition(en.getId(), entitymovementHistory.get(en.getId()).get(tickincrement)); 
                    }
                }
                    
            }
        }
 
        for(String str : entitiesL.keySet()) {   
            for(Entity en : entitiesL.get(str)) {
                // If entity type no longer exist in the prev Game State
                // Add it to removed entity list
                if (!entitiesInChangedState.get(str).contains(en)) {
                    RemovedEntityL.add(en);
                    
                }       
            }
            
        }
        // Remove all the entity that no longer exists
        removeEntities(RemovedEntityL);

        Player p = (Player) this.getPlayer();

        
        path =  entitymovementHistory.get(p.getId());
        
        if (tickincrement + 1 < path.size()) {
            List<Position> OpPath = new ArrayList<>(path.subList(tickincrement+1, path.size()));
            // Create the older player
            // Make sure to create it only when it does not exist
            if (!this.getEntities().containsKey("older_player")) {
                
                OlderPlayer op = new OlderPlayer("older_player", 
                    entitymovementHistory.get(p.getId()).get(tickincrement), 
                    (double) p.getHealth(), (double) p.getAttack(), OpPath);
                this.addEntity(op);
            }
        }
 

                
    }

 
    
    public Map<Integer, GameState> getState() {
        return State;
    }

    public List<Entity> getRemovedEntityL() {
        return RemovedEntityL;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public String getConfigName() {
        return configName;
    }

    public String getId() {
        return id;
    }

    public String goalsToString() {
        return goals.toString(this);
    }

    public Map<String, List<Entity>> getEntities() {
        return entities;
    }

    public Entity getPlayer() {
        return entities.get("player").get(0);
    }

    public List<Entity> getPortals() {
        return entities.get("portal");
    } 

    public List<Entity> getBombsEntities() {
        return entities.get("bomb");
    } 

    public List<Entity> getSwitchEntities() {
        return entities.get("switch");
    }

    public List<Entity> getSwampTileEntities() {
        return entities.get("swamp_tile");
    }

    public Entity getOlderPlayer() {
        return entities.get("older_player").get(0);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Config getConfigVariables() {
        return configVariables;
    }

    public Status getStatus(){
        return status;
    }

    public List<BattleResponse> getBattles() {
        return battles;
    }

    public int getTreasureCollected() {
        return treasureCollected;
    }

    public int getEnemiesDestroyed() {
        return enemiesDestroyed;
    }

    public void SetEnemiesDestroyed(int val) {
        enemiesDestroyed = val;
    }
}
