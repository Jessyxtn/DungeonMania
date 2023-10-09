package dungeonmania.entities.collectableEntities;

import dungeonmania.Game;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity {
 
    public InvisibilityPotion(String type, Position pos) {
        super(type, pos);
    }
    
    @Override
    public void useItem(Player p, Game g) {
        g.getInventory().useInvisibilityPotion(g);
 
    }
}
