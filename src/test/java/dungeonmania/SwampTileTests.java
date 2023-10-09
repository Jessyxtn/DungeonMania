package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import dungeonmania.exceptions.InvalidActionException;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entities.movingEntities.AllyMercenary;
import dungeonmania.entities.movingEntities.Assassin;
import dungeonmania.entities.movingEntities.Mercenary;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTests {
    @Test
    @DisplayName("Check if swamp tile slows mercenary")
    public void MercenaryStuckSwampTile() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenary_swamp_tile", "c_M3_config");
        Position newMercenaryPosition;

        res = dmc.tick(Direction.RIGHT);
        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        
        newMercenaryPosition = new Position(7, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        newMercenaryPosition = new Position(6, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());
    }

    @Test
    @DisplayName("Check if swamp tile slows assassin")
    public void AssassinStuckSwampTile() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassin_swamp_tile", "c_M3_config");
        Position newAssassinPosition;

        res = dmc.tick(Direction.RIGHT);
        Assassin a = (Assassin) dmc.getGame().getEntities().get("assassin").get(0);
        
        newAssassinPosition = new Position(7, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(newAssassinPosition, a.getPosition());

        res = dmc.tick(Direction.RIGHT);
        newAssassinPosition = new Position(6, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());
    }

    @Test
    @DisplayName("Check if mercenary takes shortest path")
    public void MercenaryShortestPath() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_advanced_or_swamp", "c_M3_config");
        Position newMercenaryPosition;

        res = dmc.tick(Direction.RIGHT);
        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        
        newMercenaryPosition = new Position(3, 4, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        newMercenaryPosition = new Position(4, 4, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        newMercenaryPosition = new Position(5, 4, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.DOWN);
        newMercenaryPosition = new Position(5, 3, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.UP);
        newMercenaryPosition = new Position(5, 2, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.LEFT);
        newMercenaryPosition = new Position(5, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.LEFT);
        newMercenaryPosition = new Position(5, 2, 0);
        assertEquals(newMercenaryPosition, m.getPosition());
    }
}
