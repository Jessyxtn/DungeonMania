package dungeonmania.logicSwitches;

import dungeonmania.Game;
import dungeonmania.entities.Entity;

public class ORSwitch extends Switch {

    public boolean isActivated(Game g, Entity e) { 
        boolean rightConnected = false;
        boolean upConnected = false;
        boolean leftConnected = false;
        boolean downConnected = false;

        if (isConnected(g, g.getEntitiesInCell(e.getPosition().getX() + 1, e.getPosition().getY())) == 1) {
            rightConnected = true;
        }

        if (isConnected(g, g.getEntitiesInCell(e.getPosition().getX(), e.getPosition().getY() + 1)) == 1) {
            upConnected = true;
        }

        if (isConnected(g, g.getEntitiesInCell(e.getPosition().getX() - 1, e.getPosition().getY())) == 1) {
            leftConnected = true;
        }

        if (isConnected(g, g.getEntitiesInCell(e.getPosition().getX(), e.getPosition().getY() - 1)) == 1) {
            downConnected = true;
        }

        return (rightConnected || upConnected || leftConnected || downConnected);
    }


}
