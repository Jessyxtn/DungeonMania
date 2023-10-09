package dungeonmania;

import java.io.Serializable;

public class Config implements Serializable {
    private int ally_attack;
    private int ally_defence;
    private int bribe_radius;
    private int bribe_amount;
    private int bomb_radius;
    private int bow_durability;
    private int player_health;
    private int player_attack;
    private int enemy_goal;
    private int invincibility_potion_duration;
    private int invisibility_potion_duration;
    private int mercenary_attack;
    private int mercenary_health;
    private int spider_attack;
    private int spider_health;
    private int spider_spawn_rate;
    private int shield_durability;
    private int shield_defence;
    private int sword_attack;
    private int sword_durability;
    private int treasure_goal;
    private int zombie_attack;
    private int zombie_health;
    private int zombie_spawn_rate;
    private int assassin_attack;
    private int assassin_bribe_amount;
    private double assassin_bribe_fail_rate;
    private int assassin_health;
    private int assassin_recon_radius;
    private int hydra_attack;
    private int hydra_health;
    private double hydra_health_increase_rate;
    private int hydra_health_increase_amount;
    private int mind_control_duration;
    private int midnight_armour_attack;
    private int midnight_armour_defence;

    public int getAllyAttack() {
        return ally_attack;
    }

    public int getAllyDefence() {
        return ally_defence;
    }

    public int getBribeRadius() {
        return bribe_radius;
    }

    public int getBribeAmount() {
        return bribe_amount;
    }

    public int getBombRadius() {
        return bomb_radius;
    }

    public int getBowDurability() {
        return bow_durability;
    }

    public int getPlayerHealth() {
        return player_health;
    }

    public int getPlayerAttack() {
        return player_attack;
    }

    public int getEnemyGoal() {
        return enemy_goal;
    }

    public int getInvincibilityPotionDuration() {
        return invincibility_potion_duration;
    }

    public int getInvisibilityPotionDurability() {
        return invisibility_potion_duration;
    }

    public int getMercenaryAttack() {
        return mercenary_attack;
    }

    public int getMercenaryHealth() {
        return mercenary_health;
    }

    public int getSpiderAttack() {
        return spider_attack;
    }

    public int getSpiderHealth() {
        return spider_health;
    }

    public int getSpiderSpawnRate() {
        return spider_spawn_rate;
    }

    public int getShieldDurability() {
        return shield_durability;
    }

    public int getShieldDefence() {
        return shield_defence;
    }

    public int getSwordAttack() {
        return sword_attack;
    }

    public int getSwordDurability() {
        return sword_durability;
    }

    public int getTreasureGoal() {
        return treasure_goal;
    }

    public int getZombieAttack() {
        return zombie_attack;
    }

    public int getZombieHealth() {
        return zombie_health;
    }

    public int getZombieSpawnRate() {
        return zombie_spawn_rate;
    }

    public int getAssassinAttack() {
        return assassin_attack;
    }

    public int getAssassinBribeAmount() {
        return assassin_bribe_amount;
    }

    public double getAssassinBribeFailRate() {
        return assassin_bribe_fail_rate;
    }

    public int getAssassinHealth() {
        return assassin_health;
    }

    public int getAssassinReconRadius() {
        return assassin_recon_radius;
    }

    public int getHydraAttack() {
        return hydra_attack;
    }

    public int getHydraHealth() {
        return hydra_health;
    }

    public double getHydraHealthIncreaseRate() {
        return hydra_health_increase_rate;
    }

    public int getHydraHealthIncreaseAmount() {
        return hydra_health_increase_amount;
    }

    public int getMindControlDuration() {
        return mind_control_duration;
    }

    public int getMindnightArmourAttack() {
        return midnight_armour_attack;
    }

    public int getMidnightArmourDefence() {
        return midnight_armour_defence;
    }

}
