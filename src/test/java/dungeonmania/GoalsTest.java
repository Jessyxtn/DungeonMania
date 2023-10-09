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

public class GoalsTest {
    @Test
    @DisplayName("Collecting a certain number of treasure OR having a boulder on all floor switches - treasure")
    public void testGoalsTreasureOrBoulders1() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_treasureOrBouldersTest", "c_treasureOrBouldersTest");

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":boulders"));
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));

    }

    @Test
    @DisplayName("Collecting a certain number of treasure OR having a boulder on all floor switches - boulder")
    public void testGoalsTreasureOrBoulders2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_treasureOrBouldersTest", "c_treasureOrBouldersTest");

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":exit"));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test goals killing enemies or getting to an exit - enemies")
    public void testGoalsEnemiesOrExit1() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemiesOrExitGoals", "c_treasureOrBouldersTest");
        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test goals killing enemies or getting to an exit - exit")
    public void testGoalsEnemiesOrExit2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemiesOrExitGoals", "c_treasureOrBouldersTest");
        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));

        // go to exit
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals("", getGoals(res));
    }
}
