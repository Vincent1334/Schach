package chess.model;

/**
 * This class contains the information about the rooks valid movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 */

public class Rook extends Figure {

    public Rook(int team) {
        super(team);
        super.figureID = 2;
    }

    public Rook(Rook sourceClass) {
        super(sourceClass.team);
        super.alreadyMoved = sourceClass.alreadyMoved;
        super.figureID = 2;
    }

    /**
     * Proofs if the move is a valid move for Rook
     *
     * @param actualPos actual position for Rook
     * @param targetPos new input position for Rook
     * @param board     actual state of chessboard
     * @return whether move was successful
     */
    @Override
    public boolean validMove(Position actualPos, Position targetPos, Board board) {

        int posX = actualPos.getPosX();
        int posY = actualPos.getPosY();
        int newX = targetPos.getPosX();
        int newY = targetPos.getPosY();

        // is a figure between the old position and the new position?
        // move right
        if (newY == posY && newX > posX) {
            for (int j = 1; j <= Math.abs(posX - newX) - 1; j++) {
                if (!(board.getFigure(posX + j, posY) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        // move left
        if (newY == posY && newX < posX) {
            for (int j = 1; j <= Math.abs(posX - newX) - 1; j++) {
                if (!(board.getFigure(posX - j, posY) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        // move up
        if (newX == posX && newY > posY) {
            for (int j = 1; j <= (Math.abs(posY - newY) - 1); j++) {
                if (!(board.getFigure(posX, posY + j) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        // move down
        if (newX == posX && newY < posY) {
            for (int j = 1; j <= Math.abs(posY - newY) - 1; j++) {
                if (!(board.getFigure(posX, posY - j) instanceof None)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u265C' : '\u2656';
    }


}