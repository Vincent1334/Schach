package chess.model;

/**
 * This class contains the information about the pawns valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Pawn extends Figure {

    public Pawn(int team) {
        super(team);
        super.figureID = 1;
    }

    public Pawn(Pawn sourceClass) {
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        this.enPassant = sourceClass.enPassant;
        super.figureID = 1;
    }

    boolean enPassant = false;

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

        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        if (((team == 0 && posY + 1 == newY) || (team == 1 && posY - 1 == newY)) && posX == newX) {
            //normal move
            return true;
        }
        if ((team == 0 && posY + 1 == newY && board.getFigure(targetPos).getTeam() == 1)
                || (team == 1 && posY - 1 == newY && board.getFigure(targetPos).getTeam() == 0)
                && (posX + 1 == newX || posX - 1 == newX)) {
            //normal attack
            return true;
        }
        if (((team == 0 && posY + 2 == newY) || (team == 1 && posY - 2 == newY)) && (posX == newX && !alreadyMoved)) {
            //first move
            enPassant = true;
            return true;
        }
        return false;
    }

    public void resetEnPassant() {
        this.enPassant = false;
    }

    //only for Testing
    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u265F' : '\u2659';
    }


}
