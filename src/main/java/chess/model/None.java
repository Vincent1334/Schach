package chess.model;

public class None extends Figure {

    boolean alreadyMoved;
    int posX;
    int posY;
    int team;

    public None(int posX, int posY, int team) {
        // TODO
    }

    /**
     *
     */
    public boolean makeMove(int newX, int newY, Board board) {
        // TODO
    }

    @Override
    public String getSymbol() {
        return " ";
    }

}
