package chess.model;

public class Rook extends Figure {

    public Rook(int team) {
        super(team);
    }

    boolean alreadyMoved;

    /**
     * Proofs if the move is a valid move for Rook and makes move if it is
     *
     * @param newX  new input x-position for Rook
     * @param newY  new input y-position for Rook
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int posX, int posY, int newX, int newY, Board board) {
        // is a figure between the old position and the new position?
        if (newY == posY) {
            for (int j = 1; j < Math.abs(posX - newX); j++) {
                if (!(board.getFigure(posX + j, posY) instanceof None)) {
                    return false;
                }
            }
        }
        if (newX == posX) {
            for (int j = 1; j < Math.abs(posY - newY); j++) {
                if (!(board.getFigure(posX + j, posY) instanceof None)) {
                    return false;
                }
            }
        }
        alreadyMoved = true;
        return true;
    }

    @Override
    public char getSymbol() {
        return team == 0 ? '\u265C' : '\u2656';
    }
}