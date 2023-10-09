package dungeonmania.entities.movingEntities;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.staticEntities.SwampTile;
import dungeonmania.util.Position;

public class MovingEntity extends Entity {
    public Double health;
    private MovementBehaviour behaviour;
    private int stuck_pos_ticks = 0;

    public MovingEntity(String type, Position pos, Double health) {
        super(type);
        this.pos = pos;
        this.health = health;
    }

    public Position getPosition() {
        return pos;
    }

    public Double getHealth() {
        return health;
    }

    @Override
    public boolean isAlive() {
        if (this.health <= 0) {
            return false;
        }
        return true;
    }

    public void setHealth(Double newHealth) {
        this.health = newHealth;
    }
    
    public void setBehaviour(MovementBehaviour behaviour) {
        this.behaviour = behaviour;
    }

    public void movement(Position playerPos, Game g) {
        behaviour.movement(this, playerPos, g);
    }

    public int getStuckPosTicks() {
        return stuck_pos_ticks;
    }

    public void setStuckPosTicks(int value) {
        this.stuck_pos_ticks = value;
    }

    public SwampTile getSwampTileOn(Game g) {
        SwampTile s = null;
        
        if (g.getSwampTileEntities() != null) {
            for (Entity o: g.getSwampTileEntities()) {
                if (o.getPosition().getX() == this.getPosition().getX() && 
                o.getPosition().getY() == this.getPosition().getY()) {
                    s = (SwampTile) o;
                }
            }
        }

        return s;
    }
}
