package chess.model;

public class None extends Figure {

    public None(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     *
     */
    public boolean validMove(int newX, int newY, Board board) {
        return false;
    }

    @Override
    public char getSymbol() {
        return ' ';
    }

}
