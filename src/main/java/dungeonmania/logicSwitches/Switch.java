package dungeonmania.logicSwitches;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.staticEntities.FloorSwitch;
import dungeonmania.entities.staticEntities.Wire;

public abstract class Switch implements Serializable { 
    public abstract boolean isActivated(Game g, Entity e);

    public int isConnected(Game g, List<Entity> entitesInPos) {
        boolean hasActiveSwitch = false;
        boolean hasOffSwitch = false;
        boolean hasActiveWire = false;
        boolean hasOffWire = false;

        for (Entity o: entitesInPos) {
            if (Objects.equals(o.getType(), "switch")) {
                FloorSwitch f = (FloorSwitch) o;
                if (f.getStatus(g)) {
                    hasActiveSwitch = true; 
                } else {
                    hasOffSwitch = true;
                }
            } else if (Objects.equals(o.getType(), "wire")) {
                Wire w = (Wire) o;
                if (w.getStatus(g)) {
                    hasActiveWire = true;
                } else {
                    hasOffWire = true;
                }
            } 
        }

        if (hasActiveSwitch || hasActiveWire) {
            return 1;
        } else if (hasOffWire || hasOffSwitch) {
            return 2;
        } else {
            return 0;
        }
    }
}
