package chess.figures;

import chess.model.Board;
import chess.model.Position;

/**
 * This class contains the information about the bishops valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Bishop extends Figure {

    /**
     * The constructor of a bishop
     *
     * The bishops team and figure ID are initialized here.
     * @param blackTeam white or not
     */
    public Bishop(boolean blackTeam) {
        super(blackTeam);
        super.figureID = 4;
    }

    /**
     * The copy constructor of this class
     *
     * @param sourceClass Bishop you want to clone
     */
    public Bishop(Bishop sourceClass) {
        super(sourceClass.blackTeam);
        super.setAlreadyMoved(sourceClass.isAlreadyMoved());
        super.figureID = 4;
    }

    /**
     * Proofs if the move is a valid move for Bishop
     *
     * @param actualPos actual position for Bishop
     * @param targetPos new input position for Bishop
     * @param board     actual state of chessboard
     * @return whether move was successful
     */

    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {
        //Is the new position on a diagonal from the old position
        if (Math.abs(actualPos.getPOS_X() - targetPos.getPOS_X()) != Math.abs(actualPos.getPOS_Y() - targetPos.getPOS_Y())) {
            return false;
        }
        //is between the old position and the new position a figure
        int directionX = 1;
        int directionY = 1;
        if(actualPos.getPOS_X() > targetPos.getPOS_X()) directionX = -1;
        if(actualPos.getPOS_Y() > targetPos.getPOS_Y()) directionY = -1;
        for(int i = 1; i < Math.abs(actualPos.getPOS_X()-targetPos.getPOS_X()); i++){
            if(!(board.getFigure(actualPos.getPOS_X()+i*directionX, actualPos.getPOS_Y()+i*directionY) instanceof None)) return false;
        }
        return true;
    }

    /**
     * return Symbol
     *
     * @return the Symbol of a Bishop
     */
    @Override
    public char getSymbol() {
        return !blackTeam ? 'B' : 'b';
    }

}
