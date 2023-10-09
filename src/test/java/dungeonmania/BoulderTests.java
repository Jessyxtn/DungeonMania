package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BoulderTests {
    @Test
    @DisplayName("Test boulder position after pushing")
    public void boulderPositionAfterPushing() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombBoulderSwitchTests_bombRadius1", "c_bombBoulderSwitchTests_bombRadius1");

        assertEquals(new Position(2, 2), getEntities(res, "boulder").get(0).getPosition());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Confirm boulder has been moved
        assertEquals(new Position(3, 2), getEntities(res, "boulder").get(0).getPosition());

    } 

    @Test
    @DisplayName("Test boulder can be pushed onto other entities")
    public void boulderOnEntities() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        assertEquals(new Position(3, 2), getEntities(res, "boulder").get(0).getPosition());

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // Boulder is on top of unused bomb
        assertEquals(new Position(3, 3), getEntities(res, "boulder").get(0).getPosition());

    
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(new Position(5, 3), getEntities(res, "boulder").get(0).getPosition());
        assertEquals(new Position(5, 3), getEntities(res, "treasure").get(0).getPosition());

    } 

    @Test
    @DisplayName("Boulder cannot be pushed onto used bombs")
    public void boulderCannotBeOnUsedBomb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombBoulderSwitchTests_bombRadius1", "c_bombBoulderSwitchTests_bombRadius1");

        assertEquals(new Position(2, 2), getEntities(res, "boulder").get(0).getPosition());
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(3, getEntities(res, "wall").size());
        assertEquals(4, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());

        // Pick up bomb
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getInventory(res, "bomb").get(0).getId() != null);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // put down bomb
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);

        assertEquals(new Position(2, 2), getEntities(res, "boulder").get(0).getPosition());
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 2), getEntities(res, "boulder").get(0).getPosition());

        // stoped by used bomb, cannot be pushed left any futher
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 2), getEntities(res, "boulder").get(0).getPosition());

    } 

    
    @Test
    @DisplayName("Boulder cannot be pushed onto wall")
    public void boulderCannotBeOnWalls() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombBoulderSwitchTests_bombRadius1", "c_bombBoulderSwitchTests_bombRadius1");

        assertEquals(new Position(2, 2), getEntities(res, "boulder").get(0).getPosition());
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(3, getEntities(res, "wall").size());
        assertEquals(4, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());

        // Going to boulder
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 2), getEntities(res, "boulder").get(0).getPosition());
        // Try to push boulder up
        res = dmc.tick(Direction.UP);
        // Unable to because of wall
        assertEquals(new Position(2, 2), getEntities(res, "boulder").get(0).getPosition());

    } 


}
