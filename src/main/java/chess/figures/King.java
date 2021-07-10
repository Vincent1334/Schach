package chess.figures;

import chess.model.Board;
import chess.model.Position;

/**
 * This class contains the information about the kings valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class King extends Figure {

    /**
     * The constructor of a king.
     * The kings team and figure ID are initialized here.
     *
     * @param blackTeam the black team
     */
    public King(boolean blackTeam) {
        super(blackTeam);
        super.figureID = 6;
    }

    /**
     * The copy constructor of this class
     *
     * @param sourceClass King you want to clone
     */
    public King(King sourceClass){
        super(sourceClass.blackTeam);
        super.setAlreadyMoved(sourceClass.isAlreadyMoved());
        super.figureID = 6;
    }

    /**
     * Proofs if the move is a valid move for King
     *
     * @param actualPos actual position for King
     * @param targetPos new input position for King
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {
        // normal move
        return Math.abs(actualPos.getPOS_X() - targetPos.getPOS_X()) <= 1 && Math.abs(actualPos.getPOS_Y() - targetPos.getPOS_Y()) <= 1;
    }

    /**
     * return Symbol
     *
     * @return the Symbol of a King
     */
    @Override
    public char getSymbol() {
        return !blackTeam ? 'K' : 'k';
    }
}
