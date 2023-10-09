package dungeonmania.entities.collectableEntities;

import dungeonmania.Game;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;

public class Key extends CollectableEntity {
    private int key;

    public Key(String type, Position pos, int key) {
        super(type, pos);
        this.key = key;
    }

    public int getKey() {
        return key;
    }


    @Override
    public void collideWithPlayer(Player p, Game g) {
        // Collect if theres no key inventory already
        if (!g.getInventory().hasKey()) {
            p.collectItem(this, g);
        }

    }

    public void useItem(Player p, Game g) {
        g.getInventory().removeEntity(this);
    }
}
