package chess.model;

public class None extends Figure {

    public None() {

    }

    /**
     *
     */
    public boolean validMove(int newX, int newY, Board board) {
        return false;
    }

    @Override
    public char getSymbol() {
        return '\u0020';
    }

}
