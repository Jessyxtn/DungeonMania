package dungeonmania.entities.staticEntities;

import dungeonmania.util.Position;

import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.logicSwitches.ANDSwitch;
import dungeonmania.logicSwitches.CO_ANDSwitch;
import dungeonmania.logicSwitches.ORSwitch;
import dungeonmania.logicSwitches.Switch;
import dungeonmania.logicSwitches.XORSwitch;

public class LightBulb extends StaticEntity {
    private Switch logicGate;

    public LightBulb(String type, Position pos, String logic) {
        super(type, pos);

        if (Objects.equals(logic, "AND")) {
            logicGate = new ANDSwitch();
        } else if (Objects.equals(logic, "OR")) {
            logicGate = new ORSwitch();
        } else if (Objects.equals(logic, "XOR")) {
            logicGate = new XORSwitch();
        } else if (Objects.equals(logic, "CO_AND")) {
            logicGate = new CO_ANDSwitch();
        } 
    }

    public void update(Game g) {
        if (logicGate.isActivated(g, this)) {
            this.type = "light_bulb_on";
        } else {
            this.type = "light_bulb_off";
        }
    }
}
