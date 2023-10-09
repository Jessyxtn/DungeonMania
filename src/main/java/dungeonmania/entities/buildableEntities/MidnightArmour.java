package dungeonmania.entities.buildableEntities;

public class MidnightArmour extends BuildableEntity {
    private Double armourAttack;
    private Double armourDefence;
    
    public MidnightArmour(String type, int durability, Double armourAttack, Double armourDefence) {
        super(type, durability);
        this.armourAttack = armourAttack;
        this.armourDefence = armourDefence;
    }

    public Double getAttack() {
        return armourAttack;
    }

    public Double getDefence() {
        return armourDefence;
    }

    @Override
    public void useItem() {
        // do nothing
    }
}
