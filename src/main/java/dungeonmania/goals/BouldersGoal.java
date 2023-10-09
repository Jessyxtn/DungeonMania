package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Entity;

public class BouldersGoal implements Goal {

    public boolean evaluate(Game g) {
        if (g.getEntities().containsKey("boulder") && g.getEntities().containsKey("switch")) {
            for (Entity b: g.getEntities().get("boulder")) {
                Boolean samePos = false;
                for (Entity s: g.getEntities().get("switch")) {
                    if (s.getPosition().getX() == b.getPosition().getX() && s.getPosition().getY() == b.getPosition().getY()) {
                        samePos = true;
                    }
                }

                if (samePos == false) {
                    return false;
                }
            }

            return true;
        } else if (!g.getEntities().containsKey("boulder") && !g.getEntities().containsKey("switch")) {
            return true;
        } else {
            return false;
        }
    }

    public String toString(Game g) {
        if (!evaluate(g)) {
            return ":boulders";
        } else {
            return "";
        }
    }

}
