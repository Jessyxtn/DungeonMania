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
import dungeonmania.entities.collectableEntities.Sword;
import dungeonmania.entities.collectableEntities.CollectableEntity;

public class SwordTests {
    @Test
    @DisplayName("Test that sword is added to inventory when picked up")
    public void pickUpSword() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwordTest_pickingUp", "c_SwordTest_pickingUp");

        // pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());
    }

    @Test
    @DisplayName("Test new sword durability is correct")
    public void newSwordDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwordTest_pickingUp", "c_SwordTest_pickingUp");
        
        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;

        Sword swordId = (Sword)(CollectableEntity) g.getInventory().getItems().get("sword").get(0);
        double durability = swordId.getDurability();
        assertEquals(3.0, durability);
    } 

    @Test
    @DisplayName("Test sword durability is decreased")
    public void reducedSwordDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwordTest_pickingUp", "c_SwordTest_pickingUp");
        
        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Sword swordId = (Sword)(CollectableEntity) g.getInventory().getItems().get("sword").get(0);

        // Use sword once
        swordId.useSword();

        double durability = swordId.getDurability();
        assertEquals(2.0, durability);
    } 

    @Test
    @DisplayName("Test sword is broken")
    public void brokenSword() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwordTest_pickingUp", "c_SwordTest_pickingUp");
        
        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());

        Game g = dmc.getGame();
        Sword swordId = (Sword)(CollectableEntity) g.getInventory().getItems().get("sword").get(0);

        // Use sword 3 times to deplete durability
        swordId.useSword();
        swordId.useSword();
        swordId.useSword();
        
        double durability = swordId.getDurability();
        assertEquals(0.0, durability);
    } 

    @Test
    @DisplayName("Test sword is removed from inventory")
    public void swordRemovedFromInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwordTest_pickingUp", "c_SwordTest_pickingUp");
        
        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());

        Game g = dmc.getGame();
        Sword swordId = (Sword)(CollectableEntity) g.getInventory().getItems().get("sword").get(0);

        // Use sword 3 times to deplete durability
        swordId.useSword();
        swordId.useSword();
        swordId.useSword();

        double durability = swordId.getDurability();
        assertEquals(0.0, durability);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, getInventory(res, "sword").size());
    } 
}
