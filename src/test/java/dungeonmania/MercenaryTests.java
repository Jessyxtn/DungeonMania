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
import dungeonmania.entities.movingEntities.Mercenary;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryTests {
    @Test
    @DisplayName("Mercenary Movement")
    public void MercenaryMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        Position newMercenaryPosition;

        res = dmc.tick(Direction.RIGHT);
        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        
        newMercenaryPosition = new Position(1, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.LEFT);
        
        newMercenaryPosition = new Position(0, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());
    }

    @Test
    @DisplayName("Mercenary Movement And Mercenary Death")
    public void MercenaryMovementAndMercenaryDeath() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        Position newMercenaryPosition;

        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        Player p = (Player) dmc.getGame().getPlayer();

        res = dmc.tick(Direction.RIGHT);
        
        newMercenaryPosition = new Position(1, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.LEFT);

        newMercenaryPosition = new Position(0, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.LEFT);
        
        newMercenaryPosition = new Position(-1, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        assertEquals(dmc.getGame().getEntities().containsKey("mercenary"), false);
    }

    @Test
    @DisplayName("Mercenary Movement And Player Death")
    public void MercenaryMovementAndPlayerDeath() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryPlayerDies");
        Position newMercenaryPosition;

        res = dmc.tick(Direction.RIGHT);
        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        
        newMercenaryPosition = new Position(1, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.LEFT);

        newMercenaryPosition = new Position(0, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.LEFT);
        
        newMercenaryPosition = new Position(-1, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        assertEquals(dmc.getGame().getEntities().containsKey("player"), false);
        
    }

    @Test
    @DisplayName("Mercenary Bribe and Follow Player")
    public void MercenaryBribeFollowPlayer() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenary", "c_bribe_radius_1");
        Position newMercenaryPosition;

        res = dmc.tick(Direction.RIGHT);
        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        
        newMercenaryPosition = new Position(7, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newMercenaryPosition = new Position(6, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newMercenaryPosition = new Position(5, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newMercenaryPosition = new Position(5, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        dmc.tick(Direction.RIGHT);

        assertDoesNotThrow(() -> dmc.interact(m.getId()));
        AllyMercenary ally = (AllyMercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        assertEquals(ally.getIsAlly(), true);

        res = dmc.tick(Direction.LEFT);
        
        newMercenaryPosition = new Position(6, 1, 0);
        assertEquals(newMercenaryPosition, ally.getPosition());

        res = dmc.tick(Direction.LEFT);
        
        newMercenaryPosition = new Position(5, 1, 0);
        assertEquals(newMercenaryPosition, ally.getPosition());
    }

    @Test
    @DisplayName("Mercenary Unsuccessful Bribe")
    public void MercenaryUnsuccessfulBribe() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenary", "c_bribe_radius_1");
        Position newMercenaryPosition;

        res = dmc.tick(Direction.RIGHT);
        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        
        newMercenaryPosition = new Position(7, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        assertThrows(InvalidActionException.class, () -> dmc.interact(m.getId()));
    }


}
