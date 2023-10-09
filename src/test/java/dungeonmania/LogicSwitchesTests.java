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
import dungeonmania.entities.staticEntities.LightBulb;
import dungeonmania.entities.staticEntities.SwitchDoor;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicSwitchesTests {
    @Test
    @DisplayName("Test if AND logic gate works")
    public void ANDLogicSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logical_and", "c_M3_config");
        LightBulb l = (LightBulb) dmc.getGame().getEntities().get("light_bulb_off").get(0);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        
        assertEquals(l.getType(), "light_bulb_on");
    }

    @Test
    @DisplayName("Test if OR logic gate works")
    public void ORLogicSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logical_or", "c_M3_config");
        LightBulb l = (LightBulb) dmc.getGame().getEntities().get("light_bulb_off").get(0);

        res = dmc.tick(Direction.RIGHT);
        
        assertEquals(l.getType(), "light_bulb_on");
    }

    @Test
    @DisplayName("Test if CO_AND logic gate works")
    public void COANDLogicSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logical_co_and", "c_M3_config");
        LightBulb l = (LightBulb) dmc.getGame().getEntities().get("light_bulb_off").get(0);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        
        assertEquals(l.getType(), "light_bulb_on");
    }

    @Test
    @DisplayName("Test if XOR logic gate works")
    public void XORLogicSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logical_xor", "c_M3_config");
        LightBulb l = (LightBulb) dmc.getGame().getEntities().get("light_bulb_off").get(0);

        res = dmc.tick(Direction.RIGHT);

        assertEquals(l.getType(), "light_bulb_on");

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        
        assertEquals(l.getType(), "light_bulb_off");
    }

    @Test
    @DisplayName("Test if Bomb logic entity works")
    public void BombLogicEntity() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logical_bomb", "c_M3_config");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(res.getEntities().size(), 1);
    }

    @Test
    @DisplayName("Test if Switch Door logic entity works")
    public void SwitchDoorLogicEntity() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logical_switch_door", "c_M3_config");
        Player p = (Player) dmc.getGame().getEntities().get("player").get(0);
        SwitchDoor s = (SwitchDoor) dmc.getGame().getEntities().get("switch_door").get(0);

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        Position playerPos = new Position(5, 1);

        assertEquals(p.getPosition(), playerPos);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        assertEquals(p.getPosition(), s.getPosition());
    }
}
