package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class ZombieToastSpawnerTests {
    
    @Test
    @DisplayName("This checks whether a zombie is spawned or not")
    public void spawnOneZombie() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        // In this config, a zombie is spawned every 2 ticks
        DungeonResponse resp = dmc.newGame("d_zombieSpawnerTest", "c_basicZombieSpawnerConfig");

        assertEquals(1, countEntityOfType(resp, "zombie_toast_spawner"));
        assertEquals(0, countEntityOfType(resp, "zombie_toast"));

        resp = dmc.tick(Direction.RIGHT);
        assertEquals(0, countEntityOfType(resp, "zombie_toast"));
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(1, countEntityOfType(resp, "zombie_toast"));
    }

    @Test
    @DisplayName("This test checks for multiple zombies spawning in 6 ticks")
    public void spawnThreeZombies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_zombieSpawnerTest", "c_basicZombieSpawnerConfig");

        for(int i = 1; i <= 6; i++) {
            resp = dmc.tick(Direction.RIGHT);
            if(dmc.getGame().modulo(i, 2) == 0) {
                assertEquals(i/2, countEntityOfType(resp, "zombie_toast"));
            }
        }
    }

    @Test
    @DisplayName("No zombies should be spawned in this test, because of blocked spawn")
    public void blockedSpawn() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_ZombieSpawnerBlocked", "c_basicZombieSpawnerConfig");

        for(int i = 0; i <= 5; i++) {
            resp = dmc.tick(Direction.RIGHT);
            assertEquals(0, countEntityOfType(resp, "zombie_toast"));       
        }
    }

    @Test
    @DisplayName("Multiple zombie spawners")
    public void multipleSpawners() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        // There are 2 zombie spawners that both spawn zombies every 2 ticks
        DungeonResponse resp = dmc.newGame("d_zombieSpawnerMultiple", "c_basicZombieSpawnerConfig");

        assertEquals(2, countEntityOfType(resp, "zombie_toast_spawner"));
        for(int i = 1; i <= 4; i++) {
            resp = dmc.tick(Direction.RIGHT);
            if(dmc.getGame().modulo(i, 2) == 0) {
                assertEquals(i, countEntityOfType(resp, "zombie_toast"));
            }
        }
    }

    @Test
    @DisplayName("Test zombie spawning functionality with the zombie movement functionality")
    public void zombieIntegrationTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_zombieSpawnerTest", "c_basicZombieSpawnerConfig");

        // Spawns a zombie
        resp = dmc.tick(Direction.RIGHT);
        resp = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(resp, "zombie_toast"));
        Position zomPos = getEntities(resp, "zombie_toast").get(0).getPosition();
        resp = dmc.tick(Direction.RIGHT);
        Position zomPos2 = getEntities(resp, "zombie_toast").get(0).getPosition();

        // Check that the zombie's position after one tick is adjacent to it's position from the previous tick
        assert(zomPos2.getAdjacentPositions().contains(zomPos));

        for(int i = 1; i <= 3; i++) {
            resp = dmc.tick(Direction.RIGHT);
        }
        assertEquals(3, countEntityOfType(resp, "zombie_toast"));
        
        
    }
}
