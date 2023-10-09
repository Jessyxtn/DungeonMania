package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.entities.Entity;


public class GameState implements Serializable {

    private Map<String, List<Entity>> entities = new HashMap<String, List<Entity>>();
  

    
    public GameState(Map<String, List<Entity>> entities) {
        this.entities = entities;
    }

    public Map<String, List<Entity>> getEntities() {
        return entities;
    }

    public Entity getPlayer() {
        return entities.get("player").get(0);
    }

   
}
