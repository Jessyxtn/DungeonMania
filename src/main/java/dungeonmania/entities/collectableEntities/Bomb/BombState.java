package dungeonmania.entities.collectableEntities.Bomb;

import java.io.Serializable;

import dungeonmania.Game;
import dungeonmania.entities.movingEntities.Player;

public interface BombState extends Serializable {
    public void collideWithPlayer(Player p, Game g);
    public void useItem(Player p, Game g);
    public void checkAdjacent(Game g);
}
