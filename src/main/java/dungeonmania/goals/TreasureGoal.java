package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal implements Goal {

    public boolean evaluate(Game g) {
        if (g.getTreasureCollected() >= g.getConfigVariables().getTreasureGoal()) {
            return true;
        } else {
            return false;
        }
    }

    public String toString(Game g) {
        if (!evaluate(g)) {
            return ":treasure";
        } else {
            return "";
        }
    }

}
