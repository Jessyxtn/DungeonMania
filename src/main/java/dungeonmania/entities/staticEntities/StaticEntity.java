package dungeonmania.entities.staticEntities;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class StaticEntity extends Entity {

    public StaticEntity(String type, Position pos) {
        super(type);
        this.pos = pos;
    }

}
