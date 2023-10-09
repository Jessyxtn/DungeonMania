package dungeonmania.Status;

import java.io.Serializable;

public class InvincibleState implements Serializable {
    private Double remaining_duration = (double) 0;
    private Double full_duration;
    
    public InvincibleState(double duration) {
        this.full_duration = duration + (double) 1;
        
    }

    public void applyStatus() {
        if (this.remaining_duration != this.full_duration)
            this.remaining_duration = this.full_duration;
    }

    public double getduration() {
        return remaining_duration;
    }

    public void nexttick() {
        if (remaining_duration != 0)
            this.remaining_duration -= 1;
    }
    
}
