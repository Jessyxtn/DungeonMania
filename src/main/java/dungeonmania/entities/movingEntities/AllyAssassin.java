package dungeonmania.entities.movingEntities;

import dungeonmania.util.Position;

public class AllyAssassin extends Assassin {

    public AllyAssassin(String type, Position pos, Double health, Double attack, int bribe_amount, Double bribe_radius, double bribe_fail_rate, int recon_radius, String id) {
        super(type, pos, health, attack, bribe_amount, bribe_radius, bribe_fail_rate, recon_radius);
        this.isInteractable = true;
        this.isAlly = true;
        this.isInteractable = true;
        this.id = id;
    }
}
