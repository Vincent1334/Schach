package chess.model;

/**
 * This class contains the information about the none figure
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-10
 */
public class None extends Figure {

    /**
     * the constructor of a none figure
     * nones team and figure ID are initialized here
     */
    public None() {
        super.figureID = 0;
    }

    /**
     * The copy constructor of none class
     * @param sourceClass
     */
    public None(None sourceClass){
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 0;
    }

    /**
     * proofs if the move is valid
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
