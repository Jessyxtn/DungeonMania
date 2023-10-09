package dungeonmania.entities.movingEntities;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.staticEntities.Door;
import dungeonmania.entities.staticEntities.SwampTile;
import dungeonmania.util.Position;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

public class EnemyMoveTowards implements MovementBehaviour {

    @Override
    public void movement(MovingEntity entity, Position playerPos, Game g) {
        PriorityQueue<Position> q = new PriorityQueue<Position>();
        Integer[][] dist = new Integer[41][41];
        Position[][] prev = new Position[41][41];
        
        int startY = entity.getPosition().getY() - 20;
        int endY = entity.getPosition().getY() + 20;
        int startX = entity.getPosition().getX() - 20;
        int endX = entity.getPosition().getX() + 20;

        Position v; 

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if (!entity.getPosition().equals(new Position(x,y))) {
                    dist[y - startY][x - startX] = Integer.MAX_VALUE;
                    prev[y - startY][x - startX] = null;
                    v = new Position(x, y, 0, dist[y - startY][x - startX]);
                    q.add(v);
                } else {
                    dist[y - startY][x - startX] = 0;
                    prev[y - startY][x - startX] = null;
                    v = new Position(x, y, 0, dist[y - startY][x - startX]); 
                    q.add(v);
                }
            }
        }

        while (!q.isEmpty()) {
            Position u = q.poll();

            Position up = null;
            Position right = null;
            Position down = null;
            Position left = null;

            for (Position e: q) {
                if (e.getX() == u.getX() && e.getY() == u.getY() + 1) {
                    up = e;
                } else if (e.getX() == u.getX() + 1 && e.getY() == u.getY()) {
                    right = e;
                } else if (e.getX() == u.getX() && e.getY() == u.getY() - 1) {
                    down = e;
                } else if (e.getX() == u.getX() - 1 && e.getY() == u.getY()) {
                    left = e;
                }
            }

            if (up != null) {
                if (canMoveTo(up, g)) {
                    SwampTile sw = g.getSwampTileAtPos(up);
                    int weight = 1;

                    if (sw != null) {
                        weight = sw.getMovementFactor();
                    }

                    int alt = dist[u.getY() - startY][u.getX() - startX] + weight;

                    if (alt < dist[up.getY() - startY][up.getX() - startX] && dist[u.getY() - startY][u.getX() - startX] != Integer.MAX_VALUE) {
                        dist[up.getY() - startY][up.getX() - startX] = alt;
                        prev[up.getY() - startY][up.getX() - startX] = u;
                        v = new Position(up.getX(), up.getY(), 0, alt);
                        q.remove(up);
                        q.add(v);
                    }
                }
            }

            if (right != null) {
                if (canMoveTo(right, g)) {
                    SwampTile sw = g.getSwampTileAtPos(right);
                    int weight = 1;

                    if (sw != null) {
                        weight = sw.getMovementFactor();
                    }

                    int alt = dist[u.getY() - startY][u.getX() - startX] + weight;

                    if (alt < dist[right.getY() - startY][right.getX() - startX] && dist[u.getY() - startY][u.getX() - startX] != Integer.MAX_VALUE) {
                        dist[right.getY() - startY][right.getX() - startX] = alt;
                        prev[right.getY() - startY][right.getX() - startX] = u;
                        v = new Position(right.getX(), right.getY(), 0, alt);
                        q.remove(right);
                        q.add(v);
                    }
                }
            }

            if (down != null) {
                if (canMoveTo(down, g)) {
                    SwampTile sw = g.getSwampTileAtPos(down);
                    int weight = 1;

                    if (sw != null) {
                        weight = sw.getMovementFactor();
                    }

                    int alt = dist[u.getY() - startY][u.getX() - startX] + weight;

                    if (alt < dist[down.getY() - startY][down.getX() - startX] && dist[u.getY() - startY][u.getX() - startX] != Integer.MAX_VALUE) {
                        dist[down.getY() - startY][down.getX() - startX] = alt;
                        prev[down.getY() - startY][down.getX() - startX] = u;
                        v = new Position(down.getX(), down.getY(), 0, alt);
                        q.remove(down);
                        q.add(v);
                    }
                }
            }

            if (left != null) {
                if (canMoveTo(left, g)) {
                    SwampTile sw = g.getSwampTileAtPos(left);
                    int weight = 1;

                    if (sw != null) {
                        weight = sw.getMovementFactor();
                    }

                    int alt = dist[u.getY() - startY][u.getX() - startX] + weight;

                    if (alt < dist[left.getY() - startY][left.getX() - startX] && dist[u.getY() - startY][u.getX() - startX] != Integer.MAX_VALUE) {
                        dist[left.getY() - startY][left.getX() - startX] = alt;
                        prev[left.getY() - startY][left.getX() - startX] = u;
                        v = new Position(left.getX(), left.getY(), 0, alt);
                        q.remove(left);
                        q.add(v);
                    }
                }
            }

            if (u.getX() == playerPos.getX() && u.getY() == playerPos.getY()) {
                Position curr = u;
                Position previous = entity.getPosition();

                while (prev[curr.getY() - startY][curr.getX() - startX] != null) {
                    previous = curr;
                    curr = prev[curr.getY() - startY][curr.getX() - startX];
                }

                if (!canMoveTo(previous, g)) {
                    previous = entity.getPosition();
                }
                
                int weight = 1;

                if (entity.getStuckPosTicks() != 0) {
                    entity.setStuckPosTicks(entity.getStuckPosTicks() - 1);
                    return;
                }

                SwampTile sw = g.getSwampTileAtPos(previous);

                if (sw != null) {
                    weight = sw.getMovementFactor();
                    entity.setStuckPosTicks(weight);
                }
        
                entity.setPosition(previous);   
                return;
            }
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
