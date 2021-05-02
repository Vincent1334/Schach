package chess.model;

public abstract class Figure {

    boolean alreadyMoved = false;
    int team;

    /**
     * Constructor for default figures
     * @param team of the figure (0=white, 1=black, 2=none)
     */
    public Figure(int team) {
        this.team = team;
    }

    /**
     * Constructor for None Figures
     */
    public Figure(){}

    public abstract boolean validMove(int posX, int posY, int newX, int newY, Board board);

    public int getTeam(){
        return this.team;
    }
    public abstract char getSymbol();

    public boolean isAlreadyMoved() {
        return alreadyMoved;
    }

    public void setAlreadyMoved(boolean alreadyMoved) {
        this.alreadyMoved = alreadyMoved;
    }
}
