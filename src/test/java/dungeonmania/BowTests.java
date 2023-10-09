package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;


import dungeonmania.entities.buildableEntities.BuildableEntity;
import dungeonmania.entities.buildableEntities.Bow;
import dungeonmania.entities.buildableEntities.Shield;


public class BowTests {
    @Test
    @DisplayName("Test build bow with 1 wood and 3 arrows")
    public void testBuildBow() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ArrowTest_pickingUp", "c_SwordTest_pickingUp");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("bow", i.getBuildables(g).get(0));
        i.buildBow(g);
        assertEquals(0, i.getBuildables(g).size());

        res = dmc.tick(Direction.RIGHT);
        //assertEquals(1, getInventory(res, "bow").size());
        assertEquals(1, g.getInventory().getItems().get("bow").size());
        assertEquals(0, g.getInventory().getItems().get("wood").size());
        assertEquals(0, g.getInventory().getItems().get("arrow").size());
       
        //assertEquals(0, getInventory(res, "wood").size());
        //assertEquals(0, getInventory(res, "arrow").size());
        //assertEquals(1, getInventory(res, "bow").size());
    }

    @Test
    @DisplayName("Test new bow durability is correct")
    public void newBowDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ArrowTest_pickingUp", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        i.buildBow(g);

        Bow bowId = (Bow)(BuildableEntity) g.getInventory().getItems().get("bow").get(0);
        double durability = bowId.getDurability();
        assertEquals(3.0, durability);
    } 

    @Test
    @DisplayName("Test bow durability is decreased")
    public void reducedBowDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ArrowTest_pickingUp", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        i.buildBow(g);

        Bow bowId = (Bow)(BuildableEntity) g.getInventory().getItems().get("bow").get(0);

        // Use bow once
        bowId.useItem();
        
        double durability = bowId.getDurability();
        assertEquals(2.0, durability);
    } 

    @Test
    @DisplayName("Test bow is broken")
    public void brokenBow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ArrowTest_pickingUp", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        i.buildBow(g);

        Bow bowId = (Bow)(BuildableEntity) g.getInventory().getItems().get("bow").get(0);

        // Use bow 3 times
        bowId.useItem();
        bowId.useItem();
        bowId.useItem();
        
        double durability = bowId.getDurability();
        assertEquals(0.0, durability);
    } 

    @Test
    @DisplayName("Test bow is removed from inventory")
    public void bowRemovedFromInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ArrowTest_pickingUp", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        i.buildBow(g);

        Bow bowId = (Bow)(BuildableEntity) g.getInventory().getItems().get("bow").get(0);

        // Use bow 3 times
        bowId.useItem();
        bowId.useItem();
        bowId.useItem();

        double durability = bowId.getDurability();
        assertEquals(0.0, durability);

        assertEquals(0, getInventory(res, "bow").size());
    } 
}
