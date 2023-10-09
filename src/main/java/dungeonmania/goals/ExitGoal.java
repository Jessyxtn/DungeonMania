package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.util.Position;

public class ExitGoal implements Goal {
    
    public boolean evaluate(Game g) {
        if (g.getEntities().containsKey("player")) {
            if (g.getEntities().containsKey("exit")) {
                Position playerPos = g.getEntities().get("player").get(0).getPosition();
                Position exitPos = g.getEntities().get("exit").get(0).getPosition();

                if (playerPos.getX() == exitPos.getX() && playerPos.getY() == exitPos.getY()) {
                    return true;
                }
            }
        }

        return false;
    }
    
    public String toString(Game g) {
        if (!evaluate(g)) {
            return ":exit";
        } else {
            return "";
        }
    }

}
