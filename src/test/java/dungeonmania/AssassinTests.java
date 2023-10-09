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

import dungeonmania.entities.movingEntities.AllyAssassin;
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

public class AssassinTests {
    @Test
    @DisplayName("Assassin Bribe Expected Successfull and Follow Player")
    public void AssassinBribeFollowPlayer() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassin", "c_fail_rate_0");
        Position newAssassinPosition;

        res = dmc.tick(Direction.RIGHT);
        Assassin a = (Assassin) dmc.getGame().getEntities().get("assassin").get(0);
        
        newAssassinPosition = new Position(7, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newAssassinPosition = new Position(6, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newAssassinPosition = new Position(5, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newAssassinPosition = new Position(5, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        dmc.tick(Direction.RIGHT);

        assertDoesNotThrow(() -> dmc.interact(a.getId()));
        AllyAssassin ally = (AllyAssassin) dmc.getGame().getEntities().get("assassin").get(0);
        assertEquals(ally.getIsAlly(), true);

        res = dmc.tick(Direction.LEFT);
        
        newAssassinPosition = new Position(6, 1, 0);
        assertEquals(newAssassinPosition, ally.getPosition());

        res = dmc.tick(Direction.LEFT);
        
        newAssassinPosition = new Position(5, 1, 0);
        assertEquals(newAssassinPosition, ally.getPosition());
    }

    @Test
    @DisplayName("Assassin Bribe Expected Unsuccessfull and Follow Player")
    public void AssassinBribeExpectedUnsuccessful() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassin", "c_fail_rate_1");
        Position newAssassinPosition;

        res = dmc.tick(Direction.RIGHT);
        Assassin a = (Assassin) dmc.getGame().getEntities().get("assassin").get(0);
        
        newAssassinPosition = new Position(7, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newAssassinPosition = new Position(6, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newAssassinPosition = new Position(5, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newAssassinPosition = new Position(5, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());

        dmc.tick(Direction.RIGHT);

        assertDoesNotThrow(() -> dmc.interact(a.getId()));
        assertEquals(a.getIsAlly(), false);
    }
}
