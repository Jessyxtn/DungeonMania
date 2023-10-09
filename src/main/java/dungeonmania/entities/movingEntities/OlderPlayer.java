package dungeonmania.entities.movingEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectableEntities.CollectableEntity;
import dungeonmania.util.Position;

public class OlderPlayer extends MovingEntity {

    private Double attack;
    public int currentpathindex;
    private int battleResponseIndex = -1;
    private List<Position> path = new ArrayList<Position>();
    private MovementBehaviour OlderPlayerMovement = new EnemyMoveMimic();
    private boolean reachedDestination;

    public OlderPlayer(String type, Position pos, Double health, Double attack, List<Position> path) {
        super(type, pos, health);
        this.attack = attack;
        this.path = path;
        this.currentpathindex = -1;
        this.reachedDestination = false;
    }
    public void updatePos(Game g) {
        this.currentpathindex++;
        this.setBehaviour(OlderPlayerMovement);
        this.movement(pos, g);
    }
    public void collectItem(CollectableEntity o, Game g) {
        if (g.getEntities().containsKey(o.getType())) {
            g.getEntities().get(o.getType()).remove(o);
        }
    }

    public void battle(Player p, Double deltaEnemy, Double deltaPlayer) {
        p.setHealth(p.getHealth() + deltaPlayer);
        this.setHealth(health + deltaEnemy);
    }

    public Double getDeltaEnemyHealth(Player p, Game g) {
        return -((g.getTotalBowAttack() * (p.getAttack() + g.getTotalAllyAttack() + g.getTotalSwordAttack())) / 5);
    }

    public Double getDeltaPlayerHealth(Player p, Game g) {
        return -((attack - (g.getTotalAllyDefence() + g.getTotalShieldDefence())) / 10);
    }

    public Double getAttack() {
        return attack;
    }

    public void setAttack(Double attack) {
        this.attack = attack;
    }

    public int getBattleResponseIndex() {
        return battleResponseIndex;
    }

    public void setBattleResponseIndex(int index) {
        this.battleResponseIndex = index;
    }
    public List<Position> getPath() {
        return path;
    }
    public boolean hasReachedDestination() {
        return reachedDestination;
    }
    public void setReachedDestination() {
        this.reachedDestination = true;
    } 
    public void resetindex() {
        this.currentpathindex = -1;
    }
    

}
