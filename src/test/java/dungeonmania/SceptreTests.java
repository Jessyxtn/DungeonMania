package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.MovingEntity;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.entities.movingEntities.Spider;
import dungeonmania.entities.movingEntities.ZombieToast;
import dungeonmania.entities.collectableEntities.Sword;
import dungeonmania.entities.collectableEntities.CollectableEntity;
import dungeonmania.entities.collectableEntities.Treasure;
import dungeonmania.entities.buildableEntities.BuildableEntity;
import dungeonmania.entities.buildableEntities.Bow;
import dungeonmania.entities.buildableEntities.Shield;

import dungeonmania.entities.movingEntities.AllyMercenary;
import dungeonmania.entities.movingEntities.Mercenary;

import dungeonmania.util.Position;

public class SceptreTests {
    // test build with wood + sunstone + key
    @Test
    @DisplayName("Test build sceptre with wood/sunstone/Treasure")
    public void testSceptreWoodSunstoneTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptre", "c_sceptre_midnightArmour");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "treasure").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("sceptre", i.getBuildables(g).get(0));
        assertEquals(1, i.getBuildables(g).size());
        i.buildSceptre(g);
        assertEquals(0, i.getBuildables(g).size());
        
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, g.getInventory().getItems().get("sceptre").size());
        assertEquals(0, g.getInventory().getItems().get("wood").size());
        assertEquals(0, g.getInventory().getItems().get("treasure").size());
        assertEquals(0, g.getInventory().getItems().get("sun_stone").size());
        
    }

    // test build with wood + sunstone + treasure
    @Test
    @DisplayName("Test build sceptre with wood/sunstone/Key")
    public void testSceptreWoodSunstoneKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptre", "c_sceptre_midnightArmour");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "key").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("sceptre", i.getBuildables(g).get(0));
        
        i.buildSceptre(g);
        assertEquals(0, i.getBuildables(g).size());
        res = dmc.tick(Direction.RIGHT);


        assertEquals(1, g.getInventory().getItems().get("sceptre").size());
        assertEquals(0, g.getInventory().getItems().get("wood").size());
        assertEquals(0, g.getInventory().getItems().get("key").size());
        assertEquals(0, g.getInventory().getItems().get("sun_stone").size());
    }

    // test build with 2 arrows + sunstone + key
    @Test
    @DisplayName("Test build sceptre with arrows/sunstone/Key")
    public void testSceptreArrowsSunstoneKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptre", "c_sceptre_midnightArmour");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "key").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("sceptre", i.getBuildables(g).get(0));
        assertEquals(1, i.getBuildables(g).size());
        
        i.buildSceptre(g);
        assertEquals(0, i.getBuildables(g).size());
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, g.getInventory().getItems().get("sceptre").size());
        assertEquals(0, g.getInventory().getItems().get("arrow").size());
        assertEquals(0, g.getInventory().getItems().get("key").size());
        assertEquals(0, g.getInventory().getItems().get("sun_stone").size());
    }

    // test build with 2 arrows + sunstone + treasure
    @Test
    @DisplayName("Test build sceptre with arrows/sunstone/Treasure")
    public void testSceptreArrowsSunstoneTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptre", "c_sceptre_midnightArmour");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "treasure").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("sceptre", i.getBuildables(g).get(0));
        
        i.buildSceptre(g);
        assertEquals(0, i.getBuildables(g).size());
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, g.getInventory().getItems().get("sceptre").size());
        assertEquals(0, g.getInventory().getItems().get("arrow").size());
        assertEquals(0, g.getInventory().getItems().get("treasure").size());
        assertEquals(0, g.getInventory().getItems().get("sun_stone").size());
    }

    // test able to bribe
    @Test
    @DisplayName("Test Sceptre Mind Control")
    public void testSceptreMindControl() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_build_sceptre", "M3_mind_control_10");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        
        i.buildSceptre(g);

        assertEquals(g.getTotalAllyAttack(), 3);
        assertEquals(g.getTotalAllyDefence(), 3);
        
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(g.getTotalAllyAttack(), 0);
        assertEquals(g.getTotalAllyDefence(), 0);
    }


    // test usability and break + removed from inventory
    @Test
    @DisplayName("Test sceptre usability runs out")
    public void testSceptreDuration() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptre", "c_sceptre_midnightArmour");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(1, getInventory(res, "key").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("sceptre", i.getBuildables(g).get(0));
        
        i.buildSceptre(g);
        assertEquals(0, i.getBuildables(g).size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, g.getInventory().getItems().get("sceptre").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, g.getInventory().getItems().get("sceptre").size());
    }
}
