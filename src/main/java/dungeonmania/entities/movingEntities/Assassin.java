package dungeonmania.entities.movingEntities;

import dungeonmania.Game;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    private double bribe_fail_rate;
    private int recon_radius;

    private MovementBehaviour moveTowards = new EnemyMoveTowards();
    private MovementBehaviour moveRandom = new EnemyMoveRandom();
    private MovementBehaviour moveAway = new EnemyMoveAway();

    public Assassin(String type, Position pos, Double health, Double attack, int bribe_amount, Double bribe_radius, Double bribe_fail_rate, int recon_radius) {
        super(type, pos, health, attack, bribe_amount, bribe_radius);
        this.isInteractable = true;
        this.bribe_fail_rate = bribe_fail_rate;
        this.recon_radius = recon_radius;
    }

    @Override
    public void updatePos(Position playerPos, Game g) {
        if (g.getStatus().isInvincible()) {
            // Move Away movement behaviour
            this.setBehaviour(moveAway);
        } else if (g.getStatus().isInvisible() && Position.calculateDistance(playerPos, this.getPosition()) <= recon_radius) {
            this.setBehaviour(moveTowards);
        } else if (g.getStatus().isInvisible()) {
            // Movement becomes random behaviour
            this.setBehaviour(moveRandom);
        } else {
            this.setBehaviour(moveTowards);
        }

        this.movement(playerPos, g);
    }

    public double getBribeFailRate() {
        return bribe_fail_rate;
    }

    public int getReconRadius() {
        return recon_radius;
    }

}
