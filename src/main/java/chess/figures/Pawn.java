package chess.figures;

import chess.model.Board;
import chess.model.Position;

/**
 * This class contains the information about the pawns valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Pawn extends Figure {

    private boolean enPassant = false;

    /**
     * The constructor of a pawn.
     * The pawns team and figure ID are initialized here.
     *
     * @param blackTeam white or not
     */
    public Pawn(boolean blackTeam) {
        super(blackTeam);
        super.figureID = 1;
    }

    /**
     * The copy constructor of this class
     *
     * @param sourceClass Pawn you want to clone
     */
    public Pawn(Pawn sourceClass) {
        super(sourceClass.isBlack());
        super.setAlreadyMoved(sourceClass.isAlreadyMoved());
        this.enPassant = sourceClass.isEnPassant();
        super.figureID = 1;
    }

    /**
     * getter for enPassant
     *
     * @return true if the pawn can be beaten with an en passant
     */
    public boolean isEnPassant() {
        return enPassant;
    }

    /**
     * Proofs if the move is a valid move for Pawn
     *
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        // check direction
        if (!checkRightDirection(actualPos, targetPos)) return false;
        //is move legal?
        return normalMove(actualPos, targetPos, board) || normalAttack(actualPos, targetPos, board) || firstMove(actualPos, targetPos, board);
    }

    /**
     * Tests if the pawn makes an normal move
     *
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @return pawn makes an normal move
     */
    private boolean normalMove(Position actualPos, Position targetPos, Board board) {
        return Math.abs(actualPos.getPOS_Y() - targetPos.getPOS_Y()) == 1 && actualPos.getPOS_X() == targetPos.getPOS_X() && board.getFigure(targetPos) instanceof None;
    }

    /**
     * Tests if the pawn makes an normal attack
     *
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @return pawn makes an normal attack
     */
    private boolean normalAttack(Position actualPos, Position targetPos, Board board) {
        return Math.abs(actualPos.getPOS_Y() - targetPos.getPOS_Y()) == 1 && Math.abs(actualPos.getPOS_X() - targetPos.getPOS_X()) == 1 && !(board.getFigure(targetPos) instanceof None);
    }

    /**
     * Tests if the pawn makes his first move
     *
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @return pawn makes first move
     */
    private boolean firstMove(Position actualPos, Position targetPos, Board board) {

        if (!alreadyMoved && Math.abs(actualPos.getPOS_Y() - targetPos.getPOS_Y()) == 2 && actualPos.getPOS_X() == targetPos.getPOS_X() && board.getFigure(targetPos) instanceof None
            && board.getFigure(new Position(targetPos.getPOS_X(), targetPos.getPOS_Y()  + (blackTeam ? 1 : -1))) instanceof None) {
            enPassant = true;
            return true;
        }
        return false;
    }

    /**
     * check if Pawn moves in right direction
     *
     * @param actualPos current position of the pawn
     * @param targetPos target position of the pawn
     * @return whether the pawn moves in correct direction
     */
    public boolean checkRightDirection(Position actualPos, Position targetPos) {
        return !blackTeam && targetPos.getPOS_Y() > actualPos.getPOS_Y()
                || blackTeam && targetPos.getPOS_Y() < actualPos.getPOS_Y();
    }

    /**
     * Set enPassant status
     *
     * @param enPassant the new enPassant status
     */
    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    /**
     * return Symbol
     *
     * @return Symbol of the pawn
     */
    @Override
    public char getSymbol() {
        return !blackTeam ? 'P' : 'p';
    }
}
