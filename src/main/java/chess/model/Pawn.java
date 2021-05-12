package chess.model;

/**
 * This class contains the information about the pawns valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Pawn extends Figure {

    private boolean enPassant = false;

    /**
     * The constructor of a pawn
     * The pawns team and figure ID are initialized here.
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
        super(sourceClass.isBlackTeam());
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
        return Math.abs(actualPos.getPosY() - targetPos.getPosY()) == 1 && actualPos.getPosX() == targetPos.getPosX() && board.getFigure(targetPos) instanceof None;
    }

    /**
     * Tests if the pawn makes an normal attack
     *
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @return pawn makes an normal attack
     */
    private boolean normalAttack(Position actualPos, Position targetPos, Board board) {
        return Math.abs(actualPos.getPosY() - targetPos.getPosY()) == 1 && Math.abs(actualPos.getPosX() - targetPos.getPosX()) == 1 && !(board.getFigure(targetPos) instanceof None);
    }

    /**
     * Tests if the pawn makes his first move
     *
     * @param actualPos actual position for Pawn
     * @param targetPos new input position for Pawn
     * @return pawn makes first move
     */
    private boolean firstMove(Position actualPos, Position targetPos, Board board) {

        if (!alreadyMoved && Math.abs(actualPos.getPosY() - targetPos.getPosY()) == 2 && actualPos.getPosX() == targetPos.getPosX() && board.getFigure(targetPos) instanceof None) {
            if(board.getFigure(new Position(targetPos.getPosX(), targetPos.getPosY()  + (blackTeam ? 1 : -1))) instanceof None){
                enPassant = true;
                return true;
            }
        }
        return false;
    }

    /**
     * sets enPassant to false
     */
    public void resetEnPassant() {
        this.enPassant = false;
    }

    /**
     * check if Pawn moves in right direction
     *
     * @param actualPos current position of the pawn
     * @param targetPos target position of the pawn
     * @return whether the pawn moves in correct direction
     */
    public boolean checkRightDirection(Position actualPos, Position targetPos) {
        if (!blackTeam && targetPos.getPosY() > actualPos.getPosY()) return true;
        if (!blackTeam && targetPos.getPosY() < actualPos.getPosY()) return false;
        if (blackTeam && targetPos.getPosY() > actualPos.getPosY()) return false;
        return blackTeam && targetPos.getPosY() < actualPos.getPosY();
    }

    //only for Testing
    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    @Override
    public char getSymbol() {
        return !blackTeam ? 'P' : 'p';
    }
}
