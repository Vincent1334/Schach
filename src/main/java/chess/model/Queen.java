package chess.model;

public class Queen extends Figure {

    public Queen(int posX, int posY, int team) {
        super(posX, posY, team);
    }

    /**
     * Proofs if the move is a valid move for Queen and makes move if it is
     * @param newX new input x-position for Queen
     * @param newY new input y-position for Queen
     * @param board actual state of chessboard
     * @return whether move was successful
     */
    public boolean validMove(int newX, int newY, Board board) {

        if (posX == newX || posY == newY || posX - newX == posY - newY) {
            return true;
        }
        return false;
    }
    @Override
    public String getSymbol() {
        return team == 0 ? "U+2658" : "U+265E";
    }

}

