package dungeonmania.entities.collectableEntities;

import dungeonmania.Game;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;


public class InvincibilityPotion extends CollectableEntity {

    public InvincibilityPotion(String type, Position pos) {
        super(type, pos);
    }
    
    @Override
    public void useItem(Player p, Game g) {
        g.getInventory().useInvincibilityPotion(g);
 
    }

}
