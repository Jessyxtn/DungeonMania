package dungeonmania.entities.staticEntities;

import java.util.List;
import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.logicSwitches.ANDSwitch;
import dungeonmania.logicSwitches.CO_ANDSwitch;
import dungeonmania.logicSwitches.ORSwitch;
import dungeonmania.logicSwitches.Switch;
import dungeonmania.logicSwitches.XORSwitch;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private Boolean isOn = false;
    private Switch logicGate;

    public FloorSwitch(String type, Position pos, String logic) {
        super(type, pos);

        if (Objects.equals(logic, "AND")) {
            logicGate = new ANDSwitch();
        } else if (Objects.equals(logic, "OR")) {
            logicGate = new ORSwitch();
        } else if (Objects.equals(logic, "XOR")) {
            logicGate = new XORSwitch();
        } else if (Objects.equals(logic, "CO_AND")) {
            logicGate = new CO_ANDSwitch();
        } else if (Objects.equals(logic, "NONE")) {
            logicGate = null;
        }
    }

    public void turnOn() {
        this.isOn = true;
    }

    public void turnOff() {
        this.isOn = false;
    }

    public boolean getStatus(Game g) {
        if (logicGate != null) {
            return (logicGate.isActivated(g, this) || isOn);
        } else {
            return isOn;
        }
    }

    public void turnSwitch(List<Entity> entitiesOnTop) {
        for (Entity n : entitiesOnTop) {
            if (Objects.equals(n.getType(), "boulder")) {
                this.turnOn();
                break;                    
            } else {
                this.turnOff();
            }
        }
    }
}
