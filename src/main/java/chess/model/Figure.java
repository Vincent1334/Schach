package chess.model;

public abstract class Figure {

    boolean alreadyMoved = false;
    int team, figureID;

    /**
     * Constructor for default figures
     *
     * @param team of the figure (0=white, 1=black, 2=none)
     */
    public Figure(int team) {
        this.team = team;
    }

    /**
     * Constructor for None Figures
     */
    public Figure() {
    }

    public abstract boolean validMove(Position actualPos, Position targetPos, Board board);

    public int getTeam() {
        return this.team;
    }

    public abstract char getSymbol();

    public boolean isAlreadyMoved() {
        return alreadyMoved;
    }

    public void setAlreadyMoved(boolean alreadyMoved) {
        this.alreadyMoved = alreadyMoved;
    }

    public int getFigureID() {
        return this.figureID;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Figure) {
            Figure figure1 = (Figure) other;
            return figure1.getFigureID() == figureID && figure1.getTeam() == team && figure1.isAlreadyMoved() == alreadyMoved;
        }
        return false;
    }

}
