package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.Hydra;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class HydraTests {
    
    @Test
    @DisplayName("Testing basic movement of the Hydra")
    public void moveHydraOnce() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_BasicHydra", "c_basicHydraConfig");
        
        Position initialHyPos = getEntities(resp, "hydra").get(0).getPosition();
        resp = dmc.tick(Direction.RIGHT);
        assertTrue(Position.isAdjacent(initialHyPos, getEntities(resp, "hydra").get(0).getPosition()));
    }

    @Test
    @DisplayName("Moves a hydra multile times after a number of ticks")
    public void moveHydraMultipleTimes() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_BasicHydra", "c_basicHydraConfig");
    
        Position initialHyPos = getEntities(resp, "hydra").get(0).getPosition();
        resp = dmc.tick(Direction.RIGHT);
        assertTrue(Position.isAdjacent(initialHyPos, getEntities(resp, "hydra").get(0).getPosition()));

        initialHyPos = getEntities(resp, "hydra").get(0).getPosition();
        resp = dmc.tick(Direction.RIGHT);
        assertTrue(Position.isAdjacent(initialHyPos, getEntities(resp, "hydra").get(0).getPosition()));

        initialHyPos = getEntities(resp, "hydra").get(0).getPosition();
        resp = dmc.tick(Direction.UP);
        assertTrue(Position.isAdjacent(initialHyPos, getEntities(resp, "hydra").get(0).getPosition()));
    }

    @Test
    @DisplayName("Test that a hydra cannot move, because it is blocked by static entities")
    public void hydraMovementBlocked() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("d_BlockedHydra", "c_basicHydraConfig");
        
        Position initialHyPos = getEntities(resp, "hydra").get(0).getPosition();
        resp = dmc.tick(Direction.RIGHT);

        assertEquals(initialHyPos, getEntities(resp, "hydra").get(0).getPosition());
    }

    @Test
    @DisplayName("Tests that a hydra either heals or is damaged after a battle, as well as the player health")
    public void hydraBattle() throws Exception {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BattleHydraTest", "c_basicHydraConfig");
    
        dmc.tick(Direction.RIGHT);

        for(Entity en : dmc.getGame().getEntitiesInCell(2, 1)) {
            if(en.getType().equals("player")) {
                Player pl = (Player) en;
                assertEquals((10.00 - (3.00/10.00)), pl.getHealth());
            } else if(en.getType().equals("hydra")) {
                Hydra hy = (Hydra) en;
                if(!( hy.getHealth() == (3.00 - (10.00/5.00))|| hy.getHealth() == 3.00 + 10.00/5.00)) {
                    throw new Exception("The hydra was not damaged / healed correctly");
                }
            }
        }

    }

    @Test
    @DisplayName("Tests that a hydra never heals when fighting the player")
    public void hyrdaNeverHeal() throws Exception {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BattleHydraTest", "c_hydraNeverHeal");

        dmc.tick(Direction.RIGHT);

        for(Entity en : dmc.getGame().getEntitiesInCell(2, 1)) {
            if(en.getType().equals("player")) {
                Player pl = (Player) en;
                assertEquals((10.00 - (3.00/10.00)), pl.getHealth());
            } else if(en.getType().equals("hydra")) {
                Hydra hy = (Hydra) en;
                if(!(hy.getHealth() == (3.00 - (10.00/5.00)))) {
                    throw new Exception("The hydra was not damaged correctly");
                }
            }
        }
    }

    @Test
    @DisplayName("Tests that a hydra always heals when fighting the player")
    public void hydraAlwaysHeal() throws Exception {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BattleHydraTest", "c_hydraHealing");
        
        dmc.tick(Direction.RIGHT);

        for(Entity en : dmc.getGame().getEntitiesInCell(2, 1)) {
            if(en.getType().equals("player")) {
                Player pl = (Player) en;
                assertEquals((10.00 - (3.00/10.00)), pl.getHealth());
            } else if(en.getType().equals("hydra")) {
                Hydra hy = (Hydra) en;
                if(!(hy.getHealth() == 3.00 + 10.00/5.00)) {
                    throw new Exception("The hydra was not healed correctly");
                }
            }
        }
    }

}
