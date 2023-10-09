package dungeonmania.entities.collectableEntities;

import dungeonmania.util.Position;

public class Sword extends CollectableEntity {
    private Double durability;
    private Double attack;
    public boolean isUsed = false;

    public Sword(String type, Position pos, Double durability, Double attack) {
        super(type, pos);
        this.durability = durability;
        this.attack = attack;
    }

    public Double getDurability() {
        return durability;
    }

    public Double getAttack() {
        return attack;
    }

    public boolean isBroken() {
        if (durability <= 0) {
            return true;
        }
        return false;
    }

    public void useSword() {
        durability--;
    }

    public void alreadyUsed() {
        this.isUsed = true;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

}
