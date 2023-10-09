package dungeonmania.goals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;

public class AND implements Goal {
    private List<Goal> childern = new ArrayList<Goal>();

    public boolean evaluate(Game g) {
        boolean val = true;

        for (Goal goal: childern) {
            val = goal.evaluate(g) && val;
        }

        return val;
    }

    @Override
    public String toString(Game g) {
        String s = "";
        int count = 0;

        for (Goal goal: childern) {
            if (!goal.evaluate(g)) {
                if (count != 0) {
                    s += " AND ";
                }

                s += goal.toString(g);
                count++;
            }
        }

        if (count <= 1) {
            return s;
        } 

        return  "(" + s + ")";
    }

    public void addChild(Goal child) {
        childern.add(child);
    }

    public void removeChild(Goal child) {
        childern.remove(child);
    }
}
