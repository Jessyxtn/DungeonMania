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
import dungeonmania.entities.buildableEntities.BuildableEntity;
import dungeonmania.entities.buildableEntities.Shield;


public class ShieldTests {
    @Test
    @DisplayName("Test build shield with 2 wood and 1 treasure")
    public void testBuildShield1() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("shield", i.getBuildables(g).get(0));
        
        i.buildShield(g);
        assertEquals(0, i.getBuildables(g).size());
        res = dmc.tick(Direction.RIGHT);

        ////assertEquals(0, getInventory(res, "wood").size());
        //assertEquals(0, getInventory(res, "treasure").size());
        //assertEquals(1, getInventory(res, "shield").size());
        assertEquals(0, g.getInventory().getItems().get("wood").size());
        assertEquals(0, g.getInventory().getItems().get("treasure").size());
        assertEquals(1, g.getInventory().getItems().get("shield").size());
    }

    @Test
    @DisplayName("Test build shield with 2 wood and 1 key")
    public void testBuildShield2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "key").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("shield", i.getBuildables(g).get(0));

        i.buildShield(g);
        assertEquals(0, i.getBuildables(g).size());
        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, g.getInventory().getItems().get("wood").size());
        assertEquals(0, g.getInventory().getItems().get("key").size());
        assertEquals(1, g.getInventory().getItems().get("shield").size());
        //assertEquals(0, getInventory(res, "key").size());
        //assertEquals(1, getInventory(res, "shield").size());
    }
    
    @Test
    @DisplayName("Test new shield durability is correct")
    public void newShieldDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        Game g = dmc.getGame();

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "key").size());

        Inventory i = g.getInventory();
        i.buildShield(g);
        res = dmc.tick(Direction.RIGHT);

        Shield shieldId = (Shield)(BuildableEntity) g.getInventory().getItems().get("shield").get(0);
        double durability = shieldId.getDurability();
        assertEquals(3.0, durability);
    } 

    @Test
    @DisplayName("Test shield durability is decreased")
    public void reducedShieldDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        Game g = dmc.getGame();

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "key").size());

        Inventory i = g.getInventory();
        i.buildShield(g);
        res = dmc.tick(Direction.RIGHT);

        Shield shieldId = (Shield)(BuildableEntity) g.getInventory().getItems().get("shield").get(0);

        // Use shield once
        shieldId.useItem();

        double durability = shieldId.getDurability();
        assertEquals(2.0, durability);
    } 

    @Test
    @DisplayName("Test shield is broken")
    public void brokenShield() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        Game g = dmc.getGame();

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "key").size());

        Inventory i = g.getInventory();
        i.buildShield(g);
        Shield shieldId = (Shield)(BuildableEntity) g.getInventory().getItems().get("shield").get(0);

        // Use shield 3 times
        shieldId.useItem();
        shieldId.useItem();
        shieldId.useItem();
        
        double durability = shieldId.getDurability();
        assertEquals(0.0, durability);
    } 

    @Test
    @DisplayName("Test shield is removed from inventory")
    public void shieldRemovedFromInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        Game g = dmc.getGame();

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "key").size());

        Inventory i = g.getInventory();
        i.buildShield(g);
        Shield shieldId = (Shield)(BuildableEntity) g.getInventory().getItems().get("shield").get(0);

        // Use shield 3 times
        shieldId.useItem();
        shieldId.useItem();
        shieldId.useItem();

        res = dmc.tick(Direction.RIGHT);

        double durability = shieldId.getDurability();
        assertEquals(0.0, durability);

        assertEquals(0, getInventory(res, "shield").size());
    } 
}
