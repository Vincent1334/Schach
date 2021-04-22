package chess.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Figure {

    boolean alreadyMoved;
    int team;

    /**
     * Constructor for default figures
     * @param team
     */
    public Figure(int team) {
        this.team = team;
    }

    /**
     * Constructor for None Figures
     */
    public Figure(){

    }

    public abstract boolean validMove(Map<String, Integer> move, Board board);

    public int getTeam(){
        return this.team;
    }
    public abstract char getSymbol();

    public boolean isAlreadyMoved() {
        return alreadyMoved;
    }

}
