package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FloorSwitchTests {
    @Test
    @DisplayName("Test when no entities on top - switch stays off")
    public void noEntitiesOnTopSwitchStaysOff() {
        // bomb placed next to inactive switch, won't detonate
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombBoulderSwitchTests_bombRadius1", "c_bombBoulderSwitchTests_bombRadius1");

        assertEquals(new Position(3, 2), getEntities(res, "switch").get(0).getPosition());
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(3, getEntities(res, "wall").size());
        assertEquals(4, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());

        //Pick up bomb
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getInventory(res, "bomb").get(0).getId() != null);

        // move next to switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // Put bomb down next to an inactive switch
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(3, getEntities(res, "wall").size());
        assertEquals(3, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }   

    @Test
    @DisplayName("Test when boulder is on top, switch turns on")
    public void boulderOnTopSwitchTurnsOn() {
        // Switch turn on will detonate bomb
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombBoulderSwitchTests_bombRadius1", "c_bombBoulderSwitchTests_bombRadius1");

        assertEquals(new Position(3, 2), getEntities(res, "switch").get(0).getPosition());
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(3, getEntities(res, "wall").size());
        assertEquals(4, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());

        //Pick up bomb
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getInventory(res, "bomb").get(0).getId() != null);

        // move next to switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // Put bomb down next to an inactive switch
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(3, getEntities(res, "wall").size());
        assertEquals(3, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());

        // push boulder onto switch and turns it on
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check bomb detonated and removed 1 wall, 1 switch, 1 boulder and 1 treasure, and the bomb itself
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(2, getEntities(res, "wall").size());
        assertEquals(2, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }   

}
