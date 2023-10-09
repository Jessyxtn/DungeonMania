package dungeonmania.entities.movingEntities;

import dungeonmania.Game;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity {
    protected Double attack;
    private MovementBehaviour moveRandom = new EnemyMoveRandom();
    private MovementBehaviour moveAway = new EnemyMoveAway();
    private int battleResponseIndex = -1;

    public ZombieToast(String type, Position pos, Double health, Double attack) {
        super(type, pos, health);
        this.attack = attack;
    }

    /**
     * Moves the zombie in a random cardinally adjacent tile if it's not blocked
     * @param g
     */
    public void updatePos(Position playerPos, Game g) {
        if (g.getStatus().isInvincible()) {
            // Move Away movement behaviour
            this.setBehaviour(moveAway);
        } else {
            // Normal zombie behaviour
            this.setBehaviour(moveRandom);
        }
       
        this.movement(playerPos, g);
    }

    public Double getAttack() {
        return attack;
    }

    public void setAttack(Double attack) {
        this.attack = attack;
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

    public int getBattleResponseIndex() {
        return battleResponseIndex;
    }

    public void setBattleResponseIndex(int index) {
        this.battleResponseIndex = index;
    }

}
