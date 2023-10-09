package dungeonmania.entities.staticEntities;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int movement_factor;

    public SwampTile(String type, Position pos, int movement_factor) {
        super(type, pos);
        this.movement_factor = movement_factor;
    }

    public int getMovementFactor() {
        return movement_factor;
    }

}
