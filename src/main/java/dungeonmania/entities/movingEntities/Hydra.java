package dungeonmania.entities.movingEntities;

import java.util.Random;

import dungeonmania.util.Position;

public class Hydra extends ZombieToast {
    private double health_increase_rate;
    private int health_increase_amount;

    public Hydra(String type, Position pos, Double health, Double attack, Double health_increase_rate, int health_increase_amount) {
        super(type, pos, health, attack);
        this.health_increase_rate = health_increase_rate;
        this.health_increase_amount = health_increase_amount;
    }

    @Override
    public void battle(Player p, Double deltaEnemy, Double deltaPlayer) {
        Random healchance = new Random();
        double res = healchance.nextDouble();
        // Based on probability and the health increase rate, either damage or heal a hydra 
        if(res <= health_increase_rate) {
            this.setHealth(health - deltaEnemy);
        } else {
            this.setHealth(health + deltaEnemy);
        }
        p.setHealth(p.getHealth() + deltaPlayer);
    }

    public double getHealthIncreaseRate() {
        return health_increase_rate;
    }

    public int getHealthIncreaseAmount() {
        return health_increase_amount;
    }

}
