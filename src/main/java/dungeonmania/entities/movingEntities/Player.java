package dungeonmania.entities.movingEntities;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectableEntities.CollectableEntity;
import dungeonmania.util.Position;

public class Player extends MovingEntity {
    private Double attack;
    private boolean hasGoneThroughPortal;
    private boolean hasTimeTravelled;
    private boolean canTeleport = true;

    public Player(String type, Position pos, Double health, Double attack) {
        super(type, pos, health);
        this.attack = attack;
    }

    public void collectItem(CollectableEntity o, Game g) {
        g.getInventory().addItem(o);

        if (g.getEntities().containsKey(o.getType())) {
            g.getEntities().get(o.getType()).remove(o);
        }
    }

    @Override
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public Double getAttack() {
        return attack;
    }
   
    public void playerUseItem(Game g, String itemId) {
        Entity item = g.getInventory().findItem(itemId);
        if (item != null) {
            item.useItem((Player)g.getPlayer(), g);
        }
    }

    public boolean hasGoneThroughPortal() {
        return hasGoneThroughPortal;
    }

    public void sethasGoneThroughPortal(boolean value) {
        hasGoneThroughPortal = value;
    }

    public boolean isCanTeleport() {
        return canTeleport;
    }

    public void setCanTeleport(boolean canTeleport) {
        this.canTeleport = canTeleport;
    }
    public boolean hasTimeTravelled() {
        return hasTimeTravelled;
    }

    public void sethasTimeTravelled(boolean value) {
        hasTimeTravelled = value;
    }
    
}
