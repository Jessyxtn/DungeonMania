package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import dungeonmania.entities.staticEntities.Boulder;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class SpiderTests {
    @Test
    @DisplayName("Test that exactly one Spider exists")
    public void spawnSpider() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");

        assertEquals(1, countEntityOfType(resp, "spider"));
    }

    @Test
    @DisplayName("Test that a spider is in the correct location")
    public void testSpawnPos() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        assertEquals(new Position(5, 5), getEntities(resp, "spider").get(0).getPosition());
        assertEquals(1, countEntityOfType(resp, "spider"));
    }

    @Test
    @DisplayName("Tests that a spider cannot move if spawned under a boulder")
    public void testBlockedSpawn() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_blockedInSpawn", "c_spiderTest_basicMovement");

        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 5), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 5), getEntities(resp, "spider").get(0).getPosition());
    }

    @Test
    @DisplayName("Test that a spider moves into the correct position after one tick")
    public void moveOnce() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");

        // Player moves, so one tick passes
        resp = dmc.tick(Direction.RIGHT);
        // By definition, the spider should move up
        assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
    }

    @Test
    @DisplayName("Test that a spider moves into the correct position after one tick")
    public void moveInCircularPath() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");

        resp = dmc.tick(Direction.RIGHT);
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 5), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.UP);
        assertEquals(new Position(6, 6), getEntities(resp, "spider").get(0).getPosition());
    }

    @Test
    @DisplayName("Test if a spider correctly reverses direction when encountering a boulder")
    public void boulderTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_boulderMovement", "c_spiderTest_basicMovement");

        /* The player movement is only used to tick the game, as long as the
         player isn't adjacent to the spider, the player direction doesn't
         really matter 
        */
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 4), getEntities(resp, "spider").get(0).getPosition());
    }

    @Test
    @DisplayName("Test the case where the spider is stuck in a path between 2 boulders")
    public void twoBoulderTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_twoBoulderMovement", "c_spiderTest_basicMovement");

        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 5), getEntities(resp, "spider").get(0).getPosition());
        
        // Reversing from Boulder 1
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 5), getEntities(resp, "spider").get(0).getPosition());
        
        // Reversing from Boulder 2
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);

        assertEquals(1, countEntityOfType(resp, "spider"));
        // And the loop goes on and on
    }

    @Test
    @DisplayName("Test that a spider does not move if it's stuck between 2 boulders")
    public void stuckBetweenBoulders() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_stuckBetweenBoulders", "c_spiderTest_basicMovement");

        assertEquals(new Position(5, 5), getEntities(resp, "spider").get(0).getPosition());
        
        for(int i = 0; i < 10; i++) {
            resp = dmc.tick(Direction.RIGHT);
            assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
        }

        assertEquals(1, countEntityOfType(resp, "spider"));
        // And the loop goes on and on
    }

    @Test
    @DisplayName("Test that a spider goes back to a full path after boulder is removed")
    public void removeBoulder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_twoBoulderMovement2", "c_spiderTest_basicMovement");

        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 4), getEntities(resp, "spider").get(0).getPosition());

        // Reversing from boulder 1
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 4), getEntities(resp, "spider").get(0).getPosition());

        // Reversing from boulder 2
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 4), getEntities(resp, "spider").get(0).getPosition());

        // Remove boulder 1
        Boulder b = (Boulder) dmc.getGame().getEntitiesInCell(6, 5).get(0);
        dmc.getGame().getEntities().get("boulder").remove(b);

        // Open up the path
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 5), getEntities(resp, "spider").get(0).getPosition());
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 6), getEntities(resp, "spider").get(0).getPosition());
    }

    @Test
    @DisplayName("Test that spiders are spawned every 'x' ticks")
    public void spawningSpiders() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_spawnSpiders");

        assertEquals(1, countEntityOfType(resp, "spider"));
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(1, countEntityOfType(resp, "spider"));
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(2, countEntityOfType(resp, "spider"));
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(2, countEntityOfType(resp, "spider"));
        resp = dmc.tick(Direction.RIGHT);
        assertEquals(3, countEntityOfType(resp, "spider"));

    }

}
