package dungeonmania.entities.collectableEntities.Bomb;


import dungeonmania.Game;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;

public class UnusedBomb implements BombState {
    Bomb b;
    
    public UnusedBomb(Bomb bomb) {
        this.b = bomb;
    }


    @Override
    public void collideWithPlayer(Player p, Game g) {
        p.collectItem(b, g);
        // deleted from dungeon map
        g.removeEntity(b);
    }


    @Override
    public void useItem(Player p, Game g) {
        // Put it down where the player is standing
        b.setPosition(new Position(p.getPosition().getX(), p.getPosition().getY()));
        g.addEntity(b);
        g.getInventory().removeEntity(b);
        b.setState(b.getUsedBombInactiveState(), g);
        b.setCanPass(false);
    }


    public void checkAdjacent(Game g) {
    }
}
