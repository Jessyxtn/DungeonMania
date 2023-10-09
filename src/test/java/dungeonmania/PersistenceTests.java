package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectableEntities.CollectableEntity;
import dungeonmania.entities.collectableEntities.Sword;
import dungeonmania.entities.movingEntities.AllyMercenary;
import dungeonmania.entities.movingEntities.Assassin;
import dungeonmania.entities.movingEntities.Hydra;
import dungeonmania.entities.movingEntities.Mercenary;
import dungeonmania.entities.movingEntities.MovingEntity;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.entities.movingEntities.Spider;
import dungeonmania.entities.staticEntities.SwitchDoor;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PersistenceTests {
    
    @Test
    @DisplayName("Test the position of player is the same after saving and loading")
    public void persistencePositionOfPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");

        // check initial position 
        assertEquals(new Position(1, 1), getEntities(res, "player").get(0).getPosition());

        // save
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest");
        assertEquals(new Position(1, 1), getEntities(res, "player").get(0).getPosition());

        // check position after moving 
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 3), getEntities(res, "player").get(0).getPosition());

        // save
        dmc.saveGame("ShieldTest");

        // load game from new controller
        newDMC = new DungeonManiaController();
        res = newDMC.loadGame("ShieldTest");
        assertEquals(new Position(3, 3), getEntities(res, "player").get(0).getPosition());
        
    }

    @Test
    @DisplayName("Test the inventory of a player is same - collectible entities")
    public void persistenceInventoryCollectibles() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "key").size());

        // save
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest");    
        
        // inventory should still be the same as before
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "key").size());
    }

    @Test
    @DisplayName("Test the inventory of a player is same - buildable entities")
    public void persistenceInventoryBuildables() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        i.buildShield(g);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getInventory(res, "shield").size());

        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(0, getInventory(res, "treasure").size());
        assertEquals(0, getInventory(res, "wood").size());


        // save
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest");    
        
        // inventory should still be the same as before
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "shield").size());
        
    }

    @Test
    @DisplayName("Test the inventory of a player is same")
    public void persistenceInventory() {
        // Test bomb, tests sword, test potions, test sword, 
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTests", "c_SwordTest_pickingUp");
        // picking up everything
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        
        // Inventory before building
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "bomb").size());
        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());

        // build bow
        try {
            dmc.build("bow");
        } catch (IllegalArgumentException | InvalidActionException e) {
            e.printStackTrace();
        }
        res = dmc.tick(Direction.DOWN);

        // inventory after building
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "bomb").size());
        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "key").size());    
        assertEquals(1, getInventory(res, "bow").size());
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
    
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        // save
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest");  

        // inventory should be the same pre-save
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "bomb").size());
        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(1, getInventory(res, "key").size());    
        assertEquals(1, getInventory(res, "bow").size());
        assertEquals(1, getInventory(res, "invincibility_potion").size());
        assertEquals(1, getInventory(res, "invisibility_potion").size());
    }

    @Test
    @DisplayName("Test the status of potions are still in effect")
    public void persistencePotionStatus() {
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

        // save game
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest");
        // Player should still be invinvible after loading
        assertEquals(true, dmc.getGame().getStatus().isInvincible()); 


        // Use a invisibility potion
        String InvisibilityPotionId = getInventory(res, "invisibility_potion").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(InvisibilityPotionId));
        assertEquals(0, getInventory(res, "invincibility_potion").size());
        assertEquals(0, getInventory(res, "invisibility_potion").size());

        // Player should still be invincible
        assertEquals(true, dmc.getGame().getStatus().isInvincible()); 


        // save game
        dmc.saveGame("persistenceTest");

        // new application + load the game
        newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest");

        // Invincibility potion should run out and now is invisible
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(true, dmc.getGame().getStatus().isInvisible()); 

    }  
    
    
    @Test
    @DisplayName("Test the durabilities of weapons")
    public void persistenceWeaponDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwordTest_pickingUp", "c_SwordTest_pickingUp");
        
        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());

        Game g = dmc.getGame();
        Sword swordId = (Sword)(CollectableEntity) g.getInventory().getItems().get("sword").get(0);

        // Use sword once
        swordId.useSword();

        double durability = swordId.getDurability();
        assertEquals(2.0, durability);

        // save game
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest");

        // durability of the sword should still be the same 
        g = newDMC.getGame();
        swordId = (Sword)(CollectableEntity) g.getInventory().getItems().get("sword").get(0);
        durability = swordId.getDurability();
        assertEquals(2.0, durability);

    }

    @Test
    @DisplayName("Test the dungeon map looks the same after saving and loading")
    public void persistenceDungeonMap() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombBoulderSwitchTests_bombRadius1", "c_bombBoulderSwitchTests_bombRadius3");

        assertEquals(new Position(3, 2), getEntities(res, "switch").get(0).getPosition());
        assertEquals(new Position(1, 0), getEntities(res, "bomb").get(0).getPosition());
        assertEquals(new Position(2, 2), getEntities(res, "boulder").get(0).getPosition());
        assertEquals(new Position(1, 1), getEntities(res, "wall").get(0).getPosition());
        assertEquals(new Position(2, 1), getEntities(res, "wall").get(1).getPosition());
        assertEquals(new Position(3, 1), getEntities(res, "wall").get(2).getPosition());
        assertEquals(new Position(1, 3), getEntities(res, "treasure").get(0).getPosition());
        assertEquals(new Position(2, 3), getEntities(res, "treasure").get(1).getPosition());
        assertEquals(new Position(3, 3), getEntities(res, "treasure").get(2).getPosition());
        assertEquals(new Position(2, 0), getEntities(res, "treasure").get(3).getPosition());
        assertEquals(new Position(0, 0), getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(8, 8), getEntities(res, "exit").get(0).getPosition());


        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(3, getEntities(res, "wall").size());
        assertEquals(4, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());

        // save game
        dmc.saveGame("persistenceTest1");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest1");

        // position of entire dungeon should still be the same
        assertEquals(new Position(3, 2), getEntities(res, "switch").get(0).getPosition());
        assertEquals(new Position(1, 0), getEntities(res, "bomb").get(0).getPosition());
        assertEquals(new Position(2, 2), getEntities(res, "boulder").get(0).getPosition());
        assertEquals(new Position(1, 1), getEntities(res, "wall").get(0).getPosition());
        assertEquals(new Position(2, 1), getEntities(res, "wall").get(1).getPosition());
        assertEquals(new Position(3, 1), getEntities(res, "wall").get(2).getPosition());
        assertEquals(new Position(1, 3), getEntities(res, "treasure").get(0).getPosition());
        assertEquals(new Position(2, 3), getEntities(res, "treasure").get(1).getPosition());
        assertEquals(new Position(3, 3), getEntities(res, "treasure").get(2).getPosition());
        assertEquals(new Position(2, 0), getEntities(res, "treasure").get(3).getPosition());
        assertEquals(new Position(0, 0), getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(8, 8), getEntities(res, "exit").get(0).getPosition());  
    }


    @Test
    @DisplayName("Test the stats of enemies are still the same")
    public void persistenceEnemyStats() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BattleTest", "c_SwordTest_pickingUp");
        
        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        Double swordAttack = g.getTotalSwordAttack();
        assertEquals(2.0, swordAttack);

        // Start battle
        Position p = new Position(2, 2);
        Spider s = new Spider("spider", p, 5.0, 1.0);
        g.addEntity(s);

        g.updateMovingEntites(P, g);
        Double initialPlayerHealth = g.getBattles().get(0).getInitialPlayerHealth();
        Double initialSpiderHealth = g.getBattles().get(0).getInitialEnemyHealth();
        assertEquals(10.0, initialPlayerHealth);
        assertEquals(5.0, initialSpiderHealth);

        Double playerHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(-0.1, playerHealthDelta);
        assertEquals(-2.4, spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(9.9, round1PlayerHealth);
        assertEquals(2.6, round1SpiderHealth);

        // save game
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        newDMC.allGames();
        res = newDMC.loadGame("persistenceTest");
        
        // health of player and spider should be the same as before the save game
        round1PlayerHealth = P.getHealth();
        round1SpiderHealth = s.getHealth();
        assertEquals(9.9, round1PlayerHealth);
        assertEquals(2.6, round1SpiderHealth);
    }

    @Test
    @DisplayName("Test the allied mercenaries are still allies")
    public void persistenceAlliedMercenaries() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenary", "c_bribe_radius_1");
        Position newMercenaryPosition;

        res = dmc.tick(Direction.RIGHT);
        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        assertEquals(new Position(7, 1, 0), m.getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1, 0), m.getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1, 0), m.getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1, 0), m.getPosition());

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

        // save game
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        res = newDMC.loadGame("persistenceTest");

        // Mercenary should still be ally after game is loaded
        ally = (AllyMercenary) newDMC.getGame().getEntities().get("mercenary").get(0);
        assertEquals(ally.getIsAlly(), true);
    }


    @Test
    @DisplayName("Test persistence of goals")
    public void persistenceComplexGoals() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_andAll", "c_complexGoalsTest_andAll");

        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":enemies"));

        // save game
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC = new DungeonManiaController();
        res = newDMC.loadGame("persistenceTest");

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":enemies"));

        // save game
        dmc.saveGame("persistenceTest");

        // Invalid ID
        DungeonManiaController newDMC1 = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> newDMC1.loadGame("persistenceTest2"));

        // pickup treasure
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":exit"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":enemies"));

        // save game
        dmc.saveGame("persistenceTest");

        // new application + load the game
        DungeonManiaController newDMC2 = new DungeonManiaController();
        res = newDMC2.loadGame("persistenceTest");

        // move to exit
        res = dmc.tick(Direction.DOWN);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test time travel - picked up item, returns still works")
    public void persistenceTimeTravelItemReturns() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest", "c_movementTest_testMovementDown");
        assertEquals(1, getEntities(res, "wood").size());

        res = dmc.tick(Direction.DOWN);
        // Pick up wood
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(0, getEntities(res, "wood").size());

        res = dmc.tick(Direction.RIGHT);
        // Pick up timeturner
        res = dmc.tick(Direction.DOWN);
      
        assertEquals(1, getInventory(res, "time_turner").size());
        assertEquals(0, getEntities(res, "time_turner").size());

        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest");
        DungeonManiaController newDMC = new DungeonManiaController();
        res = newDMC.loadGame("persistenceTest");

        res = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(1, getEntities(res, "wood").size());
        assertEquals(1, getEntities(res, "time_turner").size());
    }  
    
    @Test
    @DisplayName("Test time travel - older player picked up item, returns still works")
    public void persistenceTimeTravelOlderPlayerPickUpItems() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelMovingEntities", "c_movementTest_testMovementDown");
        // Player starts at [3,0]
        res = dmc.tick(Direction.RIGHT); // Now at [4,0]
        // Pick up timeturner
        res = dmc.tick(Direction.RIGHT); // [5,0]
        res = dmc.tick(Direction.RIGHT); // [6,0]
        res = dmc.tick(Direction.UP); //[6,-1]

        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest");
        DungeonManiaController newDMC = new DungeonManiaController();
        res = newDMC.loadGame("persistenceTest");

        // Rewind 5 tick 
        res = assertDoesNotThrow(() -> dmc.rewind(5));
        assertEquals(1, getEntities(res, "time_turner").size());
        assertEquals(1, getEntities(res, "older_player").size());
        assertEquals(new Position(3, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);

        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest1");
        DungeonManiaController newDMC1 = new DungeonManiaController();
        res = newDMC1.loadGame("persistenceTest1");
        
        
        assertEquals(new Position(4, 0), getEntities(res, "older_player").get(0).getPosition());
        assertEquals(1, getEntities(res, "time_turner").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 0), getEntities(res, "older_player").get(0).getPosition());

        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest");
        DungeonManiaController newDMC2 = new DungeonManiaController();
        res = newDMC2.loadGame("persistenceTest");

        // Time turner picked up
        assertEquals(0, getEntities(res, "time_turner").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 0), getEntities(res, "older_player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, -1), getEntities(res, "older_player").get(0).getPosition());


        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest1");
        DungeonManiaController newDMC3 = new DungeonManiaController();
        res = newDMC3.loadGame("persistenceTest1");
        
        
        res = dmc.tick(Direction.RIGHT);
        // Old player should now be gone
        assertEquals(0, getEntities(res, "older_player").size());
    }

    
    @Test
    @DisplayName("Test persistence - logic switches")
    public void persistenceLogicSwitches() {
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

        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest");
        DungeonManiaController newDMC = new DungeonManiaController();
        res = newDMC.loadGame("persistenceTest");

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

    @Test
    @DisplayName("Test persistence - Hydra")
    public void persistenceHydra() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_BasicHydra", "c_basicHydraConfig");
    
        Position initialHyPos = getEntities(resp, "hydra").get(0).getPosition();
        resp = dmc.tick(Direction.RIGHT);
        assertTrue(Position.isAdjacent(initialHyPos, getEntities(resp, "hydra").get(0).getPosition()));

        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest");
        DungeonManiaController newDMC = new DungeonManiaController();
        resp = newDMC.loadGame("persistenceTest");

        initialHyPos = getEntities(resp, "hydra").get(0).getPosition();
        resp = dmc.tick(Direction.RIGHT);
        assertTrue(Position.isAdjacent(initialHyPos, getEntities(resp, "hydra").get(0).getPosition()));

        initialHyPos = getEntities(resp, "hydra").get(0).getPosition();
        resp = dmc.tick(Direction.UP);
        assertTrue(Position.isAdjacent(initialHyPos, getEntities(resp, "hydra").get(0).getPosition()));
    }


    @Test
    @DisplayName("Test persistence - Assassins bribe unsuccessful works as previously expected")
    public void persistenceAssassins() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassin", "c_fail_rate_1");
        Position newAssassinPosition;

        res = dmc.tick(Direction.RIGHT);
        Assassin a = (Assassin) dmc.getGame().getEntities().get("assassin").get(0);
        newAssassinPosition = new Position(7, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());
        res = dmc.tick(Direction.RIGHT);
        
        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest");
        DungeonManiaController newDMC = new DungeonManiaController();
        res = newDMC.loadGame("persistenceTest");

        newAssassinPosition = new Position(6, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());
        res = dmc.tick(Direction.RIGHT);
        newAssassinPosition = new Position(5, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());
        res = dmc.tick(Direction.RIGHT);
        
        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest");
        DungeonManiaController newDMC1 = new DungeonManiaController();
        res = newDMC1.loadGame("persistenceTest");

        newAssassinPosition = new Position(5, 1, 0);
        assertEquals(newAssassinPosition, a.getPosition());
        dmc.tick(Direction.RIGHT);

        // Save, close current DMC and open new DMC
        dmc.saveGame("persistenceTest");
        DungeonManiaController newDMC2 = new DungeonManiaController();
        res = newDMC2.loadGame("persistenceTest");

        assertDoesNotThrow(() -> dmc.interact(a.getId()));
        assertEquals(a.getIsAlly(), false);
    }

    @Test
    @DisplayName("Test persistence - exception")
    public void persistenceExceptions() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_TimeTravelMovingEntities", "c_movementTest_testMovementDown");
        // Invalid ID
        DungeonManiaController newDMC1 = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> newDMC1.loadGame("unknown"));
    }

}
