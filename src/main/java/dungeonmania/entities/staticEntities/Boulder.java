package dungeonmania.entities.staticEntities;

import java.util.List;
import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectableEntities.Bomb.Bomb;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity {

    public Boulder(String type, Position pos) {
        super(type, pos);
    }

    public void collideWithPlayer(Player p, Game g) {
        // Maybe have this as helper function
        int pX = p.getPosition().getX();
        int pY = p.getPosition().getY();
        int thisX = this.getPosition().getX();
        int thisY = this.getPosition().getY();

        // Move boulder - this doesnt take into account if theres a some sort of static entity stoping it from moving
        if (pX < thisX) {
            // move right
            this.moveBoulder(Direction.RIGHT, g);
        } else if (pX > thisX) {
            // move left
            this.moveBoulder(Direction.LEFT, g);
        } else if (pY > thisY) {
            // move up
            this.moveBoulder(Direction.UP, g);
        } else if (pY < thisY) {
            // move down
            this.moveBoulder(Direction.DOWN, g);
        }
    }

    public void moveBoulder(Direction movementDirection, Game g) {
        Position offset = movementDirection.getOffset();
        Position newPos = new Position(this.getPosition().getX() + offset.getX(), this.getPosition().getY() + offset.getY());
        Position adjacentPos = new Position(this.getPosition().getX() + offset.getX(), this.getPosition().getY() + offset.getY());

        if (canMoveTo(g.getEntitiesInCell(newPos.getX(), newPos.getY())) &&
        noAdjacentBoulder(g.getEntitiesInCell(adjacentPos.getX(), adjacentPos.getY()))) {
            this.setPosition(newPos);
        } 
    }

    public boolean canMoveTo(List<Entity> entitiesInCell) {
        for (Entity o: entitiesInCell) {
            if (Objects.equals(o.getType(), "wall")) {
                return false;
            } else if (Objects.equals(o.getType(), "bomb")) {
                Bomb b = (Bomb) o;
                if (!b.isCanPass()) {
                    return false;
                }
            } 
        }

        return true;
    }


    public boolean noAdjacentBoulder(List<Entity> entitiesInCell) {
        for (Entity o: entitiesInCell) {
            if (Objects.equals(o.getType(), "boulder")) {
                return false;
            }
        }

        return true;
    }
}
