package dungeonmania.entities.collectableEntities.Bomb;

import java.util.List;
import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectableEntities.CollectableEntity;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.logicSwitches.ANDSwitch;
import dungeonmania.logicSwitches.CO_ANDSwitch;
import dungeonmania.logicSwitches.ORSwitch;
import dungeonmania.logicSwitches.Switch;
import dungeonmania.logicSwitches.XORSwitch;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity {
    private BombState unusedBomb;
    private BombState usedBombInactive;
    private Switch logicGate;

    private BombState state;
    private boolean canPass = true;
    private boolean hasDetonate = false;

    private Double radius;

    public Bomb(String type, Position pos, Double radius, String logic) {
        super(type, pos);
        this.radius = radius;

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
        
        unusedBomb = new UnusedBomb(this);
        usedBombInactive = new UsedBombInactive(this);
        state = unusedBomb;
    }

    public boolean update(Game g, Player p) {
        if (logicGate != null) {
            if (logicGate.isActivated(g, this)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void collideWithPlayer(Player p, Game g) {
        state.collideWithPlayer(p, g);
    }

    public void useItem(Player p, Game g) {
        state.useItem(p, g);
    }

    public void checkAdjacent(Game g) {
        state.checkAdjacent(g);
    }

    public void setState(BombState state, Game g) {
        this.state = state;
        state.checkAdjacent(g);
    }

    public BombState getUnusedBombState() {
        return unusedBomb;
    }

    public BombState getUsedBombInactiveState() {
        return usedBombInactive;
    }

    public BombState getState() {
        return state;
    }

    public Double getRadius() {
        return radius;
    }

    public void setCanPass(boolean canPass) {
        this.canPass = canPass;
    }

    public boolean isCanPass() {
        return canPass;
    }
    public boolean isHasDetonate() {
        return hasDetonate;
    }

    public void setHasDetonate(boolean hasDetonate) {
        this.hasDetonate = hasDetonate;
    }

}
