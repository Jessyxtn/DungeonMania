package dungeonmania.entities.movingEntities;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.staticEntities.SwampTile;
import dungeonmania.util.Position;

public class EnemyMoveCircular implements MovementBehaviour {

    @Override
    public void movement(MovingEntity entity, Position playerPos, Game g) {

        Spider s = (Spider) entity;
        // Go to the next position
        int nextPathIndex;
        int evalNextPathIndex;
        
        if(s.clockwise) {
            nextPathIndex = s.currentpathindex + 1;
            evalNextPathIndex = g.modulo(nextPathIndex, 8);
            // Reversing the direction of the spider if boulder obstructs path
            if(isBlocked(g, s.path.get(evalNextPathIndex).getX(), s.path.get(evalNextPathIndex).getY())) {
                // If the spider is stuck in spawn, then don't move it
                if(!s.path.values().contains(s.pos)) {
                    return;
                }
                s.setClockwise(!s.clockwise);
                // If the spider is stuck between 2 boulders
                nextPathIndex = s.currentpathindex - 1;
                evalNextPathIndex = g.modulo(nextPathIndex, 8);
                if(isBlocked(g, s.path.get(evalNextPathIndex).getX(), s.path.get(evalNextPathIndex).getY())) {
                    return;
                }
                moveAnticlockwise(s, g);
            } else {
                moveClockwise(s, g);
            }
        } else {
            nextPathIndex = s.currentpathindex - 1;
            evalNextPathIndex = g.modulo(nextPathIndex, 8);
            // Reversing the direction of the spider if boulder obstructs path
            if(isBlocked(g, s.path.get(evalNextPathIndex).getX(), s.path.get(evalNextPathIndex).getY())) {
                s.setClockwise(!s.clockwise);
                // If the spider is stuck between 2 boulders
                nextPathIndex = s.currentpathindex + 1;
                evalNextPathIndex = g.modulo(nextPathIndex, 8);
                if(isBlocked(g, s.path.get(evalNextPathIndex).getX(), s.path.get(evalNextPathIndex).getY())) {
                    return;
                }
                moveClockwise(s, g);
            } else {
                moveAnticlockwise(s, g);
            }
        }
    }

    /*
     * Moves the spider clockwise
     */
    private void moveClockwise(Spider s, Game g) {
        s.currentpathindex++;
        moveToNext(s, g);
    }

    /**
     * Moves the spider anti-clockwise
     */
    private void moveAnticlockwise(Spider s, Game g) {
        s.currentpathindex--;
        moveToNext(s, g);
    }

    /**
     * Checks if a spider is blocked by a boulder
     * @return
     */
    private boolean isBlocked(Game g, int x, int y) {
        for(Entity en : g.getEntitiesInCell(x, y)) {
            if(en.getType().equals("boulder")) {
                return true;
            }
        }
        return false;
    }

    /*
     * Updates the spider's position by using the modulo value for the HashMap
     * index
     */
    private void moveToNext(Spider s, Game g) {
        int modulo = s.currentpathindex % 8;
        if (modulo<0) modulo += 8;

        // The %8 allows the spider to continue to loop

        int weight = 1;

        if (s.getStuckPosTicks() != 0) {
            s.setStuckPosTicks(s.getStuckPosTicks() - 1);
            return;
        }

        SwampTile sw = g.getSwampTileAtPos(s.path.get(modulo));

        if (sw != null) {
            weight = sw.getMovementFactor();
            s.setStuckPosTicks(weight);
        }

        s.setPosition(s.path.get(modulo));
    }
    
}
