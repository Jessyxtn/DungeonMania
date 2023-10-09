package dungeonmania.entities.movingEntities;

import dungeonmania.Game;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.entities.Entity;
import dungeonmania.entities.staticEntities.SwampTile;
import dungeonmania.util.Direction;


public class EnemyMoveRandom implements MovementBehaviour{

    @Override
    public void movement(MovingEntity entity, Position playerPos, Game g) {
        // Add empty cardinally adjacent positions to a list
        List<Position> movablePositions = new ArrayList<>();
        if(enCanMoveTo(entity.pos.translateBy(Direction.UP), g)) {
            movablePositions.add(entity.pos.translateBy(Direction.UP));
        }
        
        if(enCanMoveTo(entity.pos.translateBy(Direction.DOWN), g)) {
            movablePositions.add(entity.pos.translateBy(Direction.DOWN));
        }
        
        if(enCanMoveTo(entity.pos.translateBy(Direction.LEFT), g)) {
            movablePositions.add(entity.pos.translateBy(Direction.LEFT));
        }
        
        if(enCanMoveTo(entity.pos.translateBy(Direction.RIGHT), g)) {
            movablePositions.add(entity.pos.translateBy(Direction.RIGHT));
        }

        // If the zombie cannot move, then exit the method without moving it
        if(movablePositions.size() == 0) {
            return;
        }

        // Otherwise, move it to a random, valid position
        Random rand = new Random();
        int resp1 = rand.nextInt(movablePositions.size());

        int weight = 1;

        if (entity.getStuckPosTicks() != 0) {
            entity.setStuckPosTicks(entity.getStuckPosTicks() - 1);
            return;
        }

        SwampTile sw = g.getSwampTileAtPos(movablePositions.get(resp1));

        if (sw != null) {
            weight = sw.getMovementFactor();
            entity.setStuckPosTicks(weight);
        }

        entity.setPosition(movablePositions.get(resp1));
    }

    public boolean enCanMoveTo(Position newPos, Game g) {
        List<Entity> entitesInCell = g.getEntitiesInCell(newPos.getX(), newPos.getY());

        boolean isValid = true;

        if (!entitesInCell.isEmpty()) {
            for (Entity o: entitesInCell) {
                if (o.getType().equals("door") ||
                    o.getType().equals("wall") ||
                    o.getType().equals("boulder")) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }

}