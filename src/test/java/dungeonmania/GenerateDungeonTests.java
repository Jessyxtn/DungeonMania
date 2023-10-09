package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.MovingEntity;
import dungeonmania.entities.movingEntities.Player;

public class GenerateDungeonTests {
    @Test
    @DisplayName("Test dungeon generation standard")
    public void generateDungeon() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(1, 1, 6, 6, "c_SwordTest_pickingUp");
        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;
        assertEquals(1, P.getPosition().getY());
    }

    @Test
    @DisplayName("Test dungeon generation large coordinates")
    public void generateDungeonLarge() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(40, 40, 50, 50, "c_SwordTest_pickingUp");
        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;
        assertEquals(40, P.getPosition().getY());
        assertEquals(40, P.getPosition().getY());
    }

    @Test
    @DisplayName("Test dungeon generation negative coordinates")
    public void generateDungeonNegative() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(-10, -10, 10, 10, "c_SwordTest_pickingUp");
        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;
        assertEquals(-10, P.getPosition().getY());
        assertEquals(-10, P.getPosition().getY());
    }

    @Test
    @DisplayName("Test dungeon generation large negative coordinates")
    public void generateDungeonNegativeLarge() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(-50, -50, -40, -40, "c_SwordTest_pickingUp");
        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;
        assertEquals(-50, P.getPosition().getY());
        assertEquals(-50, P.getPosition().getY());
    }

    @Test
    @DisplayName("Test dungeon generation from origin")
    public void generateDungeonStartOrigin() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(0, 0, 20, 10, "c_SwordTest_pickingUp");
        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;
        assertEquals(0, P.getPosition().getY());
        assertEquals(0, P.getPosition().getY());
    }

    @Test
    @DisplayName("Test dungeon generation end at origin")
    public void generateDungeonEndOrigin() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(-10, -10, 0, 0, "c_SwordTest_pickingUp");
        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;
        assertEquals(-10, P.getPosition().getY());
        assertEquals(-10, P.getPosition().getY());
    }
}
