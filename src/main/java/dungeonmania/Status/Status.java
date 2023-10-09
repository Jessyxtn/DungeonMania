package dungeonmania.Status;

import java.io.Serializable;

import dungeonmania.Game;

public class Status implements Serializable {

    private InvincibleState invincible;
    private InvisibleState invisible;
    
    public Status(double InvincibleDuration, double InvisibleDuration) {
        this.invincible = new InvincibleState(InvincibleDuration);
        this.invisible = new InvisibleState(InvincibleDuration);
    }

    // Check whether player is Invincible
    public boolean isInvincible() {
        return this.invincible.getduration() != (double) 0;
    }

    // Check whether player is Invisible
    public boolean isInvisible() {
        return this.invisible.getduration() != (double) 0;
    }
    // Player status is Invincible
    public void becomeInvincible() {
        this.invincible.applyStatus();
    }

    // Player status is Invisible
    public void becomeInvisible() {
        this.invisible.applyStatus();
    }

    // Update duration every tick
    private String nextPotion;
    public void updateDuration(Game g) {

        if (g.getInventory().getQueue().size() != 0) {
            nextPotion = g.getInventory().getQueue().peek();
            if (this.invincible.getduration() == 1 || this.invisible.getduration() == 1) {
                if (nextPotion.equals("invincibility_potion")) {
                    becomeInvincible();
                    g.getInventory().getQueue().remove();
                }
                if (nextPotion.equals("invisibility_potion")) {
                    becomeInvisible();
                    g.getInventory().getQueue().remove();
                }
            }    
        }
        // if currently invincible
        if (isInvincible()) {
            this.invincible.nexttick();
        // if currently invisible
        } else if (isInvisible()) {
            this.invisible.nexttick();
        } 
        
        
    }
    
}
