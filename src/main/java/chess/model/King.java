package chess.model;

/**
 * This class contains the information about the kings valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 *
 */

public class King extends Figure {

    /**
     * The constructor of a king
     * The kings team and figure ID are initialized here.
     */
    public King(boolean team) {
        super(team);
        super.figureID = 6;
    }

    /**
     * The copy constructor of this class
     * @param sourceClass
     */
    public King(King sourceClass){
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 6;
    }

    /**
     * Proofs if the move is a valid move for King
     *
     * @param actualPos actual position for King
     * @param targetPos new input position for King
     * @param board actual state of chessboard
     * @return whether move was successful
     */

    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {
        // normal move
        if(!(Math.abs(actualPos.getPosX()-targetPos.getPosX()) <= 1 && Math.abs(actualPos.getPosY()-targetPos.getPosY()) <= 1)) return false;

        //is the field empty?
        if((board.getFigure(targetPos) instanceof  None)) return true;

        // is the target field with an enemy figure?
        if(board.getFigure(targetPos).getTeam() == team) return false;

        return true;
    }

    @Override
    public char getSymbol() {
        return team == false ? 'K' : 'k';
    }



}
