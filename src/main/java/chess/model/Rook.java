package chess.model;

/**
 * This class contains the information about the rooks valid movements
 *
 * @author Lydia Engelhardt, Sofia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-05
 *
 */

public class Rook extends Figure {

    public Rook(int team) {
        super(team);
    }

    /**
     * Proofs if the move is a valid move for Rook
     * @param posX actual x-position for Rook
     * @param posY actual y-position for Rook
     * @param newX new input x-position for Rook
     * @param newY new input y-position for Rook
     * @param board actual state of chessboard
     * @return whether move was successful
     */

    @Override
    public boolean validMove(int posX, int posY, int newX, int newY, Board board) {
        // is a figure between the old position and the new position?
        // move right
        if (newY == posY && newX > posX) {
            for (int j = 1; j < Math.abs(posX - newX)-1; j++) {
                if (!(board.getFigure(posX + j, posY) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        // move left
        if (newY == posY && newX < posX) {
            for (int j = 1; j < Math.abs(posX - newX)-1; j++) {
                if (!(board.getFigure(posX - j, posY) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        // move up
        if (newX == posX && newY > posY) {
            for (int j = 1; j < Math.abs(posY - newY)-1; j++) {
                if (!(board.getFigure(posX, posY+j) instanceof None)) {
                    return false;
                }
            }
            return true;
        }
        // move down
        if (newX == posX && newY < posY) {
            for (int j = 1; j < Math.abs(posY - newY)-1; j++) {
                if (!(board.getFigure(posX, posY-j) instanceof None)) {
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