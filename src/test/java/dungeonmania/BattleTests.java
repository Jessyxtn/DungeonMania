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


public class BattleTests {
    @Test
    @DisplayName("Test player vs spider default")
    public void playerAndSpider() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

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
        assertEquals(-2.0, spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(9.9, round1PlayerHealth);
        assertEquals(3.0, round1SpiderHealth);
    }

    @Test
    @DisplayName("Test player vs zombie default")
    public void playerAndZombie() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BattleTest_Zombie", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        // Start battle
        Position p = new Position(2, 2);
        ZombieToast z = new ZombieToast("zombie_toast", p, 5.0, 1.0);
        g.addEntity(z);

        g.updateMovingEntites(P, g);
        Double initialPlayerHealth = g.getBattles().get(0).getInitialPlayerHealth();
        Double initialZombieHealth = g.getBattles().get(0).getInitialEnemyHealth();
        assertEquals(10.0, initialPlayerHealth);
        assertEquals(5.0, initialZombieHealth);

        Double playerHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(-0.1, playerHealthDelta);
        assertEquals(-2.0, spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1ZombieHealth = z.getHealth();
        assertEquals(9.9, round1PlayerHealth);
        assertEquals(3.0, round1ZombieHealth);
    }

    @Test
    @DisplayName("Test player vs multipe spiders")
    public void multipleSpiderBattle() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

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
        assertEquals(-2.0, spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(9.9, round1PlayerHealth);
        assertEquals(3.0, round1SpiderHealth);

        // Battle new spider test
        res = dmc.tick(Direction.DOWN);
        p = new Position(2, 3);
        Spider s1 = new Spider("spider", p, 5.0, 1.0);
        g.addEntity(s1);
        g.updateMovingEntites(P, g);

        // Check that second spider and player are at same cell
        int px = P.getPosition().getX();
        int py = P.getPosition().getY();
        int sx = s1.getPosition().getX();
        int sy = s1.getPosition().getY();
        assertEquals(2, px);
        assertEquals(2, py);
        assertEquals(2, sx);
        assertEquals(2, sy);

        Double playerHealthDelta2 = g.getBattles().get(1).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta2 = g.getBattles().get(1).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(-0.1, playerHealthDelta2);
        assertEquals(-2.0, spiderHealthDelta2);

        // Check second spider health decreased
        assertEquals(3.0, s1.getHealth());
    }

    @Test
    @DisplayName("Test spider killed and removed from game")
    public void spiderKilledByPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

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
        assertEquals(-2.0, spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(9.9, round1PlayerHealth);
        assertEquals(3.0, round1SpiderHealth);

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getEntities(res, "spider").size());

        int px = P.getPosition().getX();
        int py = P.getPosition().getY();
        int sx = s.getPosition().getX();
        int sy = s.getPosition().getY();
        assertEquals(3, px);
        assertEquals(1, py);
        assertEquals(3, sx);
        assertEquals(1, sy);

        // Battle same spider round 2
        g.updateMovingEntites(P, g);
        Double DeltaPlayerHealth2 = g.getBattles().get(0).getRounds().get(1).getDeltaCharacterHealth();
        Double DeltaSpiderHealth2 = g.getBattles().get(0).getRounds().get(1).getDeltaEnemyHealth();
        assertEquals(0.1, -DeltaPlayerHealth2);
        assertEquals(2.0, -DeltaSpiderHealth2);

        Double round2PlayerHealth = P.getHealth();
        Double round2SpiderHealth = s.getHealth();
        assertEquals(9.8, round2PlayerHealth);
        assertEquals(1.0, round2SpiderHealth);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        g.updateMovingEntites(P, g);
         px = P.getPosition().getX();
         py = P.getPosition().getY();
         sx = s.getPosition().getX();
         sy = s.getPosition().getY();
        assertEquals(1, px);
        assertEquals(2, py);
        assertEquals(1, sx);
        assertEquals(2, sy);

        // Battle same spider round 3
        g.updateMovingEntites(P, g);
        Double round3PlayerHealth = P.getHealth();
        Double round3SpiderHealth = s.getHealth();
        assertEquals(9.700000000000001, round3PlayerHealth);
        assertEquals(-1.0, round3SpiderHealth);

