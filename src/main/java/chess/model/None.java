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
     *proofs if the move is valid
     * @return false, none can't move
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {
        return false;
    }

    /**
     * returns the symbol of None
     * @return symbol of None
     */
    @Override
    public char getSymbol() {
        return ' ';
    }



}
