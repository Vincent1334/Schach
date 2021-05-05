package chess.model;


public class None extends Figure {

    public None() {
        super(2);
    }

    /**
     *
     */

    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {
        return false;
    }

    @Override
    public char getSymbol() {
        return '\u0020';
    }



}
