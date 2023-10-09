package dungeonmania.entities.staticEntities;

import dungeonmania.Game;
import dungeonmania.entities.collectableEntities.Key;
import dungeonmania.entities.collectableEntities.SunStone;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int key;
    private boolean isLocked = true;
    
    public Door(String type, Position pos, int key) {
        super(type, pos);
        this.key = key;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void collideWithPlayer(Player p, Game g) {

        if (this.isLocked == true && g.getInventory().hasKey()) {
            Key k = (Key) g.getInventory().getKey();

            if (k.getKey() == this.key) {
                this.setLocked(false);
                k.useItem(p, g);
            }

        } else if (this.isLocked == true && g.getInventory().hasSunStone()) {
            this.setLocked(false);
        }
    }

}
