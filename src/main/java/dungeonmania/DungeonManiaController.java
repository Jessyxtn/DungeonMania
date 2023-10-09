package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.AllyAssassin;
import dungeonmania.entities.movingEntities.AllyMercenary;
import dungeonmania.entities.movingEntities.Assassin;
import dungeonmania.entities.movingEntities.Mercenary;
import dungeonmania.entities.movingEntities.OlderPlayer;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.entities.staticEntities.ZombieToastSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.PathGenerator;
import dungeonmania.util.Position;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import org.json.JSONObject;


public class DungeonManiaController {
    private Map<String, Game> games = new HashMap<String, Game>();
    private GameState gs;
    private ArrayList<Game> gamesL = new ArrayList<Game>();
    private Game g;
    private DungeonResponse d;
    private Random rand = new Random();

        
    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    private int tickcount;

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        String dungeonContent;
        String configContent;

        try {
            dungeonContent = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
        } catch (Exception exception) {
            throw new IllegalArgumentException("DungeonFile does not exist");
        }
        
        try {
            configContent = FileLoader.loadResourceFile("/configs/" + configName + ".json");
        } catch (Exception exception) {
            throw new IllegalArgumentException("ConfigsFile does not exist");
        }
        
        g = new Game(dungeonName, configName, dungeonContent, configContent);
        // New Game should reset all the tick count and increment and clear all state and tracking
        this.tickcount = 0;
        g.getState().clear();
        g.cleartrack();
        g.getRemovedEntityL().clear();
        gs = new GameState(g.getEntities());
        g.getState().put(tickcount, gs);
        g.setTickcount(tickcount);
        updateGameStatePos();

        d = new DungeonResponse(g.getId(), g.getDungeonName(), g.getEntityResponse(), g.getInventory().getItemResponses(), g.getBattles() , g.getInventory().getBuildables(g), g.goalsToString());
         

