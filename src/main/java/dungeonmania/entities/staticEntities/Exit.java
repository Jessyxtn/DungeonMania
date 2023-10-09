package dungeonmania.entities.staticEntities;

import dungeonmania.util.Position;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.Game;

public class Exit extends StaticEntity {
    private boolean achieved = false;

    public Exit(String type, Position pos) {
        super(type, pos);
    }

    public boolean exitAcheived() {
        return achieved;
    }

    public void collideWithPlayer(Player p, Game g) {
        if (p.getPosition().getX() == this.getPosition().getX() && 
            p.getPosition().getY() == this.getPosition().getY()) {
            this.achieved = true;
        }
    }
}
