package dungeonmania.goals;

import dungeonmania.Game;

public class EnemiesGoal implements Goal {

    public boolean evaluate(Game g) {
        if (g.getEnemiesDestroyed() >= g.getConfigVariables().getEnemyGoal()) {
            return true;
        } else {
            return false;
        }
    }
    
    public String toString(Game g) {
        if (!evaluate(g)) {
            return ":enemies";
        } else {
            return "";
        }
    }

}
