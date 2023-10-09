package dungeonmania.goals;

import java.io.Serializable;

import dungeonmania.Game;

public interface Goal extends Serializable { 
    public boolean evaluate(Game g);
    public String toString(Game g);
}
