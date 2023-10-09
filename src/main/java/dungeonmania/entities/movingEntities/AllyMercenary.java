package dungeonmania.entities.movingEntities;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class AllyMercenary extends Mercenary {

    public AllyMercenary(String type, Position pos, Double health, Double attack, int bribe_amount, Double bribe_radius, String id) {
        super(type, pos, health, attack, bribe_amount, bribe_radius);
        this.isInteractable = true;
        this.isAlly = true;
        this.id = id;
    }

    public boolean canMoveTo(Position newPos, Game g) {
        List<Entity> entitesInCell = g.getEntitiesInCell(newPos.getX(), newPos.getY());

        boolean isValid = true;

        for (Entity o: entitesInCell) {
            if (o.getType() == "wall") {
                isValid = false;
            } 
        }

        return isValid;
    }

}
