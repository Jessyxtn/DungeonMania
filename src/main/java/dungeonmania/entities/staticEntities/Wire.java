package dungeonmania.entities.staticEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Wire extends StaticEntity {

    public Wire(String type, Position pos) {
        super(type, pos);
    }

    public boolean getStatus(Game g) {
        List<Entity> entitesInPos = new ArrayList<Entity>();
        boolean leftConnected = false;
        boolean upConnected = false;
        boolean rightConnected = false;
        boolean downConnected = false;

        boolean hasActiveSwitch = false;
        boolean hasActiveWire = false;

        entitesInPos = g.getEntitiesInCell(this.getPosition().getX() - 1, this.getPosition().getY());

        for (Entity o: entitesInPos) {
            if (Objects.equals(o.getType(), "switch")) {
                FloorSwitch f = (FloorSwitch) o;
                if (f.getStatus(g)) {
                    hasActiveSwitch = true; 
                }
            } else if (Objects.equals(o.getType(), "wire")) {
                Wire w = (Wire) o;
                if (w.getStatus(g)) {
                    hasActiveWire = true;
                }
            }
        }

        leftConnected = (hasActiveSwitch || hasActiveWire);

        hasActiveSwitch = false;
        hasActiveWire = false;

        entitesInPos = g.getEntitiesInCell(this.getPosition().getX(), this.getPosition().getY() + 1);

        for (Entity o: entitesInPos) {
            if (Objects.equals(o.getType(), "switch")) {
                FloorSwitch f = (FloorSwitch) o;
                if (f.getStatus(g)) {
                    hasActiveSwitch = true; 
                }
            } else if (Objects.equals(o.getType(), "wire")) {
                Wire w = (Wire) o;
                if (w.getStatus(g)) {
                    hasActiveWire = true;
                }
            }
        }

        upConnected = (hasActiveSwitch || hasActiveWire);

        hasActiveSwitch = false;
        hasActiveWire = false;

        entitesInPos = g.getEntitiesInCell(this.getPosition().getX() + 1, this.getPosition().getY());

        for (Entity o: entitesInPos) {
            if (Objects.equals(o.getType(), "switch")) {
                FloorSwitch f = (FloorSwitch) o;
                if (f.getStatus(g)) {
                    hasActiveSwitch = true; 
                }
            } else if (Objects.equals(o.getType(), "wire")) {
                Wire w = (Wire) o;
                if (w.getStatus(g)) {
                    hasActiveWire = true;
                }
            }
        }

        rightConnected = (hasActiveSwitch || hasActiveWire);

        hasActiveSwitch = false;
        hasActiveWire = false;

        entitesInPos = g.getEntitiesInCell(this.getPosition().getX(), this.getPosition().getY() - 1);

        for (Entity o: entitesInPos) {
            if (Objects.equals(o.getType(), "switch")) {
                FloorSwitch f = (FloorSwitch) o;
                if (f.getStatus(g)) {
                    hasActiveSwitch = true; 
                }
            } else if (Objects.equals(o.getType(), "wire")) {
                Wire w = (Wire) o;
                if (w.getStatus(g)) {
                    hasActiveWire = true;
                }
            }
        }

        downConnected = (hasActiveSwitch || hasActiveWire);

        return (upConnected || leftConnected || rightConnected || downConnected);
    }
}
