package dungeonmania.entities.collectableEntities;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.OlderPlayer;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;

public class CollectableEntity extends Entity {

    public CollectableEntity(String type, Position pos) {
        super(type);
        this.pos = pos;
    }

    public void collideWithPlayer(Player p, Game g) {
        p.collectItem(this, g);
        g.removeEntity(this);
    }
    public void collideWithOlderPlayer(OlderPlayer op, Game g) {
        op.collectItem(this, g);
        g.removeEntity(this);
    }
    
    public Position getPosition() {
        return pos;
    }

    public void setPosition(Position pos) {
        super.setPosition(pos);
    }

}
