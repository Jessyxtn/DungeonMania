package dungeonmania.logicSwitches;

import dungeonmania.Game;
import dungeonmania.entities.Entity;

public class CO_ANDSwitch extends Switch {
    private int previousTick = 0;
    private boolean isActive = false;
    
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

        if (numSatisfied < 2) {
            isActive = false;
            return isActive;
        } else if (!isActive) {
            boolean activate = (numSatisfied - previousTick >= 2);
            if (activate) {
                isActive = true;
            }

            return activate;
        } else {
            return isActive;
        }
    }
}
