package dungeonmania.entities.staticEntities;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.movingEntities.Player;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends StaticEntity{
    private String colour;

    public TimeTravellingPortal(String type, Position pos, String colour) {
        super(type, pos);
        this.colour = colour;
    }
    public void collideWithPlayer(Player player, Game g) {      
        g.rewind(30);
    }

}
