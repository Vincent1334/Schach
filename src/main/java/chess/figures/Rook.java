package chess.figures;

import chess.model.Board;
import chess.model.Position;

/**
 * This class contains the information about the rooks valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Rook extends Figure {

    /**
     * The constructor of a rook.
     * The rooks team and figure ID are initialized here.
     *
     * @param blackTeam white or not
     */
    public Rook(boolean blackTeam) {
        super(blackTeam);
        super.figureID = 2;
    }

    /**
     * The copy constructor of this class
     *
     * @param sourceClass Rook you want to clone
     */
    public Rook(Rook sourceClass) {
        super(sourceClass.blackTeam);
        super.setAlreadyMoved(sourceClass.isAlreadyMoved());
        super.figureID = 2;
    }

    /**
     * Proofs if the move is a valid move for Rook
     *
     * @param actualPos actual position of the Rook
     * @param targetPos new input position for Rook
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {
        // don't stay at the same place
        if (actualPos.getPOS_X() != targetPos.getPOS_X() && actualPos.getPOS_Y() != targetPos.getPOS_Y()) return false;

        if (actualPos.getPOS_X() == targetPos.getPOS_X()) {
            for (int i = 1; i < Math.abs(actualPos.getPOS_Y() - targetPos.getPOS_Y()); i++) {
                if (!(board.getFigure(actualPos.getPOS_X(), actualPos.getPOS_Y() + i * getDirection(actualPos, targetPos, true)) instanceof None))
                    return false;
            }
        } else {
            for (int i = 1; i < Math.abs(actualPos.getPOS_X() - targetPos.getPOS_X()); i++) {
                if (!(board.getFigure(actualPos.getPOS_X() + i * getDirection(actualPos, targetPos, false), actualPos.getPOS_Y()) instanceof None))
                    return false;
            }
        }

        return true;
    }

    /**
     * return Direction
     *
     * @param actualPos actual position of the Rook
     * @param targetPos new input position for Rook
     * @param axis      true = y, false = x
     * @return 1 or -1
     */
    private int getDirection(Position actualPos, Position targetPos, boolean axis) {
        if (actualPos.getPOS_X() > targetPos.getPOS_X() && !axis) return -1;
        if (actualPos.getPOS_Y() > targetPos.getPOS_Y() && axis) return -1;
        return 1;
    }

    /**
     * return Symbol
     *
     * @return the symbol of a Rook
     */
    @Override
    public char getSymbol() {
        return !blackTeam ? 'R' : 'r';
    }

}