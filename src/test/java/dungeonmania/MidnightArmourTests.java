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
public class MidnightArmourTests {
    // test midnight armour buildable
    @Test
    @DisplayName("Test build midnight armour")
    public void testBuildMidnightArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmour", "c_sceptre_midnightArmour");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "sun_stone").size());

        Game g = dmc.getGame();
        Inventory i = g.getInventory();
        assertEquals("midnight_armour", i.getBuildables(g).get(0));
        assertEquals(1, i.getBuildables(g).size());
        
        i.buildMidnightArmour(g);
        assertEquals(0, i.getBuildables(g).size());
        res = dmc.tick(Direction.DOWN);        

        //assertEquals(0, getInventory(res, "sword").size());
        //assertEquals(0, getInventory(res, "sun_stone").size());
        //assertEquals(1, getInventory(res, "midnight_armour").size());
        assertEquals(1, g.getInventory().getItems().get("midnight_armour").size());
    }

    // test midnight armour not buildable when zombies around
    @Test
    @DisplayName("Test cannot build midnight armour due to zombie")
    public void testCannotBuildArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmour", "c_sceptre_midnightArmour");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "sun_stone").size());

        Game g = dmc.getGame();
        
        Position p = new Position(2, 2);
        ZombieToast s = new ZombieToast("zombie_toast", p, 5.0, 1.0);
        g.addEntity(s);
        Inventory i = g.getInventory();
        assertEquals(0, i.getBuildables(g).size());
        
        i.buildMidnightArmour(g);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(0, getInventory(res, "midnight_armour").size());
    }

    // test  midnight armour increases attack + defence
    @Test
    @DisplayName("Test player vs spider using midnight armour")
    public void testMidnightArmourInBattle() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmour", "c_sceptre_midnightArmour");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());
        assertEquals(1, getInventory(res, "sun_stone").size());

        Game g = dmc.getGame();
        Entity e = g.getPlayer();
        MovingEntity mE = (MovingEntity) e;
        Player P = (Player) mE;
        Position p = new Position(3, 2);
        Spider s = new Spider("spider", p, 5.0, 10.0);
        g.addEntity(s);
        Inventory i = g.getInventory();
        assertEquals("midnight_armour", i.getBuildables(g).get(0));
        i.buildMidnightArmour(g);

        g.updateMovingEntites(P, g);
        Double initialPlayerHealth = g.getBattles().get(0).getInitialPlayerHealth();
        Double initialSpiderHealth = g.getBattles().get(0).getInitialEnemyHealth();
        assertEquals(10.0, initialPlayerHealth);
        assertEquals(5.0, initialSpiderHealth);

        Double playerHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaCharacterHealth();
        Double spiderHealthDelta = g.getBattles().get(0).getRounds().get(0).getDeltaEnemyHealth();
        assertEquals(-0.5, playerHealthDelta);
        assertEquals(-3.0, spiderHealthDelta);
        
        Double round1PlayerHealth = P.getHealth();
        Double round1SpiderHealth = s.getHealth();
        assertEquals(9.5, round1PlayerHealth);
        assertEquals(2.0, round1SpiderHealth);
    }

    
}
