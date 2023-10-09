package dungeonmania.entities.buildableEntities;

public class Shield extends BuildableEntity {
    private Double shieldDefence;

    public Shield(String type, int durability, Double shieldDefence) {
        super(type, durability);
        this.shieldDefence = shieldDefence;
    }

    public Double getDefence() {
        return shieldDefence;
    }

}
