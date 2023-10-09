package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.getInventory;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.MovingEntity;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.entities.movingEntities.ZombieToast;
import dungeonmania.response.models.DungeonResponse;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class PotionTests {
    @Test
    @DisplayName("Test that potion is added to inventory when picked up")
    public void testPlayerPickUpPotions() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_PotionsTest_pickingUp", "c_PotionsTest_pickingUp");

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
       
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        // pick up the invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());

    }

    @Test
    @DisplayName("Test that once potion is used, no longer in inventory")
    public void testPlayerUsePotions() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_PotionsTest_pickingUp", "c_PotionsTest_pickingUp");

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
       
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        // pick up the invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
      
        // Use a invincibility potion
        res = dmc.tick(Direction.DOWN);
        String InvincibilityPotionId = getInventory(res, "invincibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvincibilityPotionId));
        assertEquals(0, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());

        // Use a invincibility potion
        res = dmc.tick(Direction.DOWN);
        String InvisibilityPotionId = getInventory(res, "invisibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvisibilityPotionId));
        assertEquals(0, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getInventory(res, "invisibility_potion").size());


    }

    @Test
    @DisplayName("Test that once potion is used, status changes")
    public void testPlayerStatus() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_PotionsTest_pickingUp", "c_PotionsTest_pickingUp");

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
       
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        // pick up the invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
      
        // Use a invincibility potion
        res = dmc.tick(Direction.DOWN);
        String InvincibilityPotionId = getInventory(res, "invincibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvincibilityPotionId));
        assertEquals(0, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
        // Player beomces invincible
        assertEquals(true, dmc.getGame().getStatus().isInvincible()); 

        // Use a invincibility potion
        String InvisibilityPotionId = getInventory(res, "invisibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvisibilityPotionId));
        assertEquals(0, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getInventory(res, "invisibility_potion").size());
        // Player should still be invincible
        assertEquals(true, dmc.getGame().getStatus().isInvincible()); 
        
    }
    @Test
    @DisplayName("Test that once potion is used, after the duration ends, the status is normal")
    public void testPotionDurationOver() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_PotionsTest_pickingUp", "c_PotionsTest_pickingUp");

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
       
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        // pick up the invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
      
        // Use a invincibility potion
        res = dmc.tick(Direction.DOWN);
        String InvincibilityPotionId = getInventory(res, "invincibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvincibilityPotionId));
        assertEquals(0, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
        // Player beomces invincible
        assertEquals(true, dmc.getGame().getStatus().isInvincible());  

        res = dmc.tick(Direction.DOWN);
        assertEquals(true, dmc.getGame().getStatus().isInvincible());  
       
        res = dmc.tick(Direction.DOWN);
        assertEquals(true, dmc.getGame().getStatus().isInvincible()); 
         // 3 ticks is over, duration should be finished.
        res = dmc.tick(Direction.DOWN);
        assertEquals(false, dmc.getGame().getStatus().isInvincible()); 
    }
    @Test
    @DisplayName("Test the queueing of potions")
    public void testPotionQueueing() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_PotionsTest_pickingUp", "c_PotionsTest_pickingUp");

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
       
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        // pick up the invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
      
        // Use a invincibility potion
        res = dmc.tick(Direction.DOWN);
        String InvincibilityPotionId = getInventory(res, "invincibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvincibilityPotionId));
        assertEquals(0, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
        // Player beomces invincible
        assertEquals(true, dmc.getGame().getStatus().isInvincible()); 
        
        // Use a invisibility potion 
        res = dmc.tick(Direction.DOWN);
        String InvisibilityPotionId = getInventory(res, "invisibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvisibilityPotionId));
        assertEquals(0, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getInventory(res, "invisibility_potion").size());
        assertEquals(true, dmc.getGame().getStatus().isInvincible()); 
        
        res = dmc.tick(Direction.DOWN);
        // 3 ticks is over, duration should be finished, now player should be invisible.
        assertEquals(false, dmc.getGame().getStatus().isInvincible()); 
        assertEquals(true, dmc.getGame().getStatus().isInvisible()); 
        
         
    }
    @Test
    @DisplayName("Test that Invisible is active, No battles occur")
    public void testInvisibleActiveBattle() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_InvisiblePotionBattleTest", "c_PotionsTest_pickingUp");
        
        // pick up the invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "invisibility_potion").size());
      
        // Use a invisibility potion 
        String InvisibilityPotionId = getInventory(res, "invisibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvisibilityPotionId));
        assertEquals(0, getInventory(res, "invisibility_potion").size());
        assertEquals(true, dmc.getGame().getStatus().isInvisible()); 
        
        // Player now at [3,2]
        // ADD enemies next to and no battle response should be triggered
        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        // Start battle
        Position p = new Position(3, 2);
        ZombieToast z = new ZombieToast("zombie_toast", p, 5.0, 1.0);
        g.addEntity(z);

        g.updateMovingEntites(P, g);
        // No battle occurred
        assertEquals(0, (double) g.getBattles().size());
        assertEquals(true, dmc.getGame().getStatus().isInvisible()); 
    }
    
}
