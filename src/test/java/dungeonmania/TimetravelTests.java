package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;


import dungeonmania.util.Position;
import static dungeonmania.TestUtils.*;

public class TimetravelTests {
    @Test
    @DisplayName("Test invalid tick")
    public void invalidTickTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest", "c_movementTest_testMovementDown");
        assertEquals(1, getEntities(res, "wood").size());

        res = dmc.tick(Direction.DOWN);
        // Pick up wood
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(0, getEntities(res, "wood").size());

        res = dmc.tick(Direction.RIGHT);
        // Pick up timeturner
        res = dmc.tick(Direction.DOWN);
      
        assertEquals(1, getInventory(res, "time_turner").size());
        assertEquals(0, getEntities(res, "time_turner").size());

        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(0));

    }
    @Test
    @DisplayName("Test picking up item, then timetravelling, item should be back")
    public void itemReappears() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest", "c_movementTest_testMovementDown");
        assertEquals(1, getEntities(res, "wood").size());

        res = dmc.tick(Direction.DOWN);
        // Pick up wood
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(0, getEntities(res, "wood").size());

        res = dmc.tick(Direction.RIGHT);
        // Pick up timeturner
        res = dmc.tick(Direction.DOWN);
      
        assertEquals(1, getInventory(res, "time_turner").size());
        assertEquals(0, getEntities(res, "time_turner").size());
        res = assertDoesNotThrow(() -> dmc.rewind(5));
        //assertEquals(1, getEntities(res, "arrow").size());
        assertEquals(1, getEntities(res, "wood").size());
        assertEquals(1, getEntities(res, "time_turner").size());
    }
    @Test
    @DisplayName("Test rewinding one tick position of moving entities")
    public void entityPositionchanges() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_spiderTest_basicMovement");
        // Player moves, so one tick passes
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(res, "spider").get(0).getPosition());
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "time_turner").size());
        assertEquals(new Position(6, 4), getEntities(res, "spider").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 5), getEntities(res, "spider").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(6, 6), getEntities(res, "spider").get(0).getPosition());
        // Rewind 1 tick
        res = assertDoesNotThrow(() -> dmc.rewind(1));
        assertEquals(new Position(6, 5), getEntities(res, "spider").get(0).getPosition());
        
    }
    @Test
    @DisplayName("Test rewinding five tick position of moving entities")
    public void entityPositionchanges2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_spiderTest_basicMovement");
        // Player moves, so one tick passes
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getEntities(res, "spider").get(0).getPosition());
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "time_turner").size());
        assertEquals(new Position(6, 4), getEntities(res, "spider").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 5), getEntities(res, "spider").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(6, 6), getEntities(res, "spider").get(0).getPosition());
        // Rewind 5 tick -> spider returns to start position
        res = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(new Position(5, 5), getEntities(res, "spider").get(0).getPosition());
        
    }
    @Test
    @DisplayName("Test rewinding of entities that were not in that previous state")
    public void entitydoesnotExist() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_spiderTest_spawnSpiders");
        // Player moves, so one tick passes
        assertEquals(1, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, countEntityOfType(res, "spider"));
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "time_turner").size());
        assertEquals(2, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.UP);
        assertEquals(3, countEntityOfType(res, "spider"));
        // Rewind 5 tick 
        res = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, countEntityOfType(res, "spider"));

        
    }
    @Test
    @DisplayName("Test rewinding of entities that were not in that previous state for portal")
    public void entitydoesnotExistPortal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_spiderTest_spawnSpiders");
        // Player moves, so one tick passes
        assertEquals(1, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, countEntityOfType(res, "spider"));
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "time_turner").size());
        assertEquals(2, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.UP);
        assertEquals(3, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.UP);
        assertEquals(3, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.UP);
        assertEquals(4, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(4, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(5, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(5, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(6, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(6, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(7, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.DOWN);
        assertEquals(7, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.DOWN);
        assertEquals(8, countEntityOfType(res, "spider"));
        res = dmc.tick(Direction.DOWN);
        assertEquals(8, countEntityOfType(res, "spider"));
        // Goes into portal
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, countEntityOfType(res, "spider"));

    }
    @Test
    @DisplayName("Test rewinding and Olderplayer will appear at correct location")
    public void OlderPlayerTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_movementTest_testMovementDown");
        // Player moves, so one tick passes
        res = dmc.tick(Direction.RIGHT);
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        // Rewind 5 tick 
        res = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, getEntities(res, "older_player").size());
        assertEquals(new Position(3, 0), getEntities(res, "older_player").get(0).getPosition());
    }
    @Test
    @DisplayName("Test rewinding and Olderplayer will follow path")
    public void OlderPlayerMovementTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_movementTest_testMovementDown");
        // Player starts at [3,0]
        res = dmc.tick(Direction.RIGHT); // Now at [4,0]
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT); // [5,0]
        res = dmc.tick(Direction.RIGHT); // [6,0]
        res = dmc.tick(Direction.UP); //[6,-1]
        // Rewind 5 tick 
        res = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, getEntities(res, "older_player").size());
        assertEquals(new Position(3, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, -1), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        // Old player should now be gone
        assertEquals(0, getEntities(res, "older_player").size());
    }
    @Test
    @DisplayName("Test rewinding and Olderplayer will follow path and pick up item")
    public void OlderPlayerPickingUpTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_movementTest_testMovementDown");
        // Player starts at [3,0]
        res = dmc.tick(Direction.RIGHT); // Now at [4,0]
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT); // [5,0]
        res = dmc.tick(Direction.RIGHT); // [6,0]
        res = dmc.tick(Direction.UP); //[6,-1]
        // Rewind 5 tick 
        res = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, getEntities(res, "older_player").size());
        assertEquals(new Position(3, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 0), getEntities(res, "older_player").get(0).getPosition());
        assertEquals(1, getEntities(res, "time_turner").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 0), getEntities(res, "older_player").get(0).getPosition());
        // Time turner picked up
        assertEquals(0, getEntities(res, "time_turner").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, -1), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        // Old player should now be gone
        assertEquals(0, getEntities(res, "older_player").size());
    }
    @Test
    @DisplayName("Test that all the tracking and list created as well as tick count is cleared")
    public void timetravelnewGame() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_movementTest_testMovementDown");
        // Player starts at [3,0]
        res = dmc.tick(Direction.RIGHT); // Now at [4,0]
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT); // [5,0]
        res = dmc.tick(Direction.RIGHT); // [6,0]
        res = dmc.tick(Direction.UP); //[6,-1]
        // Rewind 5 tick 
        res = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, getEntities(res, "older_player").size());
        assertEquals(new Position(3, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 0), getEntities(res, "older_player").get(0).getPosition());
        // Time turner picked up
        assertEquals(0, getEntities(res, "time_turner").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, -1), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        // Old player should now be gone
        assertEquals(0, getEntities(res, "older_player").size());


        // Start second game same event
        DungeonResponse res2 = dmc.newGame("d_TimeTravelMovingEntities", "c_movementTest_testMovementDown");
        // Player starts at [3,0]
        res2 = dmc.tick(Direction.RIGHT); // Now at [4,0]
        // Pick up timeturner
        res2 = dmc.tick(Direction.RIGHT); // [5,0]
        res2 = dmc.tick(Direction.RIGHT); // [6,0]
        res2 = dmc.tick(Direction.UP); //[6,-1]
        // Rewind 5 tick 
        res2 = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(1, getEntities(res2, "time_turner").size());
        assertEquals(1, getEntities(res2, "older_player").size());
        assertEquals(new Position(3, 0), getEntities(res2, "older_player").get(0).getPosition());
        res2 = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 0), getEntities(res2, "older_player").get(0).getPosition());
        res2 = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 0), getEntities(res2, "older_player").get(0).getPosition());
        // Time turner picked up
        assertEquals(0, getEntities(res2, "time_turner").size());
        res2 = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 0), getEntities(res2, "older_player").get(0).getPosition());
        res2 = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, -1), getEntities(res2, "older_player").get(0).getPosition());
        res2 = dmc.tick(Direction.RIGHT);
        // Old player should now be gone
        assertEquals(0, getEntities(res2, "older_player").size());
    }
    @Test
    @DisplayName("Test rewinding works for enemies that were slained prior")
    public void entityrespawnafterkilled() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_battleTests_basicMercenaryMercenaryDies");
        assertEquals(1, getEntities(res, "mercenary").size());
        assertEquals(2, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, getEntities(res, "sword").size());
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        // Kills the poor mercenary
        assertEquals(0, getEntities(res, "mercenary").size());
        assertEquals(0, getEntities(res, "treasure").size());
        assertEquals(0, getEntities(res, "time_turner").size());
        assertEquals(0, getEntities(res, "sword").size());
        assertEquals(1, getInventory(res, "time_turner").size());
        assertEquals(2, getInventory(res, "treasure").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        // Back it back to portal
        assertEquals(1, getEntities(res, "mercenary").size());
        assertEquals(2, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, getEntities(res, "sword").size());
    }
    
}
