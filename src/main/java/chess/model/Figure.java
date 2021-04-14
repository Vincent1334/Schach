package chess.model;

public abstract class Figure {

    private boolean alreadyMoved;
    private int posX;
    private int posY;
    private int team;

    public Figure(int posX, int posY, int team) {
        this.posX = posX;
        this.posY = posY;
        this.team = team;
    }

    public abstract boolean makeMove(int newX, int newY, Board board);

    public int getTeam(){
        return team;
    }
}