        return d;
    }

    /**
     * /game/GameState
     */
    private void updateGameState() {
        gs = new GameState(g.getEntities());     
    }
    private void updateGameStatePos() {
        Map<String, List<Entity>> entitiesL = g.getEntities();
        for (String str : entitiesL.keySet()) {
            for (Entity en : entitiesL.get(str)) {
                    g.storePosition(en);   
                }
            }   
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        d = new DungeonResponse(g.getId(), g.getDungeonName(), g.getEntityResponse(), g.getInventory().getItemResponses(), g.getBattles() , g.getInventory().getBuildables(g), g.goalsToString());
        return d; 
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        // If item is in the inventory
        Entity item = g.getInventory().findItem(itemUsedId);
        if (item != null) {
            if (item.getType().equals("bomb") || 
            item.getType().equals("invincibility_potion") ||
            item.getType().equals("invisibility_potion")) {
                Player p = (Player) g.getPlayer();
                p.playerUseItem(g, itemUsedId);
                g.updateBombs();
                g.getStatus().updateDuration(g);
                g.updateMovingEntites(p, g);
                g.useSceptre();
                g.OlderPlayerStatus();

                d = getDungeonResponseModel();
                // Spawn spider and zombies
                g.spawnSpiderAndZoms(tickcount);             
                this.tickcount++;
                updateGameState();
                updateGameStatePos();
                g.getState().put(tickcount, gs);
                g.setTickcount(tickcount);
                return d; 
            } else {
                throw new IllegalArgumentException("Id is not a bomb or potion"); 
            }
        }
        throw new InvalidActionException("Item was not found in inventory");
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        g.movePlayer(movementDirection);
        g.removeDeadEntities();
        g.getInventory().updateInventory(g);
        g.getStatus().updateDuration(g);
        g.useSceptre();
        g.OlderPlayerStatus();
        
        d = getDungeonResponseModel();
        
        // Spawn spider and zombie/s
        g.spawnSpiderAndZoms(tickcount); 
        this.tickcount++;
        updateGameState();
        updateGameStatePos();
        g.getState().put(tickcount, gs);
        g.setTickcount(tickcount);
        return d;    
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        if (Objects.equals(buildable,"bow")) {
            int previousNumBows;

            if (g.getInventory().getItems().containsKey("bow")) {
                previousNumBows = g.getInventory().getItems().get("bow").size();
            } else {
                previousNumBows = 0;
            }
             
            g.getInventory().buildBow(g);

            int currentNumBows;

            if (g.getInventory().getItems().containsKey("bow")) {
                currentNumBows = g.getInventory().getItems().get("bow").size();
            } else {
                currentNumBows = 0;
            }

            if (previousNumBows + 1 != currentNumBows) {
                throw new InvalidActionException("Not enough materials to build bow");
            }

            d = getDungeonResponseModel();
            return d;
        } else if (Objects.equals(buildable,"sceptre")) {
            int previousNumSceptres;

            if (g.getInventory().getItems().containsKey("sceptre")) {
                previousNumSceptres = g.getInventory().getItems().get("sceptre").size();
            } else {
                previousNumSceptres = 0;
            }
             
            g.getInventory().buildShield(g);

            int currentNumSceptres;

            if (g.getInventory().getItems().containsKey("sceptre")) {
                currentNumSceptres = g.getInventory().getItems().get("sceptre").size();
            } else {
                currentNumSceptres = 0;
            }

            if (previousNumSceptres + 1 != currentNumSceptres) {
                throw new InvalidActionException("Not enough materials to build sceptre");
            }

            d = getDungeonResponseModel();
            return d;
        } else if (Objects.equals(buildable,"shield")) {
            int previousNumShields;

            if (g.getInventory().getItems().containsKey("shield")) {
                previousNumShields = g.getInventory().getItems().get("shield").size();
            } else {
                previousNumShields = 0;
            }
             
            g.getInventory().buildShield(g);

            int currentNumShields;

            if (g.getInventory().getItems().containsKey("shield")) {
                currentNumShields = g.getInventory().getItems().get("shield").size();
            } else {
                currentNumShields = 0;
            }

            if (previousNumShields + 1 != currentNumShields) {
                throw new InvalidActionException("Not enough materials to build shield");
            }
            
            d = getDungeonResponseModel();
            return d;
        } else if (Objects.equals(buildable,"midnight_armour")) {
            int previousNumMightnightArmour;

            if (g.getInventory().getItems().containsKey("midnight_armour")) {
                previousNumMightnightArmour = g.getInventory().getItems().get("midnight_armour").size();
            } else {
                previousNumMightnightArmour = 0;
            }
             
            g.getInventory().buildShield(g);

            int currentNumMidnightArmour;

            if (g.getInventory().getItems().containsKey("midnight_armour")) {
                currentNumMidnightArmour = g.getInventory().getItems().get("midnight_armour").size();
            } else {
                currentNumMidnightArmour = 0;
            }

            if (previousNumMightnightArmour + 1 != currentNumMidnightArmour) {
                throw new InvalidActionException("Not enough materials to build midnight_armour");
            }

            d = getDungeonResponseModel();
            return d;
        } else {
            throw new IllegalArgumentException("Buildable is not one of bow, shield, sceptre, or mightnight armour");
        }
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = null;

        for (String s: g.getEntities().keySet()) {
            for (Entity o: g.getEntities().get(s)) {
                if (Objects.equals(o.getId(), entityId)) {
                    e = o;
                }
            }
        }

        if (e == null) {
            throw new IllegalArgumentException("Entity does not exist");
        }

        Player p = (Player) g.getPlayer();

        if (Objects.equals(e.getType(), "mercenary")) {
            Mercenary m = (Mercenary) e;

            if (m.getIsAlly() == false) {
                Double distance = Position.calculateDistance(m.getPosition(), p.getPosition());

                if (g.getConfigVariables().getBribeRadius() < distance && g.getInventory().getItems().containsKey("sceptre") == false) {
                    throw new InvalidActionException("Not within radius to bribe");
                }

                if (g.getInventory().getItems().get("treasure").size() < g.getConfigVariables().getBribeAmount() && g.getInventory().getItems().containsKey("sceptre") == false) {
                    throw new InvalidActionException("Not enough treasure to bribe");
                }

                AllyMercenary ally = new AllyMercenary("mercenary", m.getPosition(), Double.valueOf(g.getConfigVariables().getAllyDefence()), Double.valueOf(g.getConfigVariables().getAllyAttack()), g.getConfigVariables().getBribeAmount(), Double.valueOf(g.getConfigVariables().getBribeRadius()), m.getId());
                g.getEntities().get("mercenary").remove(m);
                g.getEntities().get("mercenary").add(ally);

                for (int i = 0; i < g.getConfigVariables().getBribeAmount(); i++) {
                    g.getInventory().getItems().get("treasure").remove(0);
                }
            }
        } else if (Objects.equals(e.getType(), "zombie_toast_spawner")) {
            ZombieToastSpawner z = (ZombieToastSpawner) e;

            if (Position.isAdjacent(z.getPosition(), p.getPosition())) {
                if (g.getInventory().hasWeapon()) {
                    g.getEntities().get("zombie_toast_spawner").remove(z);
                    g.SetEnemiesDestroyed(g.getEnemiesDestroyed() + 1);
                } else {
                    throw new InvalidActionException("Player does not have weapon to destroy spawner");
                }
            } else {
                throw new InvalidActionException("Player is not adjacent to spawner");
            }
        } else if (Objects.equals(e.getType(), "assassin")) {
            Assassin a = (Assassin) e;

            if (a.getIsAlly() == false) {
                Double distance = Position.calculateDistance(a.getPosition(), p.getPosition());

                if (g.getConfigVariables().getBribeRadius() < distance) {
                    throw new InvalidActionException("Not within radius to bribe");
                }

                if (g.getInventory().getItems().get("treasure").size() < g.getConfigVariables().getAssassinBribeAmount()) {
                    throw new InvalidActionException("Not enough treasure to bribe");
                }

                AllyAssassin ally = new AllyAssassin("assassin", a.getPosition(), Double.valueOf(g.getConfigVariables().getAllyDefence()), Double.valueOf(g.getConfigVariables().getAllyAttack()), g.getConfigVariables().getAssassinBribeAmount(), Double.valueOf(g.getConfigVariables().getBribeRadius()), Double.valueOf(g.getConfigVariables().getAssassinBribeFailRate()), g.getConfigVariables().getAssassinReconRadius(), a.getId());
 
                double n = rand.nextDouble();

                if (n > g.getConfigVariables().getAssassinBribeFailRate()) {
                    g.getEntities().get("assassin").remove(a);
                    g.getEntities().get("assassin").add(ally);
                }

                for (int i = 0; i < g.getConfigVariables().getAssassinBribeAmount(); i++) {
                    g.getInventory().getItems().get("treasure").remove(0);
                }
            }
        }
     
        d = getDungeonResponseModel();

        return d;
    }
    
    public Game getGame() {
        return g;
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) {
        g.setName(name);
        gamesL.add(g);
        writeToFile();
        d = getDungeonResponseModel();
        return d;
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        List<String> gameNames = allGames();
        if (gameNames.contains(name)) {
            for (Game game: gamesL) {
                if (Objects.equals(game.getName(), name)) {
                    g = game;
                    break;
                }
            }
            d = getDungeonResponseModel();
            return d;
        } else {
            throw new IllegalArgumentException("Game id doesn't exist");
        }
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        loadFromFile();

        List<String> gameNames = new ArrayList<String>();
        for (Game game : gamesL) {
            if (game.getName() != null) {
                gameNames.add(game.getName());
            }
        }
        return gameNames;
    }

    public void writeToFile() {
        String pathToNewFile = null;
        try {
            pathToNewFile = PathGenerator.getPathForNewFile("persistence", "save.ser");
            FileOutputStream file = new FileOutputStream(pathToNewFile);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(gamesL);
            out.flush();
            out.close();
            file.close();
        } 
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void loadFromFile() {
        gamesL = new ArrayList<Game>();
        String pathToFile = null;
        try {
            pathToFile = PathGenerator.getPathForNewFile("persistence", "save.ser");
            FileInputStream file = new FileInputStream(pathToFile);
            ObjectInputStream in = new ObjectInputStream(file);
            gamesL = (ArrayList) in.readObject(); 
            in.close();
            file.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int gettick() {
        return tickcount;
    }

    
   
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException{
        
        if (ticks <= 0) {
            throw new IllegalArgumentException("Invalid number of ticks");
        }
        // Remove the time turner from inventory
        g.getInventory().getItems().get("time_turner").remove(0);
        g.rewind(ticks);
                       
        d = getDungeonResponseModel();
        return d;
    }

    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName) {
        String dungeonName;
        String dungeonContent;
        Position start = new Position(xStart, yStart);
        Position end = new Position(xEnd, yEnd);
        int width = Math.abs(xEnd - xStart) + 3;
        int height = Math.abs(yEnd - yStart) + 3;

        JSONObject config = new JSONObject();
        PrimsAlgorithm randomDungeon = new PrimsAlgorithm();
        config = randomDungeon.randomisedPrims(width, height, start, end);
        dungeonContent = config.toString();
        dungeonName = dungeonContent.substring(0, Math.min(dungeonContent.length(), 10));
        String configContent;

        try {
            configContent = FileLoader.loadResourceFile("/configs/" + configName + ".json");
        } catch (Exception exception) {
            throw new IllegalArgumentException("ConfigsFile does not exist");
        }
        
        g = new Game(dungeonName, configName, dungeonContent, configContent);
        d = new DungeonResponse(g.getId(), g.getDungeonName(), g.getEntityResponse(), g.getInventory().getItemResponses(), g.getBattles() , g.getInventory().getBuildables(g), g.goalsToString());

        return d;
    }


}
