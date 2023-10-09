package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class ZombieToastTests {

    @Test
    @DisplayName("Tests that 2 zombies have moved from their spawn positions")
    public void checkBasicMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        // In this config, there are 2 zombies and no zombiespawner
        dmc.newGame("d_zombieToastbasic", "c_zombiesMovementConfig");

        dmc.tick(Direction.RIGHT);
        // These lists should be empty because the zombies must have moved and no other
        // entities exist at those spawn locations
        assertEquals(0, dmc.getGame().getEntitiesInCell(5, 5).size());
        assertEquals(0, dmc.getGame().getEntitiesInCell(8, 8).size());
    }
    
    @Test
    @DisplayName("Tests that 2 zombies have moved after one tick and checks their travelled distance")
    public void checkDistance() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_zombieToastbasic", "c_zombiesMovementConfig");

        assertEquals(2, countEntityOfType(resp, "zombie_toast"));
        resp = dmc.tick(Direction.RIGHT);
        Position zom1 = getEntities(resp, "zombie_toast").get(0).getPosition();
        Position zom2 = getEntities(resp, "zombie_toast").get(1).getPosition();
        // Due to the order in the config file, we know that the first zombie has spawnPos of (5,5)
        // and the second zombie has a spawnPos of (8,8)
        assert(Position.calculateDistance(new Position(5, 5), zom1) == 1);
        assert(Position.calculateDistance(new Position(8, 8), zom2) == 1);

        resp = dmc.tick(Direction.RIGHT);
        zom1 = getEntities(resp, "zombie_toast").get(0).getPosition();
        zom2 = getEntities(resp, "zombie_toast").get(1).getPosition();
        assert(Position.calculateDistance(new Position(5, 5), zom1) <= 2);
        assert(Position.calculateDistance(new Position(8, 8), zom2) <= 2);
    }

    @Test
    @DisplayName("Check that zombies follow a path when there are walls and boulders nearby")
    public void followPath() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_ZombieWalls", "c_zombiesMovementConfig");
        
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 6), getEntities(resp, "zombie_toast").get(0).getPosition());
        assertEquals(new Position(8, 7), getEntities(resp, "zombie_toast").get(1).getPosition());
    }
}
