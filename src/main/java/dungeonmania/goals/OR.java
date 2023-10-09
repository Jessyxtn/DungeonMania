package dungeonmania.goals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;

public class OR implements Goal {
    private List<Goal> childern = new ArrayList<Goal>();

    public boolean evaluate(Game g) {
        for (Goal goal: childern) {
            if (goal.evaluate(g)) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public String toString(Game g) {
        int i = 1;

        if (!evaluate(g)) {
            String s = "(";
            for (Goal goal: childern) {
                s += goal.toString(g);

                if (i != childern.size()) {
                    s += " OR ";
                }

                i++;
            }

            return s + ")";
        } else {
            return "";
        }
    }

    public void addChild(Goal child) {
        childern.add(child);
    }

    public void removeChild(Goal child) {
        childern.remove(child);
    }

}
