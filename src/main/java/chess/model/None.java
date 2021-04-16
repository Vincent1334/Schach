package chess.model;

public class None extends Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

    public None(int posX, int posY, int team) {
        // TODO
        super(posX, posY, team);
    }

    /**
     *
     */
    public boolean validMove(int newX, int newY, Board board) {
        // TODO
    }

    @Override
    public String getSymbol() {
        return " ";
    }

}
