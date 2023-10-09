package dungeonmania.entities.buildableEntities;

import dungeonmania.entities.Entity;

public class BuildableEntity extends Entity {
    private int durability;
    public boolean isUsed = false;

    public BuildableEntity(String type, int durability) {
        super(type);
        this.durability = durability;
    }

    public double getDurability() {
        return durability;
    }

    public boolean isBroken() {
        if (durability <= 0) {
            return true;
        }
        return false;
    }

    public void useItem() {
        this.durability--;
    }

    public void alreadyUsed() {
        this.isUsed = true;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    public void breakItem() {
        this.durability = 0;
    }

    public String getType() {
        return type;
    }

}
