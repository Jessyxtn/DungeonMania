package dungeonmania.entities.movingEntities;
import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.staticEntities.Door;
import dungeonmania.entities.staticEntities.SwampTile;
import dungeonmania.util.Position;
import java.util.List;
import java.util.Objects;

public class EnemyMoveAway implements MovementBehaviour {
    
    @Override
    public void movement(MovingEntity entity, Position playerPos, Game g) {
        Position up = new Position(entity.getPosition().getX(), entity.getPosition().getY() + 1);
        Position down = new Position(entity.getPosition().getX(), entity.getPosition().getY() - 1);
        Position right = new Position(entity.getPosition().getX() + 1, entity.getPosition().getY());
        Position left = new Position(entity.getPosition().getX() - 1, entity.getPosition().getY());

        if (playerPos.getY() >= entity.getPosition().getY() && canMoveTo(down, g)) {
            int weight = 1;

            if (entity.getStuckPosTicks() != 0) {
                entity.setStuckPosTicks(entity.getStuckPosTicks() - 1);
                return;
            }

            SwampTile sw = g.getSwampTileAtPos(down);

            if (sw != null) {
                weight = sw.getMovementFactor();
                entity.setStuckPosTicks(weight);
            }
    
            entity.setPosition(down);  
        } else if (playerPos.getY() < entity.getPosition().getY() && canMoveTo(up, g)) {
            int weight = 1;

            if (entity.getStuckPosTicks() != 0) {
                entity.setStuckPosTicks(entity.getStuckPosTicks() - 1);
                return;
            }

            SwampTile sw = g.getSwampTileAtPos(up);

            if (sw != null) {
                weight = sw.getMovementFactor();
                entity.setStuckPosTicks(weight);
            }
    
            entity.setPosition(up);  
        } else if (playerPos.getX() >= entity.getPosition().getX() && canMoveTo(left, g)) {
            int weight = 1;

            if (entity.getStuckPosTicks() != 0) {
                entity.setStuckPosTicks(entity.getStuckPosTicks() - 1);
                return;
            }

            SwampTile sw = g.getSwampTileAtPos(left);

            if (sw != null) {
                weight = sw.getMovementFactor();
                entity.setStuckPosTicks(weight);
            }
    
            entity.setPosition(left);  
        } else if (playerPos.getX() < entity.getPosition().getX() && canMoveTo(right, g)) {
            int weight = 1;

            if (entity.getStuckPosTicks() != 0) {
                entity.setStuckPosTicks(entity.getStuckPosTicks() - 1);
                return;
            }

            SwampTile sw = g.getSwampTileAtPos(right);

            if (sw != null) {
                weight = sw.getMovementFactor();
                entity.setStuckPosTicks(weight);
            }
    
            entity.setPosition(right);  
        }
    }

    public boolean canMoveTo(Position newPos, Game g) {
        List<Entity> entitesInCell = g.getEntitiesInCell(newPos.getX(), newPos.getY());

        boolean isValid = true;

        if (!entitesInCell.isEmpty()) {
            for (Entity o: entitesInCell) {
                if (Objects.equals(o.getType(), "wall")) {
                    isValid = false;
                } else if (Objects.equals(o.getType(), "door")) {
                    Door d = (Door) o;
                    if (d.isLocked()) {
                        isValid = false;
                    }
                }
            }
        }

        return isValid;
    }
    
}
