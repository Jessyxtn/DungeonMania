package dungeonmania.entities.movingEntities;

import java.util.Iterator;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectableEntities.CollectableEntity;
import dungeonmania.util.Position;

public class EnemyMoveMimic implements MovementBehaviour {
    private int nextPathIndex;
    @Override
    public void movement(MovingEntity entity, Position playerPos, Game g) {
        OlderPlayer op = (OlderPlayer) entity;
       
        nextPathIndex = op.currentpathindex;
        if (op.getPath().size() > nextPathIndex) {
            if (!isBlocked(g, op.getPath().get(nextPathIndex)))
                op.setPosition(op.getPath().get(nextPathIndex));

            for (Entity o : g.getEntitiesInCell(op.getPosition().getX(), op.getPosition().getY())) {
                if (o instanceof CollectableEntity) {
                    CollectableEntity c = (CollectableEntity) o;
                    c.collideWithOlderPlayer(op, g);
                } 
            }
            
        } else {
            op.setReachedDestination();
            op.resetindex();    
        }
        
    }

    private boolean isBlocked(Game g, Position pos) {
        if(g.canPassThrough(g.getEntitiesInCell(pos.getX(), pos.getY()))) {
            return false;
        }
        return true;
    }

    
}
