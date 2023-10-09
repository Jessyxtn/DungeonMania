package dungeonmania.entities.staticEntities;

import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.entities.collectableEntities.Key;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.logicSwitches.ANDSwitch;
import dungeonmania.logicSwitches.CO_ANDSwitch;
import dungeonmania.logicSwitches.ORSwitch;
import dungeonmania.logicSwitches.Switch;
import dungeonmania.logicSwitches.XORSwitch;
import dungeonmania.util.Position;

public class SwitchDoor extends StaticEntity {
    private int key;
    private boolean isLocked = true;
    private Switch logicGate;
    private boolean openedWithKey = false;
    
    public SwitchDoor(String type, Position pos, int key, String logic) {
        super(type, pos);
        this.key = key;

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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void collideWithPlayer(Player p, Game g) {

        if (this.isLocked == true && g.getInventory().hasKey()) {
            Key k = (Key) g.getInventory().getKey();

            if (k.getKey() == this.key) {
                this.setLocked(false);
                k.useItem(p, g);
                openedWithKey = true;
            }

        } else if (this.isLocked == true && g.getInventory().hasSunStone()) {
            this.setLocked(false);
            openedWithKey = true;
        }
    }

    public void update(Game g) {
        if (!openedWithKey) {
            if (logicGate.isActivated(g, this)) {
                this.setLocked(false);
            } else {
                this.setLocked(true);
            }
        }
    }

}
