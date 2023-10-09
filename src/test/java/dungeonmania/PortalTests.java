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


public class PortalTests {
    @Test
    @DisplayName("Test if player collects coin after telporting on coin")
    public void CollectCoinOnTeleport() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portals_coins", "c_bombTest_placeBombRadius2");

        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getInventory(res, "treasure").size());

    }

    @Test
    @DisplayName("Test if player dosen't teleport on wall")
    public void notTeleportOnWall() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portals_wall", "c_bombTest_placeBombRadius2");

        Position pos = dmc.getGame().getPlayer().getPosition();

        res = dmc.tick(Direction.RIGHT);

        assertEquals(pos, dmc.getGame().getPlayer().getPosition());

    }

    @Test
    @DisplayName("Test basic teleport")
    public void BasicTeleport() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portals_basic", "c_bombTest_placeBombRadius2");

        Position newTeleportPos = new Position(6, 1, 0);

        res = dmc.tick(Direction.RIGHT);

        assertEquals(newTeleportPos, dmc.getGame().getPlayer().getPosition());

    }

    @Test
    @DisplayName("Test complex teleport with multiple surrounding portals")
    public void ComplexTeleport() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portals_basic", "c_bombTest_placeBombRadius2");

        Position newTeleportPos = new Position(1, 2, 0);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        assertEquals(newTeleportPos, dmc.getGame().getPlayer().getPosition());

    }
}