        res = dmc.tick(Direction.DOWN);
        assertEquals(0, getEntities(res, "spider").size());
    }

    @Test
    @DisplayName("Test player with sword vs spider")
    public void swordAttackPower() {
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

        String swordId = g.getBattles().get(0).getRounds().get(0).getWeaponryUsed().get(0).getType();
        assertEquals("sword", swordId);
    }   

    @Test
    @DisplayName("Test player with bow vs spider")
    public void bowAttackPower() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ArrowTest_pickingUp", "c_SwordTest_pickingUp");
        
        // Pickup bow materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        i.buildBow(g);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bow").size());

        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        Double bowAttack = g.getTotalBowAttack();
        assertEquals(2.0, bowAttack);

        // Start battle
        Position p = new Position(6, 2);
        Spider s = new Spider("spider", p, 5.0, 1.0);
        g.addEntity(s);

        g.updateMovingEntites(P, g);
        Double initialPlayerHealth = g.getBattles().get(0).getInitialPlayerHealth();
        Double initialSpiderHealth = g.getBattles().get(0).getInitialEnemyHealth();
        assertEquals(10.0, initialPlayerHealth);
        assertEquals(5.0, initialSpiderHealth);

        Double playerHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(0.1, -playerHealthDelta);
        assertEquals(4.0, -spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(9.9, round1PlayerHealth);
        assertEquals(1.0, round1SpiderHealth);
    }

    @Test
    @DisplayName("Test player with shield vs spider")
    public void shieldDefencePower() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        
        // Pickup bow materials
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

        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        Double shieldDefence = g.getTotalShieldDefence();
        assertEquals(1.0, shieldDefence);

        // Start battle
        Position p = new Position(5, 2);
        Spider s = new Spider("spider", p, 5.0, 1.0);
        g.addEntity(s);

        g.updateMovingEntites(P, g);
        Double initialPlayerHealth = g.getBattles().get(0).getInitialPlayerHealth();
        Double initialSpiderHealth = g.getBattles().get(0).getInitialEnemyHealth();
        assertEquals(10.0, initialPlayerHealth);
        assertEquals(5.0, initialSpiderHealth);

        Double playerHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(0.0, -playerHealthDelta);
        assertEquals(2.0, -spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(10.0, round1PlayerHealth);
        assertEquals(3.0, round1SpiderHealth);
    }
 
    @Test
    @DisplayName("Test player with sword, bow, shield vs spider")
    public void allWeaponsTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BattleTest_AllWeapons", "c_SwordTest_pickingUp");

        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        i.buildBow(g);
        
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());

        i.buildShield(g);
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "bow").size());

        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        Position p = new Position(8, 4);
        Spider s = new Spider("spider", p, 5.0, 1.0);
        g.addEntity(s);

        g.updateMovingEntites(P, g);
        Double initialPlayerHealth = g.getBattles().get(0).getInitialPlayerHealth();
        Double initialSpiderHealth = g.getBattles().get(0).getInitialEnemyHealth();
        assertEquals(10.0, initialPlayerHealth);
        assertEquals(5.0, initialSpiderHealth);

        Double playerHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(0.0, -playerHealthDelta);
        assertEquals(4.8, -spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        double round1SpiderHealth = s.getHealth();
        assertEquals(10.0, round1PlayerHealth);
        assertEquals(0.20000000000000018, round1SpiderHealth);

        String swordId = g.getBattles().get(0).getRounds().get(0).getWeaponryUsed().get(0).getType();
        String bowId = g.getBattles().get(0).getRounds().get(0).getWeaponryUsed().get(1).getType();
        String shieldId = g.getBattles().get(0).getRounds().get(0).getWeaponryUsed().get(2).getType();

        assertEquals("sword", swordId);
        assertEquals("bow", bowId);
        assertEquals("shield", shieldId);
    }
    
    @Test
    @DisplayName("Test all weapons used and broken")
    public void weaponsBrokenTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BattleTest_AllWeapons", "c_SwordTest_pickingUp");

        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());

        Game g = dmc.getGame();
        Sword swordId = (Sword)(CollectableEntity) g.getInventory().getItems().get("sword").get(0);
        assertEquals(3.0, swordId.getDurability());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(3, getInventory(res, "arrow").size());

        
        Inventory i = g.getInventory();
        i.buildBow(g);
        Bow bowId = (Bow)(BuildableEntity) g.getInventory().getItems().get("bow").get(0);
        
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "treasure").size());

        i.buildShield(g);
        Shield shieldId = (Shield)(BuildableEntity) g.getInventory().getItems().get("shield").get(0);
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "shield").size());
        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "bow").size());

        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        Position p = new Position(8, 4);
        Spider s = new Spider("spider", p, 5.0, 1.0);
        g.addEntity(s);

        // Use weapons once
        g.updateMovingEntites(P, g);
        
        Spider s1 = new Spider("spider", p, 5.0, 1.0);
        g.addEntity(s1);

        // Use weapons twice
        g.updateMovingEntites(P, g);

        Spider s2 = new Spider("spider", p, 5.0, 1.0);
        g.addEntity(s2);

        // Use weapons thrice
        g.updateMovingEntites(P, g);

        assertEquals(0.0, swordId.getDurability());
        assertEquals(0.0, shieldId.getDurability());
        assertEquals(0.0, bowId.getDurability());
        
        res = dmc.tick(Direction.RIGHT);
        
        assertEquals(0, getInventory(res, "sword").size());
        assertEquals(0, getInventory(res, "shield").size());
        assertEquals(0, getInventory(res, "bow").size());
    }

    @Test
    @DisplayName("Test player is killed")
    public void playerKilledAndRemoved() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_ShieldTest_pickingUp", "c_SwordTest_pickingUp");
        
        res = dmc.tick(Direction.RIGHT);

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        // Start battle
        Position p = new Position(2, 2);
        Spider s = new Spider("spider", p, 5.0, 100.0);
        g.addEntity(s);

        g.updateMovingEntites(P, g);
        Double initialPlayerHealth = g.getBattles().get(0).getInitialPlayerHealth();
        Double initialSpiderHealth = g.getBattles().get(0).getInitialEnemyHealth();
        assertEquals(10.0, initialPlayerHealth);
        assertEquals(5.0, initialSpiderHealth);

        Double playerHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(-10.0, playerHealthDelta);
        assertEquals(-2.0, spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(0.0, round1PlayerHealth);
        assertEquals(3.0, round1SpiderHealth);

        // Check player is killed
        assertEquals(0.0, P.getHealth());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test ally increases player's points")
    public void playerAndAlly() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenary", "c_SwordTest_pickingUp");
        
        Position newMercenaryPosition;

        res = dmc.tick(Direction.RIGHT);
        Mercenary m = (Mercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        
        newMercenaryPosition = new Position(7, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newMercenaryPosition = new Position(6, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newMercenaryPosition = new Position(5, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        res = dmc.tick(Direction.RIGHT);
        
        newMercenaryPosition = new Position(5, 1, 0);
        assertEquals(newMercenaryPosition, m.getPosition());

        dmc.tick(Direction.RIGHT);

        assertDoesNotThrow(() -> dmc.interact(m.getId()));
        AllyMercenary ally = (AllyMercenary) dmc.getGame().getEntities().get("mercenary").get(0);
        assertEquals(ally.getIsAlly(), true);

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;

        int px = P.getPosition().getX();
        int py = P.getPosition().getY();
        assertEquals(6, px);
        assertEquals(1, py);

        // Start battle
        Position p = new Position(6, 2);
        Spider s = new Spider("spider", p, 5.0, 10.0);
        g.addEntity(s);
        

        g.updateMovingEntites(P, g);
        int px1 = s.getPosition().getX();
        int py1 = s.getPosition().getY();
        assertEquals(6, px1);
        assertEquals(1, py1);

        Double initialPlayerHealth = g.getBattles().get(1).getInitialPlayerHealth();
        Double initialSpiderHealth = g.getBattles().get(1).getInitialEnemyHealth();
        assertEquals(9.8, initialPlayerHealth);
        assertEquals(5.0, initialSpiderHealth);

        Double playerHealthDelta = g.getBattles().get(1).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta = g.getBattles().get(1).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(-0.7, playerHealthDelta);
        assertEquals(-2.6, spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(9.100000000000001, round1PlayerHealth);
        assertEquals(2.4, round1SpiderHealth);

    }

    
}
