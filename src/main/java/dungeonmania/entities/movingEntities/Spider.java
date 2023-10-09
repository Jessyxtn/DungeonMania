package dungeonmania.entities.movingEntities;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.Game;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private Double attack;
    public int currentpathindex;
    public boolean clockwise;
    // Stores the path of the Spider
    public Map<Integer,Position> path = new HashMap<Integer, Position>();
    private int battleResponseIndex = -1;
    private MovementBehaviour spiderMovement = new EnemyMoveCircular();

    public Spider(String type, Position pos, Double health, Double attack) {
        super(type, pos, health);
        this.attack = attack;
        this.currentpathindex = -1;
        this.clockwise = true;
        // Initialises the path field
        createPath();
    }

    /*
     * Initialises the path HashMap of the spider
     */
    private void createPath() {
        Position pathpos0 = pos.translateBy(Direction.UP);
        this.path.put(0,pathpos0);

        Position pathpos1 = pos.translateBy(1, -1);
        this.path.put(1,pathpos1);

        Position pathpos2 = pos.translateBy(Direction.RIGHT);
        this.path.put(2,pathpos2);

        Position pathpos3 = pos.translateBy(1, 1);
        this.path.put(3,pathpos3);

        Position pathpos4 = pos.translateBy(Direction.DOWN);
        this.path.put(4,pathpos4);

        Position pathpos5 = pos.translateBy(-1, 1);
        this.path.put(5,pathpos5);

        Position pathpos6 = pos.translateBy(Direction.LEFT);
        this.path.put(6,pathpos6);

        Position pathpos7 = pos.translateBy(-1, -1);
        this.path.put(7,pathpos7);
    }

    /**
     * Moves the spider every tick
     * @param g
     * @param s
     */
    public void updatePos(Game g) {
        this.setBehaviour(spiderMovement);
        spiderMovement.movement(this, pos, g);
    }

    public void battle(Player p, Double deltaEnemy, Double deltaPlayer) {
        p.setHealth(p.getHealth() + deltaPlayer);
        this.setHealth(health + deltaEnemy);
    }

    public Double getDeltaEnemyHealth(Player p, Game g) {
        return -((g.getTotalBowAttack() * (p.getAttack() + g.getTotalAllyAttack() + g.getTotalSwordAttack() + g.getMidnightArmourAttack())) / 5);
    }

    public Double getDeltaPlayerHealth(Player p, Game g) {
        return -((attack - (g.getTotalAllyDefence() + g.getTotalShieldDefence() + g.getMidnightArmourDefence())) / 10);
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

    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
    }

    public void setCurrentpathindex(int currentpathindex) {
        this.currentpathindex = currentpathindex;
    }

}
