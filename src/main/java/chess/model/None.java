package chess.model;

public class None extends Figure {

    public None() {
        super(2);
    }

    /**
     *
     */
    @Override
    public boolean validMove(int posX, int posY, int newX, int newY, Board board) {
        return false;
    }

    @Override
    public char getSymbol() {
        return '\u0020';
    }



}
