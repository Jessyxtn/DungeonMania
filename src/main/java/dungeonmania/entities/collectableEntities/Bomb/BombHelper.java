package dungeonmania.entities.collectableEntities.Bomb;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class BombHelper {
    public static List<Entity> getCardinalAdjacentEntities(Game g, Position p) {
        List<Entity> adjacentEntities = new ArrayList<>();
        // get up
        adjacentEntities.addAll(g.getEntitiesInCell(p.getX(), p.getY() - 1));
        // get down 
        adjacentEntities.addAll(g.getEntitiesInCell(p.getX(), p.getY() + 1));
        // get left
        adjacentEntities.addAll(g.getEntitiesInCell(p.getX() - 1, p.getY()));
        // get right 
        adjacentEntities.addAll(g.getEntitiesInCell(p.getX() + 1, p.getY()));
        return adjacentEntities;
        
    }

    public static List<Entity> getSquareRadiusEntities(Game g, Position p, double radius) {
        List<Entity> entitiesL = new ArrayList<>();
        int startX = p.getX() - (int)radius;
        int startY = p.getY() - (int)radius;
        int endX = p.getX() + (int)radius;
        int endY = p.getY() + (int)radius;

        for (int j = startY; j <= endY; j++) {
            for (int i = startX; i <= endX; i++) {
                entitiesL.addAll(g.getEntitiesInCell(i, j));
            }
        }
        return entitiesL;
    }

}
