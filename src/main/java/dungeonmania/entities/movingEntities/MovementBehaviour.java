package dungeonmania.entities.movingEntities;

import java.io.Serializable;

import dungeonmania.Game;
import dungeonmania.util.Position;

public interface MovementBehaviour extends Serializable {

    public void movement(MovingEntity entity, Position playerPos, Game g);
}