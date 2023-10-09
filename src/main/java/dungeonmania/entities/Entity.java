package dungeonmania.entities;

import java.io.Serializable;
import java.util.UUID;

import dungeonmania.Game;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;

public class Entity implements Serializable {
    protected String id;
    protected String type;
    public boolean isInteractable = false;

    public Position pos;

    public Entity(String type) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsInteractable() {
        return isInteractable;
    }

    public Position getPosition() {
        return pos;
    }

    public void setPosition(Position pos) {
        this.pos = pos;
    }    
    
    public void setInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public boolean isAlive() {
        return true;
    }
    public void useItem(Player p, Game g) {
    }


}
