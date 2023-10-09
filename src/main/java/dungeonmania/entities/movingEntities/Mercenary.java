package dungeonmania.entities.movingEntities;

import dungeonmania.Game;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity {
    public Double attack;
    public int bribe_amount;
    public Double bribe_radius;
    public boolean isAlly = false;
    protected int battleResponseIndex = -1;

    private MovementBehaviour moveTowards = new EnemyMoveTowards();
    private MovementBehaviour moveRandom = new EnemyMoveRandom();
    private MovementBehaviour moveAway = new EnemyMoveAway();

    public Mercenary(String type, Position pos, Double health, Double attack, int bribe_amount, Double bribe_radius) {
        super(type, pos, health);
        this.isInteractable = true;
        this.attack = attack;
        this.bribe_amount = bribe_amount;
        this.bribe_radius = bribe_radius;
    }

    public void updatePos(Position playerPos, Game g) {
        if (g.getStatus().isInvincible()) {
            // Move Away movement behaviour
            this.setBehaviour(moveAway);
        } else if (g.getStatus().isInvisible()) {
            // Movement becomes random behaviour
            this.setBehaviour(moveRandom);
        } else {
            this.setBehaviour(moveTowards);
        }

        this.movement(playerPos, g);
    }

    public boolean getIsAlly() {
        return isAlly;
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

    public Double getAttack() {
        return attack;
    }

}
