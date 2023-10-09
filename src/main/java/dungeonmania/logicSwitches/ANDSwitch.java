package dungeonmania.logicSwitches;

import dungeonmania.Game;
import dungeonmania.entities.Entity;

public class ANDSwitch extends Switch {

    public boolean isActivated(Game g, Entity e) { 
        int numSatisfied = 0;
        int res;

        res = isConnected(g, g.getEntitiesInCell(e.getPosition().getX() + 1, e.getPosition().getY()));

        if (res == 2) {
            return false;
        } else if (res == 1) {
            numSatisfied++;
        } 

        res = isConnected(g, g.getEntitiesInCell(e.getPosition().getX(), e.getPosition().getY() + 1));

        if (res == 2) {
            return false;
        } else if (res == 1) {
            numSatisfied++;
        } 
        
        res = isConnected(g, g.getEntitiesInCell(e.getPosition().getX() - 1, e.getPosition().getY()));

        if (res == 2) {
            return false;
        } else if (res == 1) {
            numSatisfied++;
        } 

        res = isConnected(g, g.getEntitiesInCell(e.getPosition().getX(), e.getPosition().getY() - 1));

        if (res == 2) {
            return false;
        } else if (res == 1) {
            numSatisfied++;
        } 

        return (numSatisfied >= 2);
    }


}
