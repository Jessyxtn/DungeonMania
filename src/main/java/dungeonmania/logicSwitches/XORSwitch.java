package dungeonmania.logicSwitches;

import dungeonmania.Game;
import dungeonmania.entities.Entity;

public class XORSwitch extends Switch {

    public boolean isActivated(Game g, Entity e) { 
        int numSatisfied = 0;

        if (isConnected(g, g.getEntitiesInCell(e.getPosition().getX() + 1, e.getPosition().getY())) == 1) {
            numSatisfied++;
        }

        if (isConnected(g, g.getEntitiesInCell(e.getPosition().getX(), e.getPosition().getY() + 1)) == 1) {
            numSatisfied++;
        }

        if (isConnected(g, g.getEntitiesInCell(e.getPosition().getX() - 1, e.getPosition().getY())) == 1) {
            numSatisfied++;
        }

        if (isConnected(g, g.getEntitiesInCell(e.getPosition().getX(), e.getPosition().getY() - 1)) == 1) {
            numSatisfied++;
        }

        return (numSatisfied == 1);
    }
}
