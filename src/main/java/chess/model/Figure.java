package chess.model;

public abstract class Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

    public Figure(int posX, int posY, int team) {
        this.posX = posX;
        this.posY = posY;
        this.team = team;
    }

    public abstract boolean validMove(int newX, int newY, Board board);

    public int getTeam(){
        return this.team;
    }
    public abstract String getSymbol();

    public boolean isAlreadyMoved() {
        return alreadyMoved;
    }

}
