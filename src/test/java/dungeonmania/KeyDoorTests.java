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

import dungeonmania.entities.collectableEntities.Key;
import dungeonmania.entities.staticEntities.Door;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class KeyDoorTests {
    

    @Test
    @DisplayName("Test player can use a key to open and walk through a door")
    public void pickUpKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeyTest2", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
    }

    @Test
    @DisplayName("Players cannot pick up more than one key")
    public void cannotHaveMoreThanOneKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeyTest2", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // There are 3 keys in total
        assertEquals(3, getEntities(res, "key").size());

        // pick up 1 key
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // theres one key in inventory
        assertEquals(1, getInventory(res, "key").size());

        // tries to pick up another, unable to
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(2, getEntities(res, "key").size());

        // tries to pick up another, unable to
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(2, getEntities(res, "key").size());

    }

    @Test
    @DisplayName("Test basic key open door")
    public void basicKeyOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeyTest2", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1, 1), pos);

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());

        // goes to the right door
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(2, 3), pos);

    }    

    @Test
    @DisplayName("Test without key cannot open door")
    public void noKeyCannotOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeyTest2", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1, 1), pos);

        // Moves to locked door with no key and tries to go through
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(0, getInventory(res, "key").size());
        assertEquals(new Position(1, 3), pos);

        // Door exists to the right
        Position doorPos = getEntities(res, "door").get(1).getPosition();
        assertEquals(new Position(2, 3), doorPos);

        // Tries to move right, but unable to, stays in same position
        res = dmc.tick(Direction.RIGHT);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1, 3), pos);

    }    
    // With wrong key cannot open door

    @Test
    @DisplayName("Test wrong key cannot open door")
    public void wrongKeyCannotOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeyTest2", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1, 1), pos);

        // Pick up key
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(new Position(3, 1), pos);

        // Door exists down
        Position doorPos = getEntities(res, "door").get(0).getPosition();
        assertEquals(new Position(3, 2), doorPos);

        // Tries to move down, but unable to, stays in same position
        res = dmc.tick(Direction.DOWN);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(3, 1), pos);

    }

    // Key disappears after use
    @Test
    @DisplayName("Test key disappears after used")
    public void keyDisappearsAfterUse() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeyTest2", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1, 1), pos);

        // pick up key
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(1, getInventory(res, "key").size());

        // goes to the right door
        res = dmc.tick(Direction.DOWN);

        // door on the left
        res = dmc.tick(Direction.LEFT);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(3, 2), pos);
        assertEquals(0, getInventory(res, "key").size());

    }    
    // After using key can now pick up another one
    // Key disappears after use
    @Test
    @DisplayName("Test key disappears after used")
    public void canPickUpAnotherKeyAfterUsingOne() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeyTest2", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1, 1), pos);

        // pick up key
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(1, getInventory(res, "key").size());

        // goes to the right door
        res = dmc.tick(Direction.DOWN);

        // door on the left
        res = dmc.tick(Direction.LEFT);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(3, 2), pos);
        assertEquals(0, getInventory(res, "key").size());

        // key on top 
        res = dmc.tick(Direction.UP);
        assertEquals(1, getInventory(res, "key").size());
    }    

    // Door remains open after unlocking 
    @Test
    @DisplayName("Test unlocked door remains open")
    public void unlockedDoorRemainsOpen() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeyTest2", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1, 1), pos);

        // pick up key
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(1, getInventory(res, "key").size());

        // goes to the right door
        res = dmc.tick(Direction.DOWN);

        // door on the left
        res = dmc.tick(Direction.LEFT);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(3, 2), pos);
        assertEquals(0, getInventory(res, "key").size());

        // key on top 
        res = dmc.tick(Direction.UP);
        assertEquals(1, getInventory(res, "key").size());

        // back to door on the bottom 
        res = dmc.tick(Direction.DOWN);
        pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(3, 2), pos);

    } 
    
    @Test
    @DisplayName("Doors are always locked when created")
    public void lockedDoor() {
        Door d = new Door(null, null, 0);

        // Door is lock
        assertTrue(d.isLocked());
    
    }
}
