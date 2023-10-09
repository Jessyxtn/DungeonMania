package dungeonmania.entities.collectableEntities.Bomb;

import java.util.List;
import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.entities.staticEntities.FloorSwitch;

public class UsedBombInactive implements BombState {
    Bomb b;

    public UsedBombInactive(Bomb bomb) {
        this.b = bomb;
    }

    @Override
    public void collideWithPlayer(Player p, Game g) {
        // should act like a wall
    }


    @Override
    public void useItem(Player p, Game g) {
        
    }


    @Override
    public void checkAdjacent(Game g) {
        List<Entity> adjacentEntities = BombHelper.getCardinalAdjacentEntities(g, b.getPosition());
        for (Entity o : adjacentEntities) {
            if (Objects.equals(o.getType(), "switch") ) {
                FloorSwitch fs = (FloorSwitch) o;
                if (fs.getStatus(g)) {
                    b.setHasDetonate(true);                    
                    detonate(g);
                }
            }
        }        
    }

    private void detonate(Game g) {
        List<Entity> adjacentEntities = BombHelper.getSquareRadiusEntities(g, this.b.getPosition(), this.b.getRadius());
        for (Entity o : adjacentEntities) {
            if (!Objects.equals(o.getType(), "player")) {
                g.removeEntity(o);
            }
        }
    }
}
