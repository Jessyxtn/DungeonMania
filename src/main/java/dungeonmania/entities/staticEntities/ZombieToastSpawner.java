package dungeonmania.entities.staticEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {

    public ZombieToastSpawner(String type, Position pos) {
        super(type, pos);
        this.isInteractable = true;
    }

    /**
     * Spawns a zombie and returns it, every x ticks, defined by zombie_spawn_rate.
     * A zombie needs to be returned because it is then added to the list of
     * entities in Game.java
     * @param g
     * @return zom : ZombieToast
     */
    public ZombieToast spawnZombie(Game g, double health, double attack) {
        ZombieToast zom = new ZombieToast("zombie_toast", findSpawnPos(g), health, attack);
        return zom;
    }

    /**
     * Generate an empty position from the 4 cardinally adjacent tiles for the
     * zombie to spawn in
     * @param g
     * @return pos : Position
     */
    private Position findSpawnPos(Game g) {
        Position p = new Position(this.getPosition().getX(), this.getPosition().getY());
        List<Position> possibleSpawns = new ArrayList<>();
        Random rand = new Random();

        // Find all spawnable cardinally adjacent tiles
        if(isSpawnable(g, p.translateBy(Direction.UP))) {
            possibleSpawns.add(p.translateBy(Direction.UP));
        }
        if(isSpawnable(g, p.translateBy(Direction.DOWN))) {
            possibleSpawns.add(p.translateBy(Direction.DOWN));
        }
        if(isSpawnable(g, p.translateBy(Direction.LEFT))) {
            possibleSpawns.add(p.translateBy(Direction.LEFT));
        }
        if(isSpawnable(g, p.translateBy(Direction.RIGHT))) {
            possibleSpawns.add(p.translateBy(Direction.RIGHT));
        }

        // An invalid position is returned if there's no space for the zombie to spawn
        if(possibleSpawns.size() == 0) {
            return new Position(-99, -99);
        }

        p = possibleSpawns.get(rand.nextInt(possibleSpawns.size()));
        return p;
    }

    private boolean isSpawnable(Game g, Position p) {
        List<Entity> enList = g.getEntitiesInCell(p.getX(), p.getY());
        for(Entity e : enList) {
            if(e.getType().equals("wall") ||
            e.getType().equals("boulder")) {
                return false;
            }
        }
        return true;
    }
}
