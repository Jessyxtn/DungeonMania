package dungeonmania.entities.staticEntities;

import java.util.Objects;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    private String colour;

    public Portal(String type, Position pos, String colour) {
        super(type, pos);
        this.colour = colour;
    }

    public void collideWithPlayer(Player player, Game g) {
        for (Entity portal: g.getPortals()) {
            Portal p = (Portal) portal;
            if (p != this && Objects.equals(p.getColour(), this.getColour())) {
                Position newPos = new Position(this.getPosition().getX() - player.getPosition().getX() + p.getPosition().getX(),
                this.getPosition().getY() - player.getPosition().getY() + p.getPosition().getY());
                player.setPosition(newPos);

                for (Entity o: g.getEntitiesInCell(newPos.getX(), newPos.getY())) {
                    if (Objects.equals(o.getType(), "portal")) {
                        player.setPosition(p.getPosition());
                        Portal po = (Portal) o;
                        po.collideWithPlayer(player, g);
                    } else if (Objects.equals(o.getType(), "wall")) {
                        player.setCanTeleport(false);
                    }
                }
                
                player.sethasGoneThroughPortal(true);
            }
        }
    }

    public String getColour() {
        return colour;
    }

}
