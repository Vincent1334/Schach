package chess.model;

public class None extends Figure {

    public None() {
        super(2);
        super.figureID = 0;
    }

    public None(None sourceClass){
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 0;
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
